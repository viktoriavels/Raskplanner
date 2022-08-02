package ru.vels.taskplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.vels.taskplanner.dto.TaskInstanceDto;
import ru.vels.taskplanner.dto.TaskInstanceFilter;
import ru.vels.taskplanner.dto.definition.DefinitionActivityType;
import ru.vels.taskplanner.dto.definition.DefinitionPlan;
import ru.vels.taskplanner.dto.definition.DefinitionUserTask;
import ru.vels.taskplanner.entity.ProcessInstance;
import ru.vels.taskplanner.entity.TaskInstance;
import ru.vels.taskplanner.exception.DeprivedOfRightsException;
import ru.vels.taskplanner.exception.NotFoundException;
import ru.vels.taskplanner.process.ProcessEvent;
import ru.vels.taskplanner.process.ProcessEventType;
import ru.vels.taskplanner.process.ProcessListenerManager;
import ru.vels.taskplanner.repo.ProcessInstancesRepository;
import ru.vels.taskplanner.repo.TaskInstancesRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskInstanceService {
    @Autowired
    TaskInstancesRepository taskInstancesRepository;
    @Autowired
    ProcessListenerManager processListenerManager;
    @Autowired
    DefinitionPlanReader definitionPlanReader;
    @Autowired
    ProcessInstancesRepository processInstancesRepository;

    public void cancelTask(Long id) {
        Optional<TaskInstance> byId = taskInstancesRepository.findById(id);
        if (!byId.isPresent()) {
            throw new NotFoundException("not found");
        }
        TaskInstance taskInstance = byId.get();
        taskInstance.setDecision("RESETED");
        taskInstance.setCompletionDate(Instant.now());
        taskInstancesRepository.save(taskInstance);
    }

    public void startTask(long processInstanceId, String taskDefinitionId) {
        Optional<ProcessInstance> byId = processInstancesRepository.findById(processInstanceId);
        if (!byId.isPresent()) {
            throw new NotFoundException("not found");
        }
        ProcessInstance processInstance = byId.get();
        DefinitionPlan definitionPlan = definitionPlanReader.readDefinition(processInstance.getDefinition());
        if (definitionPlan == null) {
            throw new NotFoundException("not found");
        }
        Optional<DefinitionUserTask> first = definitionPlan.getActivities().stream()
                .filter(activity -> taskDefinitionId.equals(activity.getId()))
                .filter(activity -> activity.getActivityType() == DefinitionActivityType.USER_TASK)
                .map(activity -> (DefinitionUserTask) activity)
                .findFirst();
        if (!first.isPresent()) {
            throw new NotFoundException("not found");
        }

        DefinitionUserTask definitionUserTask = first.get();
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setCandidates(definitionUserTask.getCandidates());
        taskInstance.setCompletionDate(null);
        taskInstance.setProcessInstance(processInstance);
        taskInstance.setDescription(definitionUserTask.getDescription());
        taskInstance.setCreationDate(Instant.now());
        taskInstance.setTitle(definitionUserTask.getTitle());
        taskInstance.setTaskDefinitionId(taskDefinitionId);
        taskInstance.setDueDate(Instant.now().plus(definitionUserTask.getDueDateInterval()));
        taskInstance = taskInstancesRepository.save(taskInstance);

        ProcessEvent<TaskInstance> processEvent = new ProcessEvent<>();
        processEvent.setInstance(processInstance);
        processEvent.setEventSubject(taskInstance);
        processEvent.setEventType(ProcessEventType.TASK_STARTED);
        processEvent.setEventSubjectType(TaskInstance.class);

        processListenerManager.notify(processEvent);
    }

    public void completeTask(long id, String decision) throws DeprivedOfRightsException {
        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<TaskInstance> byId = taskInstancesRepository.findById(id);
        if (!byId.isPresent()) {
            throw new NotFoundException("not found");
        }
        TaskInstance taskInstance = byId.get();
        if(taskInstance.getCandidates().contains(currentUser.getUsername())||isAdmin()){
            taskInstance.setDecision(decision);
            taskInstance.setCompletionDate(Instant.now());
            taskInstancesRepository.save(taskInstance);

            ProcessEvent<TaskInstance> processEvent = new ProcessEvent<>();
            processEvent.setInstance(taskInstance.getProcessInstance());
            processEvent.setEventSubject(taskInstance);
            processEvent.setEventType(ProcessEventType.TASK_COMPLETED);
            processEvent.setEventSubjectType(TaskInstance.class);

            processListenerManager.notify(processEvent);
        }else {
            throw  new DeprivedOfRightsException("DeprivedOfRights");
        }

    }

    public List<TaskInstanceDto> searchTasks(TaskInstanceFilter filter) {
        List<TaskInstance> all = taskInstancesRepository.findAll(new Specification<TaskInstance>() {
            @Override
            public Predicate toPredicate(Root<TaskInstance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (filter.getCandidate() != null) {
                    predicates.add(criteriaBuilder.in(root.get("candidates")).value(filter.getCandidate()));
                }
                if (filter.getDecision() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("decision"), filter.getDecision()));
                }
                if (filter.getTaskDefinitionId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("definitionId"), filter.getTaskDefinitionId()));
                }
                if (filter.getDescription() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("description"), filter.getDescription()));
                }
                if (filter.getTitle() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("title"), filter.getTitle()));
                }
                if (filter.getProcessInstanceId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("processInstance"), filter.getProcessInstanceId()));
                }
                if (filter.getFromCompletionDate() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("completionDate"), filter.getFromCompletionDate()));
                }
                if (filter.getFromCreationDate() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creationDate"), filter.getFromCreationDate()));
                }
                if (filter.getFromDueDate() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), filter.getFromDueDate()));
                }
                if (filter.getToCompletionDate() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("completionDate"), filter.getToCompletionDate()));
                }
                if (filter.getToCreationDate() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creationDate"), filter.getFromCreationDate()));
                }
                if (filter.getToDueDate() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), filter.getToCompletionDate()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });

        ArrayList<TaskInstanceDto> result = new ArrayList<>();
        for (TaskInstance taskInstance : all) {
            result.add(convertProcessInstance(taskInstance));
        }
        return result;
    }

    private TaskInstanceDto convertProcessInstance(TaskInstance taskInstance) {
        TaskInstanceDto taskInstanceDto = new TaskInstanceDto();
        taskInstanceDto.setId(taskInstance.getId());
        taskInstanceDto.setTaskDefinitionId(taskInstance.getTaskDefinitionId());
        taskInstanceDto.setProcessInstanceId(taskInstance.getProcessInstance().getId());
        taskInstanceDto.setCandidates(new ArrayList<>(taskInstance.getCandidates()));
        taskInstanceDto.setCreationDate(taskInstance.getCreationDate());
        taskInstanceDto.setDueDate(taskInstance.getDueDate());
        taskInstanceDto.setCompletionDate(taskInstance.getCompletionDate());
        taskInstanceDto.setTitle(taskInstance.getTitle());
        taskInstanceDto.setDescription(taskInstance.getDescription());
        taskInstanceDto.setDecision(taskInstance.getDecision());
        return taskInstanceDto;
    }
    private boolean isAdmin() {
        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (GrantedAuthority authority : currentUser.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMINS")) {
                return true;
            }
        }
        return false;
    }
}





