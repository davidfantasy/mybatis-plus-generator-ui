<template>
  <div>
    <div class="content-header"></div>
    <div class="content">
      <div class="container">
        <codemirror v-model="sqlCode" :options="cmOptions" />
        <el-dialog title="代码输出配置" :visible.sync="showDtoConfig" width="60%" top="5vh">
          <dto-strategy-form :sql="encodedSQL" @done="showDtoConfig = false"></dto-strategy-form>
        </el-dialog>
        <div style="margin-top:10px">
          <el-button type="primary" @click="genDtoFile">生成Mapper方法&DTO</el-button>
        </div>
      </div>
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
export default {
  components: {
    codemirror,
    HelpTip,
    DtoStrategyForm
  },
  data() {
    return {
      showDtoConfig: false,
      sqlCode: "",
      encodedSQL: "",
      cmOptions: {
        tabSize: 4,
        styleActiveLine: true,
        lineNumbers: true,
        line: true,
        mode: "text/x-mysql",
        theme: "solarized light"
      }
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
    }
  }
};
</script>
