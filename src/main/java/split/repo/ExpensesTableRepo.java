package split.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import split.Exceptions.SQLQueryException;
import split.model.Expenses;
import split.model.ExpensesTable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ExpensesTableRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    //1. add a specific expense to the expenses table
    //for each user in the group there should be an entry in the expenses table
    public int addExpense(String expenseId, String groupId, String creatorId, String userId, double amount) throws SQLQueryException {
        try {
            String sql = "INSERT INTO expenses_table(group_id,creator_id,user_id,expense_id,amount) VALUES (?, ?, ?, ?, ?)";
            return jdbcTemplate.update(sql, groupId, creatorId, userId, expenseId, amount);
        }
        catch (Exception e)
        {
            throw new SQLQueryException(e.getMessage());

        }
    }

    //2. delete expense for a particular user
    public int deleteExpense(String expenseId, String userId) throws SQLQueryException {
        try {
            String sql = "DELETE from expenses_table WHERE expense_id = ? AND user_id = ?";
            return jdbcTemplate.update(sql, expenseId, userId);
        }
        catch(Exception e)
        {
            throw new SQLQueryException(e.getMessage());
        }

    }

    //3. settle up bw a user A and a userB
    //deletes all the expenses between userA and userB
    //remove all instances where userA is the creator and userB is the user and vice versa
    public int settleUp(String userIdA, String userIdB, String groupId) throws SQLQueryException {

       try{

        String sql1 = "DELETE from expenses_table WHERE creator_id = ? AND user_id = ? AND group_id = ?";
        jdbcTemplate.update(sql1, userIdA, userIdB, groupId);

        String spl2 = "DELETE from expenses_table WHERE creator_id = ? AND user_id = ? AND group_id = ?";
        return jdbcTemplate.update(spl2, userIdB, userIdA, groupId);
        }
       catch (Exception e)
       {
           throw new SQLQueryException(e.getMessage());
       }
    }

    //4. get all expenses for a particular user
    public List<ExpensesTable> getExpensesForUser(String userId) throws SQLQueryException {
        try {
            String sql = "SELECT * FROM expenses_table WHERE user_id = ?";
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                ExpensesTable expense = new ExpensesTable();
                expense.setGroupId(rs.getString("group_id"));
                expense.setCreatorId(rs.getString("creator_id"));
                expense.setUserId(rs.getString("user_id"));
                expense.setExpenseId(rs.getString("expense_id"));
                expense.setAmount(rs.getDouble("amount"));
                return expense;
            }, userId);
        }
        catch (Exception e)
        {
            throw new SQLQueryException(e.getMessage());
        }
    }

    //get how much a user owes to other person in a group
    public double getAmountOwed(String user1Id, String user2Id, String groupId) throws SQLQueryException {

       try {
           String sql1 = "SELECT SUM(amount) FROM expenses_table WHERE creator_id = ? AND user_Id = ? AND group_id = ? ";
           double getBack = (double) Optional.ofNullable(
                   jdbcTemplate.queryForObject(sql1, Double.class, user1Id, user2Id, groupId)
           ).orElse(0.0);


           String sql2 = "SELECT SUM(amount) FROM expenses_table WHERE creator_id= ? AND user_id = ? AND group_id = ?";
           double giveBack = (double) Optional.ofNullable(
                   jdbcTemplate.queryForObject(sql1, Double.class, user2Id, user1Id, groupId)
           ).orElse(0.0);

           return getBack - giveBack;
       }
         catch (Exception e)
         {
             throw  new SQLQueryException(e.getMessage());
         }

    }

    //Split an expense between all the users in a group
    public int splitExpense(String expenseId, String groupId, String creatorId, List<Map<String, Double>> users, double amount) throws SQLQueryException {

     try {   //this function will add all the enteries of users in the expense table
         String sql = "INSERT INTO expenses_table(group_id,creator_id,expense_id,amount) VALUES (?, ?, ?, ?)";
         return jdbcTemplate.update(sql, groupId, creatorId, expenseId, amount);
     }
     catch(Exception e)
     {
         throw  new SQLQueryException(e.getMessage());
     }
    }

    public List<Expenses> getUserExpenses(String userId, String groupId) throws SQLQueryException {

       try {
           String sql = "SELECT DISTINCT e.id, e.expense_name, e.expense_description, " +
                   "e.creator_id, e.group_id, e.amount " +
                   "FROM expenses e " +
                   "INNER JOIN expenses_table et ON e.id = et.expense_id " +
                   "WHERE e.group_id = ? AND (et.user_id = ? OR et.creator_id = ?)";

           List<Expenses> toReturn = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Expenses.class), groupId, userId, userId);
           System.out.println(toReturn);
           return toReturn;
       }
       catch (Exception e)
       {
           throw new SQLQueryException(e.getMessage());

       }

    }


}
