<template>
  <div>
    <div class="content-header"></div>
    <div class="content">
      <div class="container">
        <div style="margin-buttom:10px;">请输入待转换的SQL语句：</div>
        <el-alert title="Tip：" type="info" style="margin:10px;" show-icon :closable="false">
          支持在SQL中嵌入Mybatis的动态参数，系统会自动识别参数，并生成对应的条件控制语句
          <a
            href="javascript:;"
            @click="showDynamicParamsDemo = true"
          >查看示例</a>
        </el-alert>
        <codemirror v-model="sqlCode" :options="cmOptions" />
        <el-dialog title="SQL转代码的输出配置" :visible.sync="showDtoConfig" width="60%" top="5vh">
          <dto-strategy-form :sql="encodedSQL" @done="showDtoConfig = false"></dto-strategy-form>
        </el-dialog>
        <div style="margin-top:10px">
          <el-button type="primary" @click="genDtoFile">生成Mapper方法&DTO</el-button>
        </div>
      </div>
      <el-dialog
        :modal="false"
        title="动态SQL增强示例"
        :visible.sync="showDynamicParamsDemo"
        width="80%"
        top="5vh"
      >
        <dynamic-sql-demo></dynamic-sql-demo>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import { codemirror } from "vue-codemirror";
import "codemirror/mode/sql/sql.js";
import "codemirror/lib/codemirror.css";
import "codemirror/theme/solarized.css";
import HelpTip from "@/components/HelpTip";
import Axios from "axios";
import { Base64 } from "js-base64";
import DtoStrategyForm from "@/components/DtoStrategyForm";
import DynamicSqlDemo from "@/components/DynamicSqlDemo";

export default {
  components: {
    DynamicSqlDemo,
    codemirror,
    HelpTip,
    DtoStrategyForm,
  },
  data() {
    return {
      showDynamicParamsDemo: false,
      showDtoConfig: false,
      sqlCode: "",
      encodedSQL: "",
      cmOptions: {
        tabSize: 4,
        styleActiveLine: true,
        lineNumbers: true,
        line: true,
        mode: "text/x-mysql",
        theme: "solarized light",
      },
    };
  },
  methods: {
    genDtoFile() {
      if (!this.sqlCode) {
        this.$message.error("请先输入SQL语句");
        return;
      }
      this.encodedSQL = Base64.encodeURI(this.sqlCode);
      this.showDtoConfig = true;
    },
  },
};
</script>
