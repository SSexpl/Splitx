package split.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import split.model.Users;
import split.repo.UserRepo;
import split.utils.JwtUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepo userRepo;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        //auth header is taken from header
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);//token is fetched
            username = jwtUtil.extractUsername(jwt); //get the username from the token
        }
        try {
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Users fetchedUser = userRepo.getUserByEmail(username);
                Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
                User userDetail=new User(fetchedUser.getEmail(),
                        new BCryptPasswordEncoder().encode(fetchedUser.getPassword()),
                        authorities
                );
                //this will load the details of the user from userDetailsService which is fetching details of user from the DB
                // for our case if the userdetails are null then exception is thrown.
                //the validation code is triggered passing the token and the username
                if (jwtUtil.validateToken(jwt, userDetail.getUsername())) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(fetchedUser, null, userDetail.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        catch(Exception e) //exception is there if user not found in db
            {
                System.out.println("Failed to access route due to user req. not in db ");
            }

            chain.doFilter(request, response);

    }
}
