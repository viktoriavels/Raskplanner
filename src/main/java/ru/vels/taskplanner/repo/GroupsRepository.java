package ru.vels.taskplanner.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vels.taskplanner.entity.Group;
@Repository
public interface GroupsRepository extends JpaRepository<Group,Long> {
 Group getByName(String name);
}
