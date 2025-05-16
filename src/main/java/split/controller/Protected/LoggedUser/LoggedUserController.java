package split.controller.Protected.LoggedUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import split.Exceptions.SQLQueryException;
import split.model.Users;
import split.repo.ExpensesTableRepo;
import split.service.GroupMapperService;
import split.utils.AuthDetails;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("logged")
public class LoggedUserController {

    @Autowired
    GroupMapperService groupMapperService;

    @Autowired
    ExpensesTableRepo expenseTableRepo;
    //1. get the details of the current logged in user
    @GetMapping
    public ResponseEntity getCurrentUser()
    {
        //from the authDetail fetch this

            Users loggedIn= AuthDetails.getCurrentUserDetails();
            return new ResponseEntity<>(loggedIn, HttpStatus.ACCEPTED);
    }

    //for current user specific to a group how much he ows to a person.
    @PostMapping("/getowed")
    public Map<String, Double> getOwed(@RequestBody Map<String, String> tmp) throws SQLQueryException {
        // Get groupId
        String groupId = tmp.get("groupId");
        //this should come from the jwt for the current logged in user
        String userId = AuthDetails.getCurrentUserDetails().getId();



        // Fetch all users of that group
        List<String> users = groupMapperService.usersInAGroup(groupId);

        // Create a map to store the balance
        Map<String, Double> temp = new HashMap<>();

        // Iterate through users and calculate balance
        for (String user : users) {
            if (user.equals(userId))  // Use .equals() instead of ==
                continue;

            double balance = expenseTableRepo.getAmountOwed(userId, user, groupId);
            temp.put(user, balance);  // Corrected the syntax of put()
        }
        return temp;  // Added return statement
    }

    //get all expenses of a user specific to a group.
    @PostMapping("/getAllExpenses")
    public ResponseEntity<?> getAllExpenseofUser(@RequestBody Map<String,String>temp) {
        String userId = AuthDetails.getCurrentUserDetails().getId();
        String groupId = temp.get("groupId");
        try {
            return new ResponseEntity<>(expenseTableRepo.getUserExpenses(userId, groupId), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
 //add a endpoint to get the groups of which the current user is part of
    @GetMapping("/groups")
    public ResponseEntity<?>getAllGroupsOfWhichUserIsPart() throws SQLQueryException {
        String userId=AuthDetails.getCurrentUserDetails().getId();

        return new ResponseEntity<>(groupMapperService.groupsUserisPartOf(userId),HttpStatus.OK);
    }


}
