package com.github.davidfantasy.mybatisplus.generatorui.service;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.IDbQuery;
import com.github.davidfantasy.mybatisplus.generatorui.dto.TableInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSourceConfig dataSourceConfig;

    public List<TableInfo> getTablesFromDb() {
        IDbQuery dbQuery = dataSourceConfig.getDbQuery();
        List<Map<String, Object>> results = jdbcTemplate.queryForList(dbQuery.tablesSql());
        List<TableInfo> tableInfos = Lists.newArrayList();
        for (Map<String, Object> table : results) {
            TableInfo tableInfo = new TableInfo();
            tableInfo.setName((String) table.get(dbQuery.tableName()));
            tableInfo.setComment((String) table.get(dbQuery.tableComment()));
            tableInfos.add(tableInfo);
        }
        return tableInfos;
    }

}
