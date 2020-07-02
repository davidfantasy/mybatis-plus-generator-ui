package com.github.davidfantasy.mybatisplus.generatorui.sqlparser;

import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

import java.util.List;

@Slf4j
public class DynamicParamSqlEnhancer {


    public List<ConditionExpr> parseSqlConditions(String sql) {
        if (Strings.isNullOrEmpty(sql)) {
            throw new ServiceException("sql不能为空");
        }
        try {
            CCJSqlParser ccjSqlParser = new CCJSqlParser(sql);
            Statement statement = ccjSqlParser.Statement();
            if (statement instanceof Select) {
                SelectConditionParser parser = new SelectConditionParser();
                Select select = (Select) statement;
                select.getSelectBody().accept(parser);
                return parser.getParsedConditions();
            } else {
                throw new ServiceException("只能处理SQL查询语句");
            }
        } catch (ParseException e) {
            log.error("解析SQL条件发生错误", e);
            throw new ServiceException("解析SQL条件发生错误");
        }
    }


}
