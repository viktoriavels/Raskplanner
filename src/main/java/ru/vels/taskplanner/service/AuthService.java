package ru.vels.taskplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.vels.taskplanner.dto.UserDto;
import ru.vels.taskplanner.entity.User;
import ru.vels.taskplanner.exception.DuplicateUsernameException;
import ru.vels.taskplanner.repo.UsersRepository;

@Component
public class AuthService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto addUser(UserDto userDto) throws DuplicateUsernameException {
        if(usersRepository.findByUsername(userDto.getUsername())!=null){
            throw new DuplicateUsernameException("Duplicate username");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPatronymic(userDto.getPatronymic());
        user.setDeleted(false);
        user.setLastName(userDto.getLastName());
        user.setFirstName(userDto.getFirstName());
        user.setSecret(passwordEncoder.encode(userDto.getSecret()));
        usersRepository.save(user);
        UserDto userDto1 =new UserDto();
        userDto1.setUsername(user.getUsername());
        userDto1.setFirstName(user.getFirstName());
        userDto1.setDeleted(false);
        userDto1.setPatronymic(user.getPatronymic());
        userDto1.setLastName(user.getLastName());
        return userDto1;
    }

}
