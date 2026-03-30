import { Message, MessageBox } from "element-ui"; //消息提示框
import store from "@/store";
import router from "@/router";

/**
 * 公共确认提示框
 * @param {*} config
 * @returns
 */
export function ConfirmMessageBox(config) {
  let _config = {
    title: config.title || "提示",
    content: config.content || "",
  };
  return new Promise(function (resolve) {
    MessageBox.confirm(_config.content, _config.title, {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(() => {
        resolve(true);
      })
      .catch(() => {
        resolve(false);
      });
  });
}

/**
 * 获取路径中的文件名称
 */
export function GetFileNameByPath(path) {
  if (!path) return "";
  let parts = path.split("/");
  return parts[parts.length - 1];
}
/**
 * 获取路径中的文件格式
 */
export function GetFileTypeByPath(path) {
  if (!path) return "";
  let parts = path.split(".");
  return parts[parts.length - 1];
}
/**
 * 切割字段返回一个集合
 * @param {*} value 需要切割字段
 * @param {*} cutting 切割的符号
 */
export function ConvertArray(value = "", cutting = ",") {
  if (!value || value.length === 0) return [];
  return value.split(cutting).filter(x => x.length > 0);
}
/**
 * 根据路径获取文件的详细信息
 */
export function FullConvertUrlArray(value = "", cutting = ",") {
  var arr = ConvertArray(value, cutting);

  arr = arr.map((x) => {
    return {
      url: ReplaceImageHttp(x),
      name: GetFileNameByPath(x),
      type: GetFileTypeByPath(x),
    };
  });
  return arr;
}
/**
 * 处理图片路径
 * @param {*} value
 * @returns
 */
export function ReplaceImageHttp(value) {
  if (!value) return value;
  if (Array.isArray(value)) {
    return value.map(x => ReplaceImageHttp(x));
  }
  // 如果已经是完整URL则直接返回
  if (value.startsWith("http://") || value.startsWith("https://")) {
    return value;
  }
  return process.env.VUE_APP_BASE_API + value;
}

/**
 * 补零
 */
function padZero(num) {
  return num < 10 ? '0' + num : '' + num;
}

/**
 * 得到完整的时间格式 年-月-日 时分秒
 */
export function YMDHMSFormat(val) {
  if (!val) return "";
  let date = new Date(val);
  if (isNaN(date.getTime())) return val;
  return date.getFullYear() + '-' + padZero(date.getMonth() + 1) + '-' + padZero(date.getDate()) + ' ' + padZero(date.getHours()) + ':' + padZero(date.getMinutes()) + ':' + padZero(date.getSeconds());
}
/**
 * 得到完整的时间格式 年-月-日
 */
export function YMDFormat(val) {
  if (!val) return "";
  let date = new Date(val);
  if (isNaN(date.getTime())) return val;
  return date.getFullYear() + '-' + padZero(date.getMonth() + 1) + '-' + padZero(date.getDate());
}

/**
 * 得到时分秒格式
 */
export function HMSFormat(val) {
  if (!val) return "";
  let date = new Date(val);
  if (isNaN(date.getTime())) return val;
  return padZero(date.getHours()) + ':' + padZero(date.getMinutes()) + ':' + padZero(date.getSeconds());
}
/**
 * 获取相对时间描述
 * @param {Date|string|number} date 要比较的时间
 * @returns {string} 返回相对时间描述
 */
export function GetRelativeTimeDesc(date) {
  if (!date) return "";
  let now = new Date();
  let target = new Date(date);
  let diff = now - target;
  let seconds = Math.floor(diff / 1000);
  if (seconds < 60) return "刚刚";
  let minutes = Math.floor(seconds / 60);
  if (minutes < 60) return minutes + "分钟前";
  let hours = Math.floor(minutes / 60);
  if (hours < 24) return hours + "小时前";
  let days = Math.floor(hours / 24);
  if (days < 30) return days + "天前";
  return YMDFormat(date);
}

/**
 * 检查是否登录状态
 * @returns
 */
export function CheckIsLogin() {
  if (!store.getters.Token) {
    setTimeout(() => {
      router.push("/login");
    }, 500);
    Message({
      showClose: true,
      message: "请先登录后,再操作",
      type: "error",
    });
    return false;
  }
  return true;
}

export default {
  CheckIsLogin,
  ConfirmMessageBox,
  ConvertArray,
  FullConvertUrlArray,
  GetFileNameByPath,
  GetFileTypeByPath,
  ReplaceImageHttp,
  YMDHMSFormat,
  YMDFormat,
  HMSFormat,
  GetRelativeTimeDesc,
};
