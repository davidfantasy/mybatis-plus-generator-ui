<template>
  <div class="wrapper">
    <nav class="main-header navbar navbar-expand-md navbar-light navbar-white">
      <div class="container">
        <a href="javascript:;" class="navbar-brand">
          <img
            alt="Mybatis-Plus代码生成器"
            src="~/assets/img/mybatis-logo.png"
            class="brand-image img-circle elevation-3"
            style="opacity: .8"
          />
          <span class="brand-text font-weight-light">Mybatis-Plus代码生成器</span>
        </a>
        <div class="collapse navbar-collapse order-3" id="navbarCollapse">
          <!-- Left navbar links -->
          <ul class="navbar-nav">
            <li class="nav-item">
              <router-link to="/table" class="nav-link">
                <i class="fa fa-table"></i>
                数据表浏览
              </router-link>
            </li>
            <li class="nav-item">
              <router-link to="/config" class="nav-link">
                <i class="fa fa-cog"></i>
                输出配置
              </router-link>
            </li>
            <li class="nav-item">
              <a href="https://github.com/davidfantasy/mybatis-plus-generator-ui" class="nav-link">
                <i class="fa fa-github"></i>
                GitHub
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper" v-cloak>
      <nuxt-child :key="viewKey" />
    </div>
  </div>
</template>
<script>
import axios from "axios";

export default {
  props: [],
  beforeDestroy: function() {},
  computed: {
    viewKey() {
      return this.$route.name !== undefined
        ? this.$route.name + +new Date()
        : this.$route + +new Date();
    }
  },
  data() {
    return {};
  },
  mounted: function() {},
  methods: {}
};
</script>
<style>
</style>


