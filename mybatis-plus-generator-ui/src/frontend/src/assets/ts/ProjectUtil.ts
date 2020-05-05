import { MessageBox } from "element-ui";
import _ from "lodash";
export default class {
  /**
   * 对请求的查询参数进行封装，便于后端识别和组装
   * @param params
   */
  public static wrapReqParams(params: Object): Object {
    let reqData: any = {};
    for (let key in params) {
      let val = reqData[key];
      if (_.trim(val).length > 0) {
        //参数中不能出现英文的中括号，[]，否则无法转义导致请求错误
        let leftBracket = /[\[]/g;
        let rightBracket = /[\]]/g;
        reqData["qp_" + key] = _.trim(val)
          .replace(leftBracket, "")
          .replace(rightBracket, "");
      }
    }
    return reqData;
  }
}
