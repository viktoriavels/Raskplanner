package ru.vels.taskplanner.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.vels.taskplanner.entity.ProcessDefinition;

@Repository
public interface ProcessDefinitionsRepository extends JpaRepository<ProcessDefinition,Long> {
}
