package split.controller.Public.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import split.model.DTO.AuthenticationRequestDTO;
import split.model.Users;
import split.repo.UserRepo;
import split.utils.JwtUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("login")
public class LoginController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private WebClient webClient;

    @PostMapping("/emailAndPassword")
    public String authenticate(@RequestBody AuthenticationRequestDTO request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new Exception("Invalid credentials");

        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        return jwtUtil.generateToken(userDetails.getUsername());
        //this will return the jwt key
    }

    @PostMapping("/gmail")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String code) {
        try {
            String clientId="615961089023-rphbhimb4hcjjsi10rr11qm7bm4v8t1q.apps.googleusercontent.com";
            String clientSecret="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";
            String tokenEndpoint = "https://oauth2.googleapis.com/token";
            HashMap<String, String> params = new HashMap<>();
            params.put("code", code);
            params.put("client_id", clientId);
            params.put("client_secret", clientSecret);
            params.put("redirect_uri", "https://developers.google.com/oauthplayground");
            params.put("grant_type", "authorization_code");
            ResponseEntity<Map> tokenResponse = webClient.post()
                    .uri(tokenEndpoint)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(params)  // ✅ Using bodyValue()
                    .retrieve()
                    .toEntity(Map.class)
                    .block();
            String idToken = (String) tokenResponse.getBody().get("id_token");
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            ResponseEntity<Map> userInfoResponse = webClient.get().uri(userInfoUrl).retrieve().toEntity(Map.class).block();
            if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                UserDetails userDetails = null;
                try{
                    userDetails = userDetailsService.loadUserByUsername(email);
                }catch (Exception e){
                    Users user = new Users();
                    user.setEmail(email);
                    user.setUserName(email);
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    userRepo.addUser(user.getUserName(),user.getEmail(),user.getPassword());
                }
                String jwtToken = jwtUtil.generateToken(email);
                return ResponseEntity.ok(Collections.singletonMap("token", jwtToken));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
           // log.error("Exception occurred while handleGoogleCallback ", e);
            System.out.println(e);
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }










}

