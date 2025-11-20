package com.ming.stock;
import com.ming.stock.mapper.StockBlockRtInfoMapper;
import com.ming.stock.mapper.StockBusinessMapper;
import com.ming.stock.mapper.SysUserMapper;
import com.ming.stock.pojo.domain.StockBlockDomain;
import com.ming.stock.pojo.entity.StockBusiness;
import com.ming.stock.pojo.entity.SysUser;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;
import java.util.List;
@SpringBootTest
public class TestSharding {
    @Autowired
    private SysUserMapper sysUserMapper;
    /**
     * 测试默认的数据源
     */
    @Test
    public void testDef(){
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(1246368763562037248L);
        System.out.println("sysUser = " + sysUser);
    }
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Test
    public void testBoradCast(){
        //测试广播表
        StockBusiness build = StockBusiness.builder().stockCode("900009").stockName("AAA").business("AAA").blockName("AAA")
                .blockLabel("AAA").updateTime(new Date()).build();
        //stockBusinessMapper.insert(build);
        stockBusinessMapper.deleteByPrimaryKey("90000");
    }
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Test
    public void testCommonDB(){
        //测试公共分库算法类
        Date date= DateTime.parse("2023-12-21 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockBlockDomain> indo=stockBlockRtInfoMapper.sectorAllLimit(date);
        System.out.println("indo = " + indo);
    }



}
