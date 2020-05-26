<template>
  <el-form ref="editForm" :model="form" label-width="180px" size="mini">
    <el-form-item label="Service的超类名称">
      <el-input v-model="form.superServiceClass"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">保存</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
import axios from "axios";
import _ from "lodash";
import HelpTip from "@/components/HelpTip";
export default {
  props: ["userConfig"],
  components: {
    HelpTip
  },
  data() {
    return {
      form: {
        superServiceClass: ""
      },
      rules: {}
    };
  },
  watch: {
    userConfig: function() {
      _.assign(this.form, this.userConfig.serviceStrategy);
    }
  },
  mounted: function() {
    _.assign(this.form, this.userConfig.serviceStrategy);
  },
  methods: {
    onSubmit() {
      axios
        .post("/api/output-file-info/save-service-strategy", this.form)
        .then(res => {
          this.$message.success("信息保存成功");
        });
    }
  }
};
</script>