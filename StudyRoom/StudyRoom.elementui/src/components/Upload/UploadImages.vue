<template>
    <div class="upload-files-wrap">
        <el-upload :action="uploadUrl" list-type="picture-card" :show-file-list="true" :on-success="handleUploadSuccess"
            :on-remove="handleRemove" :file-list="fileList" accept=".jpg,.png,.jpeg" :limit="limit" :multiple="true">
            <i class="el-icon-plus"></i>

        </el-upload>
    </div>
</template>

<script>
import { ReplaceImageHttp, GetFileNameByPath } from "@/utils/comm.js";
import store from "@/store";
export default {
    name: "UploadImages",
    props: {
        value: { type: [Number, String], default: '' },
        limit: {
            type: Number,
            default: 1,
        },
    },
    data() {
        return {
            uploadUrl: process.env.VUE_APP_BASE_API + "/File/BatchUpload",
            dialogImageUrl: "",
            fileList: []

        };
    },

    mounted() {
        //第一次进来绑定对应的值
        if (this.$props.value) {
            this.fileList = this.$props.value.split(",").map(x => { return { url: ReplaceImageHttp(x), name: GetFileNameByPath(ReplaceImageHttp(x)), status: "success" }; });
        }
    },

    methods: {
        /**
         * 得到成功的文件集合对象
         * @param files 
         */
        FileListConvert(files) {
            let list = [];
            if (Array.isArray(files)) {
                files.filter(x => x.status == "success").forEach((item) => {
                    if (item.response != null) {
                        list = [...list, { name: "", url: ReplaceImageHttp(item.response.Data[0].Url) }];
                    }
                    else {
                        list = [...list, item];
                    }
                })
            }
            return list;
        },
        /**
         *  文件上传成功处理
         * @param response 响应
         * @param file  当前文件
         * @param fileList  所有文件
         */
        async handleUploadSuccess(response, file, fileList) {
            let fs = this.FileListConvert(fileList);
            let url = fs.length > 0 ? fs.map(x => x.url).join(",") : "";
            this.$emit('input', url);
        },

        /**
         *  文件移除处理
         * @param file  当前文件
         * @param fileList  所有文件
         */
        async handleRemove(file, fileList) {
            let fs = this.FileListConvert(fileList);
            let url = fs.length > 0 ? fs.map(x => x.url).join(",") : "";
            this.$emit('input', url);
        },
    },
};
</script>

<style scoped>
.uploadImage {
    background-color: transparent;
}

.el-upload--picture-card {
    background-color: transparent !important;
}
</style>