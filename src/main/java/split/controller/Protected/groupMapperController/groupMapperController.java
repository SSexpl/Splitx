package split.controller.Protected.groupMapperController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import split.model.GroupMapper;
import split.model.Users;
import split.service.GroupMapperService;
import split.utils.AuthDetails;

import java.util.List;

@RestController
@RequestMapping("/groupMapper")
public class groupMapperController {

    @Autowired
    GroupMapperService groupMapperService;

    //Any user can add any user
    //current user can add anyone to the group by sending his/her id here.
    @PostMapping("/addToGroupRepositoryMapper")
    public ResponseEntity<?> addToGroup(@RequestBody GroupMapper groupMapper) {
        //add to group_mapper table
        try {
            //we need 2 routes
            //we will get the current user here

            groupMapperService.addToGroupRepositoryMapper(groupMapper.getUserId(), groupMapper.getGroupId());
            return ResponseEntity.status(201).body("User added to group");
        }
        catch (Exception e) {
            System.out.println("exception : "+e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }



    }

    //Get all the user from the current group if only the current user is the part of this group
     @PostMapping("/groupUsers")
    public ResponseEntity<?> getAllinGroup(@RequestBody String groupId) {

        try {

            List<String> groupUsers=groupMapperService.usersInAGroup(groupId);
            return ResponseEntity.status(201).body(groupUsers);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }




}
