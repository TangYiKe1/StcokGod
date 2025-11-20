package com.ming.stock.pojo.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetChangeLim10 {
    private String date;
    private BigDecimal tradeAmt;
    private BigDecimal tradeVol;
    private BigDecimal tradePrice;
}
