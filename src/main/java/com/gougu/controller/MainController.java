package com.gougu.controller;

import com.alibaba.fastjson.JSONObject;
import com.gougu.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
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
    public Object test() {
        List<String> stocks = Arrays.asList("AIEEQ");
        usTradeService.writeTrade("D:\\trade", stocks);
        return "OK";
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/s1")
    public Object obj() {
        HttpHeaders requestHeaders = new HttpHeaders();

        requestHeaders.add("Cookie", "aliyungf_tc=AQAAAJqPDXLeTgAAQx/leDuvard00VRL; csrfToken=gtCDgWSUveZZp0FTVXmocKRj; TYCID=07ed836098cb11e98bb57571a620a184; undefined=07ed836098cb11e98bb57571a620a184; ssuid=5306130484; bannerFlag=undefined; RTYCID=bfdda5d86fbb4054a08f89545a514b1b; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1561633340; CT_TYCID=f6cc2c3c5e1f415fb279b9200b8b6034; _ga=GA1.2.238984598.1561633341; _gid=GA1.2.116847623.1561633341; tyc-user-info=%257B%2522claimEditPoint%2522%253A%25220%2522%252C%2522myAnswerCount%2522%253A%25220%2522%252C%2522myQuestionCount%2522%253A%25220%2522%252C%2522signUp%2522%253A%25220%2522%252C%2522explainPoint%2522%253A%25220%2522%252C%2522privateMessagePointWeb%2522%253A%25220%2522%252C%2522nickname%2522%253A%2522%25E7%25B1%25B3%25E5%258D%25A1%25E5%259F%2583%25E5%25B0%2594%25C2%25B7%25E8%25A5%25BF%25E5%25B0%2594%25E9%259F%25A6%25E6%2596%25AF%25E7%2589%25B9%2522%252C%2522integrity%2522%253A%25220%2525%2522%252C%2522privateMessagePoint%2522%253A%25220%2522%252C%2522state%2522%253A%25220%2522%252C%2522announcementPoint%2522%253A%25220%2522%252C%2522isClaim%2522%253A%25220%2522%252C%2522vipManager%2522%253A%25220%2522%252C%2522discussCommendCount%2522%253A%25220%2522%252C%2522monitorUnreadCount%2522%253A%2522298%2522%252C%2522onum%2522%253A%25220%2522%252C%2522claimPoint%2522%253A%25220%2522%252C%2522token%2522%253A%2522eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzEzMjEzMDQ5MyIsImlhdCI6MTU2MTYzNDE4OSwiZXhwIjoxNTkzMTcwMTg5fQ.ZBaiLym5JTx9p-fBm7b5lWuP1zXp7DH0l5wXYz1_ScM2diY-NhMpMnmSUU9yjspP6Ur0Qxyehlxo4jPtW2V0ew%2522%252C%2522pleaseAnswerCount%2522%253A%25220%2522%252C%2522redPoint%2522%253A%25220%2522%252C%2522bizCardUnread%2522%253A%25220%2522%252C%2522vnum%2522%253A%25220%2522%252C%2522mobile%2522%253A%252217132130493%2522%257D; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzEzMjEzMDQ5MyIsImlhdCI6MTU2MTYzNDE4OSwiZXhwIjoxNTkzMTcwMTg5fQ.ZBaiLym5JTx9p-fBm7b5lWuP1zXp7DH0l5wXYz1_ScM2diY-NhMpMnmSUU9yjspP6Ur0Qxyehlxo4jPtW2V0ew; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1561634389; cloud_token=4c1b8904570941e5a60c0e3de6103a1c");
        requestHeaders.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        requestHeaders.add("Accept-Language", "zh-CN,zh;q=0.9");
        requestHeaders.add("Connection", "keep-alive");
        requestHeaders.add("Host", "www.tianyancha.com");
        requestHeaders.add("Referer", "https://www.tianyancha.com/");
        requestHeaders.add("Upgrade-Insecure-Requests", "1");
        requestHeaders.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");


        requestHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
        String url = "https://www.tianyancha.com/stock/shareholder.xhtml?graphId=26516263&index=4&type=1&time=2019-03-31&_=1561634121997";
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return response.getBody();
    }

}
