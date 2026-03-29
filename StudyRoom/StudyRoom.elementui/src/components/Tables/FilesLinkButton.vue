<template>
    <span style="margin-left:10px;margin-right: 10px;">
        <el-button type="text" icon="el-icon-share" @click="ShowImport()">文件查看</el-button>
        <el-dialog title="文件查看" :visible.sync="show" width="70%" :lock-scroll="true" height="800px">
            <el-table :data="files" style="width: 100%">
                <el-table-column prop="name" label="文件名称" align="center">

                    <template slot-scope="scope">
                        <el-tag>{{ scope.row.name }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="type" label="文件类型" align="center">
                    <template slot-scope="scope">
                        <el-tag type="success">{{ scope.row.type }}</el-tag>
                    </template>
                </el-table-column>


                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-download" @click="Down(scope.$index)">下载</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>
    </span>
</template>

<script>

import {
    FullConvertUrlArray
} from "@/utils/comm"
import store from "@/store";
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
            files: []

        }
    },
    created() {
        let files = FullConvertUrlArray(this.url);
        this.files = files;


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
            this.show = true;
        },
        Down(index) {
            window.open(this.files[index].url, '_blank')

        }


    }
}

</script>