package com.ming.stock.config;

import com.ming.stock.pojo.vo.StockInfoConfig;
import com.ming.stock.utils.IdWorker;
import com.ming.stock.utils.ParserStockInfoUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author: Ming
 * @Description TODO
 */
@Configuration
@EnableConfigurationProperties(StockInfoConfig.class) //开启对相关配置对象的加载
public class CommonConfig {
    /*
    *
    * 密码加密器BCryptPasswordEncoder方法采用SHA-256对密码进行加密
    * */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public IdWorker idWorker(){
        //参数1:机器id 参数2:机房id 一般由运维人员确定
        return new IdWorker(1l,2L);
    }

    /**
     * 定义解析股票大盘 外盘 个股 板块相关信息的工具类bean
     * @return
     */
    @Bean
    public ParserStockInfoUtil parserStockInfoUtil(){
        return new ParserStockInfoUtil(idWorker());
    }
}
