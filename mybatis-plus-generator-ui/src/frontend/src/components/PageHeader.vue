<template>
  <section class="crumbs-menu">
    <div class="bread-box crumbs-box">
      <ol class="breadcrumb flex-y">
        <div @click="returnBack()" class="goback el-page-header__left" v-if="needBack">
          <i class="el-icon-back" style="line-height: 20px;"></i>
          <a class="crumbs-text">返回</a>
        </div>
        <li v-for="(item,index) in nav" :key="index">{{item}}</li>
      </ol>
      <div class="flex-y" style="padding-left:100px;">
        <slot></slot>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      required: true
    },
    needBack: {
      type: Boolean,
      default: true,
      required: false
    }
  },
  data() {
    return { nav: [] };
  },
  watch: {
    title: function() {
      this.nav = this.title.split(",");
    }
  },
  mounted: function() {
    this.nav = this.title.split(",");
  },
  methods: {
    returnBack() {
      window.history.back();
    }
  }
};
</script>

<style>
.goback {
  display: block;
}

.goback img {
  width: 30px;
  height: 30px;
}

.crumbs-text {
  font-size: 14px;
  font-weight: 500;
  margin: 0;
  padding-left: 10px;
  color: #333333;
}

.crumbs-box {
  display: flex;
}
.el-page-header__left {
  display: flex;
  cursor: pointer;
  margin-right: 40px;
  position: relative;
}
.el-page-header__left:after {
  content: "";
  position: absolute;
  width: 1px;
  height: 16px;
  right: -20px;
  top: 50%;
  transform: translateY(-50%);
  background-color: #dcdfe6;
}
</style>