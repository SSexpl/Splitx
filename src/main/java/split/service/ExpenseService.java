package split.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import split.Exceptions.SQLQueryException;
import split.model.DTO.ExpenseDTO;
import split.model.Expenses;
import split.repo.ExpenseRepo;
import split.repo.ExpensesTableRepo;

import java.util.Map;

@Service
public class ExpenseService {

    @Autowired
    ExpensesTableRepo expensesTableRepo;
    @Autowired
    ExpenseRepo expenseRepo;

    public int createExpenseAndShareOfEach(Expenses expense , Map<String , Double> share) throws SQLQueryException {
       //first add the expense definition
        int ans1= expenseRepo.addExpense(expense);

      //then the share of each of the user in Expensetable for each users share in that particular split expense.
        for (Map.Entry<String, Double> entry : share.entrySet()) {
            String userId = entry.getKey();
            Double amount = entry.getValue();

            expensesTableRepo.addExpense(
                    expense.getId(),
                    expense.getGroupId(),
                    expense.getCreatorId(),
                    userId,  // Iterate over userId
                    amount   // Iterate over amount
            );
        }
        return 1;
    }
    //returns the expense definition and share of each person
    public ExpenseDTO getExpense(String expenseId) throws SQLQueryException {
       Expenses expenses= expenseRepo.getExpense(expenseId);

       Map<String,Double> share=expenseRepo.getExpenseShare(expenseId);

         ExpenseDTO expenseDTO=new ExpenseDTO();
         expenseDTO.setExpense(expenses);
         expenseDTO.setShare(share);

         return expenseDTO;
    }
}
