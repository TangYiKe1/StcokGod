package com.ming.stock.pojo.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor@NoArgsConstructor
@Builder
public class MyStockInfo {
    /**
     * 大盘名称
     */
    private String name;

    /**
     * 大盘当前点
     */
    private BigDecimal curPoint;

    /**
     * 大盘涨跌值
     */
    private BigDecimal upDown;

    /**
     * 大盘涨幅
     */
    private BigDecimal rose;

    /**
     * 当前时间
     */
    private Date curTime;
}
