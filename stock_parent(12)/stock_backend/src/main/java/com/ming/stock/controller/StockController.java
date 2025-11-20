package com.ming.stock.controller;

import com.ming.stock.pojo.domain.*;
import com.ming.stock.pojo.my.MyStockInfo;
import com.ming.stock.service.StockService;
import com.ming.stock.vo.resp.PageResult;
import com.ming.stock.vo.resp.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: Ming
 * @Description 定义股票相关接口控制器
 */
@Tag(name = "定义股票相关接口控制器", description = "定义股票相关接口控制器")
@RestController
@RequestMapping("/api/quot")
public class StockController {
    @Autowired
    private StockService stockService;
    /**
     * 获取国内大盘最新数据
     * @return
     */
    @Operation(summary = "获取国内大盘最新数据", description = "获取国内大盘最新数据")
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo(){
        return stockService.getInnerMarketInfo();
    }

    /**
     * 获取板块最新数据
     * @return
     */
    @Operation(summary = "获取板块最新数据", description = "获取板块最新数据")
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> sectorAll(){
        return stockService.sectorAllLimit();
    }

    /**
     * 分页查询股票最新数据 并且按照涨幅排序查询
     * @param page
     * @param pageSize
     * @return
     */
    @Parameters({
            @Parameter(name = "page", description = "", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "", in = ParameterIn.QUERY)
    })
    @Operation(summary = "分页查询股票最新数据 并且按照涨幅排序查询", description = "分页查询股票最新数据 并且按照涨幅排序查询")
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getStockPageInfo(
            @RequestParam(name = "page",required = false,defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "20") Integer pageSize){
        return stockService.getStockPageInfo(page,pageSize);
    }

    /**
     * 统计沪深两市个股最新交易数据 并且按照涨幅降序排序查前四条
     * @return
     */
    @Operation(summary = "统计沪深两市个股最新交易数据 并且按照涨幅降序排序查前四条", description = "统计沪深两市个股最新交易数据 并且按照涨幅降序排序查前四条")
    @GetMapping("/stock/increase")
    public R<List<StockUpdownDomain>> getTopStockCreate(){
        return stockService.getTopStockCreate();
    }

    /**
     * 统计最新股票交易日每分钟的涨跌停的股票数量
     * @return
     */
    @GetMapping("/stock/updown/count")
    public R<Map<String,List>> getStockUpdownCount(){
        return stockService.getStockUpdownCount();
    }
    /**
     * 将指定页的股票数据导出到excel表中
     * @param page
     * @param pageSize
     * @param response
     */
    @GetMapping("/stock/export")
    public void exportStockUpDownInfo(
            @RequestParam(name = "page",required = false,defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "20") Integer pageSize,
            HttpServletResponse response){
        stockService.exportStockUpDownInfo(page,pageSize,response);
    }

    /**
     * 统计国内A大盘T日和T-1日成交量对比功能
     * @return
     */
    @GetMapping("/stock/tradeAmt")
    public R<Map<String,List>> getComparedStockTradeAmt(){
        return stockService.getComparedStockTradeAmt();
    }

    /**
     * 统计最新交易时间点下股票 在各个涨幅区间的数量
     * @return
     */
    @GetMapping("/stock/updown")
    public R<Map> getIncreaseRangeInfo(){
        return stockService.getIncreaseRangeInfo();
    }

    /**
     * 获取指定股票T日分时数据
     * @param stockCode
     * @return
     */
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(@RequestParam(value = "code",required = true) String stockCode){
        return stockService.getStockScreenTimeSharing(stockCode);
    }

    /**
     * 个股日K数据查询
     * @param stockCode
     * @return
     */
    @GetMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getStockScreenDKLine(@RequestParam(value = "code",required = true) String stockCode){
        return stockService.getStockScreenDKLine(stockCode);
    }

    @GetMapping("/external/index")
    public R<List<MyStockInfo>> getPre4(){
        return stockService.getPre4();
    }
}
