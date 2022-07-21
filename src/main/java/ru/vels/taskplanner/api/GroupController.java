package ru.vels.taskplanner.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vels.taskplanner.dto.GroupDto;
import ru.vels.taskplanner.dto.GroupFilter;
import ru.vels.taskplanner.exception.DeprivedOfRightsException;
import ru.vels.taskplanner.exception.DuplicateException;
import ru.vels.taskplanner.service.AuthService;

import java.util.ArrayList;

@RestController
public class GroupController {
    @Autowired
    private AuthService authService;

    @DeleteMapping(path = "/group/{groupName}")
    public ResponseEntity<Void> deleteGroup(@PathVariable String groupName) throws DeprivedOfRightsException {
        authService.removeGroup(groupName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/group")
    public GroupDto createGroup(@RequestBody GroupDto groupDto) throws DuplicateException {
        return authService.createGroup(groupDto);
    }

    @PutMapping("/group/{groupName}/user/{username}")

    public void addUserToGroup(@PathVariable String username, @PathVariable String groupName) throws DeprivedOfRightsException {
        authService.addUserToGroup(username, groupName);
    }
    @PostMapping(path = "/group/search",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<GroupDto> searchGroup(@RequestBody GroupFilter filter) throws DuplicateException {
        return authService.searchGroup(filter);
    }

    @DeleteMapping("/group/{groupName}/user/{username}")
    public void removeUserFromGroup(@PathVariable String username,@PathVariable String groupName) throws DeprivedOfRightsException {
        authService.removeUserFromGroup(username, groupName);
    }
    @GetMapping(path = "/group/{groupName}", produces = "application/json")
    public GroupDto getGroupInfo(@RequestBody String name) {
        return authService.getGroupInfo(name);
    }
    @PatchMapping(path = "/group/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupDto updateGroup(@RequestBody GroupDto updateGroupDto) throws DuplicateException, DeprivedOfRightsException {
        return authService.updateGroupDto(updateGroupDto);
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
