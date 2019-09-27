package com.gougu.controller;

import com.gougu.service.HistoryService;
import com.gougu.service.UsIndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/financial")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FinancialController {


    @Autowired
    private UsIndexService usIndexService;

    @Autowired
    private HistoryService historyService;


    @GetMapping("/start2")
    public Object createStock() throws InterruptedException {
        historyService.saveMarket();
        return "OK";
    }


}
