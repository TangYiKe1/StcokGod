package com.ming.stock.pojo.my;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WeekInfoDomin {
    private BigDecimal avgPrice;
    private BigDecimal minPrice;
    private BigDecimal weekLow;
    private BigDecimal openPrice;
    private BigDecimal maxPrice;
    private BigDecimal closePrice;
    private Integer stockCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date mxTime;

}
