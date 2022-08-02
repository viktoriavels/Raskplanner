package ru.vels.taskplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vels.taskplanner.dto.ProcessInstanceFilter;
import ru.vels.taskplanner.dto.RunProcessInstanceDto;
import ru.vels.taskplanner.dto.ProcessInstanceDto;
import ru.vels.taskplanner.entity.ProcessDefinition;
import ru.vels.taskplanner.entity.ProcessInstance;
import ru.vels.taskplanner.entity.TaskInstance;
import ru.vels.taskplanner.exception.DeprivedOfRightsException;
import ru.vels.taskplanner.exception.NotFoundException;
import ru.vels.taskplanner.process.ProcessEvent;
import ru.vels.taskplanner.process.ProcessEventType;
import ru.vels.taskplanner.process.ProcessListenerManager;
import ru.vels.taskplanner.repo.ProcessDefinitionRepository;
import ru.vels.taskplanner.repo.ProcessInstancesRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProcessInstanceService {

    @Autowired
    TaskInstanceService taskInstanceService;
    @Autowired
    ProcessInstancesRepository processInstancesRepository;
    @Autowired
    ProcessListenerManager processListenerManager;
    @Autowired
    ProcessDefinitionRepository processDefinitionRepository;

    @Transactional
    public ProcessInstanceDto startProcess(RunProcessInstanceDto runProcessInstanceDto) {

        Optional<ProcessDefinition> byId = processDefinitionRepository.findById(runProcessInstanceDto.getDefinitionId());
        if (!byId.isPresent()) {
            throw new NotFoundException("not found");
        }
        ProcessDefinition processDefinition = byId.get();
        ProcessInstance processInstance = new ProcessInstance();

        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        processInstance.setOwner(principal.getUsername());
        processInstance.setCompletionDate(null);
        processInstance.setDefinition(processDefinition.getId());
        processInstance.setCreationDate(Instant.now());
        processInstance.setDueDate(Instant.now().plus(1, ChronoUnit.DAYS));
        processInstance = processInstancesRepository.save(processInstance);

        ProcessEvent<ProcessInstance> processEvent = new ProcessEvent<>();
        processEvent.setInstance(processInstance);
        processEvent.setEventSubject(processInstance);
        processEvent.setEventType(ProcessEventType.PROCESS_STARTED);
        processEvent.setEventSubjectType(ProcessInstance.class);

        processListenerManager.notify(processEvent);

        ProcessInstanceDto processInstanceDto = new ProcessInstanceDto();

        processInstanceDto.setCompletionDate(processInstance.getCompletionDate());
        processInstanceDto.setDefinition(processInstance.getDefinition());
        processInstanceDto.setDueDate(processInstance.getDueDate());
        processInstanceDto.setId(processInstance.getId());
        processInstanceDto.setCreationDate(processInstance.getCreationDate());

        return processInstanceDto;
    }

    @Transactional
    public void cancelProcessInstance(long id) throws DeprivedOfRightsException {
        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ProcessInstance> byId = processInstancesRepository.findById(id);
        if (!byId.isPresent()) {
            throw new NotFoundException("not found");
        }
        ProcessInstance processInstance = byId.get();
        if (isAdmin() || processInstance.getOwner().equals(currentUser.getUsername())) {
            for (TaskInstance taskInstance : processInstance.getTaskInstanceList()) {
                taskInstanceService.cancelTask(taskInstance.getId());
            }
            processInstance.setCompletionDate(Instant.now());
            processInstancesRepository.save(processInstance);
        } else {
            throw new DeprivedOfRightsException("DeprivedOfRights");
        }
    }

    @Transactional
    public List<ProcessInstanceDto> searchProcessInstance(ProcessInstanceFilter filter) {
        List<ProcessInstance> all = processInstancesRepository.findAll(new Specification<ProcessInstance>() {
            @Override
            public Predicate toPredicate(Root<ProcessInstance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (filter.getDefinition() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("definition"), filter.getDefinition()));
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

        ArrayList<ProcessInstanceDto> result = new ArrayList<>();
        for (ProcessInstance processInstance : all) {
            result.add(convertProcessInstance(processInstance));
        }
        return result;
    }

    private ProcessInstanceDto convertProcessInstance(ProcessInstance processInstance) {
        ProcessInstanceDto processInstanceDto = new ProcessInstanceDto();
        processInstanceDto.setId(processInstance.getId());
        processInstanceDto.setDueDate(processInstance.getDueDate());
        processInstanceDto.setCompletionDate(processInstance.getCompletionDate());
        processInstanceDto.setOwner(processInstance.getOwner());
        processInstanceDto.setDefinition(processInstance.getDefinition());
        processInstanceDto.setCreationDate(processInstance.getCreationDate());

        return processInstanceDto;
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
