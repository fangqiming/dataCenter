package com.gougu.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于获取美股的股票代码以及名称的工具
 */
@Service
public class UsStockIndexService {

    private String CODE = "https://xueqiu.com/service/v5/stock/screener/quote/list?page=%s&size=90&order=desc&orderby=market_capital&order_by=market_capital&market=US&type=us";

    @Autowired
    private CookieService cookieService;

    @Autowired
    private FileHelpService fileHelpService;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 调用此方法会将美股的股票代码和公司名称写入指定的位置
     *
     * @param path 写入的路径，默认为 D:/stock.txt
     * @return 返回美股的全部股票代码
     */
    public List<String> getStock(String path) {
        OutputStreamWriter stockFile = fileHelpService.create(StringUtils.isEmpty(path) ? "D:/stock.txt" : path);
        List<String> result = new ArrayList<>();
        for (int i = 1; i < 85; i++) {
            HttpEntity<MultiValueMap<String, String>> cookie = cookieService.getCookie("https://xueqiu.com");
            String stock_url = String.format(CODE, i);
            ResponseEntity<JSONObject> stock = restTemplate.exchange(stock_url, HttpMethod.GET, cookie, JSONObject.class);
            JSONArray jsonArray = stock.getBody().getJSONObject("data").getJSONArray("list");
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject item = jsonArray.getJSONObject(j);
                result.add(item.getString("symbol"));
                String line = item.getString("symbol") + "," + item.getString("name");
                fileHelpService.write(stockFile, line);
            }
            FileHelpService.sleep(500L);
        }
        fileHelpService.close(stockFile);
        return result;
    }
}
