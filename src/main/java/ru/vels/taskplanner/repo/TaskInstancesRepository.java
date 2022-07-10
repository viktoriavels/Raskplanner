package ru.vels.taskplanner.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.vels.taskplanner.entity.TaskInstance;

@Repository
public interface TaskInstancesRepository extends JpaRepository<TaskInstance,Long> {
}
