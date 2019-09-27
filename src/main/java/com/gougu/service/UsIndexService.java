package com.gougu.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gougu.dao.mapper.UsIndexMapper;
import com.gougu.dao.model.UsIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UsIndexService {

    @Autowired
    private UsIndexMapper usIndexMapper;

    private String URL = "https://www.quandl.com/api/v3/datatables/SHARADAR/TICKERS?api_key=kvyEjKaDf6bHTcEXYroz";

    @Autowired
    private RestTemplate restTemplate;

    public void save() {
        ResponseEntity<JSONObject> stock = restTemplate.getForEntity(URL, JSONObject.class);
        JSONArray jsonArray = stock.getBody().getJSONObject("datatable").getJSONArray("data");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray o = jsonArray.getJSONArray(i);
            UsIndex build = UsIndex.builder().table(o.getString(0)).permaticker(o.getInteger(1)).ticker(o.getString(2)).name(o.getString(3)).exchange(o.getString(4))
                    .isdelisted(o.getString(5)).category(o.getString(6)).cusips(o.getString(7)).siccode(o.getInteger(8))
                    .sicsector(o.getString(9)).sicindustry(o.getString(10)).famasector(o.getString(11)).famaindustry(o.getString(12))
                    .sector(o.getString(13)).industry(o.getString(14)).scalemarketcap(o.getString(15)).scalerevenue(o.getString(16))
                    .relatedtickers(o.getString(17)).currency(o.getString(18)).location(o.getString(19)).lastupdated(o.getString(20))
                    .firstadded(o.getString(21)).firstpricedate(o.getString(22)).lastpricedate(o.getString(23)).firstquarter(o.getString(24))
                    .lastquarter(o.getString(25)).secfilings(o.getString(26)).companysite(o.getString(27)).build();
            usIndexMapper.insert(build);
        }
    }
}
