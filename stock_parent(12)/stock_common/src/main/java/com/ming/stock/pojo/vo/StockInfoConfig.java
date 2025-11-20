package com.ming.stock.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Author: Ming
 * @Description 定义股票相关的值对象的封装
 */
@Schema(description = "定义股票相关的值对象的封装")
@ConfigurationProperties(prefix = "stock")
@Data
public class StockInfoConfig {
    //封装国内A股大盘编码集合
    @Schema(description = "封装国内A股大盘编码集合")
    private List<String> inner;
    //外盘编码集合
    @Schema(description = "外盘编码集合")
    private List<String> outer;
    //股票涨幅区间标题集合
    @Schema(description = "股票涨幅区间标题集合")
    private List<String> upDownRange;
    /**
     * 大盘 外盘 个股的公共URL
     */
    @Schema(description = "大盘 外盘 个股的公共URL")
    private String marketUrl;
    /**
     * 板块采集URL1
     */
    @Schema(description = "板块采集URL0")
    private String blockUrl;
}
