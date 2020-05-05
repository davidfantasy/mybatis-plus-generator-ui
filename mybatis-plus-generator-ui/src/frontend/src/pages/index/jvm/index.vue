<template>
  <div>
    <page-header title="主页,24小时JVM状态监控" :need-back="false"></page-header>
    <section class="content mag_t120">
      <div class="condition-bar clearfix flex-x-b">
        <div class="flex-1">
          <el-input v-model="params.project" placeholder="项目名称"></el-input>
          <el-input v-model="params.host" placeholder="主机名"></el-input>
          <el-button type="primary" @click="getData">开始监控</el-button>
        </div>
      </div>
      <div>
        <ve-line :data="chartData" :settings="chartSettings" :extend="chartExtend"></ve-line>
      </div>
    </section>
  </div>
</template>
<script lang="ts">
import axios, { AxiosResponse } from "axios";
import dayjs from "dayjs";
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import util from "@/assets/ts/ProjectUtil";
import _ from "lodash";

@Component
export default class extends Vue {
  private params: {
    project: String;
    host: String;
    gt$ts: Number;
  } = {
    project: "",
    host: "",
    gt$ts: 0
  };

  private chartExtend = {};

  private chartSettings = {
    labelMap: {
      maxMem: "最大内存",
      usedMem: "已使用内存"
    }
  };

  private chartData: {
    columns: Array<String>;
    rows: Array<Object>;
  } = {
    columns: ["ts", "maxMem", "usedMem"],
    rows: []
  };

  private intervalId: any;

  mounted() {}

  getData() {
    if (this.params.project && this.params.host) {
      console.log("getData");
      axios
        .get("/api/log/jvm/list", { params: util.wrapReqParams(this.params) })
        .then((res: any) => {
          _.forEach(res.records, v => {
            v.ts = dayjs(parseInt(v.ts)).format("HH:mm");
            v.maxMem = (parseInt(v.maxMem) / (1024 * 1024)).toFixed(2);
            v.usedMem = (parseInt(v.usedMem) / (1024 * 1024)).toFixed(2);
          });
          this.chartData.rows = res.records;
          this.params.gt$ts = new Date().getTime();
          if (!this.intervalId) {
            this.intervalId = setInterval(() => {
              this.getData();
            }, 60 * 1000);
          }
        });
    }
  }
}
</script>

<style>
</style>
