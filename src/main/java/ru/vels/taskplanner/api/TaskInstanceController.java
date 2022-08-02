package ru.vels.taskplanner.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vels.taskplanner.dto.TaskInstanceDto;
import ru.vels.taskplanner.dto.TaskInstanceFilter;
import ru.vels.taskplanner.exception.DeprivedOfRightsException;
import ru.vels.taskplanner.exception.NotFoundException;
import ru.vels.taskplanner.service.TaskInstanceService;

import java.util.List;

@RestController
public class TaskInstanceController {

    @Autowired
    TaskInstanceService taskInstanceService;

    @PostMapping("/task-instance/complete")
    public void completeTaskInstance(@RequestParam Long id, @RequestParam String decision) throws DeprivedOfRightsException {
        taskInstanceService.completeTask(id, decision);
    }

    @PostMapping("/task-instance/search")
    public List<TaskInstanceDto> searchTasks(@RequestBody TaskInstanceFilter taskInstanceFilter) {
        return taskInstanceService.searchTasks(taskInstanceFilter);
    }

    @ExceptionHandler(DeprivedOfRightsException.class)
    public ResponseEntity<Void> handleException(DeprivedOfRightsException e) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleException(NotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
