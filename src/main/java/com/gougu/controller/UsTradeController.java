package com.gougu.controller;


import com.gougu.dao.service.UsTradeDaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/us")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsTradeController {

    @Autowired
    private UsTradeDaoService usTradeDaoService;

    @GetMapping("/s1")
    public Object us(@RequestParam String d1, @RequestParam String d2) {
        return usTradeDaoService.find(d1, d2);
    }
}
