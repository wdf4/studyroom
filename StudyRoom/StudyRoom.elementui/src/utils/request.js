import store from "@/store";
import { getToken } from "@/utils/cache";
import axios from "axios";
import { Message } from "element-ui";

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // 请求的url地址
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 50000, //请求超时时间
});

// request interceptor
service.interceptors.request.use(
  (config) => {
    let token = getToken();
    if (token) {
      config.headers["Authorization"] = token;
    }
    return config;
  },
  (error) => {
    // do something with request error
    console.log(error); // for debug
    return Promise.reject(error);
  }
);

// response interceptor
service.interceptors.response.use(
  (response) => {
    const res = response.data;
    let { Success, Code, Msg } = res;

    if (Success == true) {
      return res;
    } else {
      if (Code == 500) {
        Message({
          message: Msg,
          type: "error",
          duration: 5 * 1000,
        });
        return res;
      } else if (Code == 401) {
        Message({
          message: "用户信息已过期",
          type: "error",
          duration: 5 * 1000,
        });
        store.dispatch("Logout").then(() => {
          location.reload();
        });
        return Promise.reject(new Error("用户信息已过期"));
      } else {
        return Promise.reject(new Error(Msg || "Error"));
      }
    }
  },
  (error) => {
    //如果网络错误就是后端没有启动
    if (error.message.indexOf("Network Error") != -1) {
      Message({
        message: "后端服务没有启动",
        type: "error",
        duration: 5 * 1000,
      });

      return Promise.reject(new Error(error || "Error"));
    }

    console.log("err" + error); // for debug
    Message({
      message: error.message,
      type: "error",
      duration: 5 * 1000,
    });
    return Promise.reject(error);
  }
);

export default service;
