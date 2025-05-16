package split.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import split.Exceptions.GeneralServerException;
import split.Exceptions.MultipleEntryException;
import split.Exceptions.NotFoundException;
import split.Exceptions.SQLQueryException;
import split.model.Users;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository  // Marks this class as a data repository
public class UserRepo {

    private static final Logger log = LoggerFactory.getLogger(UserRepo.class);
    @Autowired
    private  JdbcTemplate jdbcTemplate;



    // CREATE: Insert a new user
    public int addUser(String userName, String email,String password ) throws SQLQueryException , GeneralServerException {
        try {
            String sql = "INSERT INTO users(user_name, email, password) VALUES (?, ?, ?)";
            return jdbcTemplate.update(sql, userName, email, password);
        }//here ID will be auto generated hence there is no need to append.

        catch(DataAccessException e)
        {
            log.error("Exception Type: " + e.getClass().getName());
            e.printStackTrace();
            throw new SQLQueryException("Error in adding user to the database");
        }
        catch(Exception e)
        {
            log.error("Exception Type: " + e.getClass().getName());
            e.printStackTrace();
            log.error("Error in adding user to the database");
            throw new GeneralServerException("Error in adding user to the database");
        }
    }

    // READ: Get all users
    public List<Users> getAllUsers() throws SQLQueryException, GeneralServerException {
        String sql = "SELECT * FROM users";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Users user = new Users();
                user.setId(rs.getString("id"));
                user.setUserName(rs.getString("user_name")); //the column name is user_name in DB and that is mapped to object in class'
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                return user;
            });
        } catch (DataAccessException e) {
            log.error("Exception Type: " + e.getClass().getName());
            e.printStackTrace();
            throw new SQLQueryException("Error in getting all users from the database");

        } catch (Exception e) {
            log.error("Exception Type: " + e.getClass().getName());
            e.printStackTrace();
            log.error("Error in getting all users from the database");
            throw new GeneralServerException("Error in getting all users from the database");
        }
    }

    // READ: Get user by ID
    public Users getUserById(String id) throws NotFoundException, SQLQueryException, GeneralServerException, MultipleEntryException {
        try {
            String sql = "SELECT id, user_name, email, password FROM users WHERE id = ?";
            //queryForObject this will not return null if no found
            //queryForObject will throw EmptyResultDataAccessException if no result is found
            //queryForObject will throw IncorrectResultSizeDataAccessException if more than one result is found


            return jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(Users.class),
                    id
            );
        }
        catch (EmptyResultDataAccessException e)
        {
            log.error("Exception Type: " + e.getClass().getName());
            e.printStackTrace();
            throw new NotFoundException("User not in  database");
        }
        catch (IncorrectResultSizeDataAccessException e)
        {
            log.error("Exception Type: " + e.getClass().getName());
            e.printStackTrace();
            throw  new MultipleEntryException("Multiple entries found for the user");

        }
        catch (DataAccessException e) {
            log.error("Exception Type: " + e.getClass().getName());
            e.printStackTrace();
            throw new SQLQueryException("Error in getting all users from the database");

        }
        catch (Exception e) {
            log.error("Exception Type: " + e.getClass().getName());
            e.printStackTrace();
            log.error("Error in getting all users from the database");
            throw new GeneralServerException("Error in getting all users from the database");
        }
        //BeanPropertyRowMapper is used to map the resultset to the object of the class
        //but this follows the convention of the columnName and object type interconversion
        //example user_name in db is mapped to userName in class
        //and users table is mapped to Users class
    }


    // UPDATE: Update user email
    public int updateUserEmail(String id, String newEmail) {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newEmail, id);
    }

    // DELETE: Delete user by ID
    public int deleteUser(String id) throws SQLQueryException, GeneralServerException {

        try {
            String sql = "DELETE FROM users WHERE id = ?";

            return jdbcTemplate.update(sql, id);
        }
        catch (DataAccessException e) {
            log.error("Exception Type: " + e.getClass().getName());
            e.printStackTrace();
            throw new SQLQueryException("Error in getting all users from the database");

        }
        catch (Exception e) {
            log.error("Exception Type: " + e.getClass().getName());
            e.printStackTrace();
            log.error("Error in getting all users from the database");
            throw new GeneralServerException("Error in getting all users from the database");
        }
    }
    //function to fetch a user by his/her email
    public Users getUserByEmail(String email ) throws GeneralServerException {
        try
        {
            String sql = "Select * from users where email = ? ";

            return jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(Users.class),
                    email
            );
        }
        catch (Exception e) {
            log.error("Exception Type: " + e.getClass().getName());
            e.printStackTrace();
            log.error("Error in getting all users from the database");
            throw new GeneralServerException("Error in getting all users from the database");
        }
    }
}
