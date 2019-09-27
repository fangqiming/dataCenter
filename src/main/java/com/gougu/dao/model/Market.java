package com.gougu.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Market {
    private String symbol;
    private String timestamp;
    private Double volume;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
}
