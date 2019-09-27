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
public class Ai {

    private LocalDate date;

    private BigDecimal position;

    private BigDecimal diff;

    private BigDecimal sz;

    private BigDecimal gougu;
}
