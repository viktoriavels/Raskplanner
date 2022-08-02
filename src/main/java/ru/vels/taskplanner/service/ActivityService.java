package ru.vels.taskplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vels.taskplanner.dto.definition.*;
import ru.vels.taskplanner.entity.ProcessInstance;
import ru.vels.taskplanner.exception.ActivityException;
import ru.vels.taskplanner.repo.ProcessInstancesRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ActivityService {

    @Autowired
    DefinitionPlanReader definitionPlanReader;
    @Autowired
    ProcessInstancesRepository processInstancesRepository;
    @Autowired
    TaskInstanceService taskInstanceService;

    @Transactional
    public void startProcess(long processInstanceId) throws ActivityException {
        Optional<ProcessInstance> byId = processInstancesRepository.findById(processInstanceId);
        if (!byId.isPresent()) {
            throw new ActivityException("processInstance with id 9876 not found");
        }
        ProcessInstance processInstance = byId.get();

        DefinitionPlan definitionPlan = definitionPlanReader.readDefinition(processInstance.getDefinition());
        DefinitionActivity definitionActivity = definitionPlan.getActivities().get(0);
        taskInstanceService.startTask(processInstanceId, definitionActivity.getId());
    }

    @Transactional
    public void stopActivity(long processInstanceId, String activityDefinitionId, String decision) throws ActivityException {
        Optional<ProcessInstance> byId = processInstancesRepository.findById(processInstanceId);
        if (!byId.isPresent()) {
            throw new ActivityException("processInstance with id 9876 not found");
        }
        ProcessInstance processInstance = byId.get();
        DefinitionPlan definitionPlan = definitionPlanReader.readDefinition(processInstance.getDefinition());

        List<DefinitionTransition> collect = definitionPlan.getTransitions().stream()
                .filter(transition -> activityDefinitionId.equals(transition.getFromActivityId()))
                .filter(transition -> {
                    if (transition.getDecision() != null) {
                        return transition.getDecision().equals(decision);
                    }
                    return true;
                })
                .collect(Collectors.toList());
        for (DefinitionTransition definitionTransition : collect) {
            Optional<DefinitionUserTask> first = definitionPlan.getActivities().stream()
                    .filter(activity -> definitionTransition.getToActivityId().equals(activity.getId()))
                    .filter(activity -> activity.getActivityType() == DefinitionActivityType.USER_TASK)
                    .map(activity -> (DefinitionUserTask) activity)
                    .findFirst();
            if (!first.isPresent()) {
                throw new ActivityException("processInstance with id 9876 not found");
            }
            DefinitionUserTask definitionUserTask = first.get();
            taskInstanceService.startTask(processInstanceId, definitionUserTask.getId());
        }
    }
}
