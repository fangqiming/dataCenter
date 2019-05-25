package com.gougu.controller;

import com.gougu.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/data")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MainController {

    @Autowired
    private UsStockIndexService usStockIndexService;

    @Autowired
    private UsTradeService usTradeService;

    @Autowired
    private FileHelpService fileHelpService;

    @Autowired
    private TwStockService twStockService;

    @GetMapping("/start")
    public Object start() {
        List<String> stocks = usStockIndexService.getStock("D:\\us_index");
//        List<String> stocks = Arrays.asList("MSFT", "AAPL", "BABA");
        usTradeService.writeTrade("D:\\trade", stocks);
        return "OK";
    }

    @GetMapping("/start1")
    public Object start1() {
        fileHelpService.mergeFile("D:\\us_trade", "D:\\trade", 0, 15, "code");
        return "OK";
    }

    @GetMapping("/start2")
    public Object tw() {
        return twStockService.getTw();
    }

    @Autowired
    private XDService xdService;

    @GetMapping("/start3")
    public Object tw2() {
        xdService.writeXd();
        return "OK";
    }

}
