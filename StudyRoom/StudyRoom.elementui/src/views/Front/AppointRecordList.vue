<template>
    <div class="app-container margin-top-lg">
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <el-button type="primary" size="mini" icon="el-icon-search" @click="SearchClick">搜 索</el-button>
                <el-button type="warning" size="mini" @click="ResetClick" icon="el-icon-s-promotion">重 置</el-button>
            </div>
            <div class="tb-body">
                <el-form ref="searchFormRef" :model="searchForm" :inline="true" label-width="120px" :style="`flex:1;`">
                    <el-form-item label="流水编号" prop="NoLike">
                        <el-input v-model.trim="searchForm.NoLike" placeholder="请输入流水编号" :clearable="true"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号" prop="PhoneLike">
                        <el-input v-model.trim="searchForm.PhoneLike" placeholder="请输入手机号" :clearable="true"></el-input>
                    </el-form-item>
                    <el-form-item label="姓名" prop="NameLike">
                        <el-input v-model.trim="searchForm.NameLike" placeholder="请输入姓名" :clearable="true"></el-input>
                    </el-form-item>
                    <el-form-item label="自习室">
                        <SigleSelect url="/Room/List" columnName="Name" :clearable="true" columnValue="Id"
                            v-model="searchForm.RoomId">
                        </SigleSelect>
                    </el-form-item>
                    <el-form-item label="座位">
                        <SigleSelect url="/Seat/List" columnName="No" :clearable="true" columnValue="Id"
                            v-model="searchForm.SeatId">
                        </SigleSelect>
                    </el-form-item>

                    <el-form-item label="预约状态">
                        <SigleSelect url="/Select/AppointStatusEnum" columnName="Name" :clearable="true"
                            columnValue="Code" v-model="searchForm.AppointStatus">
                        </SigleSelect>
                    </el-form-item>
                    <el-form-item label="起始时间">
                        <el-date-picker v-model="searchForm.BeginTimeRange" type="datetimerange"
                            start-placeholder="开始起始时间" end-placeholder="结束起始时间" :default-time="['00:00:00']"
                            value-format="yyyy-MM-dd HH:mm:ss">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="截至时间">
                        <el-date-picker v-model="searchForm.EndTimeRange" type="datetimerange"
                            start-placeholder="开始截至时间" end-placeholder="结束截至时间" :default-time="['00:00:00']"
                            value-format="yyyy-MM-dd HH:mm:ss">
                        </el-date-picker>
                    </el-form-item>
                </el-form>
            </div>

        </el-card>


        <el-dialog title="评论评分" :visible.sync="editorShow" width="50%" :lock-scroll="true" height="800px">
            <el-form v-if="editorShow == true" ref="editModalForm" :rules="editModalFormRules" :model="formData"
                label-width="140px" size="mini">
                <el-row :gutter="10" class="EditFromBody">

                    <el-col :span="24">
                        <el-form-item label="评论" prop="Comment">
                            <el-input type="textarea" :rows="10" v-model="formData.Comment" placeholder="请输入评论"
                                :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="评分" prop="CommentScore">

                            <el-rate v-model="formData.CommentScore"></el-rate>
                        </el-form-item>
                    </el-col>
                </el-row>


                <el-row type="flex" justify="end" align="bottom">
                    <el-form-item>
                        <el-button type="primary" plain @click="CreateOrEditForm()">确 定</el-button>
                        <el-button @click="editorShow = false">取 消</el-button>
                    </el-form-item>
                </el-row>
            </el-form>

        </el-dialog>


        <PaginationTable ref="PaginationTableId" url="/AppointRecord/List" :column="dataColum" :where="where">
            <template v-slot:header>

            </template>
            <template v-slot:Operate="scope">
                <el-button type="primary" size="mini" v-if="scope.row.AppointStatus == 6"
                    @click="ShowEditModal(scope.row.Id)">评 论</el-button>
                <el-button type="primary" size="mini" v-if="scope.row.AppointStatus == 1"
                    @click="ArrivalClock(scope.row.Id)">到场打卡</el-button>
                <el-button type="warning" size="mini" v-if="scope.row.AppointStatus == 2"
                    @click="EarlyEnd(scope.row.Id)">提前结束</el-button>
                <el-button type="danger" size="mini" v-if="scope.row.AppointStatus == 1"
                    @click="CancelAppoint(scope.row.Id)">取消预约</el-button>
                <!-- <el-button type="danger" size="mini" @click="ShowDeleteModal(scope.row.Id)">删 除</el-button> -->
            </template>
        </PaginationTable>
    </div>
</template>

