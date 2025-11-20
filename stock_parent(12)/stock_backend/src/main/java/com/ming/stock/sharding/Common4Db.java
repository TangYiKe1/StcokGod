
package com.ming.stock.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.joda.time.DateTime;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 定义公共的分库算法类:覆盖个股 大盘 板块相关的表
 */
public class Common4Db implements StandardShardingAlgorithm<Date> {

    /**
     * 精准查询
     * 分库策略: 按照年分库
     * 精确查询走这个方法的时候cur_time条件必须是=或者是in
     * @param dbNames   ds-2023   ds-2024   ds-2025
     * @param shardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> dbNames, PreciseShardingValue<Date> shardingValue) {
        //inline模式下分片算法 ds${user_id%2+1}
        //获取逻辑表名称 t_user
        String logicTableName = shardingValue.getLogicTableName();
        //分片键 cur_time
        String columnName = shardingValue.getColumnName();
        //获取数据库指定的分键片名称 条件值
        Date curTime = shardingValue.getValue();
        //自定义算法 跟传入的值找对应的数据源
        //获取条件值对象的年份 然后从db集合中过滤出以该年份结尾的数据源即可
        String year = new DateTime(curTime).getYear()+"";
        Optional<String> result = dbNames.stream().filter(dbname -> dbname.endsWith(year)).findFirst();
        if (result.isPresent()){
            return result.get();
        }

        return null;
    }

    /**
     * 根据片键的范围匹配数据库
     *
     * @param dsNames       ds-2023   ds-2024  ds-2025
     * @param shardingValue 片键信息的封装
     * @return 当前数据库查询名称的数据库集合
     * select * form xxx where user_id between 10 and 100
     */
    @Override
    public Collection<String> doSharding(Collection<String> dsNames, RangeShardingValue<Date> shardingValue) {
        //获取分片键名称
        String columnName = shardingValue.getColumnName();
        //获取逻辑表
        String logicTableName = shardingValue.getLogicTableName();
        //对范围数据的封装
        Range<Date> valueRange = shardingValue.getValueRange();
        //判断是否有范围查询的起始值
        if (valueRange.hasLowerBound()) {
            //获取起始值 2023
            Date startTime = valueRange.lowerEndpoint();//10
            //获取条件所属年份
            int startYear = new DateTime(startTime).getYear();
            //过滤出数据源中年份大于等于startYear 2023数据源
            //ds-2023 ds-2024 ds-2025
            dsNames = dsNames.stream().
                    filter(dsName->Integer.parseInt(
                            dsName.substring(dsName.lastIndexOf("-")+1))>=startYear)
                    .collect(Collectors.toList());
        }
        //判断是否有上限值
        if (valueRange.hasUpperBound()) {
            //获取起始值2023
            Date endTime = valueRange.upperEndpoint();
            //获取条件所属年份
            int endYear = new DateTime(endTime).getYear();
            //过滤出数据源中年份小于等于endYear
            dsNames = dsNames.stream().filter(
                            dsName->Integer.parseInt(
                                    dsName.substring(
                                            dsName.lastIndexOf("-")+1))<=endYear)
                    .collect(Collectors.toList());
        }
        return dsNames;
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