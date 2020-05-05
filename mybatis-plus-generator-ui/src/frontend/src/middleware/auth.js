/**
 * 默认的权限过滤器，只有具备必要的权限才能访问对应页面
 */
import anonPaths from "@/assets/config/anonymous";
import axios from "axios";

export default function ({
  route,
  store,
  redirect
}) {
  let isAnon = false;
  anonPaths.forEach(anon => {
    if (anon == route.fullPath) {
      isAnon = true;
    } else if (_.endsWith(anon, '**')) {
      anon = anon.substring(0, anon.length - 3)
      if (_.startsWith(route.fullPath, anon)) {
        isAnon = true;
      }
    }
  });
  if (isAnon || store.state.authToken) {
    return;
  } else if (sessionStorage.getItem("token") && !store.state.authToken) {
    //尝试通过token重新进行登录
    let token = sessionStorage.getItem("token");
    store.commit("refreshToken", token);
    return axios.get("/api/authority/userinfo").then((res) => {
      store.commit('login', res)
    }).catch((e) => {
      redirect('/login')
    })
  } else {
    return redirect('/login')
  }
}