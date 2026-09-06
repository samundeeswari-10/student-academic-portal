//package com.studentportal.studentacademicportal.controller;
//
//public class HelloController {
//
//}
package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/api/hello")
    public String hello() {
        return helloService.getMessage();
    }
}