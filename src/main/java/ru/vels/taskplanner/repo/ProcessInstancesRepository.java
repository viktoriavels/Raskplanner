package ru.vels.taskplanner.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vels.taskplanner.entity.ProcessInstance;

@Repository
public interface ProcessInstancesRepository extends JpaRepository<ProcessInstance,Long> {
}
