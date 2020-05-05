<template>
  <div>
    <page-header title="主页,错误明细列表" :need-back="false"></page-header>
    <section class="content mag_t120">
      <div class="condition-bar clearfix flex-x-b">
        <div class="flex-1">
          <el-date-picker
            :picker-options="pickerOptions"
            v-model="reportTime"
            :clearable="false"
            :editable="false"
            type="datetimerange"
            @change="dateChanged"
            :default-time="['00:00:00','23:59:59']"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </div>
      </div>
      <div>
        <el-table :data="datas" border style="width: 100%" :max-height="computedTableHeight">
          <el-table-column label="序号" width="60" type="index" fixed></el-table-column>
          <el-table-column label="项目" prop="project" width="100" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="错误类型" prop="errorName" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="错误内容" prop="errorMsg" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="主机名" prop="host" width="100" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="发生时间" width="180" prop="ts">
            <template slot-scope="scope">{{scope.row.ts | tsformat}}</template>
          </el-table-column>
          <el-table-column label="错误跟踪" width="180" prop="ts">
            <template slot-scope="scope">
              <a
                v-if="scope.row.errorTrace"
                href="javascript:;"
                @click="showErrorTrace(scope.row.errorTrace)"
              >详细</a>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <pager ref="pager" :url="listAction" :params="params" @page-changed="pageChanged"></pager>
      <el-dialog title="错误跟踪明细" :visible.sync="showErrorTraceFlag">
        <div>
          <div
            style="margin-top:2px;font-size:16px;"
            v-for="(trace,index) in errorTraces"
            :key="index"
          >{{trace}}</div>
        </div>
      </el-dialog>
    </section>
  </div>
</template>
<script>
import axios from "axios";
import moment from "moment";
import listMixin from "@/assets/js/list-mixin";
export default {
  mixins: [listMixin],
  data() {
    return {
      showErrorTraceFlag: false,
      errorTraces: null,
      datas: [],
      listAction: "/api/log/error/list",
      params: {},
      params: {
        bs$ts: new Date().getTime(),
        be$ts: new Date().getTime() - 24 * 60 * 60 * 1000
      },
      reportTime: [],
      pickerOptions: {
        shortcuts: [
          {
            text: "最近一天",
            onClick(picker) {
              picker.$emit("pick", [
                moment()
                  .subtract(1, "days")
                  .toDate(),
                moment().toDate()
              ]);
            }
          },
          {
            text: "最近一个月",
            onClick(picker) {
              picker.$emit("pick", [
                moment()
                  .subtract(1, "months")
                  .toDate(),
                moment().toDate()
              ]);
            }
          },
          {
            text: "最近三个月",
            onClick(picker) {
              picker.$emit("pick", [
                moment()
                  .subtract(3, "months")
                  .toDate(),
                moment().toDate()
              ]);
            }
          }
        ]
      }
    };
  },
  beforeMount: function() {
    this.reportTime = [
      moment()
        .subtract(1, "days")
        .toDate(),
      new Date()
    ];
  },
  methods: {
    showErrorTrace(item) {
      if (item) {
        this.errorTraces = item.split(";");
      }
      this.showErrorTraceFlag = true;
    },
    dateChanged(value) {
      if (value && value.length === 2) {
        this.params.bs$ts = value[0].getTime();
        this.params.be$ts = value[1].getTime();
      } else {
        this.reportTime = [
          new Date(),
          moment()
            .subtract(1, "days")
            .toDate()
        ];
      }
    },
    pageChanged: function(datas) {
      this.datas = datas;
    }
  }
};
</script>
	
	
<style>
</style>
