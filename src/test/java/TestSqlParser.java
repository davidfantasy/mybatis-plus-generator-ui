import cn.hutool.core.util.ReUtil;
import com.github.davidfantasy.mybatisplus.generatorui.sqlparser.ConditionExpr;
import com.github.davidfantasy.mybatisplus.generatorui.sqlparser.DynamicParamSqlEnhancer;
import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;


public class TestSqlParser {


    @Test
    public void testConditionParse() {
        DynamicParamSqlEnhancer enhancer = new DynamicParamSqlEnhancer();
        String sql = "SELECT \n" +
                "    *\n" +
                "FROM\n" +
                "    t_order\n" +
                "WHERE status = 1\n" +
                "    AND order_code='#{orderCode}' AND city LIKE '#{city}'\n" +
                "        AND customer_id IN  '#{customerId}'\n" +
                "        AND creator =    '#{creator}'\n" +
                "        AND confirm_time BETWEEN '#{startTime}' AND  '#{endTime}'";
        List<ConditionExpr> conditions = enhancer.parseSqlConditions(sql);
        Assertions.assertThat(conditions.size()).isEqualTo(5);
        List<String> dParams = Lists.newArrayList();
        conditions.forEach((condition) -> {
            System.out.println(condition);
            dParams.addAll(condition.getParamNames());
            Assertions.assertThat(ReUtil.contains(Pattern.compile(condition.getFindPattern(), Pattern.CASE_INSENSITIVE), sql)).isTrue();
        });
        Assertions.assertThat(dParams).contains("orderCode", "city", "customerId", "creator", "startTime", "endTime");
    }


}
