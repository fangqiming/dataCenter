package com.gougu.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/***
 * 美股的财务报表信息
 */
@Service
public class UsFinanceService {

    private String INCOME = "https://stock.xueqiu.com/v5/stock/finance/us/income.json?symbol=%s&type=Q4&is_detail=true&count=50&timestamp=";
    private String BALANCE = "https://stock.xueqiu.com/v5/stock/finance/us/balance.json?symbol=%s&type=Q4&is_detail=true&count=50&timestamp=";
    private String CASH_FLOW = "https://stock.xueqiu.com/v5/stock/finance/us/cash_flow.json?symbol=%s&type=Q4&is_detail=true&count=50&timestamp=";

    @Autowired
    private FileHelpService fileHelpService;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 将财务数据写入磁盘
     *
     * @param stocks
     */
    public void writeFinancialToDist(List<String> stocks) {
        List<List<String>> lists = FileHelpService.cutList(stocks, 15);
        for (int i = 5; i < lists.size(); i++) {
            handleWrite(i, lists.get(i));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将相应的股票代码的财务数据写入磁盘
     *
     * @param i      文件片段标记
     * @param stocks 股票代码列表
     */
    private void handleWrite(Integer i, List<String> stocks) {
        OutputStreamWriter income = fileHelpService.create("D:/income" + i);
        fileHelpService.write(income, "code,report_name,sd,ed,total_revenue,income_from_co_before_it,net_income_atcss,income_from_co_before_tax_si,income_tax,income_from_co,net_income,preferred_dividend,net_income_atms_interest,total_compre_income_atms,total_compre_income_atcss,total_basic_earning_common_ps,total_dlt_earnings_common_ps,total_compre_income,total_net_income_atcss,revenue,othr_revenues,sales_cost,gross_profit,marketing_selling_etc,rad_expenses,net_interest_expense,interest_income,interest_expense,total_operate_expenses_si,total_operate_expenses,operating_income,share_of_earnings_of_affiliate");
        OutputStreamWriter balance = fileHelpService.create("D:/balance" + i);
        fileHelpService.write(balance, "code,report_name,sd,ed,total_assets,total_liab,asset_liab_ratio,net_property_plant_and_equip,total_assets_special_subject,lt_debt,total_liab_si,preferred_stock,common_stock,add_paid_in_capital,retained_earning,treasury_stock,accum_othr_compre_income,total_holders_equity_si,total_holders_equity,minority_interest,total_equity_special_subject,total_equity,cce,st_invest,total_cash,net_receivables,inventory,dt_assets_current_assets,prepaid_expense,current_assets_special_subject,total_current_assets,gross_property_plant_and_equip,accum_depreciation,equity_and_othr_invest,goodwill,net_intangible_assets,accum_amortization,dt_assets_noncurrent_assets,nca_si,total_noncurrent_assets,st_debt,accounts_payable,income_tax_payable,accrued_liab,deferred_revenue_current_liab,current_liab_si,total_current_liab,deferred_tax_liab,dr_noncurrent_liab,noncurrent_liab_si,total_noncurrent_liab");
        OutputStreamWriter cash = fileHelpService.create("D:/cash" + i);
        fileHelpService.write(cash, "code,report_name,sd,ed,net_cash_provided_by_oa,net_cash_used_in_ia,net_cash_used_in_fa,payment_for_property_and_equip,effect_of_exchange_chg_on_cce,cce_at_boy,cce_at_eoy,increase_in_cce,depreciation_and_amortization,operating_asset_and_liab_chg,purs_of_invest,common_stock_issue,repur_of_common_stock,dividend_paid");
        try {
            for (String code : stocks) {
                System.out.println(code);
                writeIncome(code, income);
                writeBalance(code, balance);
                writeCashFlow(code, cash);
            }
            Thread.sleep(500L);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fileHelpService.close(income);
            fileHelpService.close(balance);
            fileHelpService.close(cash);
        }
    }

    private void writeIncome(String code, OutputStreamWriter osw) {
        String indicator = String.format(INCOME, code);
        HttpEntity<MultiValueMap<String, String>> cookie = cookieService.getCookie("https://xueqiu.com");
        ResponseEntity<JSONObject> indicatorJson = restTemplate.exchange(indicator, HttpMethod.GET, cookie, JSONObject.class);
        if (Objects.nonNull(indicatorJson)) {
            JSONArray jsonArray = indicatorJson.getBody().getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject t = jsonArray.getJSONObject(i);
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s"
                        , code
                        , t.getString("report_name")
                        , t.getString("sd")
                        , t.getString("ed")
                        , ifPresent(t, "total_revenue")
                        , ifPresent(t, "income_from_co_before_it")
                        , ifPresent(t, "net_income_atcss")
                        , ifPresent(t, "income_from_co_before_tax_si")
                        , ifPresent(t, "income_tax")
                        , ifPresent(t, "income_from_co")
                        , ifPresent(t, "net_income")
                        , ifPresent(t, "preferred_dividend")
                        , ifPresent(t, "net_income_atms_interest")
                        , ifPresent(t, "total_compre_income_atms")
                        , ifPresent(t, "total_compre_income_atcss")
                        , ifPresent(t, "total_basic_earning_common_ps")
                        , ifPresent(t, "total_dlt_earnings_common_ps")
                        , ifPresent(t, "total_compre_income")
                        , ifPresent(t, "total_net_income_atcss")
                        , ifPresent(t, "revenue")
                        , ifPresent(t, "othr_revenues")
                        , ifPresent(t, "sales_cost")
                        , ifPresent(t, "gross_profit")
                        , ifPresent(t, "marketing_selling_etc")
                        , ifPresent(t, "rad_expenses")
                        , ifPresent(t, "net_interest_expense")
                        , ifPresent(t, "interest_income")
                        , ifPresent(t, "interest_expense")
                        , ifPresent(t, "total_operate_expenses_si")
                        , ifPresent(t, "total_operate_expenses")
                        , ifPresent(t, "operating_income")
                        , ifPresent(t, "share_of_earnings_of_affiliate")
                );
                fileHelpService.write(osw, line);
            }
        }
    }

