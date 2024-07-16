package com.security.Spring_Security.demoController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/demo")
@RestController
public class DemoController {
    @GetMapping
    public ResponseEntity<String> getStringFromSecuredAPI(){
        return ResponseEntity.ok("getting this from secured api");
    }
}
