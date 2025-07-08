package com.alibaba.nacos.plugin.datasource.impl.postgresql;


import com.alibaba.nacos.plugin.datasource.constants.DatabaseTypeConstant;
import com.alibaba.nacos.plugin.datasource.impl.mysql.ConfigInfoGrayMapperByMySql;

public class ConfigInfoGrayMapperByPostgresql extends ConfigInfoGrayMapperByMySql {
//    @Override
    public String getDataSource() {
        return DatabaseTypeConstant.POSTGRESQL;
    }
}
