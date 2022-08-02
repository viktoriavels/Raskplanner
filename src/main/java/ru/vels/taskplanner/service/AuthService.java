package ru.vels.taskplanner.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vels.taskplanner.dto.GroupDto;
import ru.vels.taskplanner.dto.GroupFilter;
import ru.vels.taskplanner.dto.UserDto;
import ru.vels.taskplanner.dto.UserFilter;
import ru.vels.taskplanner.entity.Group;
import ru.vels.taskplanner.entity.User;
import ru.vels.taskplanner.exception.DeprivedOfRightsException;
import ru.vels.taskplanner.exception.ConflictException;
import ru.vels.taskplanner.repo.GroupsRepository;
import ru.vels.taskplanner.repo.UsersRepository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthService {

    @Autowired
    UsersRepository usersRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    GroupsRepository groupsRepository;

    @Transactional
    public UserDto addUser(UserDto userDto) throws ConflictException {
        if (usersRepository.findByUsername(userDto.getUsername()) != null) {
            throw new ConflictException("Duplicate username");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPatronymic(userDto.getPatronymic());
        user.setDeleted(false);
        user.setLastName(userDto.getLastName());
        user.setFirstName(userDto.getFirstName());
        user.setSecret(passwordEncoder.encode(userDto.getSecret()));
        user = usersRepository.save(user);
        return convertUser(user);
    }

    @Transactional
    public GroupDto createGroup(GroupDto groupDto) throws ConflictException {
        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (groupsRepository.getByName(groupDto.getName()) != null) {
            throw new ConflictException("Duplicate group name");
        }
        Group group = new Group();
        group.setName(groupDto.getName());
        group.setTitle(groupDto.getTitle());
        group.setDeleted(false);
        group.setOwner(currentUser.getUsername());

        group = groupsRepository.save(group);
        return convertGroup(group);
    }

    @Transactional
    public void removeUser(String username) throws DeprivedOfRightsException {
        User user = usersRepository.findByUsername(username);
        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getUsername().equals(username)) {
            user.setDeleted(true);
            usersRepository.save(user);
        } else if (isAdmin()) {
            user.setDeleted(true);
            usersRepository.save(user);
        } else {
            throw new DeprivedOfRightsException("Недостаточно прав");
        }
    }

    @Transactional
    public void removeGroup(String groupName) throws DeprivedOfRightsException {
        Group group = groupsRepository.getByName(groupName);
        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (group.getOwner().equals(currentUser.getUsername()) || isAdmin()) {
            group.setDeleted(true);
            groupsRepository.save(group);
        } else {
            throw new DeprivedOfRightsException("Нет прав на удаление группы");
        }
    }

    @Transactional
    public UserDto getUserInfo(String username) {
        User user = usersRepository.findByUsername(username);
        UserDto userDto = convertUser(user);
        return userDto;
    }

    @Transactional
    public GroupDto getGroupInfo(String name) {
        Group group = groupsRepository.getByName(name);
        GroupDto groupDto = convertGroup(group);
        return groupDto;
    }

    @Transactional
    public UserDto updateUserDto(UserDto userDto) throws DeprivedOfRightsException {
        User user = usersRepository.findByUsername(userDto.getUsername());
        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUsername().equals(currentUser.getUsername()) || isAdmin()) {
            user.setPatronymic(userDto.getPatronymic());
            user.setLastName(userDto.getLastName());
            user.setFirstName(userDto.getFirstName());
            user = usersRepository.save(user);
            UserDto userDto1 = new UserDto();
            userDto1.setUsername(user.getUsername());
            userDto1.setFirstName(user.getFirstName());
            userDto1.setDeleted(false);
            userDto1.setPatronymic(user.getPatronymic());
            userDto1.setLastName(user.getLastName());
            return userDto1;

        }
        throw new DeprivedOfRightsException("Нет прав для обновления профиля");
    }

    @Transactional
    public GroupDto updateGroupDto(GroupDto groupDto) throws DeprivedOfRightsException {
        Group group = groupsRepository.getByName(groupDto.getName());
        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (group.getOwner().equals(currentUser.getUsername()) || isAdmin()) {
            group.setName(groupDto.getName());
            group.setTitle(groupDto.getTitle());
            group = groupsRepository.save(group);
            GroupDto groupDto1 = new GroupDto();
            groupDto1.setTitle(group.getTitle());
            groupDto1.setName(group.getName());
            groupDto1.setDeleted(false);

            return groupDto1;
        }
        throw new DeprivedOfRightsException("Нет прав для обновления группы");
    }

    @Transactional
    public ArrayList<UserDto> searchUser(UserFilter userFilter) {
        List<User> all = usersRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (userFilter.getFirstName() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("firstName"), userFilter.getFirstName()));
                }
                if (userFilter.getLastName() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("lastName"), userFilter.getLastName()));
                }
                if (userFilter.getPatronymic() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("patronymic"), userFilter.getPatronymic()));
                }
                if (userFilter.getGroup() != null) {
                    Join<Object, Object> groups = root.join("groups");
                    predicates.add(criteriaBuilder.equal(groups.get("name"), userFilter.getGroup()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
        ArrayList<UserDto> result = new ArrayList<>();
        for (User user : all) {
            result.add(convertUser(user));
        }
        return result;
    }

    @Transactional
    public ArrayList<GroupDto> searchGroup(GroupFilter groupFilter) {
        List<Group> all = groupsRepository.findAll(new Specification<Group>() {
            @Override
            public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (groupFilter.getName() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("name"), groupFilter.getName()));
                }
                if (groupFilter.getOwner() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("owner"), groupFilter.getOwner()));
                }
                if (groupFilter.getTitle() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("title"), groupFilter.getTitle()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
        ArrayList<GroupDto> result = new ArrayList<>();
        for (Group group : all) {
            result.add(convertGroup(group));
        }
        return result;
    }

    @Transactional
    public void addUserToGroup(String username, String groupName) throws DeprivedOfRightsException {
        User user = usersRepository.findByUsername(username);
        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Group group = groupsRepository.getByName(groupName);
        if (isAdmin() || group.getOwner().equals(currentUser.getUsername())) {
            if (group.getUsers() == null) {
                group.setUsers(new ArrayList<>());
            }
            group.getUsers().add(user);
            groupsRepository.save(group);
        } else {
            throw new DeprivedOfRightsException("Нет прав для добавления пользователя");
        }
    }

    @Transactional
    public void removeUserFromGroup(String username, String groupName) throws DeprivedOfRightsException {
        User user = usersRepository.findByUsername(username);
        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Group group = groupsRepository.getByName(groupName);
        if (isAdmin() || group.getOwner().equals(currentUser.getUsername())) {
            group.getUsers().remove(user);
            groupsRepository.save(group);
        } else {
            throw new DeprivedOfRightsException("Нет прав для удаления пользователя");
        }
    }

    private GroupDto convertGroup(Group group) {
        GroupDto groupDto = new GroupDto();
        groupDto.setName(group.getName());
        groupDto.setOwner(group.getOwner());
        groupDto.setTitle(group.getTitle());
        groupDto.setDeleted(group.getDeleted());
        groupDto.setId(group.getId());
        ArrayList<UserDto> users = new ArrayList<>();
        if (group.getUsers() != null) {
            for (User g : group.getUsers()) {
                UserDto userDto = new UserDto();
                userDto.setUsername(g.getUsername());
                userDto.setPatronymic(g.getPatronymic());
                userDto.setFirstName(g.getFirstName());
                userDto.setLastName(g.getLastName());
                userDto.setDeleted(g.getDeleted());
                users.add(userDto);
            }
        }
        groupDto.setUsers(users);
        return groupDto;
    }

    private UserDto convertUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setLastName(user.getLastName());
        userDto.setFirstName(user.getFirstName());
        userDto.setPatronymic(user.getPatronymic());
        userDto.setDeleted(user.getDeleted());
        ArrayList<GroupDto> group = new ArrayList<>();
        if (user.getGroups() != null) {
            for (Group g : user.getGroups()) {
                GroupDto groupDto = new GroupDto();
                groupDto.setName(g.getName());
                groupDto.setOwner(g.getOwner());
                groupDto.setTitle(g.getTitle());
                groupDto.setDeleted(g.getDeleted());
                groupDto.setId(g.getId());
                group.add(groupDto);
            }
        }
        userDto.setGroups(group);
        return userDto;
    }

    private boolean isAdmin() {
        org.springframework.security.core.userdetails.User currentUser
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (GrantedAuthority authority : currentUser.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMINS")) {
                return true;
            }
        }
        return false;
    }
}
