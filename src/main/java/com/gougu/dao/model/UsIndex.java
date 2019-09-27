package com.gougu.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsIndex {


    private String table;


    private Integer permaticker;


    private String ticker;


    private String name;


    private String exchange;


    private String isdelisted;


    private String category;


    private String cusips;


    private Integer siccode;


    private String sicsector;


    private String sicindustry;


    private String famasector;


    private String famaindustry;


    private String sector;


    private String industry;


    private String scalemarketcap;


    private String scalerevenue;


    private String relatedtickers;


    private String currency;


    private String location;


    private String lastupdated;


    private String firstadded;


    private String firstpricedate;


    private String lastpricedate;


    private String firstquarter;


    private String lastquarter;


    private String secfilings;


    private String companysite;

}
