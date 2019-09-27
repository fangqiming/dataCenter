package com.gougu.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsStock {

    private List<String> date;

    private List<BigDecimal> sp500;

    private List<BigDecimal> gougu;

    private List<BigDecimal> aieq;


}
