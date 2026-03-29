import request from '@/utils/request'
import { ConfirmMessageBox } from "@/utils/comm"

//完成对axios的进一步封装

/**
 * 通用的post请求
 * @param {*} url  请求的地址
 * @param {*} data  请求的数据
 * @returns 
 */
export async function Post(url, data) {
    let response = {
        Code: "",
        Data: "",
        Msg: "",
        Success: true,
    };
    await request({
        url: url,
        method: "post",
        data: data || {},
    })
        .then((res) => {
            response = res;
        })
        .catch((ex) => {
            response.Success = false;
            response.Msg = ex;
        });
    return response;
}

/**
 * 带提示框的删除
 * @param {*} url  请求的地址
 * @param {*} data  请求的数据
 * @returns 
 */
export async function PostDelete(url, data) {

    let confirm = await ConfirmMessageBox({ content: "你确定要删除吗?" })
    if (confirm) {
        await Post(url, data);
    }
    return { Success: false, Msg: "用户取消操作" };
}
/**
 * 更新某个表的字段请求
 * @param {*} getUrl 单个查询后端地址
 * @param {*} saveUrl 保存或者修改后端地址
 * @param {*} id  需要修改的数据id
 * @param {*} content 修改的内容提示
 * @param {*} obj  修改的数据
 * @returns 
 */
export async function PostSigleUpdate(getUrl, saveUrl, id, content, obj) {

    let confirm = await ConfirmMessageBox({ content })
    if (confirm) {
        let { Data } = await Post(getUrl, { Id: id });
        Data = { ...Data, ...obj };
        await Post(saveUrl, Data);
    }

    return { Success: false, Msg: "用户取消操作" };
}
