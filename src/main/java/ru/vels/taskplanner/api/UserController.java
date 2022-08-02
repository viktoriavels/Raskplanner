package ru.vels.taskplanner.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vels.taskplanner.dto.UserDto;
import ru.vels.taskplanner.dto.UserFilter;
import ru.vels.taskplanner.exception.DeprivedOfRightsException;
import ru.vels.taskplanner.exception.DuplicateException;
import ru.vels.taskplanner.service.AuthService;

import java.util.ArrayList;

@RestController
public class UserController {

    @Autowired
    AuthService authService;

    @PostMapping(path = "/user/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto createUser(@RequestBody UserDto newUser) throws DuplicateException {
        return authService.addUser(newUser);
    }

    @GetMapping(path = " /user/{username}", produces = "application/json")
    public UserDto getUserInfo(@PathVariable String username) {
        return authService.getUserInfo(username);
    }

    @PatchMapping(path = "/user/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto updateUser(@RequestBody UserDto updateUserDto) throws DeprivedOfRightsException {
        return authService.updateUserDto(updateUserDto);
    }

    @DeleteMapping(path = "/user/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) throws DeprivedOfRightsException {
        authService.removeUser(username);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/user/search",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<UserDto> searchUser(@RequestBody UserFilter filter) {
        return authService.searchUser(filter);
    }

    @ExceptionHandler(DeprivedOfRightsException.class)
    public ResponseEntity handleException(DeprivedOfRightsException e) {
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity handleException(DuplicateException e) {
        return new ResponseEntity(HttpStatus.CONFLICT);
    }
}

