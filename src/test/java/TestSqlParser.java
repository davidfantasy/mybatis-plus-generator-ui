import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.github.davidfantasy.mybatisplus.generatorui.sqlparser.ConditionExpr;
import com.github.davidfantasy.mybatisplus.generatorui.sqlparser.DynamicParamSqlEnhancer;
import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(conditions.size()).isEqualTo(5);
        List<String> dParams = Lists.newArrayList();
        conditions.forEach((condition) -> {
            String dynamicSql = enhancer.toDynamicSql(condition).trim();
            System.out.println(dynamicSql);
            dParams.addAll(condition.getParamNames());
            Assertions.assertThat(getCorrectDsql().contains(dynamicSql)).isTrue();
            Assertions.assertThat(ReUtil.contains(Pattern.compile(condition.getFindPattern(), Pattern.CASE_INSENSITIVE), sql)).isTrue();
        });
        Assertions.assertThat(dParams).contains("orderCode", "city", "customerIds", "creator", "startTime", "endTime");
    }

    @Test
    public void testSqlParser2() {
        String sql = "SELECT * FROM (SELECT pay_time AS actionTime, total_fee AS totalFee, 1 AS isPay, pay_type AS actionType, status FROM t_payment WHERE payer_openid = '#{params.openid}' AND pay_time BETWEEN '#{params.startDate}' AND '#{params.endDate}' UNION ALL SELECT refund_time AS actionTime, refund_fee AS totalFee, 0 AS isPay, refund_type AS actionType, status FROM t_refund WHERE openid = '#{params.openid}' AND create_time BETWEEN '#{params.startDate}' AND '#{params.endDate}' ) t1 order by t1.actionTime desc";
        DataSourceConfig dsc = new DataSourceConfig.Builder("jdbc:mysql://localhost:3306/test", "", "")
                .build();
        DynamicParamSqlEnhancer enhancer = new DynamicParamSqlEnhancer(dsc.getDbType());
        List<ConditionExpr> conditions = enhancer.parseSqlDynamicConditions(sql);
        conditions.forEach((condition) -> {
            String dynamicSql = enhancer.toDynamicSql(condition).trim();
            System.out.println(dynamicSql);
        });
    }


}
