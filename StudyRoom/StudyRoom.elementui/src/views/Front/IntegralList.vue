<template>
    <div class="app-container">
        <el-page-header class="card margin-top-lg" @back="goBack" :content="'我的总积分 ' + MyIntegralData.TotalIntegral">
        </el-page-header>


        <el-card class="box-card margin-top-lg">
            <div slot="header" class="clearfix">
                <el-button type="primary" size="mini" icon="el-icon-search" @click="SearchClick">搜 索</el-button>
                <el-button type="warning" size="mini" @click="ResetClick" icon="el-icon-s-promotion">重 置</el-button>
            </div>
            <div class="tb-body">
                <el-form ref="searchFormRef" :model="searchForm" :inline="true" label-width="120px" :style="`flex:1;`">
                    <el-form-item label="标题" prop="TitleLike">
                        <el-input v-model.trim="searchForm.TitleLike" placeholder="请输入标题" :clearable="true"></el-input>
                    </el-form-item>
                    <el-form-item label="来源" prop="SourceLike">
                        <el-input v-model.trim="searchForm.SourceLike" placeholder="请输入来源" :clearable="true"></el-input>
                    </el-form-item>
                    <el-form-item label="关联号" prop="RelativeCodeLike">
                        <el-input v-model.trim="searchForm.RelativeCodeLike" placeholder="请输入关联号"
                            :clearable="true"></el-input>
                    </el-form-item>

                </el-form>
            </div>

        </el-card>



        <PaginationTable ref="PaginationTableId" url="/Integral/List" :column="dataColum" :where="where">
            <template v-slot:header>
                <el-button type="primary" size="mini" plain icon="el-icon-edit"
                    @click="OverdueTimesClear()">积分兑换逾期次数</el-button>
            </template>

        </PaginationTable>
    </div>
</template>

<script>
import store from '@/store';
import { mapGetters } from 'vuex'
export default {
    name: "IntegralList",
    computed: {
        ...mapGetters([
            'Token', 'UserInfo', 'RoleType', 'HasUserInfo', 'ColumnType', "UserId"
        ])
    },
    data() {

        return {
            where: {
                UserId: store.getters.UserId
            },
            searchForm: {},//搜索定义的数据对象
            dataColum: [
                {
                    key: "Id",
                    hidden: true,

                },
                {
                    key: "Title",
                    title: "标题",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "UserId",
                    hidden: true,
                },
                {
                    key: "UserDto.Name",
                    title: "用户名称",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "IntegralValue",
                    title: "积分值",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "Source",
                    title: "来源",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "RelativeCode",
                    title: "关联号",

                    type: store.getters.ColumnType.SHORTTEXT,
                },

            ],

            formData: {

            },//保存或者修改定义的数据对象
            editorShow: false,
            MyIntegralData: {
                TotalIntegral: 0
            }

        };
    },
    created() {
        this.GetMyIntegralData();

    },
    methods: {
        //返回上一个页面
        goBack() {
            this.$router.go(-1)
        },
        /**
         * 点击表格搜索按钮会触发
         */
        async SearchClick() {
            this.$refs.PaginationTableId.Reload(this.searchForm);
        },
        /**
         * 点击清空表单会触发
         */
        async ResetClick() {
            this.searchForm = {};
            this.$refs.PaginationTableId.Reload(this.searchForm);
        },
        //得到我的积分数据
        async GetMyIntegralData() {
            let { Data } = await this.$Post("/Integral/GetMyIntegralData", {})
            this.MyIntegralData = Data;
        },
        //清空逾期次数
        async OverdueTimesClear() {
            let confirm = await this.$comm.ConfirmMessageBox({ content: "你确定要用积分清空逾期次数吗?" })
            if (confirm) {
                let { Data } = await this.$Post("/Integral/OverdueTimesClear", {})
                this.GetMyIntegralData();
                this.$refs.PaginationTableId.Reload(this.searchForm);
            }
        }




    },
};
</script>