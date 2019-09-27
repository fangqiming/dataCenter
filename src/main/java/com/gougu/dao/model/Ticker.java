package com.gougu.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticker {
    private String permaticker;
    private String ticker;
    private String name;
    private String exchange;
    private String isdelisted;
    private String lastpricedate;
}
