<template>
  <el-form ref="editForm" :rules="rules" :model="form" label-width="230px">
    <el-form-item label="代码作者">
      <el-input v-model="form.author"></el-input>
    </el-form-item>
    <el-form-item label="DTO输出位置" prop="fullPackage">
      <el-input v-model="form.fullPackage" placeholder="com.example.dto.ExampleDto"></el-input>
      <help-tip content="生成文件的输出路径，包含完整的包和类名称，例如：com.example.ExampleDto"></help-tip>
    </el-form-item>
    <el-form-item label="DTO是否启用Lombok注解">
      <el-switch v-model="form.enableLombok"></el-switch>
    </el-form-item>
    <el-form-item label="DTO是否生成swagger2注解">
      <el-switch v-model="form.enableSwagger2"></el-switch>
    </el-form-item>
    <el-form-item label="同时生成Mapper方法及ResultMapper">
      <el-switch v-model="form.enableResultMapper"></el-switch>
      <help-tip content="开启后，可自动生成在制订的Mapper.xml中生成查询语句节点及ResultMapper（结果集映射表）"></help-tip>
    </el-form-item>
    <el-form-item
      v-if="form.enableResultMapper"
      prop="mapperLocation"
      label="Mapper方法引用"
      placeholder="com.example.ExampleMapper.selectDatas"
    >
      <el-input v-model="form.mapperLocation" placeholder="com.example.ExampleMapper.selectDatas">
        <el-select
          v-model="form.mapperLocationPrefix"
          style="width:110px;"
          slot="prepend"
          placeholder="请选择源码目录"
        >
          <el-option label="java"></el-option>
          <el-option label="resources" value="resources"></el-option>
        </el-select>
      </el-input>
      <help-tip
        content="生成的Mapper方法所在的Mapper.xml的包名，以及节点ID，例如：com.example.ExampleMapper.selectDatas，其中selectDatas为节点ID。选择java或resources会在对应的源码根目录中去查找资源"
      ></help-tip>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">生成代码</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
import axios from "axios";
import _ from "lodash";
import HelpTip from "@/components/HelpTip";
export default {
  props: ["sql"],
  components: {
    HelpTip
  },
  data() {
    return {
      form: {
        enableLombok: false,
        enableSwagger2: false,
        author: "",
        fullPackage: "",
        enableResultMapper: true,
        mapperLocation: "",
        mapperLocationPrefix: "resources"
      },
      rules: {
        fullPackage: [
          {
            required: true,
            message: "DTO输出位置输出位置必须填写",
            trigger: "change"
          }
        ],
        mapperLocation: [
          {
            required: true,
            message: "查询语句所在位置必须填写",
            trigger: "change"
          }
        ]
      }
    };
  },
  watch: {
    enableResultMapper() {
      if (this.enableResultMapper) {
        this.rules.mapperLocation = [
          {
            required: true,
            message: "查询语句所在位置必须填写",
            trigger: "change"
          }
        ];
      } else {
        this.rules.mapperLocation = null;
        this.form.mapperLocation = "";
      }
    }
  },
  mounted: function() {
    axios.get("/api/sql/basepackage").then(res => {
      this.form.fullPackage = res + ".dto.ExampleDto";
    });
  },
  methods: {
    onSubmit() {
      this.$refs["editForm"].validate(valid => {
        if (valid) {
          axios
            .post("/api/sql/gen-dto-file", { sql: this.sql, config: this.form })
            .then(res => {
              this.$message.success("代码已经成功生成");
              this.$emit("done");
            });
        } else {
          return false;
        }
      });
    }
  }
};
</script>
