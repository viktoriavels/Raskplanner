package ru.vels.taskplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vels.taskplanner.dto.ProcessDefinitionDto;
import ru.vels.taskplanner.dto.ProcessDefinitionFilter;
import ru.vels.taskplanner.entity.ProcessDefinition;
import ru.vels.taskplanner.exception.DeprivedOfRightsException;
import ru.vels.taskplanner.exception.ConflictException;
import ru.vels.taskplanner.exception.NotFoundException;
import ru.vels.taskplanner.repo.ProcessDefinitionRepository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProcessDefinitionService {

    @Autowired
    ProcessDefinitionRepository processDefinitionRepository;

    @Transactional
    public ProcessDefinitionDto getProcessDefinitionInfo(String guid) {
        Optional<ProcessDefinition> byId = processDefinitionRepository.findById(guid);
        if (!byId.isPresent()) {
            throw new NotFoundException("not found");
        }
        ProcessDefinition processDefinition = byId.get();
        return convertProcessDefinition(processDefinition);
    }

    private ProcessDefinitionDto convertProcessDefinition(ProcessDefinition processDefinition) {
        ProcessDefinitionDto processDefinitionDto = new ProcessDefinitionDto();
        processDefinitionDto.setId(processDefinition.getId());
        processDefinitionDto.setDefinitionPlan(processDefinition.getDefinitionPlan());
        processDefinitionDto.setDescription(processDefinition.getDescription());
        processDefinitionDto.setOwner(processDefinition.getOwner());
        processDefinitionDto.setTitle(processDefinition.getTitle());
        processDefinitionDto.setDeleted(processDefinition.isDeleted());
        return processDefinitionDto;
    }

    @Transactional
    public List<ProcessDefinitionDto> searchProcessDefinitions(ProcessDefinitionFilter filter) {
        List<ProcessDefinition> all = processDefinitionRepository.findAll(new Specification<ProcessDefinition>() {
            @Override
            public Predicate toPredicate(Root<ProcessDefinition> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (filter.getOwner() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("owner"), filter.getOwner()));
                }
                if (filter.getDescription() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("description"), filter.getDescription()));
                }
                if (filter.getTitle() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("title"), filter.getTitle()));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
        ArrayList<ProcessDefinitionDto> result = new ArrayList<>();
        for (ProcessDefinition processDefinition : all) {
            result.add(convertProcessDefinition(processDefinition));
        }
        return result;
    }

    @Transactional
    public ProcessDefinitionDto createProcessDefinition(ProcessDefinitionDto processDefinitionDto) throws ConflictException {
        ProcessDefinition processDefinition = new ProcessDefinition();
        if (processDefinitionDto.getId() == null) {
            processDefinition.setId(UUID.randomUUID().toString());
        } else {
            if (processDefinitionRepository.existsById(processDefinitionDto.getId())) {
                throw new ConflictException("Duplicate title");
            }
            processDefinition.setId(processDefinitionDto.getId());
        }
        processDefinition.setDefinitionPlan(processDefinitionDto.getDefinitionPlan());
        processDefinition.setDeleted(false);
        processDefinition.setDescription(processDefinitionDto.getDescription());
        processDefinition.setOwner(processDefinitionDto.getOwner());
        processDefinition.setTitle(processDefinitionDto.getTitle());

        processDefinition = processDefinitionRepository.save(processDefinition);
        return convertProcessDefinition(processDefinition);
    }

    @Transactional
    public void removeProcessDefinition(String guid) throws DeprivedOfRightsException {
        Optional<ProcessDefinition> byId = processDefinitionRepository.findById(guid);
        if (!byId.isPresent()) {
            throw new NotFoundException("not found");
        }
        ProcessDefinition processDefinition = byId.get();
        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getUsername().equals(processDefinition.getOwner())) {
            processDefinition.setDeleted(true);
            processDefinitionRepository.save(processDefinition);
        } else {
            throw new DeprivedOfRightsException("Недостаточно прав");
        }
    }

}
