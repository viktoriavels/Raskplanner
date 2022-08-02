package ru.vels.taskplanner.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vels.taskplanner.dto.ProcessInstanceDto;
import ru.vels.taskplanner.dto.ProcessInstanceFilter;
import ru.vels.taskplanner.dto.RunProcessInstanceDto;
import ru.vels.taskplanner.exception.DeprivedOfRightsException;
import ru.vels.taskplanner.exception.NotFoundException;
import ru.vels.taskplanner.service.ProcessInstanceService;

import java.util.List;

@RestController
public class ProcessInstanceController {

    @Autowired
    private ProcessInstanceService processInstanceService;

    @PostMapping("/process-instance")
    public ProcessInstanceDto startProcess(@RequestBody RunProcessInstanceDto runProcessInstanceDto) {
        return processInstanceService.startProcess(runProcessInstanceDto);
    }

    @DeleteMapping(path = "/process-instance/{id}")
    public void cancelProcessInstance(@PathVariable long id) throws DeprivedOfRightsException {
        processInstanceService.cancelProcessInstance(id);
    }

    @PostMapping("/process-instance/search")
    public List<ProcessInstanceDto> searchProcessInstances(@RequestBody ProcessInstanceFilter processInstanceFilter) {
        return processInstanceService.searchProcessInstance(processInstanceFilter);
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
