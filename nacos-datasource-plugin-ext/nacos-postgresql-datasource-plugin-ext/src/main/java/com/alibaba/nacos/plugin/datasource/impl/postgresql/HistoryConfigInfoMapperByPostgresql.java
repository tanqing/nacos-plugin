package com.alibaba.nacos.plugin.datasource.impl.postgresql;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.DatabaseTypeConstant;
import com.alibaba.nacos.plugin.datasource.impl.base.BaseHistoryConfigInfoMapper;
import com.alibaba.nacos.plugin.datasource.impl.mysql.HistoryConfigInfoMapperByMySql;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

/**
 * The postgresql implementation of HistoryConfigInfoMapper.
 *
 * @author Long Yu
 **/
public class HistoryConfigInfoMapperByPostgresql extends BaseHistoryConfigInfoMapper {



    public MapperResult removeConfigHistory(MapperContext context) {
//        String sql = "DELETE FROM his_config_info WHERE gmt_modified < ? LIMIT ?";
        String sql="DELETE FROM his_config_info WHERE ctid IN (    SELECT ctid    FROM his_config_info    WHERE gmt_modified < ?    LIMIT ?)";
        return new MapperResult(sql, CollectionUtils.list(new Object[]{context.getWhereParameter("startTime"), context.getWhereParameter("limitSize")}));
    }




    //    @Override
    public String getDataSource() {
        return DatabaseTypeConstant.POSTGRESQL;
    }

}
