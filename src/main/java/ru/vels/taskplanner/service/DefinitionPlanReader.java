package ru.vels.taskplanner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vels.taskplanner.dto.definition.DefinitionPlan;
import ru.vels.taskplanner.entity.ProcessDefinition;
import ru.vels.taskplanner.repo.ProcessDefinitionRepository;

@Service
public class DefinitionPlanReader {

    @Autowired
    ProcessDefinitionRepository processDefinitionRepository;

    public DefinitionPlan readDefinition(String guid) {
        return readDefinition(processDefinitionRepository.findById(guid).get());
    }

    public DefinitionPlan readDefinition(ProcessDefinition processDefinition) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DefinitionPlan definitionPlan = objectMapper.readValue(processDefinition.getDefinitionPlan(), DefinitionPlan.class);
            return definitionPlan;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
