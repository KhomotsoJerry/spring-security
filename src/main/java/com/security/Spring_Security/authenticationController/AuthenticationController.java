package com.security.Spring_Security.authenticationController;

import com.security.Spring_Security.student.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/register")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
      @PostMapping("/register")
      public ResponseEntity<AuthenticationResponse> register (@RequestBody Register request){
          return ResponseEntity.ok(authenticationService.register(request));
      }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody Authenticate response){
          return ResponseEntity.ok(authenticationService.authenticate(response));
    }
}
