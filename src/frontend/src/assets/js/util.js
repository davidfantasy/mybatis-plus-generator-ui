import {
  MessageBox
} from "element-ui";


export default {
  contextPath: () => {
    return process.env.contextPath;
  },
  getQueryString: name => {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r !== null) {
      return unescape(r[2]);
    }
    return null;
  }
};