<template>
  <div>
    <div class="content-header"></div>
    <div class="content">
      <div class="container">
        <div class="pull-right" style="margin-bottom:10px;">
          <el-button type="primary" @click="addNew()">新增</el-button>
        </div>
        <el-table :data="userConfig.outputFiles" border style="width: 100%">
          <el-table-column prop="fileType" label="输出文件类型"></el-table-column>
          <el-table-column width="500" prop="outputLocation" label="输出路径"></el-table-column>
          <el-table-column label="文件模板">
            <template slot-scope="scope">
              <a href="javascript:;" @click="download(scope.row.fileType, scope.row.templateName)">
                <i class="fa fa-paperclip"></i>
                {{scope.row.templateName}}
              </a>
            </template>
          </el-table-column>
          <el-table-column label="是否内置" width="80">
            <template slot-scope="scope">
              <span v-if="scope.row.builtIn" style="color:red;">是</span>
              <span v-else>否</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="info"
                icon="el-icon-edit"
                circle
                @click="editFileInfo(scope.row)"
              ></el-button>
              <el-button
                size="mini"
                circle
                type="warning"
                icon="el-icon-delete"
                v-if="!scope.row.builtIn"
                @click="deleteFileInfo(scope.row)"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-dialog :visible.sync="showEditForm" width="70%" top="5vh">
        <el-tabs v-model="activeName">
          <el-tab-pane label="基本配置" name="base">
            <el-form ref="editForm" :rules="rules" :model="form" label-width="120px">
              <el-form-item
                label="类型标识"
                prop="fileType"
                placeholder="用于标识该类生成文件，如：服务接口,Controller等"
              >
                <el-input v-model="form.fileType" :readonly="isUpdate"></el-input>
              </el-form-item>
              <el-form-item label="包名" prop="outputLocationPkg" placeholder="例如：org.example.entity">
                <el-input v-model="form.outputLocationPkg" placeholder="org.example.entity">
                  <el-select
                    v-model="form.outputLocationPrefix"
                    style="width:110px;"
                    slot="prepend"
                    placeholder="请选择源码目录"
                  >
                    <el-option label="java" value="java"></el-option>
                    <el-option label="resources" value="resources"></el-option>
                  </el-select>
                </el-input>
                <help-tip
                  content="生成的文件所输出的位置，选择不同的源码目录，会影响输出位置的根目录。例如选择resources，将会以resources作为包的根目录:example.mapper将保存到/resources/example/mapper目录中"
                ></help-tip>
              </el-form-item>
              <el-form-item label="文件模板" prop="templateName">
                <a
                  href="javascript:;"
                  @click="download(form.fileType, form.templateName)"
                >{{ form.templateName }}</a>
                <el-upload
                  ref="upload"
                  :show-file-list="false"
                  :limit="1"
                  :data="uploadParams"
                  :file-list="uplaodFileList"
                  action="/api/template/upload"
                  :on-success="onUploadSuccess"
                  :before-upload="beforeTemplateUpload"
                >
                  <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                  <div slot="tip" class="el-upload__tip">仅支持.btl文件</div>
                </el-upload>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="onSubmit">保存</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="策略配置" name="strategy" v-if="form.builtIn">
            <entity-strategy-form v-if="form.fileType=='Entity'" :user-config="userConfig"></entity-strategy-form>
            <mapper-xml-strategy-form v-if="form.fileType=='Mapper.xml'" :user-config="userConfig"></mapper-xml-strategy-form>
            <mapper-strategy-form v-if="form.fileType=='Mapper.java'" :user-config="userConfig"></mapper-strategy-form>
            <service-strategy-form v-if="form.fileType=='Service'" :user-config="userConfig"></service-strategy-form>
            <service-impl-strategy-form
              v-if="form.fileType=='ServiceImpl'"
              :user-config="userConfig"
            ></service-impl-strategy-form>
            <controller-strategy-form v-if="form.fileType=='Controller'" :user-config="userConfig"></controller-strategy-form>
          </el-tab-pane>
        </el-tabs>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import _ from "lodash";
import FileSaver from "file-saver";
import EntityStrategyForm from "@/components/EntityStrategyForm";
import MapperXmlStrategyForm from "@/components/MapperXmlStrategyForm";
import MapperStrategyForm from "@/components/MapperStrategyForm";
import ServiceStrategyForm from "@/components/ServiceStrategyForm";
import ServiceImplStrategyForm from "@/components/ServiceImplStrategyForm";
import ControllerStrategyForm from "@/components/ControllerStrategyForm";
import HelpTip from "@/components/HelpTip";

