package com.alibaba.nacos.plugin.datasource.impl.base;


import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.dialect.DatabaseDialect;
import com.alibaba.nacos.plugin.datasource.impl.mysql.ConfigInfoGrayMapperByMySql;
import com.alibaba.nacos.plugin.datasource.manager.DatabaseDialectManager;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

import java.util.Collections;

public class BaseConfigInfoGrayMapper extends ConfigInfoGrayMapperByMySql {

    private DatabaseDialect databaseDialect;

    public BaseConfigInfoGrayMapper() {
        databaseDialect = DatabaseDialectManager.getInstance().getDialect(getDataSource());
    }

    @Override
    public MapperResult findAllConfigInfoGrayForDumpAllFetchRows(MapperContext context) {
        String sql = " SELECT id,data_id,group_id,tenant_id,gray_name,gray_rule,app_name,content,md5,gmt_modified "
                + " FROM  config_info_gray  ORDER BY id OFFSET " + context.getStartRow() + " LIMIT " + context.getPageSize();
        return new MapperResult(sql, Collections.emptyList());
    }

    @Override
    public String getTableName() {
        return TableConstant.CONFIG_INFO_GRAY;
    }

    @Override
    public String getFunction(String functionName) {
        return databaseDialect.getFunction(functionName);
    }

}
