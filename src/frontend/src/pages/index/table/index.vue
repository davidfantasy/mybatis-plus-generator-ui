<template>
  <div>
    <div class="content-header"></div>
    <div class="content">
      <div class="container">
        <div style="margin-bottom: 10px">
          <el-input
            clearable
            style="width: 300px; margin-right: 20px"
            placeholder="请输入表名进行查询"
            prefix-icon="el-icon-search"
            v-model="searchKey"
          ></el-input>
          <el-button type="primary" @click="openGenSetting">代码生成</el-button>
          <span style="color: #e6a23c; font-size: 14px" v-if="isNewProject"
            >当前项目还没保存自定义配置，将使用默认配置。您也可以<a
              href="#"
              @click="openImportProjectView"
              >导入</a
            >其它项目的配置，或者自定义
            <router-link to="/config">配置</router-link>
          </span>
        </div>
        <el-table
          ref="tableList"
          :data="queryTables"
          border
          style="width: 100%"
          @selection-change="handleTableSelection"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column
            type="index"
            width="50"
            label="序号"
          ></el-table-column>
          <el-table-column sortable prop="name" label="table名称">
            <template slot-scope="scope">{{ scope.row.name }}</template>
          </el-table-column>
          <el-table-column prop="comment" label="table注释"></el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog
      :visible.sync="showGenSettingDialog"
      title="输出配置"
      width="70%"
      top="5vh"
    >
      <el-form label-width="220px">
        <el-form-item label="代码作者">
          <el-input v-model="genSetting.author" style="width: 260px"></el-input>
        </el-form-item>
        <el-form-item label="功能模块名">
          <el-input
            v-model="genSetting.moduleName"
            placeholder="模块名将加入到输出包名之后，用于划分不同的模块"
            style="width: 400px"
          ></el-input>
        </el-form-item>
        <el-form-item label="本次需生成的文件">
          <el-checkbox-group v-model="genSetting.choosedOutputFiles">
            <el-checkbox
              v-for="file in userConfig.outputFiles"
              :label="file.fileType"
              :key="file.fileType"
              >{{ file.fileType }}</el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="需生成的Controller方法" v-if="isControllerChecked">
          <el-alert
            title="注意：如果更换了Controller的模板，以下选项可能不会生效，需自行在模板中实现"
            type="warning"
          ></el-alert>
          <el-checkbox-group v-model="genSetting.choosedControllerMethods">
            <el-checkbox label="list" key="list">列表查询</el-checkbox>
            <el-checkbox label="getById" key="getById">按ID查询</el-checkbox>
            <el-checkbox label="create" key="create">新增</el-checkbox>
            <el-checkbox label="update" key="update">修改</el-checkbox>
            <el-checkbox label="delete" key="delete">删除</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="存在同名文件时是否覆盖">
          <el-switch v-model="genSetting.override"></el-switch>
        </el-form-item>
        <el-form-item label="目标项目根目录">
          <el-input
            v-model="genSetting.rootPath"
            style="width: 400px"
          ></el-input>
          <help-tip
            content="最终生成的代码位置等于：目标项目根目录 + 具体某个类别的源码设置的包目录"
          ></help-tip>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="genCode()">开始生成</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      :visible.sync="showSavedProjectsDialog"
      title="项目配置导入"
      width="50%"
      top="5vh"
    >
      <el-table :data="savedProjects" height="300px">
        <el-table-column label="项目包路径">
          <template slot-scope="scope">
            {{ scope.row }}
          </template>
        </el-table-column>
        <el-table-column label="导入">
          <template slot-scope="scope">
            <el-button
              type="info"
              size="mini"
              @click="importProjectConfig(scope.row)"
              >导入</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>
