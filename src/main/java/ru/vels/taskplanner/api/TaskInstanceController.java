package ru.vels.taskplanner.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vels.taskplanner.dto.TaskInstanceFilter;
import ru.vels.taskplanner.exception.DeprivedOfRightsException;
import ru.vels.taskplanner.service.TaskInstanceService;

@RestController
public class TaskInstanceController {

    @Autowired
    TaskInstanceService taskInstanceService;

    @PostMapping("/task-instance/complete")
    public void completeTaskInstance(@RequestBody Long id, String decision) throws DeprivedOfRightsException {
        taskInstanceService.completeTask(id, decision);
    }

    @PostMapping("/task-instance/search")
    public void searchTasks(@RequestBody TaskInstanceFilter taskInstanceFilter) {
        taskInstanceService.searchTasks(taskInstanceFilter);
    }
}
