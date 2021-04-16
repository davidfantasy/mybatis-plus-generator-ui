<template>
  <el-form ref="editForm" :model="form" label-width="180px" size="mini">
    <el-form-item label="Entity的超类名称">
      <el-input v-model="form.superEntityClass"></el-input>
    </el-form-item>
    <el-form-item label="Entity的公共字段">
      <el-input v-model="superEntityColumn" style="width: 200px"></el-input>
      <el-button @click="addNewColumn">新增字段</el-button>
      <help-tip
        content="公共字段默认是从超类中继承的，即使表里面存在相关字段也不会生成到Entity中（注意，要配置数据库原始字段名，不是超类中的属性名！！）"
      ></help-tip>
      <div style="margin-top: 5px">
        <el-tag
          style="margin-right: 5px"
          v-for="col in form.superEntityColumns"
          :key="col"
          @close="removeColumn(col)"
          closable
          >{{ col }}</el-tag
        >
      </div>
    </el-form-item>
    <el-form-item label="需要自动填充的字段">
      <el-input v-model="tableFillCol" style="width: 300px">
        <el-select
          style="width: 120px"
          v-model="tableFillType"
          slot="prepend"
          placeholder="请选择"
        >
          <el-option label="insert_update" value="insert_update"></el-option>
          <el-option label="insert" value="insert"></el-option>
          <el-option label="update" value="update"></el-option>
        </el-select>
      </el-input>
      <el-button @click="addNewtableFill">新增字段</el-button>
      <help-tip
        content="设置了自定填充的字段，将会在字段上自动添加对应的注解项，例如：@TableField(fill = FieldFill.INSERT_UPDATE)"
      ></help-tip>
      <div style="margin-top: 5px">
        <el-tag
          style="margin-right: 5px"
          v-for="col in form.tableFills"
          :key="col"
          @close="removeTableFill(col)"
          closable
          >{{ col }}</el-tag
        >
      </div>
    </el-form-item>
    <el-form-item label="标识乐观锁的字段名">
      <el-input v-model="form.versionFieldName"></el-input>
    </el-form-item>
    <el-form-item label="标识逻辑删除的字段名">
      <el-input v-model="form.logicDeleteFieldName"></el-input>
    </el-form-item>
    <el-form-item label="是否生成serialVersionUID" placeholder>
      <el-switch v-model="form.entitySerialVersionUID"></el-switch>
    </el-form-item>
    <el-form-item label="是否生成字段名常量" placeholder>
      <el-switch v-model="form.entityColumnConstant"></el-switch>
      <help-tip
        content="为每个字段生成一个静态常量，如：public static final String ID = 'test_id'"
      ></help-tip>
    </el-form-item>
    <el-form-item label="是否启用ActiveRecord模式" placeholder>
      <el-switch v-model="form.activeRecord"></el-switch>
    </el-form-item>
    <el-form-item label="是否启用构建者模式" placeholder>
      <el-switch v-model="form.entityBuilderModel"></el-switch>
    </el-form-item>
    <el-form-item label=" 是否启用Lombok注解" placeholder>
      <el-switch v-model="form.entityLombokModel"></el-switch>
    </el-form-item>
    <el-form-item label="是否移除字段的is前缀" placeholder>
      <el-switch v-model="form.entityBooleanColumnRemoveIsPrefix"></el-switch>
      <help-tip
        content="比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx"
      ></help-tip>
    </el-form-item>
    <el-form-item label="是否强制生成字段名注解" placeholder>
      <el-switch v-model="form.entityTableFieldAnnotationEnable"></el-switch>
    </el-form-item>
    <el-form-item label="是否生成swagger2注解" placeholder>
      <el-switch v-model="form.swagger2"></el-switch>
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
    HelpTip,
  },
  data() {
    return {
      superEntityColumn: "",
      tableFillCol: "",
      tableFillType: "insert",
      form: {
        superEntityClass: "",
        superEntityColumns: [],
        tableFills: [],
        entitySerialVersionUID: false,
        entityColumnConstant: false,
        activeRecord: false,
        entityBuilderModel: false,
        entityLombokModel: false,
        entityBooleanColumnRemoveIsPrefix: false,
        entityTableFieldAnnotationEnable: false,
        versionFieldName: "",
        logicDeleteFieldName: "",
        swagger2: false,
      },
      rules: {},
    };
  },
  watch: {
    userConfig: function () {
      _.assign(this.form, this.userConfig.entityStrategy);
    },
  },
  mounted: function () {
    _.assign(this.form, this.userConfig.entityStrategy);
  },
  methods: {
    addNewColumn() {
      if (!this.form.superEntityColumns) {
        this.form.superEntityColumns = [];
      }
      if (this.superEntityColumn) {
        if (
          this.form.superEntityColumns.indexOf(this.superEntityColumn) === -1
        ) {
          this.form.superEntityColumns.push(this.superEntityColumn);
        }
        this.superEntityColumn = "";
      } else {
        this.$message.warning("请输入字段名");
      }
    },
    removeColumn(col) {
      if (!this.form.superEntityColumns) {
        return;
      }
      this.form.superEntityColumns.splice(
        this.form.superEntityColumns.indexOf(col),
        1
      );
    },
    addNewtableFill() {
      if (!this.form.tableFills) {
        this.form.tableFills = [];
      }

      if (this.tableFillCol) {
        let tableFill = this.tableFillCol + ":" + this.tableFillType;
        if (this.form.tableFills.indexOf(tableFill) === -1) {
          this.form.tableFills.push(tableFill);
        }
        this.tableFillCol = "";
      } else {
        this.$message.warning("请输入字段名");
      }
    },
    removeTableFill(col) {
      if (!this.form.tableFills) {
        return;
      }
      this.form.tableFills.splice(this.form.tableFills.indexOf(col), 1);
    },
    onSubmit() {
      axios
        .post("/api/output-file-info/save-entity-strategy", this.form)
        .then((res) => {
          this.$message.success("信息保存成功");
        });
    },
  },
};
</script>
