package split.utils;


//this class is specially made so that fetching userdetails from db becomes easy


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import split.Exceptions.GeneralServerException;
import split.model.Users;
import split.repo.UserRepo;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    //we need to provide an implementation for fetching information of user from the db
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //this will fetch the details of the User. using repo layer for implementation.
        Users fetchedUser= null;
        try {
            fetchedUser = userRepo.getUserByEmail(email);
        } catch (GeneralServerException e) {
            //if user is not found then this exception is raised  which can be handled by the calling method.
            throw new RuntimeException(e);
        }
        //sends back the user details from the db

        System.out.println("user found : "+fetchedUser);

        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();

        System.out.println(authorities);

        if(fetchedUser ==null) //in case no user is found
            return null;
        //this User is a predefined data type in Spring security to perform authentication .
        User user=new User(fetchedUser.getEmail(),
                new BCryptPasswordEncoder().encode(fetchedUser.getPassword()),
                authorities
        );

        return user;
    }
}
