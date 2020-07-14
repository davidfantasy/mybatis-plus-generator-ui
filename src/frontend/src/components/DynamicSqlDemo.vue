<template>
  <div>
    <div>动态sql增强是指将含有mybatis动态参数（#{...}）的where条件，转换为mybatis的动态SQL条件，一般用于用户输入条件进行查询的情况，例如：</div>
    <codemirror v-model="sqlCode" :options="sqlOptions" />
    <div style="margin-buttom:10px,margin-top:10px">
      以上的SQL将会被转换为下面的动态SQL,注意
      <span style="color:red">动态参数需要用引号括起来</span>
    </div>
    <codemirror v-model="xmlCode" :options="xmlOptions" />
  </div>
</template>
<script>
import { codemirror } from "vue-codemirror";
import "codemirror/mode/xml/xml.js";
export default {
  components: {
    codemirror
  },
  data() {
    return {
      sqlOptions: {
        readOnly: true,
        tabSize: 4,
        styleActiveLine: true,
        lineNumbers: true,
        line: true,
        mode: "text/x-mysql",
        theme: "solarized light"
      },
      xmlOptions: {
        readOnly: true,
        tabSize: 4,
        styleActiveLine: true,
        lineNumbers: true,
        line: true,
        mode: "application/xml",
        theme: "solarized light"
      },
      sqlCode: "",
      xmlCode: ""
    };
  },
  mounted: function() {
    this.sqlCode =
      "SELECT \n" +
      "    *\n" +
      "FROM\n" +
      "    t_order t\n" +
      "        LEFT JOIN\n" +
      "    t_order_good t1 ON t.id = t1.order_id\n" +
      "WHERE\n" +
      "    t.order_code = '#{orderCode}'\n" +
      "        AND t.city LIKE '#{city}'\n" +
      "        AND t.customer_id IN '#{customerIds}'\n" +
      "        AND t.creator = '#{creator}'\n" +
      "        AND t.confirm_time BETWEEN '#{startTime}' AND '#{endTime}'";
    this.xmlCode =
      "<!--Author:david，Date:2020-07-14,由mybatis-plus-generator-ui自动生成-->\n" +
      '  <select id="selectOrders" resultMap="ExampleDtoMap"> <![CDATA[\n' +
      "      SELECT \n" +
      "          *\n" +
      "      FROM\n" +
      "          t_order t\n" +
      "              LEFT JOIN\n" +
      "          t_order_good t1 ON t.id = t1.order_id\n" +
      "      WHERE\n" +
      "           1=1 \n" +
      '      <if test="order_code!=null">\n' +
      "        AND  order_code = #{orderCode}\n" +
      "      </if>\n" +
      '      <if test="city!=null">\n' +
      "       AND city like concat('%',#{city},'%')\n" +
      "      </if>\n" +
      '      <if test="customer_id!=null">\n' +
      "       AND customer_id in \n" +
      '       <foreach item="item" collection="#{customerIds}" open="(" separator="," close=")">\n' +
      "       #{item}\n" +
      "       </foreach>\n" +
      "      </if>\n" +
      '      <if test="creator!=null">\n' +
      "       AND creator = #{creator}\n" +
      "      </if>\n" +
      '      <if test="confirm_time!=null">\n' +
      "       AND confirm_time between #{startTime} and #{endTime}\n" +
      "      </if>\n" +
      "    ]]>\n" +
      "  </select>";
  },
  methods: {}
};
</script>
