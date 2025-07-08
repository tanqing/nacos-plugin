/*
 * Copyright 1999-2022 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zlcdgroup.nacos.plugins.postgresql;

import com.alibaba.nacos.plugin.datasource.constants.DataSourceConstant;
import com.alibaba.nacos.plugin.datasource.constants.DatabaseTypeConstant;
import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.impl.mysql.TenantInfoMapperByMySql;
import com.alibaba.nacos.plugin.datasource.impl.postgresql.TenantInfoMapperByPostgresql;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TenantInfoMapperByPostgreSqlTest {

    private TenantInfoMapperByPostgresql tenantInfoMapperByPostgresql;

    @BeforeEach
    void setUp() throws Exception {
        tenantInfoMapperByPostgresql = new TenantInfoMapperByPostgresql();
    }

    @Test
    void testGetTableName() {
        String tableName = tenantInfoMapperByPostgresql.getTableName();
        assertEquals(TableConstant.TENANT_INFO, tableName);
    }

    @Test
    void testGetDataSource() {
        String dataSource = tenantInfoMapperByPostgresql.getDataSource();
        assertEquals(DatabaseTypeConstant.POSTGRESQL, dataSource);
    }
}
