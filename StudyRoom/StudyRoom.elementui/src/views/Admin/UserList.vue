<template>
    <div>

        <el-card class="box-card">
            <div slot="header" class="clearfix">

                <el-row>
                    <el-button type="primary" size="mini" @click="SearchClick">查询</el-button>
                    <el-button type="warning" size="mini" @click="ResetClick">清空条件</el-button>

                </el-row>
            </div>
            <div>
                <el-form :inline="true" :model="searchForm" class="demo-form-inline">
                    <el-form-item label="姓名">
                        <el-input v-model="searchForm.Name" placeholder="请输入姓名"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱">
                        <el-input v-model="searchForm.Eamil" placeholder="请输入邮箱"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号码">
                        <el-input v-model="searchForm.PhoneNumber" placeholder="请输入手机号码"></el-input>
                    </el-form-item>


                </el-form>
            </div>
        </el-card>


        <PaginationTable ref="PaginationTableId" url="/User/List" :column="dataColum">
            <template v-slot:header>
                <el-button type="primary" size="mini" icon="el-icon-edit" @click="ShowEditModal()">新 增</el-button>
                <ExportButton exportUrl="/User/Export" :where="searchForm"></ExportButton>

            </template>
            <template v-slot:Operate="scope">
                <el-button type="primary" size="mini" @click="ShowEditModal(scope.row.Id)">修 改</el-button>
                <el-button type="danger" size="mini" @click="ShowDeleteModal(scope.row.Id)">删 除</el-button>

            </template>
        </PaginationTable>


        <el-dialog :title="formData.Id ? '修改用户' : '添加用户'" :visible.sync="editorShow" width="80%" :lock-scroll="true"
            min-height="800px">
            <el-form v-if="editorShow == true" ref="editModalForm" :rules="editModalFormRules" :model="formData"
                label-width="140px" size="mini">

                <el-row :gutter="10">
                    <el-col :span="12">
                        <el-form-item label="账号" prop="UserName" placeholder="请输入账号">
                            <el-input v-model.trim="formData.UserName" :disabled='formData.Id != null'></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="密码" prop="Password">
                            <el-input type="password" v-model.trim="formData.Password"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="角色" prop="RoleType">
                            <SigleSelect v-model="formData.RoleType" url="/Select/RoleType" columnName="Name"
                                columnValue="Code" columnLabel="Label"></SigleSelect>
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item label="邮箱" prop="Email" placeholder="请输入邮箱">
                            <el-input v-model.trim="formData.Email"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="联系方式" prop="PhoneNumber" placeholder="请输入联系方式">
                            <el-input v-model.trim="formData.PhoneNumber"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="出生时间" prop="Birth" placeholder="请输入出生时间">
                            <el-date-picker v-model="formData.Birth" align="right" type="date" placeholder="选择出生时间"
                                value-format="yyyy-MM-dd HH:mm:ss">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item label="姓名" prop="Name" placeholder="请输入姓名">
                            <el-input v-model.trim="formData.Name"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="图片" prop="ImageUrls">
                            <UploadImages :limit="1" v-model="formData.ImageUrls"></UploadImages>
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



    </div>
</template>


<script>
import store from '@/store';
export default {
    name: "UserList",
    data() {
        return {
            searchForm: {},
            editorShow: false,
            dataColum: [
                {
                    key: "Id",
                    hidden: true,

                },
                {
                    key: "UserName",
                    title: "账号",
                    type: store.getters.ColumnType.SHORTTEXT,

                },

                {
                    key: "Password",
                    title: "密码",
                    type: store.getters.ColumnType.SHORTTEXT,

                },
                {
                    key: "Email",
                    title: "邮箱",
                    type: store.getters.ColumnType.SHORTTEXT,

                },
                {
                    key: "ImageUrls",
                    title: "头像",
                    type: store.getters.ColumnType.IMAGES,

                },

                {
                    key: "PhoneNumber",
                    title: "手机号",
                    type: store.getters.ColumnType.PHONE,

                },
                {
                    key: "OverdueTimes",
                    title: "逾期次数",
                    type: store.getters.ColumnType.SHORTTEXT,

                },

                {
                    key: "Birth",
                    title: "出生年月",
                    type: store.getters.ColumnType.DATE,

                },
                {
                    key: "RoleTypeFormat",
                    title: "所属角色",
                    type: store.getters.ColumnType.SHORTTEXT,

                },
                {
                    title: "操作",
                    width: "300px",
                    key: "Operate",
                    type: store.getters.ColumnType.USERDEFINED,
                },
            ],
            formData: {
            },
            editModalFormRules: {
                "UserName": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "Password": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "Email": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            var reg = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
                            if (!value || !reg.test(value)) {
                                callback(new Error('请输入正确的邮箱'));
                            }
                            else {
                                callback();
                            }
                        }, trigger: 'blur'
                    },
                ],
                "ImageUrls": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "Name": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],

                "PhoneNumber": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            var reg = /^1[34578]\d{9}$/;
                            if (!value || !reg.test(value)) {
                                callback(new Error('请输入正确的手机号'));
                            }
                            else {
                                callback();
                            }
                        }, trigger: 'blur'
                    },
                ],
                "Birth": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "RoleType": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],

            },

            listLoading: false,
        }
    },
    created() {

    },
    methods: {
        /**
        * 点击新增或者编辑的时候会触发
        */
        async ShowEditModal(Id) {

            let { Data } = await this.$Post(`/User/Get`, { Id: Id });
            this.formData = Data;
            this.editorShow = true;

        },

        /**
       * 点击保存的时候会触发
       */
        async CreateOrEditForm() {

            this.$refs.editModalForm.validate(async valid => {
                if (valid) {
                    var { Success } = await this.$Post(`/User/CreateOrEdit`, this.formData);
                    if (Success) {
                        this.editorShow = false;
                        this.$refs.PaginationTableId.Reload(this.searchForm);
                    }
                }
            })
        },

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
        /**
       * 单个删除的时候会触发
       */
        async ShowDeleteModal(Id) {
            await this.$PostDelete(`/User/Delete`, { Id: Id });
            this.$refs.PaginationTableId.Reload(this.searchForm);
        },
    }
}
</script>

<style scoped></style>