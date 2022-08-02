package ru.vels.taskplanner.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.vels.taskplanner.entity.ProcessDefinition;

@Repository
public interface ProcessDefinitionRepository extends JpaRepository<ProcessDefinition, String>, JpaSpecificationExecutor<ProcessDefinition> {

}

