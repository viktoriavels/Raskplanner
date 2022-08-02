package ru.vels.taskplanner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vels.taskplanner.dto.definition.DefinitionPlan;
import ru.vels.taskplanner.entity.ProcessDefinition;
import ru.vels.taskplanner.repo.ProcessDefinitionRepository;

@Service
public class DefinitionPlanReader {

    @Autowired
    private ProcessDefinitionRepository processDefinitionRepository;
    private final ObjectMapper objectMapper;

    public DefinitionPlanReader() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Transactional
    public DefinitionPlan readDefinition(String guid) {
        return readDefinition(processDefinitionRepository.findById(guid).get());
    }

    @Transactional
    public DefinitionPlan readDefinition(ProcessDefinition processDefinition) {
        try {
            return objectMapper.readValue(processDefinition.getDefinitionPlan(), DefinitionPlan.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
