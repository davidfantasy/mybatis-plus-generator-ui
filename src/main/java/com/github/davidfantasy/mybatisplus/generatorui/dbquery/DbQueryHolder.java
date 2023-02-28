package com.github.davidfantasy.mybatisplus.generatorui.dbquery;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.IDbQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class DbQueryHolder {

    @Autowired
    private DataSourceConfig dataSourceConfig;

    private final Map<DbType, IDbQuery> dbQueryMap = new EnumMap<>(DbType.class);

    public DbQueryHolder() {
        dbQueryMap.put(DbType.SQL_SERVER, new SqlServerQuery());
    }

    /**
     * 先查找本地是否有自定义的query实现，没有的话再去找mp内置的dbquery
     */
    public IDbQuery getDbQuery(DbType dbType) {
        IDbQuery dbQuery = dbQueryMap.get(dbType);
        if (dbQuery != null) {
            return dbQuery;
        }
        return dataSourceConfig.getDbQuery();
    }

}
