<template>
    <span style="margin-left:10px;margin-right: 10px;">
        <el-button type="text" icon="el-icon-video-play" @click="ShowImport()">播 放 视 频</el-button>
        <el-dialog :title="fileName" :visible.sync="show" width="50%" :lock-scroll="true" height="800px">

            <video controls muted width="100%" v-if="show">
                <source :src="url" type="video/webm" />
            </video>
        </el-dialog>
    </span>
</template>

<script>

import {
    GetFileNameByPath
} from "@/utils/comm"
import store from "@/store";
export default {

    props: {

        url: {
            type: String,//默认的请求路径
            default: "",
        },
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
