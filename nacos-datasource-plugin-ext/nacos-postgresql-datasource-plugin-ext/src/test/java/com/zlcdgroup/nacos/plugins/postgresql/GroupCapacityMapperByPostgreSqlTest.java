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
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.impl.mysql.GroupCapacityMapperByMysql;
import com.alibaba.nacos.plugin.datasource.impl.postgresql.GroupCapacityMapperByPostgresql;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class GroupCapacityMapperByPostgreSqlTest {

    private final Object[] emptyObjs = new Object[] {};

    int startRow = 0;

    int pageSize = 5;

    Object groupId = "group";

    Object createTime = new Timestamp(System.currentTimeMillis());

    Object modified = new Timestamp(System.currentTimeMillis());

    MapperContext context;

    private GroupCapacityMapperByPostgresql groupCapacityMapperByPostgresql;

    @BeforeEach
    void setUp() throws Exception {
        groupCapacityMapperByPostgresql = new GroupCapacityMapperByPostgresql();
        context = new MapperContext(startRow, pageSize);
        context.putUpdateParameter(FieldConstant.GMT_MODIFIED, modified);

        context.putWhereParameter(FieldConstant.GMT_MODIFIED, modified);
        context.putWhereParameter(FieldConstant.GROUP_ID, groupId);
    }

    @Test
    void testGetTableName() {
        String tableName = groupCapacityMapperByPostgresql.getTableName();
        assertEquals(TableConstant.GROUP_CAPACITY, tableName);
    }

    @Test
    void testGetDataSource() {
        String dataSource = groupCapacityMapperByPostgresql.getDataSource();
        assertEquals(DatabaseTypeConstant.POSTGRESQL, dataSource);
    }

    @Test
    void testInsertIntoSelect() {
        Object group = "group";
        Object quota = "quota";
        Object maxAggrSize = 10;
        Object maxAggrCount = 3;
        Object maxSize = 1;

        context.putUpdateParameter(FieldConstant.GROUP_ID, group);
        context.putUpdateParameter(FieldConstant.QUOTA, quota);
        context.putUpdateParameter(FieldConstant.MAX_SIZE, maxSize);
        context.putUpdateParameter(FieldConstant.MAX_AGGR_SIZE, maxAggrSize);
        context.putUpdateParameter(FieldConstant.MAX_AGGR_COUNT, maxAggrCount);

        context.putUpdateParameter(FieldConstant.GMT_CREATE, createTime);
        context.putUpdateParameter(FieldConstant.GMT_MODIFIED, modified);

        MapperResult mapperResult = groupCapacityMapperByPostgresql.insertIntoSelect(context);
        assertEquals(mapperResult.getSql(),
                "INSERT INTO group_capacity (group_id, quota, usage, max_size, max_aggr_count, max_aggr_size,gmt_create,"
                        + " gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info");

        assertArrayEquals(new Object[] {group, quota, maxSize, maxAggrCount, maxAggrSize, createTime, modified},
                mapperResult.getParamList().toArray());
    }

    @Test
    void testInsertIntoSelectByWhere() {
        Object group = "group";
        Object quota = "quota";
        Object maxAggrSize = 10;
        Object maxAggrCount = 3;
        Object maxSize = 1;
        Object createTime = new Timestamp(System.currentTimeMillis());
        Object modified = new Timestamp(System.currentTimeMillis());

        context.putUpdateParameter(FieldConstant.GROUP_ID, group);
        context.putUpdateParameter(FieldConstant.QUOTA, quota);
        context.putUpdateParameter(FieldConstant.MAX_SIZE, maxSize);
        context.putUpdateParameter(FieldConstant.MAX_AGGR_SIZE, maxAggrSize);
        context.putUpdateParameter(FieldConstant.MAX_AGGR_COUNT, maxAggrCount);
        context.putUpdateParameter(FieldConstant.GMT_CREATE, createTime);
        context.putUpdateParameter(FieldConstant.GMT_MODIFIED, modified);

        MapperResult mapperResult = groupCapacityMapperByPostgresql.insertIntoSelectByWhere(context);
        assertEquals(mapperResult.getSql(),
                "INSERT INTO group_capacity (group_id, quota, usage, max_size, max_aggr_count, max_aggr_size, gmt_create,"
                        + " gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info WHERE group_id=? AND tenant_id = ''");
        assertArrayEquals(new Object[] {group, quota, maxSize, maxAggrCount, maxAggrSize, createTime, modified, group},
                mapperResult.getParamList().toArray());
    }

    @Test
    void testIncrementUsageByWhereQuotaEqualZero() {
        Object usage = 1;
        context.putWhereParameter(FieldConstant.USAGE, usage);
        MapperResult mapperResult = groupCapacityMapperByPostgresql.incrementUsageByWhereQuotaEqualZero(context);
        assertEquals("UPDATE group_capacity SET usage = usage + 1, gmt_modified = ? WHERE group_id = ? AND usage < ? AND quota = 0",
                mapperResult.getSql());
        assertArrayEquals(new Object[] {modified, groupId, usage}, mapperResult.getParamList().toArray());
    }

    @Test
    void testIncrementUsageByWhereQuotaNotEqualZero() {

        MapperResult mapperResult = groupCapacityMapperByPostgresql.incrementUsageByWhereQuotaNotEqualZero(context);
        assertEquals("UPDATE group_capacity SET usage = usage + 1, gmt_modified = ? WHERE group_id = ? AND usage < quota AND quota != 0",
                mapperResult.getSql());
        assertArrayEquals(new Object[] {modified, groupId}, mapperResult.getParamList().toArray());
    }

    @Test
    void testIncrementUsageByWhere() {
        MapperResult mapperResult = groupCapacityMapperByPostgresql.incrementUsageByWhere(context);
        assertEquals("UPDATE group_capacity SET usage = usage + 1, gmt_modified = ? WHERE group_id = ?", mapperResult.getSql());
        assertArrayEquals(new Object[] {modified, groupId}, mapperResult.getParamList().toArray());
    }

    @Test
    void testDecrementUsageByWhere() {
        MapperResult mapperResult = groupCapacityMapperByPostgresql.decrementUsageByWhere(context);
        assertEquals("UPDATE group_capacity SET usage = usage - 1, gmt_modified = ? WHERE group_id = ? AND usage > 0",
                mapperResult.getSql());
        assertArrayEquals(new Object[] {modified, groupId}, mapperResult.getParamList().toArray());
    }

    @Test
    void testUpdateUsage() {
        MapperResult mapperResult = groupCapacityMapperByPostgresql.updateUsage(context);
        assertEquals("UPDATE group_capacity SET usage = (SELECT count(*) FROM config_info), gmt_modified = ? WHERE group_id = ?",
                mapperResult.getSql());
        assertArrayEquals(new Object[] {modified, groupId}, mapperResult.getParamList().toArray());
    }

    @Test
    void testUpdateUsageByWhere() {
        MapperResult mapperResult = groupCapacityMapperByPostgresql.updateUsageByWhere(context);
        assertEquals(mapperResult.getSql(),
                "UPDATE group_capacity SET usage = (SELECT count(*) FROM config_info WHERE group_id=? AND tenant_id = ''),"
                        + " gmt_modified = ? WHERE group_id= ?");
        assertArrayEquals(new Object[] {groupId, modified, groupId}, mapperResult.getParamList().toArray());
    }

    @Test
    void testSelectGroupInfoBySize() {
        Object id = 1;
        context.putWhereParameter(FieldConstant.ID, id);
        MapperResult mapperResult = groupCapacityMapperByPostgresql.selectGroupInfoBySize(context);
        System.out.printf(mapperResult.getSql());
        assertEquals("SELECT id, group_id FROM group_capacity WHERE id > ? LIMIT ? ", mapperResult.getSql());
        context.putWhereParameter(FieldConstant.GMT_CREATE, createTime);
        assertArrayEquals(new Object[] {id, pageSize}, mapperResult.getParamList().toArray());
    }
}
