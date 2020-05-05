<template>
  <div style="text-align:right" v-if="totalCount!=0">
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-sizes="[10, 20, 50, 100]"
      :page-size.sync="pageSize"
      :pager-count="11"
      layout="total, sizes, prev, pager, next"
      :total="totalCount"
    ></el-pagination>
    <div v-if="totalCount==0" style="padding-top:20px;">
      <el-alert title="当前没有查询到任何数据" :closable="false" type="info" center show-icon></el-alert>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import _ from "lodash";
import { Loading } from "element-ui";
export default {
  props: {
    autoFreshWhenParamsChanged: {
      type: Boolean,
      default: true
    },
    fetchDataOnMounted: {
      type: Boolean,
      default: true
    },
    params: {
      default: {}
    },
    defaultPageSize: {
      type: Number,
      default: 10
    },
    url: {
      type: String,
      default: ""
    },
    statusHoldKey: {
      type: String,
      default: ""
    },
    confirmOnDataChanged: {
      type: Boolean,
      default: false
    },
    /**
     * 排序字段
     */
    orders: {
      type: Object,
      default: function() {
        return {};
      }
    }
  },
  data: function() {
    return {
      totalPages: 0,
      totalCount: 0,
      currentPage: 1,
      pageSize: 10,
      lastSavedPageSize: 10,
      lastSavedPage: 1,
      cachedPageIndex: null,
      wacthParamsFlag: false
    };
  },
  beforeMount() {
    this.pageSize = this.defaultPageSize;
    //获取状态保持的key，如果外部有传入，优先使用外部传入参数
    let statusHoldKey = this.statusHoldKey
      ? this.statusHoldKey
      : this.$route.fullPath;
    //尝试恢复最后一次访问该页面的当前页码
    let status = this.$store.state.pageStatus[statusHoldKey];
    if (status) {
      if (status.currentPage) {
        this.cachedPageIndex = status.currentPage;
      }
      if (status.currentPageSize) {
        this.pageSize = status.currentPageSize;
      }
    }
  },
  mounted: function() {
    if (this.fetchDataOnMounted) {
      if (this.cachedPageIndex) {
        this.fetchData(this.cachedPageIndex, true, () => {
          //如果存在已经缓存的页码，说明需要恢复最后一次访问的页码
          //注意，对this.currentPage的更新必须是在数据查询完成后，否则是无效的
          this.currentPage = this.cachedPageIndex;
        });
      } else {
        this.fetchData(this.currentPage, true);
      }
    }
    if (this.autoFreshWhenParamsChanged) {
      this.wacthParams();
    }
    this.$watch(
      "orders",
      () => {
        this.fetchData(1, true);
      },
      {
        deep: true
      }
    );
  },
  methods: {
    wacthParams: function() {
      if (!this.wacthParamsFlag) {
        var _self = this;
        this.$watch(
          "params",
          _.debounce(function(val, oldVal) {
            _self.fetchData(1, true);
          }, 500),
          {
            deep: true
          }
        );
        this.wacthParamsFlag = true;
      }
    },
    getReqParams() {
      var reqData = {};
      //封装查询参数
      _.each(this.params, (v, k) => {
        if (_.trim(v).length > 0) {
          //参数中不能出现英文的中括号，[]，否则无法转义导致请求错误
          let leftBracket = /[\[]/g;
          let rightBracket = /[\]]/g;
          reqData["qp_" + k] = _.trim(v)
            .replace(leftBracket, "")
            .replace(rightBracket, "");
        }
      });
      //封装排序字段
      _.each(this.orders, (val, key) => {
        if (key && val) {
          if (val === "ascending") {
            val = "asc";
          } else if (val === "descending") {
            val = "desc";
          }
          reqData["sort_" + key] = val;
        }
      });
      return reqData;
    },
    fetchData: function(currentPage, refresh, callback) {
      if (!this.url || this.url === "") {
        return;
      }
      let reqData = this.getReqParams();
      reqData["offset"] = (currentPage - 1) * this.pageSize;
      reqData["limit"] = this.pageSize;
      let loadingInstance = Loading.service({
        text: "获取数据中，请稍后"
      });
      axios
        .get(this.url, {
          params: reqData
        })
        .then(res => {
          var newCount = parseInt(res.total, 10);
          if (refresh) {
            this.init(newCount);
          }
          if (callback) {
            callback();
          }
          this.lastSavedPageSize = this.pageSize;
          this.lastSavedPage = this.currentPage;
          this.currentPage = currentPage;
          this.$emit("page-changed", res.records);
          loadingInstance.close();
        })
        .catch(() => {
          loadingInstance.close();
        });
    },
    init: function(totalCount) {
      if (!isNaN(totalCount) && totalCount >= 0) {
        this.totalCount = totalCount;
      }
    },
    getCurrentPage() {
      return this.currentPage;
    },
    getPageSize() {
      return this.pageSize;
    },
    handleSizeChange() {
      if (this.confirmOnDataChanged) {
        this.$confirm(
          "页面将要刷新，本页未保存的数据将会丢失，确认吗?",
          "操作提示",
          {
            type: "warning"
          }
        )
          .then(() => {
            this.fetchData(this.currentPage);
          })
          .catch(() => {
            this.pageSize = this.lastSavedPageSize;
          });
      } else {
        this.fetchData(this.currentPage);
      }
    },
    handleCurrentChange(pageIndex) {
      if (this.confirmOnDataChanged) {
        this.$confirm(
          "页面将要刷新，本页未保存的数据将会丢失，确认吗?",
          "操作提示",
          {
            type: "warning"
          }
        )
          .then(() => {
            this.fetchData(this.currentPage);
          })
          .catch(() => {
            this.currentPage = this.lastSavedPage;
          });
      } else {
        this.fetchData(this.currentPage);
      }
    }
  },
  watch: {
    url: function(val, oldVal) {
      this.fetchData(1, true);
    }
  }
};
</script>
<style>
</style>
