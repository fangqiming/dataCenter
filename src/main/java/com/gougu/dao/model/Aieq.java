package com.gougu.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aieq {
    private String code;
    private LocalDate date;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal amount;
    private BigDecimal volume;
    private BigDecimal turnoverrate;
    private BigDecimal percent;
}
