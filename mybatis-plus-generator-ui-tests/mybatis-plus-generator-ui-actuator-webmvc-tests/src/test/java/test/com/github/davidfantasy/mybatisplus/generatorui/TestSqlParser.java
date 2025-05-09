package test.com.github.davidfantasy.mybatisplus.generatorui;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.github.davidfantasy.mybatisplus.generatorui.common.sqlparser.ConditionExpr;
import com.github.davidfantasy.mybatisplus.generatorui.common.sqlparser.DynamicParamSqlEnhancer;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

public class TestSqlParser {


    private List<String> getCorrectDsql() {
        List<String> dsqls = Lists.newArrayList();
        dsqls.add("1=1 \n" +
                "<if test=\"orderCode!=null\">\n" +
                "  AND  order_code = #{orderCode}\n" +
                "</if>");
        dsqls.add("<if test=\"city!=null\">\n" +
                " AND city like concat('%',#{city},'%')\n" +
                "</if>");
        dsqls.add("<if test=\"customerIds!=null\">\n" +
                " AND customer_id in \n" +
                " <foreach item=\"item\" collection=\"#{customerIds}\" open=\"(\" separator=\",\" close=\")\">\n" +
                " #{item}\n" +
                " </foreach>\n" +
                "</if>");
        dsqls.add("<if test=\"creator!=null\">\n" +
                " AND creator = #{creator}\n" +
                "</if>");
        dsqls.add("<if test=\"startTime!=null\">\n" +
                " AND confirm_time between #{startTime} and #{endTime}\n" +
                "</if>");
        return dsqls;
    }


    @Test
    public void testConditionParse() {
        DataSourceConfig dsc = new DataSourceConfig.Builder("jdbc:mysql://localhost:3306/test", "", "")
                .build();
        DynamicParamSqlEnhancer enhancer = new DynamicParamSqlEnhancer(dsc.getDbType());
        String sql = "SELECT \n" +
                "    *\n" +
                "FROM\n" +
                "    t_order\n" +
                "WHERE order_code='#{orderCode}' AND city LIKE '#{city}'\n" +
                "        AND customer_id in  '#{customerIds}'\n" +
                "        AND creator =    '#{creator}'\n" +
                "        AND confirm_time BETWEEN '#{startTime}' AND  '#{endTime}'";
        System.out.println(sql);
        List<ConditionExpr> conditions = enhancer.parseSqlDynamicConditions(sql);
        Assertions.assertEquals(conditions.size(), 5);
        List<String> dParams = Lists.newArrayList();
        conditions.forEach((condition) -> {
            String dynamicSql = enhancer.toDynamicSql(condition).trim();
            System.out.println(dynamicSql);
            dParams.addAll(condition.getParamNames());
            Assertions.assertTrue(getCorrectDsql().contains(dynamicSql));
            Assertions.assertTrue(ReUtil.contains(Pattern.compile(condition.getFindPattern(), Pattern.CASE_INSENSITIVE), sql));
        });
        Assertions.assertTrue(
                CollUtil.containsAll(
                        dParams,
                        CollectionUtil.newArrayList("orderCode", "city", "customerIds", "creator", "startTime", "endTime")));
    }

    @Test
    public void testSqlParser2() {
        String sql = "SELECT * FROM (SELECT pay_time AS actionTime, total_fee AS totalFee, 1 AS isPay, pay_type AS actionType, status FROM t_payment WHERE payer_openid = '#{params.openid}' AND pay_time BETWEEN '#{params.startDate}' AND '#{params.endDate}' UNION ALL SELECT refund_time AS actionTime, refund_fee AS totalFee, 0 AS isPay, refund_type AS actionType, status FROM t_refund WHERE openid = '#{params.openid}' AND create_time BETWEEN '#{params.startDate}' AND '#{params.endDate}' ) t1 order by t1.actionTime desc";
        DataSourceConfig dsc = new DataSourceConfig.Builder("jdbc:mysql://localhost:3306/test", "", "")
                .build();
        DynamicParamSqlEnhancer enhancer = new DynamicParamSqlEnhancer(dsc.getDbType());
        List<ConditionExpr> conditions = enhancer.parseSqlDynamicConditions(sql);
        Assertions.assertEquals(conditions.size(), 4);
        conditions.forEach((condition) -> {
            String dynamicSql = enhancer.toDynamicSql(condition).trim();
            System.out.println(dynamicSql);
        });
    }

    @Test
    public void testSqlParser3() throws JSQLParserException {
        String sqlStr = "(  SELECT *\n" +
                "    FROM (  SELECT 1 )\n" +
                "    UNION ALL\n" +
                "    SELECT *\n" +
                "    FROM ( VALUES 1, 2, 3 )\n" +
                "    UNION ALL\n" +
                "    VALUES ( 1, 2, 3 ) )";
        ParenthesedSelect parenthesedSelect = (ParenthesedSelect) CCJSqlParserUtil.parse(sqlStr);
        SetOperationList setOperationList = parenthesedSelect.getSetOperationList();

        PlainSelect select1 = (PlainSelect) setOperationList.getSelect(0);
        PlainSelect subSelect1 = ((ParenthesedSelect) select1.getFromItem()).getPlainSelect();
        org.junit.jupiter.api.Assertions.assertEquals(1L, subSelect1.getSelectItem(0).getExpression(LongValue.class).getValue());

        Values values = (Values) setOperationList.getSelect(2);
        Assertions.assertEquals(3, values.getExpressions().size());

    }

    @Test
    public void testSqlParser4() {
        String sqlStr = "select 1 from dual where a=b";

        PlainSelect select = null;
        try {
            select = (PlainSelect) CCJSqlParserUtil.parse(sqlStr);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

        SelectItem selectItem =
                select.getSelectItems().get(0);
        Assertions.assertEquals(
                new LongValue(1)
                , selectItem.getExpression());

        Table table = (Table) select.getFromItem();
        Assertions.assertEquals("dual", table.getName());

        ComparisonOperator equalsTo = (ComparisonOperator) select.getWhere();
        Column a = (Column) equalsTo.getLeftExpression();
        Column b = (Column) equalsTo.getRightExpression();
        Assertions.assertEquals("a", a.getColumnName());
        Assertions.assertEquals("b", b.getColumnName());

    }


}
