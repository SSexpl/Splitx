package split.controller.Protected.Admin;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import split.Exceptions.GeneralServerException;
import split.Exceptions.MultipleEntryException;
import split.Exceptions.NotFoundException;
import split.Exceptions.SQLQueryException;
import split.model.Users;
import split.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    UserService userService;

    //this is to get the detals of a particular user from the db just user details no expenses or shares
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable String id) throws SQLQueryException, NotFoundException, GeneralServerException, MultipleEntryException {
        //for this Id get the user from

        Users user= userService.getUserById(id);
        try {
            if (user!=null)
                return new ResponseEntity<>(user, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    //this is sort of Admin control
    //not needed
    @GetMapping("getAllUsers")
    public ResponseEntity<List<Users>>getAllUsers()
    {
        try {
            List<Users> allUsers = userService.getAllUsers();
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
        catch(Exception e)
        {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //admin control
    //not needed
    @PostMapping("addUser")
    public ResponseEntity<?> addUser(@RequestBody @Valid Users user) throws SQLQueryException, GeneralServerException {
        System.out.println("user controller "+ user);
        int count_of_affected_rows = userService.addUser(user.getUserName(), user.getEmail(), user.getPassword());
        try {
            if (count_of_affected_rows == 0)
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            else
                return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    //admin control
    @PostMapping("deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody Users user) throws SQLQueryException, GeneralServerException {
        System.out.println("user controller "+ user);
        int count_of_affected_rows = userService.deleteUser(user.getId());
        try {
            if (count_of_affected_rows == 0)
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            else
                return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