export default {
  props: [],
  components: {
    EntityStrategyForm,
    MapperStrategyForm,
    MapperXmlStrategyForm,
    ServiceStrategyForm,
    ServiceImplStrategyForm,
    ControllerStrategyForm,
    HelpTip,
  },
  data() {
    return {
      activeName: "base",
      isUpdate: false,
      uplaodFileList: [],
      uploadParams: {
        fileType: "",
      },
      form: {
        fileType: "",
        packageName: "",
        outputLocationPkg: "",
        outputLocationPrefix: "",
        outputLocation: "",
        templateName: "",
        templatePath: "",
        builtIn: false,
      },
      rules: {
        fileType: [
          { required: true, message: "请输入文件类型", trigger: "change" },
        ],
        outputLocationPkg: [
          {
            required: true,
            message: "请输入保存文件的包名",
            trigger: "change",
          },
        ],
        templateName: [
          { required: true, message: "请上传文件模板", trigger: "change" },
        ],
      },
      userConfig: {},
      showEditForm: false,
      searchKey: "",
      tables: [],
      choosedTables: [],
    };
  },
  mounted: function () {
    this.getFileInfos();
  },
  methods: {
    editFileInfo(fileInfo) {
      this.form = Object.assign(this.form, fileInfo);
      this.isUpdate = true;
      this.uploadParams.fileType = this.form.fileType;
      if (this.form.outputLocation) {
        if (this.form.outputLocation.indexOf(":") !== -1) {
          let temp = this.form.outputLocation.split(":");
          this.form.outputLocationPrefix = temp[0];
          this.form.outputLocationPkg = temp[1];
        } else {
          this.form.outputLocationPkg = this.form.outputLocation;
        }
      }
      //mapper.xml文件默认根目录为resources，其它默认为java
      if (!this.form.outputLocationPrefix) {
        if (this.form.fileType === "Mapper.xml") {
          this.form.outputLocationPrefix = "resources";
        } else {
          this.form.outputLocationPrefix = "java";
        }
      }
      this.showEditForm = true;
    },
    getFileInfos() {
      axios.get("/api/output-file-info/user-config").then((res) => {
        this.userConfig = res;
      });
    },
    clearForm() {
      this.form.fileType = "";
      this.form.outputLocationPrefix = "";
      this.form.outputLocationPkg = "";
      this.form.outputLocation = "";
      this.form.templateName = "";
      this.form.templatePath = "";
      this.form.builtIn = false;
    },
    addNew() {
      this.showEditForm = true;
      this.isUpdate = false;
      this.clearForm();
    },
    beforeTemplateUpload(file) {
      let tempArray = file.name.split(".");
      let fileType = tempArray[tempArray.length - 1];
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (fileType != "btl") {
        this.$message.error("请选择正确的模板文件格式");
        return false;
      }
      if (!isLt2M) {
        this.$message.error("模板文件不能超过 2MB!");
        return false;
      }
      return true;
    },
    onSubmit() {
      this.$refs["editForm"].validate((valid) => {
        if (valid) {
          this.form.outputLocation =
            this.form.outputLocationPrefix + ":" + this.form.outputLocationPkg;
          axios.post("/api/output-file-info/save", this.form).then((res) => {
            this.$message.success("信息保存成功");
            this.clearForm();
            this.activeName = "base";
            this.showEditForm = false;
            this.getFileInfos();
          });
        } else {
          return false;
        }
      });
    },
    deleteFileInfo(fileInfo) {
      this.$confirm("确认删除?", "操作提示", {
        type: "warning",
      }).then(() => {
        axios.post("/api/output-file-info/delete", fileInfo).then((res) => {
          this.$message({
            message: "输出文件已删除",
            type: "success",
          });
          this.getFileInfos();
        });
      });
    },
    download(fileType, templateName) {
      axios
        .get("/api/template/download", {
          params: {
            fileType: fileType,
          },
          responseType: "blob",
        })
        .then((response) => {
          FileSaver.saveAs(response, templateName);
        });
    },
    onUploadSuccess(res, file, fileList) {
      if (res.code === 200) {
        this.$message.success("模板文件上传成功");
        this.form.templateName = res.data.templateName;
        this.form.templatePath = res.data.templatePath;
        this.$refs.upload.clearFiles();
      } else {
        this.$message.error("模板文件上传失败");
      }
    },
  },
};
</script>
<style></style>
