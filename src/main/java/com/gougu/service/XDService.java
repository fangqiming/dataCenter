package com.gougu.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

@Service
public class XDService {

    private String URL = "http://dcfm.eastmoney.com/EM_MutiSvcExpandInterface/api/js/get?type=DCSOBS&token=70f12f2f4f091e459a279469fe49eca5&p=1&ps=50&sr=-1&st=ReportingPeriod&filter=&cmd=%s";

    private List<String> USER_CODE = Arrays.asList("300498", "300146", "300383", "002271", "002007", "002422", "600487", "300003", "603833", "002468", "600779", "000830", "000963", "300760", "600585", "601318", "601888", "600309", "000786", "600340", "002440", "300033", "002415", "600036", "002032", "000651", "300144", "603288", "603517", "002507", "000568", "600519", "300015", "600566", "002027");

    @Autowired
    private FileHelpService fileHelpService;

    @Autowired
    private RestTemplate restTemplate;

    public void writeXd() {
        OutputStreamWriter osw = fileHelpService.create("D:\\xd");
        fileHelpService.write(osw, "代码,名称,股权登记日,除权除息日,每10股派");
        for (String code : USER_CODE) {
            String url = String.format(URL, code);
            System.out.println(url);
            String strBody = restTemplate.getForObject(url, String.class);
            JSONArray body = JSONArray.parseArray(strBody);
            for (int i = 0; i < body.size(); i++) {
                JSONObject t = body.getJSONObject(i);
                String line = String.format("%s,%s,%s,%s,%s"
                        , t.getString("Code")
                        , t.getString("Name")
                        , t.getString("GQDJR").split("T")[0]
                        , t.getString("CQCXR").split("T")[0]
                        , t.getString("XJFH"));
                fileHelpService.write(osw, line);
            }
            FileHelpService.sleep(1000L);
        }
        fileHelpService.close(osw);
    }
}
