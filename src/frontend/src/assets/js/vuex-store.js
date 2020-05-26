import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    pageStatus: {},
    authToken: null,
    user: {
      account: "",
      name: "",
      department: "",
      lastLoginTime: "",
      roles: [],
      perms: [],
      productLines: []
    }
  },
  mutations: {
    refreshToken(state, token) {
      state.authToken = token
    },
    login(state, user) {
      if (user.token) {
        //同时写入sessionStorage，用于在刷新页面时恢复数据
        sessionStorage.setItem('token', user.token)
        state.authToken = user.token;
      }
      state.user.account = user.account;
      state.user.name = user.name;
      state.user.perms = user.perms;
      state.user.roles = user.roles;
      state.user.department = user.department;
      state.user.productLines = user.productLines;
    },
    logout(state) {
      sessionStorage.setItem('token', "")
      state.authToken = "";
      state.user.name = "";
      state.user.lastLoginTime = "";
      state.user.perms = [];
      state.user.roles = [];
      state.user.productLines = [];
      state.user.account = "";
      state.user.department = null;
    },
    pushPageStatus(state, status) {
      state.pageStatus[status.fullPath] = status;
    },
    clearPageStatus(state) {
      state.pageStatus = {};
    }
  }
})


export {
  store
}