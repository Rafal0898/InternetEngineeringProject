package recipe.controller;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.DecodeException;

import org.apache.tomcat.util.json.JSONParser;

import java.util.Map;

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

// TODO: 04.02.2020 naprawic wyszukiwanie uzytkownikow
//    @GetMapping(value = "/user/{id}")
//    public ResponseEntity<String> getUser(@PathVariable("id") int userId) {
//        List<User> userList = userRepository.getAllUsers();
//        if (checkIfExists(userList, userId)) {
//            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
//        }
//        User user = userRepository.getUserByUser_id(userId);
//        String json = jsonCreator.createJsonForObject(user);
//        return new ResponseEntity<>(json, HttpStatus.OK);
//    }
//    @GetMapping(value = "/user")
//    public ResponseEntity<String> getUsersByParameters(@RequestParam(value = "name", required = false) String name,
//                                              @RequestParam(value = "favourite_dish", required = false, defaultValue = "0") int favouriteDishId,
//                                              @RequestParam(value = "limit", required = false, defaultValue = "20")int limit,
//                                              @RequestParam(value = "offset", required = false, defaultValue = "0")int offset) {
//        List<User> userList = userRepository.getUsersByParameters(name, favouriteDishId, limit, offset);
//        if(userList.isEmpty()){
//            return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
//        }
//        String json = jsonCreator.createJsonForObject(userList);
//        return  new ResponseEntity<>(json, HttpStatus.OK);
//    }

    @PostMapping(value = "/user/login")
    public ResponseEntity<String> login(@RequestBody String body) throws DecodeException {
        JSONParser parser = new JSONParser(body);
        try {
            Map<String, String> bodyContent = (Map<String, String>) parser.parse();
            String username = bodyContent.get("username");
            String password = bodyContent.get("password");
            User user = userRepository.getUserByUsername(username);
            if (user == null) {
                return new ResponseEntity<>(Responses.NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            if (user.checkPassword(password)) {
                String token = JsonWebTokenUtils.getJsonWebToken(user);
                return new ResponseEntity<>(token, HttpStatus.OK);
            }
            return new ResponseEntity<>(Responses.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/user/create")
    public ResponseEntity<String> createNewUser(@RequestBody String body) {
        JSONParser parser = new JSONParser(body);
        try {
            Map<String, String> bodyContent = (Map<String, String>) parser.parse();
            String username = bodyContent.get("username");
            String password = bodyContent.get("password");
            User user = userRepository.getUserByUsername(username);
            if (user != null) {
                return new ResponseEntity<>(Responses.ALREADY_EXIST, HttpStatus.CONFLICT);
            }
            User newUser = new User(username, password);
            User savedUser = userRepository.save(newUser);
            String token = JsonWebTokenUtils.getJsonWebToken(savedUser);
            return new ResponseEntity<>(Responses.ADDED + token, HttpStatus.CREATED);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(Responses.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
    }
}