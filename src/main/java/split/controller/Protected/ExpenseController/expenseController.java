package split.controller.Protected.ExpenseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;
import split.Exceptions.SQLQueryException;
import split.model.DTO.ExpenseDTO;
import split.model.Expenses;
import split.model.ExpensesTable;
import split.repo.ExpensesTableRepo;
import split.service.ExpenseService;
import split.service.GroupMapperService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expense")
public class expenseController {

    @Autowired
    private ExpensesTableRepo expenseTableRepo;

    @Autowired
    GroupMapperService groupMapperService;

    @Autowired
    ExpenseService expenseService;


//    // 1. add a specific expense to the expenses table for each user for a expenseId and amt.
//    //Not in scope right now
//    @PostMapping("/addExpensefor2")
//    public ResponseEntity<?> addExpense(@RequestBody ExpensesTable expense) {
//        try {
//            expenseTableRepo.addExpense(expense.getExpenseId(), expense.getGroupId(), expense.getCreatorId(), expense.getUserId(), expense.getAmount());
//            return new ResponseEntity<>(HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//
//    }

    //this route will be used to add an expense of a group => add expense definition amd share of each used
    //Permission the person should be in the group not an outside per group this check could be done at the frontend
   // We can put a group verification for the logged in user if the user doesnot belong to the group cancel this expenses
   //return an unauthorized response
    @PostMapping("/addExpense")
    public ResponseEntity<?> addExpense(@RequestBody ExpenseDTO expenseDTO) {
        try {
            expenseService.createExpenseAndShareOfEach(expenseDTO.getExpense(), expenseDTO.getShare());
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Settle up bw user A and user B to be used not user specific
    //permission - only the current logged in user should be able to settle up his case with others
    //check- db we will pass only the correct userId

    @PostMapping("/settleUp")
    public ResponseEntity<?> settleUp(@RequestBody Map<String, Object> tmp) {
        System.out.println(tmp);
        try {
            String userId1 = (String) tmp.get("userId1");
            String userId2 = (String) tmp.get("userId2");
            String groupId = (String) tmp.get("groupId");
            expenseTableRepo.settleUp(userId1, userId2, groupId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //not used
    @PostMapping("/getBalancebw2")
    public ResponseEntity<?> getBalancebw2(@RequestBody Map<String, Object> tmp) {
        try {
            String userId1 = (String) tmp.get("userId1");
            String userId2 = (String) tmp.get("userId2");

            String groupId = (String) tmp.get("groupId");
            double balance = expenseTableRepo.getAmountOwed(userId1, userId2, groupId);
            return new ResponseEntity<>(balance, HttpStatus.OK);
            //this returns how much userA should get from userB
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get the ownings of a particular user in a group how much the current logged in users has share with other people in the group
    //used User specific
    @PostMapping("/getowed")
    public Map<String, Double> getOwed(@RequestBody Map<String, String> tmp) throws SQLQueryException {
        // Get groupId and userId from request body
        String groupId = tmp.get("groupId");
        String userId = tmp.get("userId"); //this should come from the jwt for the current logged in user

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

    //for the current user get all his or her expenses for a particular group
    // used
    @PostMapping("/getAllExpenses")
    public ResponseEntity<?> getAllExpenseofUser(@RequestBody Map<String,String>temp) {
        String userId = temp.get("userId"); //for the current logged in user get all the expenses of his or her
        String groupId = temp.get("groupId");
        try {
            return new ResponseEntity<>(expenseTableRepo.getUserExpenses(userId, groupId), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    //details of a particular expense
    //to be used on clicking on the screen we should be able to see the expense detail and the share of each user.
    //permission current logged in user should have this particular expense. as one of the expense.
    //check in db if there is a column with this userId, expenseId pair.
    @GetMapping("/getExpense/{id}")
    public ResponseEntity<?> getExpense(@PathVariable String id) {
        try {
            //we will get the id of the expense and we need to return all the details of the expense with share of each
            return new ResponseEntity<>(expenseService.getExpense(id), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

