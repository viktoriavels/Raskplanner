package ru.vels.taskplanner.api;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vels.taskplanner.dto.UserDto;

import ru.vels.taskplanner.exception.DuplicateUsernameException;
import ru.vels.taskplanner.service.AuthService;


@RestController

public class UserController {
    @Autowired
    private AuthService authService;

    @PostMapping(path = "/user/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto createUser(@RequestBody UserDto newUser) throws DuplicateUsernameException {
        return  authService.addUser(newUser);
    }
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity handleException(DuplicateUsernameException e) {
        return new ResponseEntity(HttpStatus.CONFLICT );
    }

}

