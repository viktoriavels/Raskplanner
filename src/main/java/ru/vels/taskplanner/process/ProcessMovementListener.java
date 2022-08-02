package ru.vels.taskplanner.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vels.taskplanner.entity.TaskInstance;
import ru.vels.taskplanner.exception.ActivityException;
import ru.vels.taskplanner.service.ActivityService;

import javax.annotation.PostConstruct;

@Service
public class ProcessMovementListener implements ProcessListener {

    @Autowired
    private ProcessListenerManager manager;
    @Autowired
    private ActivityService activityService;

    @Override
    @PostConstruct
    public void init() {
        manager.register(this);
    }

    @Override
    public void notify(ProcessEvent processEvent) throws ActivityException {
        switch (processEvent.getEventType()) {
            case PROCESS_STARTED:
                activityService.startProcess(processEvent.getInstance().getId());
                break;
            case TASK_COMPLETED:
                activityService.stopActivity(processEvent.getInstance().getId(),
                        ((TaskInstance) processEvent.getEventSubject()).getTaskDefinitionId(),
                        ((TaskInstance) processEvent.getEventSubject()).getDecision());
                break;
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
