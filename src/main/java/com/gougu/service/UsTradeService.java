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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 获取美股股票的交易数据
 */
@Service
public class UsTradeService {

    private String URL = "https://stock.xueqiu.com/v5/stock/chart/kline.json?symbol=%s&begin=%s&period=month&type=before&count=-142&indicator=kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private CookieService cookieService;

    @Autowired
    private FileHelpService fileHelpService;

    @Autowired
    private RestTemplate restTemplate;

    public void writeTrade(String path, List<String> stocks) {
        List<List<String>> lists = FileHelpService.cutList(stocks, 15);
        for (int i = 0; i < lists.size(); i++) {
            writeTradeToDist(lists.get(i), i, path);
            FileHelpService.sleep(5000L);
        }
    }

    /**
     * 将指定的股票行情数据写入到文件
     *
     * @param stocks 股票代码列表
     * @param i      标记文件
     */
    private void writeTradeToDist(List<String> stocks, int i, String path) {
        OutputStreamWriter tradeOsw = fileHelpService.create(StringUtils.isEmpty(path) ? "D:/us_trade" : path + i);
        if (i == 0) {
            writeTitle(tradeOsw);
        }
        for (String stock : stocks) {
            System.out.println(stock);
            writeTrade(stock, tradeOsw);
            FileHelpService.sleep(500L);
        }
        fileHelpService.close(tradeOsw);
    }

    private void writeTrade(String code, OutputStreamWriter osw) {
        String url = String.format(URL, code, System.currentTimeMillis());
        HttpEntity<MultiValueMap<String, String>> cookie = cookieService.getCookie("https://xueqiu.com");
        ResponseEntity<JSONObject> json = restTemplate.exchange(url, HttpMethod.GET, cookie, JSONObject.class);
        JSONArray jsonArray = json.getBody().getJSONObject("data").getJSONArray("item");
        //最后一个月份的数据可能不完整，直接去掉
        for (int i = 0; i < jsonArray.size() - 1; i++) {
            //code,时间,开盘价，收盘价，成交额，成交量，换手率，涨跌幅
            JSONArray t = jsonArray.getJSONArray(i);
            String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s", code
                    , changeDateFormat(t.getLong(0))
                    , t.getBigDecimal(2)
                    , t.getBigDecimal(5)
                    , t.getBigDecimal(9)
                    , t.getBigDecimal(1)
                    , t.getBigDecimal(8)
                    , t.getBigDecimal(7));
            fileHelpService.write(osw, line);
        }
    }

    private void writeTitle(OutputStreamWriter osw) {
        String line = "code,date,open,close,amount,volume,turnoverrate,percent";
        fileHelpService.write(osw, line);
    }

    private String changeDateFormat(long time) {
        return sdf.format(new Date(time));
    }
}
