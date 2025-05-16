package split.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import split.Exceptions.SQLQueryException;
import split.model.Expenses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ExpenseRepo {

      @Autowired
      private JdbcTemplate jdbcTemplate;
    //add a expense to table

    public int addExpense(Expenses expense) throws SQLQueryException {

        try {
            String sql = "INSERT INTO expenses(id, creator_id, group_id, expense_name, expense_description, amount) VALUES (?, ?, ?, ?, ?, ?)";
            return jdbcTemplate.update(sql,
                    expense.getId(),
                    expense.getCreatorId(),
                    expense.getGroupId(),
                    expense.getExpenseName(),
                    expense.getExpenseDescription(),
                    expense.getAmount()
            );
        }
        catch (Exception e)
        {
            throw new SQLQueryException(e.getMessage());
        }

    }

    public int deleteExpense(String expenseId) throws SQLQueryException {
        try {
            String sql = "DELETE from expenses WHERE id = ? ";
            return jdbcTemplate.update(sql, expenseId);
        }
        catch (Exception e)
        {
            throw new SQLQueryException(e.getMessage());
        }
    }

    public Expenses getExpense(String expenseId) throws SQLQueryException {
        try
        {
        String sql="SELECT * FROM expenses WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{expenseId},new BeanPropertyRowMapper<>(Expenses.class));
        }
        catch(Exception e)
        {
            throw new SQLQueryException(e.getMessage());
        }
    }

    public Map<String ,Double> getExpenseShare(String expenseId) throws SQLQueryException {
        try {
            String sql = "SELECT user_id,amount FROM expenses_table WHERE expense_id = ?";
            List<Map<String, Double>> tempRes = jdbcTemplate.query(sql, (rs, rowNum) -> {

                return Map.of(rs.getString("user_id"), rs.getDouble("amount"));
            }, expenseId);
            //Now we traverse the list and add all the entries to a single map
            Map<String, Double> res = new HashMap<>();
            for (Map<String, Double> entry : tempRes) {
                res.putAll(entry);
            }
            System.out.println(res);
            return res;
        }
        catch (Exception e)
        {
            throw new SQLQueryException(e.getMessage());
        }
    }

}