<script>
import axios from "axios";
import _ from "lodash";
import { Loading } from "element-ui";
import HelpTip from "@/components/HelpTip";
export default {
  props: [],
  components: {
    HelpTip,
  },
  data() {
    return {
      searchKey: "",
      showGenSettingDialog: false,
      tables: [],
      choosedTables: [],
      genSetting: {
        author: "",
        choosedOutputFiles: [],
        override: false,
        moduleName: "",
        choosedControllerMethods: [],
        rootPath: "",
      },
      userConfig: {},
      outputFileInfos: [],
      isNewProject: false,
      showSavedProjectsDialog: false,
      savedProjects: [],
    };
  },
  computed: {
    isControllerChecked: function () {
      if (this.genSetting.choosedOutputFiles) {
        if (this.genSetting.choosedOutputFiles.indexOf("Controller") !== -1) {
          return true;
        } else {
          return false;
        }
      }
      return false;
    },
    queryTables: function () {
      let tmp = [];
      this.tables.forEach((t, i) => {
        if (
          t.name &&
          (t.name.toLowerCase().indexOf(this.searchKey.toLowerCase()) !== -1 ||
            !this.searchKey)
        ) {
          tmp.push(t);
        }
      });
      return tmp;
    },
  },
  mounted: function () {
    this.getAllTables();
    this.getUserConfig();
    axios.get("/api/output-file-info/check-if-new-project").then((res) => {
      this.isNewProject = res;
    });
  },
  methods: {
    handleTableSelection(val) {
      this.choosedTables = val.map((t) => t.name);
    },
    getAllTables() {
      axios.get("/api/db/tables").then((res) => {
        this.tables = res;
      });
    },
    getUserConfig() {
      axios.get("/api/output-file-info/user-config").then((res) => {
        this.userConfig = res;
      });
    },
    openGenSetting() {
      if (this.choosedTables.length === 0) {
        this.$message.warning("请至少选择一张数据表");
        return;
      }
      //获取上一次的生成配置
      try {
        let lastSetting = sessionStorage.getItem("gen-setting");
        if (lastSetting) {
          _.assign(this.genSetting, JSON.parse(lastSetting));
          //清空部分一次性配置
          this.genSetting.moduleName = "";
          this.genSetting.override = false;
        }
        if (!this.genSetting.rootPath) {
          axios.get("/api/output-file-info/project-root-path").then((res) => {
            this.genSetting.rootPath = res;
          });
        }
      } catch (e) {
        console.error(e);
      }
      this.showGenSettingDialog = true;
    },
    genCode() {
      this.$confirm(
        "确认生成所选择的'" +
          this.choosedTables.length +
          "'张数据表的业务代码吗?",
        "操作提示",
        {
          type: "warning",
        }
      ).then(() => {
        let setting = JSON.stringify(_.clone(this.genSetting));
        sessionStorage.setItem("gen-setting", setting);
        let params = {};
        params.genSetting = this.genSetting;
        params.tables = this.choosedTables;
        let aLoading = Loading.service();
        axios
          .post("/api/mbp-generator/gen-code", params)
          .then((res) => {
            this.$message({
              message: "业务代码已生成",
              type: "success",
            });
            aLoading.close();
            this.showGenSettingDialog = false;
            this.choosedTables = [];
            this.$refs.tableList.clearSelection();
          })
          .catch(() => {
            aLoading.close();
          });
      });
    },
    openImportProjectView() {
      this.showSavedProjectsDialog = true;
      axios.get("/api/output-file-info/all-saved-project").then((res) => {
        this.savedProjects = res;
      });
    },
    importProjectConfig(sourceProjectPkg) {
      this.$confirm("确定导入" + sourceProjectPkg + "的配置吗？")
        .then(() => {
          axios
            .post(
              "/api/output-file-info/import-project-config/" + sourceProjectPkg
            )
            .then(() => {
              this.$message.success("配置已导入");
              this.showSavedProjectsDialog = false;
              setTimeout(() => {
                window.location.reload();
              }, 2);
            });
        })
        .catch(() => {});
    },
  },
};
</script>
<style></style>
