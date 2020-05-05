<template>
  <div style="position: relative;">
    <header class="main-header fixed-header">
      <!-- Logo -->
      <a href="javascript:;" class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini">
          <b>monitor</b>
        </span>
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg" style="font-size: 22px;">系统监控平台</span>
      </a>
      <nav class="navbar navbar-static-top">
        <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
          <span class="sr-only">菜单缩放</span>
        </a>
        <div class="navbar-custom-menu">
          <ul class="nav navbar-nav">
            <li class="dropdown user user-menu">
              <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown">
                <img src="~/assets/img/user_info.jpg" class="user-image" />
                <span class="hidden-xs">欢迎，{{loginUser.name}}</span>
              </a>
              <ul class="dropdown-menu">
                <li class="user-header">
                  <img src="~/assets/img/user_info.jpg" class="img-circle" alt="User Image" />
                  <p>
                    {{loginUser.name}}
                    <small>{{loginUser.lastLoginTime}}</small>
                  </p>
                </li>
                <!-- Menu Footer-->
                <li class="user-footer">
                  <div class="pull-left">
                    <a href="javascript:;" @click="logout" class="btn btn-default btn-flat">安全退出</a>
                  </div>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </nav>
    </header>

    <aside class="main-sidebar">
      <!-- sidebar: style can be found in sidebar.less -->
      <section class="sidebar">
        <form class="sidebar-form">
          <div class="input-group">
            <input type="text" v-model="menuKeyWord" class="form-control" placeholder="搜索菜单..." />
            <span class="input-group-btn">
              <button style="margin:0px;" type="button" class="btn btn-flat">
                <i class="fa fa-search"></i>
              </button>
            </span>
          </div>
        </form>
        <!--系统菜单组件-->
        <index-menu :keyWord="menuKeyWord" />
      </section>
      <!-- /.sidebar -->
    </aside>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper" v-cloak>
      <nuxt-child :key="viewKey" />
    </div>
    <div class="control-sidebar-bg"></div>
  </div>
</template>
<script>
import axios from "axios";
import userMixin from "@/assets/js/user-mixin";

export default {
  props: [],
  mixins: [userMixin],
  beforeDestroy: function() {},
  computed: {
    viewKey() {
      return this.$route.name !== undefined
        ? this.$route.name + +new Date()
        : this.$route + +new Date();
    }
  },
  data() {
    return {
      modalshow: false,
      menuKeyWord: ""
    };
  },
  mounted: function() {
    //adminlte修正
    $(window).trigger("resize");
    $(window).trigger("load");
  },
  methods: {
    logout() {
      this.$store.commit("logout");
      this.$router.push("/login");
    }
  }
};
</script>
<style>
i.iconfont {
  margin-right: 5px;
}
.sidebar-menu li.header {
  font-size: 16px;
}
.skin-blue .sidebar-menu > li.header {
  color: #909399;
  background: #1a2226;
}
/*.btn {
  margin: 0 0 10px 10px;
}*/
.content-wrapper {
  background-color: #fff;
  overflow: auto;
}
</style>


