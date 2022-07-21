package ru.vels.taskplanner.repo;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vels.taskplanner.entity.Group;

import java.util.List;

@Repository
public interface GroupsRepository extends JpaRepository<Group, Long> {
    Group getByName(String name);


    List<Group> findAll(Specification<Group> groupSpecification);
}