    private void writeBalance(String code, OutputStreamWriter osw) {
        String indicator = String.format(BALANCE, code);
        HttpEntity<MultiValueMap<String, String>> cookie = cookieService.getCookie("https://xueqiu.com");
        ResponseEntity<JSONObject> balanceJson = restTemplate.exchange(indicator, HttpMethod.GET, cookie, JSONObject.class);
        if (Objects.nonNull(balanceJson)) {
            JSONArray jsonArray = balanceJson.getBody().getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject t = jsonArray.getJSONObject(i);
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s"
                        , code
                        , t.getString("report_name")
                        , t.getString("sd")
                        , t.getString("ed")
                        , ifPresent(t, "total_assets")
                        , ifPresent(t, "total_liab")
                        , ifPresent(t, "asset_liab_ratio")
                        , ifPresent(t, "net_property_plant_and_equip")
                        , ifPresent(t, "total_assets_special_subject")
                        , ifPresent(t, "lt_debt")
                        , ifPresent(t, "total_liab_si")
                        , ifPresent(t, "preferred_stock")
                        , ifPresent(t, "common_stock")
                        , ifPresent(t, "add_paid_in_capital")
                        , ifPresent(t, "retained_earning")
                        , ifPresent(t, "treasury_stock")
                        , ifPresent(t, "accum_othr_compre_income")
                        , ifPresent(t, "total_holders_equity_si")
                        , ifPresent(t, "total_holders_equity")
                        , ifPresent(t, "minority_interest")
                        , ifPresent(t, "total_equity_special_subject")
                        , ifPresent(t, "total_equity")
                        , ifPresent(t, "cce")
                        , ifPresent(t, "st_invest")
                        , ifPresent(t, "total_cash")
                        , ifPresent(t, "net_receivables")
                        , ifPresent(t, "inventory")
                        , ifPresent(t, "dt_assets_current_assets")
                        , ifPresent(t, "prepaid_expense")
                        , ifPresent(t, "current_assets_special_subject")
                        , ifPresent(t, "total_current_assets")
                        , ifPresent(t, "gross_property_plant_and_equip")
                        , ifPresent(t, "accum_depreciation")
                        , ifPresent(t, "equity_and_othr_invest")
                        , ifPresent(t, "goodwill")
                        , ifPresent(t, "net_intangible_assets")
                        , ifPresent(t, "accum_amortization")
                        , ifPresent(t, "dt_assets_noncurrent_assets")
                        , ifPresent(t, "nca_si")
                        , ifPresent(t, "total_noncurrent_assets")
                        , ifPresent(t, "st_debt")
                        , ifPresent(t, "accounts_payable")
                        , ifPresent(t, "income_tax_payable")
                        , ifPresent(t, "accrued_liab")
                        , ifPresent(t, "deferred_revenue_current_liab")
                        , ifPresent(t, "current_liab_si")
                        , ifPresent(t, "total_current_liab")
                        , ifPresent(t, "deferred_tax_liab")
                        , ifPresent(t, "dr_noncurrent_liab")
                        , ifPresent(t, "noncurrent_liab_si")
                        , ifPresent(t, "total_noncurrent_liab")
                );
                fileHelpService.write(osw, line);
            }
        }

    }

    private void writeCashFlow(String code, OutputStreamWriter osw) {
        String indicator = String.format(CASH_FLOW, code);
        HttpEntity<MultiValueMap<String, String>> cookie = cookieService.getCookie("https://xueqiu.com");
        ResponseEntity<JSONObject> cashJson = restTemplate.exchange(indicator, HttpMethod.GET, cookie, JSONObject.class);
        if (Objects.nonNull(cashJson)) {
            JSONArray jsonArray = cashJson.getBody().getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject t = jsonArray.getJSONObject(i);
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s"
                        , code
                        , t.getString("report_name")
                        , t.getString("sd")
                        , t.getString("ed")
                        , ifPresent(t, "net_cash_provided_by_oa")
                        , ifPresent(t, "net_cash_used_in_ia")
                        , ifPresent(t, "net_cash_used_in_fa")
                        , ifPresent(t, "payment_for_property_and_equip")
                        , ifPresent(t, "effect_of_exchange_chg_on_cce")
                        , ifPresent(t, "cce_at_boy")
                        , ifPresent(t, "cce_at_eoy")
                        , ifPresent(t, "increase_in_cce")
                        , ifPresent(t, "depreciation_and_amortization")
                        , ifPresent(t, "operating_asset_and_liab_chg")
                        , ifPresent(t, "purs_of_invest")
                        , ifPresent(t, "common_stock_issue")
                        , ifPresent(t, "repur_of_common_stock")
                        , ifPresent(t, "dividend_paid")
                );
                fileHelpService.write(osw, line);
            }
        }
    }

    /**
     * 从指定的JSON对象中获取指定的K对应的值
     *
     * @param t
     * @param key
     * @return 返回对象的K值，如果没有就返回null
     */
    private BigDecimal ifPresent(JSONObject t, String key) {
        if (t.containsKey(key)) {
            return t.getJSONArray(key).getBigDecimal(0);
        }
        return null;
    }

}
