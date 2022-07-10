package ru.vels.taskplanner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vels.taskplanner.repo.UsersRepository;
import ru.vels.taskplanner.entity.Group;
import ru.vels.taskplanner.entity.User;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UsersDetailServiceInd implements UserDetailsService {

    private final UsersRepository userDao;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Group group : user.getGroups()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + group.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getSecret(), grantedAuthorities);
    }
}
