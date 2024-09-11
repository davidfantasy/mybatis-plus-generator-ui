package com.github.davidfantasy.mybatisplus.generatorui.common.service;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.IDbQuery;
import com.github.davidfantasy.mybatisplus.generatorui.common.dbquery.DbQueryHolder;
import com.github.davidfantasy.mybatisplus.generatorui.common.dto.TableInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Autowired
    private DbQueryHolder dbQueryHolder;

    public List<TableInfo> getTablesFromDb() {
        IDbQuery dbQuery = dbQueryHolder.getDbQuery(dataSourceConfig.getDbType());
        //利用jdbcTemplate来查询数据库表信息，然后组装信息
        List<Map<String, Object>> results = jdbcTemplate.queryForList(getTableSql());
        List<TableInfo> tableInfos = Lists.newArrayList();
        for (Map<String, Object> table : results) {
            TableInfo tableInfo = new TableInfo();
            tableInfo.setName((String) table.get(dbQuery.tableName()));
            tableInfo.setComment((String) table.get(dbQuery.tableComment()));
            tableInfos.add(tableInfo);
        }
        tableInfos.sort(Comparator.comparing(TableInfo::getName));
        return tableInfos;
    }

    public String getTableSql() {
        String tablesSql = dbQueryHolder.getDbQuery(dataSourceConfig.getDbType()).tablesSql();
        String schema = dataSourceConfig.getSchemaName();
        if (schema == null) {
            schema = getDefaultSchema();
        }
        tablesSql = String.format(tablesSql, schema);
        return tablesSql;
    }


    private String getDefaultSchema() {
        String schema = null;
        DbType dbType = dataSourceConfig.getDbType();
        if (DbType.POSTGRE_SQL == dbType) {
            //pg 默认 schema=public
            schema = "public";
        } else if (DbType.KINGBASE_ES == dbType) {
            //kingbase 默认 schema=PUBLIC
            schema = "PUBLIC";
        } else if (DbType.DB2 == dbType) {
            //db2 默认 schema=current schema
            schema = "current schema";
        } else if (DbType.ORACLE == dbType) {
            //oracle 默认 schema=username
            schema = Objects.requireNonNull(dataSourceConfig.getUsername()).toUpperCase();
        } else if (DbType.SQL_SERVER == dbType) {
            //SQL_SERVER 2005以上 默认 schema=dbo
            schema = "dbo";
        }
        return schema;
    }

}
