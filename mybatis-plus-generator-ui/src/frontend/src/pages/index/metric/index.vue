<template>
  <div>
    <page-header title="主页,指标聚合统计" :need-back="false"></page-header>
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
          <el-input v-model="params.project" placeholder="项目名称"></el-input>
          <el-input v-model="params.host" placeholder="主机名"></el-input>
          <el-input v-model="params.metric" placeholder="指标项"></el-input>
        </div>
      </div>
      <div>
        <el-table
          :data="datas"
          border
          style="width: 100%"
          :max-height="computedTableHeight"
          @sort-change="handlerListSortChange"
        >
          <el-table-column label="序号" width="60" type="index" fixed></el-table-column>
          <el-table-column label="指标" prop="metric" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column
            label="上报次数"
            sortable="custom"
            prop="count"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            label="时间汇总"
            sortable="custom"
            prop="timeConsuming"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column label="单次时间" prop="timeConsumingAvg" :show-overflow-tooltip="true"></el-table-column>
        </el-table>
        <pager
          ref="pager"
          :url="listAction"
          :orders="listColumnSortOrder"
          :params="params"
          @page-changed="pageChanged"
        ></pager>
      </div>
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
      datas: [],
      listAction: "/api/log/metric/list",
      reportTime: [],
      params: {
        startTime: new Date().getTime(),
        endTime: new Date().getTime() - 24 * 60 * 60 * 1000,
        metric: "",
        project: "",
        host: ""
      },
      listColumnPropMapping: {
        timeConsuming: "time_consuming"
      },
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
    dateChanged(value) {
      if (value && value.length === 2) {
        this.params.startTime = value[0].getTime();
        this.params.endTime = value[1].getTime();
      } else {
        this.reportTime = [
          new Date(),
          moment()
            .subtract(1, "days")
            .toDate()
        ];
      }
    },
    showErrorTrace(item) {
      if (item) {
        this.errorTraces = item.split(";");
      }
      this.showErrorTraceFlag = true;
    },
    pageChanged: function(datas) {
      this.datas = datas;
    }
  }
};
</script>
	
	
<style>
</style>
