package com.gougu.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gougu.dao.mapper.MarketMapper;
import com.gougu.dao.mapper.TickerMapper;
import com.gougu.dao.model.Market;
import com.gougu.dao.model.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class HistoryService {

    @Autowired
    private TickerMapper tickerMapper;

    @Autowired
    private MarketMapper marketMapper;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private RestTemplate restTemplate;

    private SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private String URL = "https://stock.xueqiu.com/v5/stock/chart/kline.json?symbol=%s&begin=1569464128245&period=year&type=normal&count=-10&indicator=kline";

    public void saveMarket() throws InterruptedException {

        for (int current = 354; current < 360; current++) {
            Page<Ticker> page = new Page<>(current, 100);
            List<Ticker> tickers = tickerMapper.selectPage(page, null);
            for (Ticker ticker : tickers) {
                HttpEntity<MultiValueMap<String, String>> cookie = cookieService.getCookie("https://xueqiu.com");
                if (ticker.getLastpricedate().compareTo("2010-12-31") >= 0) {
                    String url = String.format(URL, ticker.getTicker());
                    ResponseEntity<JSONObject> stock = restTemplate.exchange(url, HttpMethod.GET, cookie, JSONObject.class);
                    System.out.println(ticker.getTicker());
                    JSONArray jsonArray = stock.getBody().getJSONObject("data").getJSONArray("item");
                    if (Objects.nonNull(jsonArray)) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONArray item = jsonArray.getJSONArray(i);
                            Market market = Market.builder().timestamp(getDate(item.getLong(0))).volume(item.getDouble(1))
                                    .open(item.getDouble(2)).high(item.getDouble(3)).low(item.getDouble(4))
                                    .close(item.getDouble(5)).symbol(ticker.getTicker()).build();
                            System.out.println("当前页:" + current);
                            marketMapper.insert(market);
                        }
                    }
                }
                Thread.sleep(100);
            }
        }
    }

    private String getDate(Long timestamp) {
        Date date = new Date(timestamp);
        return SDF.format(date);
    }
}
