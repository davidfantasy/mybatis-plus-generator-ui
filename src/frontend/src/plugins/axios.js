import axios from 'axios'
import Vue from 'vue'
import _ from 'lodash'
import {
  store
} from '@/assets/js/vuex-store'
import {
  Message
} from "element-ui"

export default ({
  app
}) => {

  axios.defaults.baseURL = process.env.contextPath;

  axios.interceptors.request.use(function (config) {
    if (config.method === 'get') {
      config.url = encodeURI(config.url);
    }
    //添加权限token
    if (store.state.authToken) {
      config.headers["jwt-token"] = store.state.authToken;
    }
    return config;
  }, function (error) {
    Message.error("请求不可用");
    return Promise.reject(error);
  });

  axios.interceptors.response.use(function (res) {
    if (res.headers["jwt-token"]) {
      //刷新即将过期的token
      store.commit('refreshToken', res.headers["jwt-token"]);
    }
    var result = res.data;
    if (!result.code) {
      return result;
    }
    switch (result.code) {
      case (200):
        return result.data;
      case (401):
        Message.error("您还没有登录或登录信息已过期");
        return Promise.reject(result.code);
      case (403):
        Message.error("您没有权限访问该功能");
        return Promise.reject(result.code);
      default:
        Vue.prototype.$loading().close();
        Message.error(result.message);
        return Promise.reject(result.code);
    }
  }, function (error) {
    Message.error("无法访问服务器，请检查网络");
    return Promise.reject(error);
  });
}