<template>
  <el-form ref="editForm" :model="form" label-width="180px" size="mini">
    <el-form-item label="Controller的超类名称">
      <el-input v-model="form.superControllerClass"></el-input>
    </el-form-item>
    <el-form-item label="启用REST接口注解" placeholder>
      <el-switch v-model="form.restControllerStyle"></el-switch>
      <help-tip content="用@RestController注解替换@Controller"></help-tip>
    </el-form-item>
    <el-form-item label="Mapping Url格式转换" placeholder>
      <el-switch v-model="form.controllerMappingHyphenStyle"></el-switch>
      <help-tip content="驼峰转连字符，如：/managerUserActionHistory -> /manager-user-action-history "></help-tip>
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
        superControllerClass: "",
        restControllerStyle: false,
        controllerMappingHyphenStyle: false
      },
      rules: {}
    };
  },
  watch: {
    userConfig: function() {
      _.assign(this.form, this.userConfig.controllerStrategy);
    }
  },
  mounted: function() {
    _.assign(this.form, this.userConfig.controllerStrategy);
  },
  methods: {
    onSubmit() {
      axios
        .post("/api/output-file-info/save-controller-strategy", this.form)
        .then(res => {
          this.$message.success("信息保存成功");
        });
    }
  }
};
</script>