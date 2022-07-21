package ru.vels.taskplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.vels.taskplanner.dto.ProcessDefinitionDto;
import ru.vels.taskplanner.dto.ProcessDefinitionFilter;
import ru.vels.taskplanner.entity.ProcessDefinition;
import ru.vels.taskplanner.exception.DeprivedOfRightsException;
import ru.vels.taskplanner.exception.DuplicateException;
import ru.vels.taskplanner.repo.ProcessDefinitionRepository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
@Component
public class ProcessDefinitionService {
    @Autowired
    ProcessDefinitionRepository processDefinitionRepository;

    public ProcessDefinitionDto getProcessDefinitionInfo(String guid) {
        ProcessDefinition processDefinition = processDefinitionRepository.findByTitle(guid);
        ProcessDefinitionDto processDefinitionDto = convertProcessDefinition(processDefinition);
        return processDefinitionDto;
    }

    private ProcessDefinitionDto convertProcessDefinition(ProcessDefinition processDefinition) {
        ProcessDefinitionDto processDefinitionDto = new ProcessDefinitionDto();
        processDefinitionDto.setDefinitionPlan(processDefinition.getDefinitionPlan());
        processDefinitionDto.setDescription(processDefinition.getDescription());
        processDefinitionDto.setOwner(processDefinition.getOwner());
        processDefinitionDto.setTitle(processDefinition.getTitle());
        processDefinitionDto.setDeleted(false);
        return processDefinitionDto;
    }

    public List<ProcessDefinitionDto> searchProcessDefinitions(ProcessDefinitionFilter filter) {
        List<ProcessDefinition> all = processDefinitionRepository.findAll(new Specification<ProcessDefinition>() {
            @Override
            public Predicate toPredicate(Root<ProcessDefinition> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (filter.getDefinitionPlan() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("definitionPlan"), filter.getDefinitionPlan()));
                }
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

    public ProcessDefinitionDto createProcessDefinition(ProcessDefinitionDto processDefinitionDto) throws DuplicateException {
        if (processDefinitionRepository.findByTitle(processDefinitionDto.getTitle()) != null) {
            throw new DuplicateException("Duplicate title");
        }
        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setDefinitionPlan(processDefinitionDto.getDefinitionPlan());
        processDefinition.setDeleted(false);
        processDefinition.setDescription(processDefinitionDto.getDescription());
        processDefinition.setOwner(processDefinitionDto.getOwner());
        processDefinition.setTitle(processDefinitionDto.getTitle());

        processDefinition = processDefinitionRepository.save(processDefinition);
        ProcessDefinitionDto processDefinitionDto1 = new ProcessDefinitionDto();
        processDefinitionDto1.setDefinitionPlan(processDefinition.getDefinitionPlan());
        processDefinitionDto1.setDeleted(false);
        processDefinitionDto1.setDescription(processDefinition.getDescription());
        processDefinitionDto1.setOwner(processDefinition.getOwner());
        processDefinitionDto1.setTitle(processDefinition.getTitle());

        return processDefinitionDto1;

    }

    public void removeProcessDefinition(String guid) throws DeprivedOfRightsException {
        ProcessDefinition processDefinition = processDefinitionRepository.findByTitle(guid);

        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getUsername().equals(processDefinition.getOwner().getUsername())) {
            processDefinition.setDeleted(true);
            processDefinitionRepository.save(processDefinition);
        } else {
            throw new DeprivedOfRightsException("Недостаточно прав");
        }
    }

}
