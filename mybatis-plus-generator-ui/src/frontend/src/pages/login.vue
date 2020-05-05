<template>
  <div class="login-box">
    <div class="login-img"></div>
    <div class="login-imgbox"></div>
    <div class="login-logo animated" v-bind:class="{'bounceInDown' : into, 'fadeOutUp': !into}">
      <a href>系统监控平台</a>
    </div>
    <div
      class="login-box-body animated"
      v-bind:class="{'bounceInDown' : into, 'fadeOutDown': !into}"
    >
      <p class="login-box-msg">请登录您的账户进行访问</p>
      <form>
        <div class="form-group has-feedback">
          <input
            autofocus
            type="text"
            v-model="form.account"
            class="form-control"
            placeholder="用户账户"
          />
          <span class="glyphicon glyphicon-user form-control-feedback"></span>
        </div>
        <div class="form-group has-feedback">
          <input
            @keyup.enter="login"
            v-model="form.password"
            type="password"
            class="form-control"
            placeholder="密码"
          />
          <span class="glyphicon glyphicon-lock form-control-feedback"></span>
        </div>
        <div class="form-group has-feedback" v-if="form.clientKey">
          <input
            @keyup.enter="login"
            v-model="form.captcha"
            class="form-control"
            placeholder="请输入图形验证码"
          />
          <span class="fa fa-get-pocket form-control-feedback"></span>
          <img :src="base64Captcha" style="margin-top:15px;" />
        </div>
        <div class="row">
          <div class="col-xs-8 el-check" style="padding-top:5px;"></div>
          <div class="col-xs-4">
            <button type="button" @click="login" class="btn btn-primary btn-block btn-flat">登录</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import Vue from "vue";
import moment from "moment";

export default {
  data() {
    return {
      into: true,
      timer: "",
      base64Captcha: "",
      form: {
        account: "",
        password: "",
        clientKey: "",
        captcha: ""
      }
    };
  },
  mounted: function() {
    this.into = true;
  },
  methods: {
    login() {
      axios.post("/api/authority/login", this.form).then(res => {
        if (res.ok) {
          this.into = false;
          this.timer = setTimeout(() => {
            this.$store.commit("login", res), this.$router.push("/");
          }, 1000);
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
          this.form.clientKey = res.clientKey;
          this.base64Captcha = res.base64Captcha;
        }
      });
    }
  }
};
</script>
<style>
.login-box {
  width: 450px;
}

.login-logo a {
  font-size: 40px;
  color: white;
}

.login-box {
  margin: 10% auto;
}

.login-box-msg {
  color: white;
}

.login-box-body {
  padding: 40px 20px;
  background: rgba(0, 0, 0, 0.5);
  box-shadow: 0px 0px 23px 10px rgba(255, 255, 255, 0.5);
}

.login-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  z-index: -10;
  background-size: cover;
}

.login-imgbox {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  z-index: -5;
  background: rgba(0, 0, 0, 0.66);
}

.el-check .el-checkbox__label {
  color: white;
}
</style>