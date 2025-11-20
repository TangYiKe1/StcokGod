package com.ming.stock.pojo.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Ming
 * @Description 股票涨跌信息
 */
@Schema(description = "股票涨跌信息")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdownDomain {
    //股票编码
    @Schema(description = "股票编码")
    @ExcelProperty(value = {"股票涨幅信息统计表","股票编码"},index = 0)
    private String code;
    //股票名称
    @Schema(description = "股票名称")
    @ExcelProperty(value = {"股票涨幅信息统计表","股票名称"},index = 1)

    private String name;
    //前收盘价
    @Schema(description = "前收盘价")
    @ExcelProperty(value = {"股票涨幅信息统计表","前收盘价"},index = 2)
    private BigDecimal preClosePrice;
    //当前交易价格
    @Schema(description = "当前交易价格")
    @ExcelProperty(value = {"股票涨幅信息统计表","当前交易价格"},index = 3)
    private BigDecimal tradePrice;
    //涨跌值
    @Schema(description = "涨跌值")
    @ExcelProperty(value = {"股票涨幅信息统计表","涨跌值"},index = 4)
    private BigDecimal increase;
    //涨幅
    @Schema(description = "涨幅")
    @ExcelProperty(value = {"股票涨幅信息统计表","涨幅"},index = 5)
    private BigDecimal upDown;
    //振幅
    @Schema(description = "振幅")
    @ExcelProperty(value = {"股票涨幅信息统计表","振幅"},index = 6)
    private BigDecimal amplitude;
    //交易量
    @Schema(description = "交易量")
    @ExcelProperty(value = {"股票涨幅信息统计表","交易量"},index = 7)
    private Long tradeAmt;
    //交易金额
    @Schema(description = "交易金额")
    @ExcelProperty(value = {"股票涨幅信息统计表","交易金额"},index = 8)
    private BigDecimal tradeVol;
    //日期
    @Schema(description = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ExcelProperty(value = {"股票涨幅信息统计表","日期"},index = 9)
    @DateTimeFormat("yyyy-MM-dd HH:mm")
    private Date curDate;

}
