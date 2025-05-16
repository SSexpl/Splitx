package split.controller.Protected;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import split.model.Users;
import split.utils.AuthDetails;

@RestController
@RequestMapping("/protected")

public class Secure {

    @GetMapping
    public ResponseEntity<?> SecureGet()
    {

        Users currentUser= AuthDetails.getCurrentUserDetails();

        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

}
