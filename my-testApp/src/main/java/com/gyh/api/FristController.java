package com.gyh.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guoyanhong
 * @date 2018/9/28 17:12
 */
@RestController
public class FristController {

    @GetMapping("getSomeMsg")
    public Object getSomeMsg(){
        return "i`m your father";
    }
}
