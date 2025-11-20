package com.ming.stock.controller;
import com.ming.stock.pojo.my.GetChangeLim10;
import com.ming.stock.pojo.my.PersonInfo;
import com.ming.stock.pojo.my.WeekInfoDomin;
import com.ming.stock.service.StockService;
import com.ming.stock.vo.resp.R;

import org.apache.poi.hpsf.Decimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.parsers.SAXParser;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quot")
public class MyStockCon {
    @Autowired
    private StockService stockService;
@GetMapping("/stock/describe")
    public R<Map> getPersonInfoStock(String code){
    return stockService.getPersonInfoStock(code);
}
@GetMapping("/stock/screen/second/detail")
    public R<PersonInfo> getPersonInfo(@RequestParam(name = "code") String code){
      return   stockService.getPersonInfo(code);
}
@GetMapping("/stock/screen/second")
    public R<List<GetChangeLim10>> getCahngeLim10(@RequestParam String code){
    return stockService.getCahngeLim10(code);
}
    @GetMapping("/stock/screen/weekkline")
    public R<WeekInfoDomin> WeekInfoSelect(@RequestParam(name = "code")String code){
return stockService.WeekInfoSelect(code);
    }
}
