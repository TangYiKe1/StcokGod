package com.ming.stock;

import com.ming.stock.pojo.Account;
import com.ming.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: Ming
 * @Description TODO
 */
@SpringBootTest
public class TestRestTemplate {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StockTimerTaskService stockTimerTaskService;
    /*
    * 测试get请求携带url参数 访问外部接口
    * */
    @Test
    public void test01(){
        String url="http://localhost:6767/account/getByUserNameAndAddress?userName=zhangsan&address=sh";
        /*
        * 参数1:url请求地址
        * 参数2:请求返回的数据类型
        * */
        ResponseEntity<String> result = restTemplate.getForEntity(url,String.class);
        //获取请求头
        HttpHeaders headers = result.getHeaders();
        System.out.println(headers);
        int statusCodeValue = result.getStatusCodeValue();
        System.out.println(statusCodeValue);
        //获取响应数据
        String respData = result.getBody();
        System.out.println(respData);
    }
    /*
    * 测试响应数据自动封装到vo当中
    * */
    @Test
    public void test02(){
        String url="http://localhost:6767/account/getByUserNameAndAddress?userName=zhangsan&address=sh";
        /*
        * 参数1: url请求地址
        * 参数e; 请求返回的数据类型
        * */
        Account account = restTemplate.getForObject(url, Account.class);
        System.out.println(account);
    }
    /*
    * 请求头设置参数 访问指定接口
    * */
    @Test
    public void test03(){
        String url = "http://localhost:6767/account/getHeader";
        //设置请求头参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("userName","zhangsan");
        //请求头填充到请求对象下
        HttpEntity<Map> entity = new HttpEntity<>(headers);
        //发送请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String result = responseEntity.getBody();
        System.out.println(result);
    }
    /*
    * post连接 模拟form表单提交数据
    * */
    @Test
    public void test04(){
        String url = "http://localhost:6767/account/addAccount";
        //设置请求头 指定请求数据方式
        HttpHeaders headers = new HttpHeaders();
        //告知被调用方 请求方式是form表单提交 这样对方解析数据的时候 就会按照表单的方式解析处理
        headers.add("Content-type","application/x-www-form-urlencoded");
        //组装模拟form表单提交数据 内部元素相当于form表单的input框
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("id","10");
        map.add("userName","Ming");
        map.add("address","haerbin");
        HttpEntity<LinkedMultiValueMap<String,Object>> httpEntity = new HttpEntity<>(map,headers);
        ResponseEntity<Account> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Account.class);
        Account body = exchange.getBody();
        System.out.println(body);
    }
    /*
    *  post请求发送json数据
    * */
    @Test
    public void test05(){
        String url = "http://localhost:6767/account/updateAccount";
        //设置请求头 指定请求数据方式
        HttpHeaders headers = new HttpHeaders();
        //告知被调用方 发送的数据格式的json格式 这样对方解析数据的时候 就会按照json的方式解析处理
        headers.add("Content-type","application/json; charset=utf-8");
        //组装json格式数据
        String jsonReq="{\"address\":\"上海\",\"id\":\"1\",\"userName\":\"zhangsan\"}";
        //构建对象
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonReq,headers);
        ResponseEntity<Account> responseEntity = restTemplate.exchange(url,HttpMethod.POST,httpEntity, Account.class);
        Account body = responseEntity.getBody();
        System.out.println(body);
    }
    /*
    * 获取请求cookie值
    * */
    @Test
    public void test06(){
        String url = "http://localhost:6767/account/getCookie";
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        //获取cookies
        List<String> cookies = result.getHeaders().get("Set-Cookie");
        //获取响应数据
        String resStr = result.getBody();
        System.out.println(resStr);
        System.out.println(cookies);
    }
    /*
    * 测试采集国内大盘数据
    * */
    @Test
    public void testInnerGetMarketInfo(){
        //stockTimerTaskService.getInnerMarketInfo();
        stockTimerTaskService.getStockRtIndex();
    }
    /*
    * 获取板块测试
    * */
    @Test
    public void testInner(){
        stockTimerTaskService.getStockSectorRtIndex();
    }
    /**
     * 测试采集国外大盘数据
     */
@Test
    public void testOuter(){
        stockTimerTaskService.getOuterStockInfo();
}
}
