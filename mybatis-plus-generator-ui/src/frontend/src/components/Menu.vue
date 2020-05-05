<template>
  <ul class="sidebar-menu resmenu" data-widget="tree">
    <!-- <li class="header">系统菜单</li>-->
    <li class="treeview" v-for="(m,index) in moduleData" :key="index">
      <a href="javascript:;">
        <i :class="m.iconClass"></i>
        <span>{{m.title}}</span>
        <span class="pull-right-container">
          <i class="fa fa-angle-left pull-right"></i>
        </span>
      </a>
      <ul class="treeview-menu">
        <li
          v-for="(subMenu,subMenuIndex) in m.menus"
          :key="subMenuIndex"
          :class="{ clicked:clickedMenu ===subMenu.title}"
        >
          <a href="javascript:;" v-if="subMenu.viewName" @click="onMenuClick(subMenu)">
            <i class="fa fa-circle-o"></i>
            {{subMenu.title}}
          </a>
          <a v-if="subMenu.viewPath" href="javascript:;" @click="onMenuClick(subMenu)">
            <i class="fa fa-circle-o"></i>
            {{subMenu.title}}
          </a>
        </li>
      </ul>
    </li>
  </ul>
</template>
<script>
import axios from "axios";
import _ from "lodash";
import modules from "@/assets/config/menu";
import userMixin from "@/assets/js/user-mixin";

export default {
  props: ["keyWord"],
  mixins: [userMixin],
  data: function() {
    return {
      clickedMenu: "",
      moduleData: [],
      userOwnedModules: []
    };
  },
  mounted: function() {
    let userModules = this.cloneModules(modules);
    userModules.forEach(m => {
      m.menus = m.menus.filter(menu => {
        return !menu.hidden;
      });
    });
    this.userOwnedModules = userModules.filter(m => {
      return !m.hidden && m.menus.length > 0;
    });
    this.moduleData = this.cloneModules(this.userOwnedModules);
  },
  watch: {
    keyWord: function(val) {
      this.filterMenu(this);
    }
  },
  methods: {
    addClass: function(index) {
      this.current = index;
    },
    onMenuClick(menu) {
      if (menu.viewName) {
        this.$router.push({
          name: menu.viewName,
          params: menu.params
        });
      } else if (menu.viewPath) {
        this.$router.push({
          path: menu.viewPath,
          query: menu.query
        });
      }
      this.clickedMenu = menu.title;
      this.$store.commit("clearPageStatus");
    },
    cloneModules(target) {
      let copyModules = [];
      target.forEach(m => {
        let copy = _.cloneDeep(m);
        copyModules.push(copy);
      });
      return copyModules;
    },
    filterMenu: _.debounce(self => {
      if (!self.keyWord) {
        self.moduleData = self.cloneModules(self.userOwnedModules);
      } else {
        self.moduleData.forEach(m => {
          m.menus = m.menus.filter(menu => {
            return menu.title.indexOf(self.keyWord) !== -1;
          });
        });
        self.moduleData = self.moduleData.filter(m => {
          return m.menus.length > 0;
        });
        if (self.moduleData.length > 0) {
          self.$nextTick(() => {
            $(window).trigger("resize");
            $(window).trigger("load");
            $(".treeview-menu").css("display", "block");
            $(".treeview").addClass("menu-open");
          });
        }
      }
    }, 500)
  }
};
</script>
<style>
.clicked a {
  color: orange !important;
}
.resmenu li a:hover {
  color: orange !important;
}
</style>