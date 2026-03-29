<template>
    <div>
        <el-upload v-show='false' :on-success='handleSuccess' ref="upload" :limit="8" :multiple="true"
            accept=".jpg,.png,.jpeg" :action="uploadUrl">
        </el-upload>
        <quill-editor v-model="content" ref="myQuillEditor" :options="editorOption" @blur="onEditorBlur($event)"
            @focus="onEditorFocus($event)" @change="onEditorChange($event)">
        </quill-editor>
    </div>
</template>

<script>


// 引入样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

import { quillEditor } from 'vue-quill-editor'

import Quill from 'quill'
import ImageResize from 'quill-image-resize-module'
Quill.register('modules/imageResize', ImageResize)

const toolbarOptions = [
    ['bold', 'italic', 'underline', 'strike'], //加粗，斜体，下划线，删除线
    ['blockquote', 'code-block'], //引用，代码块
    [{ 'header': 1 }, { 'header': 2 }], // 标题，键值对的形式；1、2表示字体大小
    [{ 'list': 'ordered' }, { 'list': 'bullet' }], //列表
    [{ 'script': 'sub' }, { 'script': 'super' }], // 上下标
    [{ 'indent': '-1' }, { 'indent': '+1' }], // 缩进
    [{ 'direction': 'rtl' }], // 文本方向
    [{ 'size': ['small', false, 'large', 'huge'] }], // 字体大小
    [{ 'header': [1, 2, 3, 4, 5, 6, false] }], //几级标题
    [{ 'color': [] }, { 'background': [] }], // 字体颜色，字体背景颜色
    [{ 'font': [] }], //字体
    [{ 'align': [] }], //对齐方式
    ['clean'], //清除字体样式
    ['image'] //上传图片、
];
export default {
    name: "QillRichText",
    props: {
        value: {
            type: String,
            default: ''
        },
    },
    components: {
        "quill-editor": quillEditor
    },

    data() {
        let that = this;
        return {
            content: ``, //双向数据绑定数据
            editorOption: {
                modules: {
                    toolbar: {
                        container: toolbarOptions, // 工具栏

                        handlers: {
                            'image': function (value) {
                                that.$refs["upload"].$refs["upload-inner"].handleClick()
                            }
                        }
                    },
                    imageResize: { //添加
                        displayStyles: { //添加
                            backgroundColor: 'black',
                            border: 'none',
                            color: 'white'
                        },
                        modules: ['Resize', 'DisplaySize', 'Toolbar'] //添加
                    },

                }, //
            }, //编辑器配置项
            uploadUrl: process.env.VUE_APP_BASE_API + "/File/BatchUpload",
            fileList: []
        };
    },
    created() {
        console.log("aa", this.value)
        this.content = this.value;

    },
    methods: {
        handleSuccess(res) {
            console.log(res);
            // 获取富文本组件实例
            let quill = this.$refs.myQuillEditor.quill;

            // 如果上传成功
            if (res.Success == true) {
                res.Data.forEach((item) => {
                    // 获取光标所在位置
                    let length = quill.getSelection().index;
                    // 插入图片，url为服务器返回的图片链接地址
                    quill.insertEmbed(length, 'image', item.Url);
                    // 调整光标到最后
                    quill.setSelection(length + 1);
                })

            } else {
                // 提示信息，需引入Message
                this.$message.error('图片插入失败');
            }
        },
        onEditorBlur() { }, // 失去焦点触发事件
        onEditorFocus() { }, // 获得焦点触发事件
        onEditorChange() {
            this.$emit('input', this.content);
        }, // 内容改变触发事件
    }
}
</script>

<style>
.ql-editor {
    height: 600px;
}
</style>