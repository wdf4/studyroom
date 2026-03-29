import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@/css/index.css'
import '@/css/font/iconfont.css'

import store from "./store"

import App from './App.vue'
import router from './router'
import comm from "@/utils/comm.js"

//挂载comm请求到我们vue的原型链上
Vue.prototype.$comm = comm;

/**
 * 年月日时分秒格式过滤器
 */
Vue.filter('YMDHMSFormat', function (value) {
  return comm.YMDHMSFormat(value);
})
/**
 * 年月日格式过滤器
 */
Vue.filter('YMDFormat', function (value) {

  return comm.YMDFormat(value);
})


import { Post, PostDelete, PostSigleUpdate } from "@/api/http"
//挂载Post请求到我们vue的原型链上
Vue.prototype.$Post = Post;
//挂载PostDelete请求到我们vue的原型链上
Vue.prototype.$PostDelete = PostDelete;
//PostSigleUpdate
Vue.prototype.$PostSigleUpdate = PostSigleUpdate;


import UploadImages from "@/components/Upload/UploadImages.vue"
//图片上传组件
Vue.component("UploadImages", UploadImages)


import UploadFiles from "@/components/Upload/UploadFiles.vue"
//通用文件上传组件
Vue.component("UploadFiles", UploadFiles)

import SigleSelect from "@/components/Select/SigleSelect.vue"
//通用单选框
Vue.component("SigleSelect", SigleSelect)

import QillRichText from "@/components/RichText/QillRichText.vue"
//通用富文本
Vue.component("QillRichText", QillRichText)


import PaginationTable from "@/components/Tables/PaginationTable.vue"
//分页表格组件
Vue.component("PaginationTable", PaginationTable)

import ExportButton from "@/components/Buttons/ExportButton.vue"
//导出按钮组件
Vue.component("ExportButton", ExportButton)





Vue.use(ElementUI);
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
