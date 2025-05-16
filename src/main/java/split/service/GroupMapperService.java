package split.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import split.Exceptions.SQLQueryException;
import split.repo.GroupMapperRepo;

import java.util.List;

@Service
public class GroupMapperService {

    //add to group_mapper table and delete from group_mapper table
    @Autowired
    GroupMapperRepo groupMapperRepo;
    public int addToGroupRepositoryMapper(String userId, String groupId) throws SQLQueryException {
        //add to group_mapper table
       return groupMapperRepo.addToGroupRepositoryMapper(userId, groupId);
    }

    public int removeFromGroupUser(String userId, String groupId) throws SQLQueryException {
        //delete from group_mapper table
        return groupMapperRepo.removeFromGroupUser(userId, groupId);
    }
    public List<String> usersInAGroup(String groupId) throws SQLQueryException {
        return groupMapperRepo.usersInAGroup(groupId);
    }
    public List<String> groupsUserisPartOf(String userId) throws SQLQueryException{
        return groupMapperRepo.usersIsPartOf(userId);
    }
}
