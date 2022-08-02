package ru.vels.taskplanner.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vels.taskplanner.dto.ProcessInstanceDto;
import ru.vels.taskplanner.dto.ProcessInstanceFilter;
import ru.vels.taskplanner.dto.RunProcessInstanceDto;
import ru.vels.taskplanner.exception.DeprivedOfRightsException;
import ru.vels.taskplanner.service.ProcessInstanceService;

import java.util.List;

@RestController
public class ProcessInstanceController {

    @Autowired
    private ProcessInstanceService processInstanceService;

    @PostMapping("/process-instance/{guid}")
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
}
