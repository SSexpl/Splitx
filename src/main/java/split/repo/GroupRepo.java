package split.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import split.Exceptions.SQLQueryException;

@Repository
public class GroupRepo {

    @Autowired
    private  JdbcTemplate jdbcTemplate;

    public int addGroup(String groupName, String groupDescription) throws SQLQueryException {
        try {
            String sql = "INSERT INTO groups(group_name,group_description) VALUES (?, ?)";
            return jdbcTemplate.update(sql, groupName, groupDescription);
        }
        catch(Exception e)
        {
            throw new SQLQueryException(e.getMessage());
        }
    }

    public int deleteGroup(String groupId) throws SQLQueryException {
        try {
            String sql = "DELETE from group_mapper WHERE group_id = ?";
            return jdbcTemplate.update(sql, groupId);
        }
        catch(Exception e)
        {
            throw new SQLQueryException(e.getMessage());
        }
    }
}
