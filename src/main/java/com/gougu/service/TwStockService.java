package com.gougu.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TwStockService {

    /**
     * ALLBUT0999 不包含权证等
     */
    private String URL = "http://www.twse.com.tw/exchangeReport/MI_INDEX?response=json&date=20190521&type=ALLBUT0999&_=%s";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CookieService cookieService;

    public Object getTw() {
        //
        String url = String.format(URL, System.currentTimeMillis());
        HttpEntity<MultiValueMap<String, String>> cookie = cookieService.getCookie("http://www.twse.com.tw/");
        ResponseEntity<JSONObject> stock = restTemplate.exchange(url, HttpMethod.GET, cookie, JSONObject.class);
        return stock.getBody();
    }

}
