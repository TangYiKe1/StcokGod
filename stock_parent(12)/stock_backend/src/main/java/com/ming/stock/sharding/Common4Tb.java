
package com.ming.stock.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @Author: Ming
 * @Description: 定义精准和范围的分片算法
 */
public class Common4Tb implements StandardShardingAlgorithm<Date> {
    /**
     * eg:select * from stock_rt_info    eg: 2025->202501....202512
     * @param tbNames       从配置的数据节点:ds${1..2}.t_order_${1..2} 获取表名称集合 t_order_1 t_order_2
     * @param shardingValue 封装逻辑表 分片列名称 条件值等
     * @return
     */
    @Override
    public String doSharding(Collection<String> tbNames, PreciseShardingValue<Date> shardingValue) {
        //inline模式下分片算法 ds${user_id%2+1}
        //获取逻辑表名称 t_user
        String logicTableName = shardingValue.getLogicTableName();
        //分片键 cur_time
        String columnName = shardingValue.getColumnName();
        //获取数据库指定的分键片名称 条件值
        Date curTime = shardingValue.getValue();
        //获取条件之对应的年月  然后从tb集合中过滤出该年月结尾的数据源
        String yearMonth = new DateTime(curTime).toString("yyyyMM");
        Optional<String> result = tbNames.stream().filter(tbName->tbName.endsWith(yearMonth)).findFirst();
        if (result.isPresent()){
            return result.get();
        }


        return null;
    }

    /**
     * 根据片键的范围匹配数据库
     *
     * @param tbNames       所有数据源的名称集合
     * @param shardingValue 片键信息的封装
     * @return 当前数据库查询名称的数据库集合
     * select * form xxx where user_id between 10 and 100
     */
    @Override
    public Collection<String> doSharding(Collection<String> tbNames, RangeShardingValue<Date> shardingValue) {
        //获取分片键名称 cur_time
        String columnName = shardingValue.getColumnName();
        //获取逻辑表
        String logicTableName = shardingValue.getLogicTableName();
        //对范围数据的封装
        Range<Date> valueRange = shardingValue.getValueRange();
        //判断下限
        if (valueRange.hasLowerBound()) {
            //获取起始值
            Date startTime = valueRange.lowerEndpoint();//10
            //获取条件所属年份
            int startYearMonth = Integer.parseInt(new DateTime(startTime).toString("yyyyMM"));
            //过滤出数据中年份大于等于startYear 2023数据即可
            tbNames = tbNames.stream().filter(
                    tbName->Integer.parseInt(
                            tbName.substring(tbName.lastIndexOf("-")+1))
                            >=startYearMonth).collect(Collectors.toList());
        }
        //判断是否有上限值
        if (valueRange.hasUpperBound()) {
            Date endTime = valueRange.upperEndpoint();
            //获取条件所属年份
            int endYearMonth = Integer.parseInt(new DateTime(endTime).toString("yyyyMM"));
            ////过滤出数据中年份小于等于endYear
            tbNames = tbNames.stream().filter(
                    tbName->Integer.parseInt(tbName.substring(
                            tbName.lastIndexOf("-")+1))<=endYearMonth).collect(Collectors.toList());
        }
        //一般会根据start和end值找符合条件的数据源集合
        return tbNames;
    }
    //获取属性
    @Override
    public Properties getProps() {
        //返回属性
        return new Properties();
    }
    //初始化
    @Override
    public void init(Properties properties) {
        // 初始化逻辑（如果需要）
    }
}