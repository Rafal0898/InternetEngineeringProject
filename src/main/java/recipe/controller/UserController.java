package recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

import recipe.database.entities.User;
import recipe.database.repositories.UserRepository;
import recipe.json.JsonCreator;
import recipe.json.JsonWebTokenUtils;
import recipe.json.Responses;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JsonCreator jsonCreator;

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<String> getUser(@PathVariable("id") int userId) {
        List<User> userList = userRepository.getAllUsers();
        if (checkIfExists(userList, userId)) {
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        User user = userRepository.getUserByUser_id(userId);
        String json = jsonCreator.createJsonForObject(user);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping(value = "/user")
    public ResponseEntity<String> getAllUsers(@RequestParam(value = "name", required = false) String name,
                                              @RequestParam(value = "favourite_dish", required = false) int favouriteDishId,
                                              @RequestParam(value = "limit", required = false, defaultValue = "20")int limit,
                                              @RequestParam(value = "offset", required = false, defaultValue = "0")int offset) {
        List<User> userList = userRepository.getUsersByParameters(name, favouriteDishId, limit, offset);
        if(userList.isEmpty()){
            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        String json = jsonCreator.createJsonForObject(userList);
        return  new ResponseEntity<>(json, HttpStatus.OK);

    }

    @PostMapping(value = "/user/login")
    public ResponseEntity<String> getToken() {
        User u = new User();
        User savedUser = userRepository.save(u);
        String token = JsonWebTokenUtils.getJsonWebToken(savedUser);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    private boolean checkIfExists(List<User> userList, int userId) {
        for (User user : userList) {
            if (Objects.equals(user.getUser_id(), userId)) {
                return false;
            }
        }
        return true;
    }
}

