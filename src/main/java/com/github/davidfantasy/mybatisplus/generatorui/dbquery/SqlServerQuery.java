package com.github.davidfantasy.mybatisplus.generatorui.dbquery;

public class SqlServerQuery extends com.baomidou.mybatisplus.generator.config.querys.SqlServerQuery {

    /**
     * 相对于MP原版增加了以SCHEMA作为过滤条件
     */
    @Override
    public String tablesSql() {
        return "select a.TABLE_NAME,b.COMMENTS from INFORMATION_SCHEMA.TABLES a left join \n" +
                "(select cast(so.name as varchar(500)) as TABLE_NAME, cast(sep.value as varchar(500)) as COMMENTS from sysobjects so left JOIN sys.extended_properties sep on sep.major_id = so.id and sep.minor_id = 0 where (xtype = 'U' or xtype = 'v')) b \n" +
                "on a.TABLE_NAME = b.TABLE_NAME\n" +
                "where a.TABLE_SCHEMA = '%s'";
    }

}
