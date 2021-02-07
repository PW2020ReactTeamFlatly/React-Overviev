package pw.react.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.dao.UserRepository;
import pw.react.backend.model.*;
import pw.react.backend.service.*;
import pw.react.backend.utils.LoginRequest;

import java.util.*;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(FlatController.class);

    private final UserRepository userRepository;

    private final SecurityProvider securityService;

    @Autowired
    public UserController(UserRepository userRepository, SecurityProvider securityService) {
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "")
    public ResponseEntity<String>  getKey(@RequestBody  Collection<LoginRequest> requests){

        // ONLY FOR PRESENTATION PURPOSES
        if(userRepository.count() < 2)
        {
            User admin_user = new User("admin", "admin", securityService.generateNewUUID(), "admin user", "admin user");
            userRepository.save(admin_user);
            User bookly_user = new User("bookly", "bookly", securityService.generateNewUUID(), "bookly user", "bookly user");
            userRepository.save(bookly_user);
        }
        //////////////////////////

        if(requests.size() > 1)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brute force attack?");


        Collection<User> users = userRepository.findAll();
        Iterator<User> iterator = users.iterator();
        Iterator<LoginRequest> log_it = requests.iterator();

        while(log_it.hasNext())
        {
            LoginRequest request = log_it.next();

            while (iterator.hasNext()) {
                User curUser = iterator.next();
                if(Objects.equals(curUser.getUser_login(), request.getLogin()))
                {
                    if(Objects.equals(curUser.getUser_password(), request.getPassword()))
                    {
                        return ResponseEntity.ok(curUser.getUser_key());
                    }
                }
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad login or password");
    }
}
