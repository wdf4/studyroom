<template>
    <span style="margin-left:10px;margin-right: 10px;">
        <el-button type="text" icon="el-icon-headset" @click="ShowImport()">音频资源</el-button>
        <el-dialog :title="fileName" :visible.sync="show" width="40%" :lock-scroll="true" height="800px">
            <audio style="width: 100%;" :src="url" controls autoplay muted></audio>
        </el-dialog>
    </span>
</template>

<script>

import {
    GetFileNameByPath
} from "@/utils/comm"

export default {

    props: {

        url: {
            type: String,//默认的文件路径
            default: "",
        },
    },
    watch: {

    },
    data() {
        return {
            show: false,
            files: [],
            fileName: ""

        }
    },
    created() {


    },
    methods: {
        ShowImport() {
            if (!this.url) {
                this.$message({
                    message: '请先完成资源上传!',
                    type: 'warning'
                });
                return;
            }
            this.fileName = GetFileNameByPath(this.url);
            this.show = true;
        },



    }
}

</script>