<script>
import store from '@/store';
import { mapGetters } from 'vuex'
export default {
    name: "AppointRecordList",
    computed: {
        ...mapGetters([
            'Token', 'UserInfo', 'RoleType', 'HasUserInfo', 'ColumnType', "UserId"
        ])
    },
    data() {

        return {
            where: {
                UserId: store.getters.UserId,
            },
            searchForm: {},//搜索定义的数据对象
            dataColum: [
                {
                    key: "Id",
                    hidden: true,

                },
                {
                    key: "RoomId",
                    hidden: true,
                },
                {
                    key: "No",
                    title: "流水编号",
                    width: "240px",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "RoomDto.Name",
                    title: "自习室名称",
                    width: "160px",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "SeatId",
                    hidden: true,
                },
                {
                    key: "SeatDto.No",
                    title: "座位编号",
                    width: "160px",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "UserId",
                    hidden: true,
                },
                {
                    key: "AppointDate",
                    title: "预约日期",
                    width: "160px",
                    type: store.getters.ColumnType.DATE,
                },
                {
                    key: "AppointDateTypeFormat",
                    title: "预约时间段",
                    width: "160px",
                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "UserDto.Name",
                    title: "用户名称",
                    width: "160px",

                    type: store.getters.ColumnType.SHORTTEXT,
                },

                {
                    key: "Phone",
                    title: "手机号",
                    width: "160px",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "Name",
                    title: "姓名",
                    width: "160px",

                    type: store.getters.ColumnType.SHORTTEXT,
                },


                {
                    key: "BeginTime",
                    title: "签到时间",
                    width: "240px",
                    type: store.getters.ColumnType.DATETIME,
                },
                {
                    key: "EndTime",
                    title: "离开时间",
                    width: "240px",
                    type: store.getters.ColumnType.DATETIME,
                },
                {
                    key: "CommentScore",
                    title: "评分",
                    width: "160px",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "Comment",
                    title: "评论",
                    width: "160px",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "AppointStatus",
                    hidden: true,
                }, {
                    key: "AppointStatusFormat",
                    title: "预约状态",
                    width: "160px",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    title: "操作",
                    width: "300px",
                    key: "Operate",
                    type: store.getters.ColumnType.USERDEFINED,
                },
            ],
            editModalFormRules: {
                "No": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "Name": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "RoomId": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "SeatId": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "UserId": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "AppointStatus": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "BeginTime": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "EndTime": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "Phone": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "Comment": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "CommentScore": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
            },
            formData: {

            },//保存或者修改定义的数据对象
            editorShow: false,

        };
    },
    created() {

    },
    methods: {
        //到场打卡
        async ArrivalClock(id) {
            let { Data } = await this.$PostSigleUpdate(`/AppointRecord/Get`, `/AppointRecord/ArrivalClock`, id, '你确定要打卡吗?', {});
            this.$refs.PaginationTableId.Reload(this.searchForm);
        },
        //取消预约
        async CancelAppoint(id) {
            let { Data } = await this.$PostSigleUpdate(`/AppointRecord/Get`, `/AppointRecord/CancelAppoint`, id, '你确定要取消预约吗?', {});
            this.$refs.PaginationTableId.Reload(this.searchForm);
        },
        //提前结束
        async EarlyEnd(id) {
            let { Data } = await this.$PostSigleUpdate(`/AppointRecord/Get`, `/AppointRecord/EarlyEnd`, id, '你确定要提前结束吗?', {});
            this.$refs.PaginationTableId.Reload(this.searchForm);
        },



        //修改属性
        async UpdateEntityAsync(Id, title, data) {
            let { Data } = await this.$PostSigleUpdate(`/AppointRecord/Get`, `/AppointRecord/CreateOrEdit`, Id, title, data);

            this.$refs.PaginationTableId.Reload(this.searchForm);
        },
        /**
         * 点击评论
         */
        async ShowEditModal(Id) {

            let { Data } = await this.$Post(`/AppointRecord/Get`, { Id: Id });
            this.formData = Data;

            this.editorShow = true;

        },
        /**
         * 评论保存
         */
        async CreateOrEditForm() {

            this.$refs.editModalForm.validate(async valid => {
                if (valid) {
                    var { Success } = await this.$Post(`/AppointRecord/Comment`, this.formData);

                    if (Success) {
                        this.editorShow = false;
                        this.$refs.PaginationTableId.Reload(this.searchForm);
                    }
                }
            })
        },
        /**
         * 单个删除的时候会触发
         */
        async ShowDeleteModal(Id) {
            await this.$PostDelete(`/AppointRecord/Delete`, { Id: Id });
            this.$refs.PaginationTableId.Reload(this.searchForm);
        },
        /**
         * 批量删除的时候会触发
         */
        async ShowBatchDeleteModal() {
            var ids = this.$refs.PaginationTableId.GetSelectionRow().map(x => x.Id);
            if (ids.length == 0) {
                this.$message.error("你选择需要删除的记录");
                return;
            }
            await this.$PostDelete(`/AppointRecord/BatchDelete`, { Ids: ids });

            this.$refs.PaginationTableId.Reload(this.searchForm);
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




    },
};
</script>