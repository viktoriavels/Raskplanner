package ru.vels.taskplanner.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vels.taskplanner.dto.ProcessDefinitionDto;
import ru.vels.taskplanner.dto.ProcessDefinitionFilter;
import ru.vels.taskplanner.exception.DeprivedOfRightsException;
import ru.vels.taskplanner.exception.DuplicateException;
import ru.vels.taskplanner.service.ProcessDefinitionService;

import java.util.List;

@RestController
public class ProcessDefinitionController {

    @Autowired
    ProcessDefinitionService processDefinitionService;

    @DeleteMapping(path = "/process-definition/{guid}")
    public ResponseEntity<Void> removeProcessDefinition(@PathVariable String guid) throws DeprivedOfRightsException {
        processDefinitionService.removeProcessDefinition(guid);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/process-definition/create")
    public ProcessDefinitionDto createProcessDefinition(@RequestBody ProcessDefinitionDto processDefinitionDto) throws DuplicateException {
        return processDefinitionService.createProcessDefinition(processDefinitionDto);
    }

    @PostMapping(path = "/process-definition/search",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProcessDefinitionDto> searchProcessDefinitions(@RequestBody ProcessDefinitionFilter filter) {
        return processDefinitionService.searchProcessDefinitions(filter);
    }

    @GetMapping()
    public ProcessDefinitionDto getProcessDefinitionInfo(@RequestBody String title) {
        return processDefinitionService.getProcessDefinitionInfo(title);
    }

    @ExceptionHandler(DeprivedOfRightsException.class)
    public ResponseEntity<Void> handleException(DeprivedOfRightsException e) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Void> handleException(DuplicateException e) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
