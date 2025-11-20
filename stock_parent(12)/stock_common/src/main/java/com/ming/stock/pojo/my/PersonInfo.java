package com.ming.stock.pojo.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class PersonInfo {
    /*
     * 前收盘价| 昨日收盘价
     */
    private BigDecimal preClosePrice;
    private BigDecimal lowPrice;
    private BigDecimal highPrice;

    /**
     * 开盘价
     */
    private BigDecimal openPrice;

    /**
     * 当前价格
     */
    private BigDecimal tradePrice;
    /**
     * 成交量
     */
    private Long tradeAmt;//

    /**
     * 成交金额
     */
    private BigDecimal tradeVol;

    /**
     * 当前时间
     */
    private Date curDate;
}
