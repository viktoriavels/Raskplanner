package ru.vels.taskplanner.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vels.taskplanner.entity.Group;
import ru.vels.taskplanner.entity.User;
import ru.vels.taskplanner.repo.GroupsRepository;
import ru.vels.taskplanner.repo.UsersRepository;

import java.util.ArrayList;

@Component
public class InitUsers {

    private volatile boolean initialized;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    GroupsRepository groupsRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @EventListener
    @Transactional
    public synchronized void init(ContextRefreshedEvent event) {
        if (!initialized) {
            User admin = usersRepository.findByUsername("admin");
            if (admin == null) {
                admin = new User();
                admin.setUsername("admin");
                admin.setDeleted(false);
                admin.setSecret(passwordEncoder.encode("admin"));
                admin.setFirstName("admin");
                admin.setLastName("admin");
                admin.setPatronymic("ADMIN");
                admin = usersRepository.save(admin);
            }

            Group group = groupsRepository.getByName("ADMINS");
            if (group == null) {
                group = new Group();
                group.setDeleted(false);
                group.setTitle("ad");
                group.setName("ADMINS");
                group.setOwner("admin");
                ArrayList<User> users = new ArrayList<>();
                users.add(admin);
                group.setUsers(users);
                groupsRepository.save(group);
            }
        }
    }
}
