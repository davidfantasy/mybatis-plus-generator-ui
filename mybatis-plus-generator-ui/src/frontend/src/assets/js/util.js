import { MessageBox } from "element-ui";

const alertMsg = (title, message, type, callback) => {
  MessageBox.alert(message, title, {
    title: title,
    type: type,
    center: true,
    confirmButtonText: "确定",
    showClose: false
  }).then(action => {
    if (callback) {
      callback();
    }
  });
};

export default {
  contextPath: () => {
    return process.env.contextPath;
  },
  uuid: () => {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
      s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";
    var uuid = s.join("");
    return uuid;
  },
  getQueryString: name => {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r !== null) {
      return unescape(r[2]);
    }
    return null;
  },
  alertMsg: alertMsg,
  wrapReqParams: params => {
    var reqData = {};
    _.each(params, (v, k) => {
      if (_.trim(v).length > 0) {
        //参数中不能出现英文的中括号，[]，否则无法转义导致请求错误
        let leftBracket = /[\[]/g;
        let rightBracket = /[\]]/g;
        reqData["qp_" + k] = _.trim(v)
          .replace(leftBracket, "")
          .replace(rightBracket, "");
      }
    });
    return reqData;
  }
};
