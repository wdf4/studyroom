<template>
    <el-button size="mini" type="success" icon="el-icon-download" @click="ExportClick()">导 出</el-button>
</template>
<script>
export default {

    props: {
        exportUrl: {
            type: String, //导出地址
            default: "",
        },
        where: {//搜索属性
            type: Object,
            default: () => { }
        },
    },

    methods: {
        /**
         * 导出
         */
        async ExportClick() {
            
            var query = JSON.stringify(this.$props.where);
            window.open(process.env.VUE_APP_BASE_API + `${this.$props.exportUrl}?query=` + query, '_blank')

        },
    }
}

</script>