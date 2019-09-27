package com.gougu.controller;

import com.gougu.dao.service.Performance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/performance")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PerformanceController {

    @Autowired
    private Performance performance;

    @GetMapping("/start")
    public Object find() {
        LocalDate start = LocalDate.parse("2019-02-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate end = LocalDate.parse("2019-06-30", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return performance.find(start, end);
    }

    @GetMapping("/Dd")
    public Object findD() {
        LocalDate start = LocalDate.parse("2019-02-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate end1 = LocalDate.parse("2019-06-30", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate start1 = LocalDate.parse("2018-11-15", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        System.out.println("AIEQ的最大回撤是 :" + performance.calAieqmaxDd(start, end1));
//        System.out.println("SP500的最大回撤是 :" + performance.calSPMacDd(start, end1));
//        System.out.println("U_Stock的最大回撤是 :" + performance.calUsMaxDd(start, end1));


        System.out.println("上证指数的最大回撤是 :" + performance.calSzMaxDd(start1, end1));
//        System.out.println("A_Stock的最大回撤是 :" + performance.calAMaxDd(start1, end1));
        return "OK";
    }

    @GetMapping("/ai")
    public Object find2(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return performance.find2(startDate, endDate);
    }

    @GetMapping("/week")
    public Object find3(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return performance.find3(startDate, endDate);
    }
}
