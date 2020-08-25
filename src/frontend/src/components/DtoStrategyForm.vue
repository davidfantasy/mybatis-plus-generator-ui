<template>
  <el-form ref="editForm" :rules="rules" :model="form" label-width="230px">
    <el-form-item label="代码作者">
      <el-input v-model="form.author" style="width:95%;"></el-input>
    </el-form-item>
    <el-form-item label="是否启用Lombok注解">
      <el-switch v-model="form.enableLombok"></el-switch>
    </el-form-item>
    <el-form-item prop="mapperPackage" label="Mapper(Xml)的包名">
      <el-autocomplete
        v-model="form.mapperPackage"
        style="width:95%;"
        :fetch-suggestions="queryMapperPackage"
        @select="handleMapperPackageSelect"
        placeholder="例如：com.example.OrderMapper"
      >
        <el-select
          v-model="form.mapperLocationPrefix"
          style="width:110px;"
          slot="prepend"
          placeholder="请选择源码目录"
        >
          <el-option label="java" value="java"></el-option>
          <el-option label="resources" value="resources"></el-option>
        </el-select>
      </el-autocomplete>
      <help-tip
        content="生成的Mapper方法所在的Mapper.xml的包名，例如：com.example.OrderMapper，选择java或resources会在对应的源码根目录中去查找资源"
      ></help-tip>
    </el-form-item>
    <el-form-item prop="mapperMethod" label="Mapper(Xml)的SQL节点ID">
      <el-input
        style="width:95%;"
        v-model="form.mapperMethod"
        placeholder="例如：selectOrders"
        @blur="handleMapperMethodInput"
      ></el-input>
    </el-form-item>
    <el-form-item label="封装查询结果的DTO对象" prop="fullPackage">
      <el-input
        :clearable="true"
        style="width:95%;"
        v-model="form.fullPackage"
        placeholder="例如：com.example.dto.ExampleResultDto"
      ></el-input>
      <help-tip content="用于映射查询的结果的DTO类，如果为空，则默认使用Map封装查询结果。需包含完整的包和类名称，不存在该类时会自动生成"></help-tip>
    </el-form-item>
    <el-form-item label="同步生成Mapper(java)的方法">
      <el-switch v-model="form.enableCreateDaoMethod"></el-switch>
    </el-form-item>
    <el-form-item v-if="form.enableCreateDaoMethod" label="Mapper(java)方法的参数类型">
      <el-radio-group v-model="form.daoMethodParamType">
        <el-radio label="map">Map（SQL动态参数作为Map的key）</el-radio>
        <el-radio label="bean">Java Bean（SQL动态参数作为Bean的Property）</el-radio>
        <el-radio label="single">多参数（SQL动态参数每个都作为一个独立方法参数）</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item
      v-if="form.enableCreateDaoMethod&&form.daoMethodParamType=='bean'"
      label="Mapper(java)方法参数DTO"
    >
      <el-input
        style="width:80%;"
        v-model="form.daoMethodParamDto"
        placeholder="例如：com.example.dto.XXXMethodParamsDto"
      ></el-input>
      <help-tip content="Mapper的方法参数Bean的引用位置，如不存在则会根据SQL动态参数自动生成。"></help-tip>
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
    HelpTip,
  },
  data() {
    return {
      showDynamicParamsDemo: false,
      userBasePkg: "",
      form: {
        enableLombok: false,
        enableCreateDaoMethod: false,
        enableParseDynamicParams: true,
        author: "",
        fullPackage: "",
        mapperMethod: "",
        mapperPackage: "",
        mapperLocation: "",
        mapperLocationPrefix: "resources",
        daoMethodParamType: "bean",
        daoMethodParamDto: "",
      },
      rules: {
        mapperPackage: [
          {
            required: true,
            message: "Mapper文件的包名必须填写",
            trigger: "change",
          },
        ],
        mapperMethod: [
          {
            required: true,
            message: "Mapper方法名必须填写",
            trigger: "change",
          },
        ],
      },
    };
  },
  mounted: function () {
    axios.get("/api/sql/basepackage").then((res) => {
      this.userBasePkg = res;
    });
  },
  methods: {
    handleMapperMethodInput() {
      if (this.form.mapperMethod) {
        this.form.fullPackage =
          this.userBasePkg +
          ".dto." +
          _.upperFirst(this.form.mapperMethod) +
          "ResultDto";
        this.form.daoMethodParamDto =
          this.userBasePkg +
          ".dto." +
          _.upperFirst(this.form.mapperMethod) +
          "ParamDto";
      }
    },
    queryMapperPackage(queryString, cb) {
      axios
        .get("/api/ac/mapperxml", {
          params: {
            mapperLocationPrefix: this.form.mapperLocationPrefix,
            searchKey: this.form.mapperPackage,
          },
        })
        .then((res) => {
          if (res) {
            let options = res.map((v) => {
              return { value: v };
            });
            cb(options);
          }
        });
    },
    handleMapperPackageSelect(op) {
      this.form.mapperPackage = op.value;
    },
    onSubmit() {
      this.$refs["editForm"].validate((valid) => {
        if (valid) {
          this.form.mapperLocation =
            this.form.mapperPackage + "." + this.form.mapperMethod;
          axios
            .post("/api/sql/gen-mapper-method", {
              sql: this.sql,
              config: this.form,
            })
            .then((res) => {
              this.$message.success("代码已经成功生成");
              this.$emit("done");
            });
        } else {
          return false;
        }
      });
    },
  },
};
</script>
