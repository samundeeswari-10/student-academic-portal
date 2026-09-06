//package com.studentportal.studentacademicportal.service;
//
//public class HelloService {
//}
package com.studentportal.studentacademicportal.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String getMessage() {
        return "Hello from Service!";
    }
}