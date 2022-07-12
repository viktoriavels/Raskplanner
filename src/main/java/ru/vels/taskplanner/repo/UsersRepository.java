package ru.vels.taskplanner.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.vels.taskplanner.entity.User;
@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
