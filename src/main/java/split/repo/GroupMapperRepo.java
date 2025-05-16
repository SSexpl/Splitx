package split.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import split.Exceptions.SQLQueryException;

import java.util.List;


@Repository
public class GroupMapperRepo{
    @Autowired
    private  JdbcTemplate jdbcTemplate;

    //Inserts a new group entry into the db .
    public int addToGroupRepositoryMapper(String userId, String groupId) throws SQLQueryException {

        try {
            String sql = "INSERT INTO group_mapper(user_id,group_id) VALUES (?, ?)";

            return jdbcTemplate.update(sql, userId, groupId);
        }
        catch(Exception e)
        {
            throw new SQLQueryException(e.getMessage());

        }
    }

    //remove a user from a group
    public int removeFromGroupUser(String userId, String groupId) throws SQLQueryException {
        try {
            String sql = "DELETE from group_mapper WHERE user_id = ? AND group_id = ?";
            return jdbcTemplate.update(sql, userId, groupId);
        }
        catch (Exception e)
        {
            throw new SQLQueryException(e.getMessage());
        }
    }
    // Get all user IDs in a group
    public List<String> usersInAGroup(String groupId) throws SQLQueryException {

        try {
            String sql = "SELECT user_id FROM group_mapper WHERE group_id = ?";

            return jdbcTemplate.queryForList(sql, String.class, groupId);
        }
        catch (Exception e){
            throw new SQLQueryException(e.getMessage());

        }
    }
    //get all the group user is part of
    public List<String> usersIsPartOf(String userId) throws SQLQueryException {

        try {
            String sql = "SELECT group_id FROM group_mapper WHERE user_id = ?";

            return jdbcTemplate.queryForList(sql, String.class, userId);
        }
        catch (Exception e){
            throw new SQLQueryException(e.getMessage());

        }
    }

}
