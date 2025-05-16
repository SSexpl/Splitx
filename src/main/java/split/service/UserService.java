package split.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import split.Exceptions.GeneralServerException;
import split.Exceptions.MultipleEntryException;
import split.Exceptions.NotFoundException;
import split.Exceptions.SQLQueryException;
import split.model.Users;
import split.repo.UserRepo;

import java.util.List;

@Service
@Slf4j
public class UserService {


    @Autowired
    UserRepo userRepo;
    public Users getUserById(String id) throws SQLQueryException, NotFoundException, GeneralServerException, MultipleEntryException {
        log.info("returning current user by id");
        return userRepo.getUserById(id);
              
    }
    public int addUser(String userName , String email , String password) throws SQLQueryException, GeneralServerException {
        return userRepo.addUser(userName,email,password);
    }
    public List<Users> getAllUsers() throws SQLQueryException, GeneralServerException {
        return userRepo.getAllUsers();
    }

    public int deleteUser(String id) throws SQLQueryException, GeneralServerException {
        return userRepo.deleteUser(id);
    }
}
