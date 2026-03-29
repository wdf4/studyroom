<template>
    <div class="app-container">
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <el-button type="primary" size="mini" icon="el-icon-search" @click="SearchClick">搜 索</el-button>
                <el-button type="warning" size="mini" @click="ResetClick" icon="el-icon-s-promotion">重 置</el-button>
            </div>
            <div class="tb-body">
                <el-form ref="searchFormRef" :model="searchForm" :inline="true" label-width="120px" :style="`flex:1;`">
                    <el-form-item label="编号" prop="NoLike">
                        <el-input v-model.trim="searchForm.NoLike" placeholder="请输入编号" :clearable="true"></el-input>
                    </el-form-item>
                    <el-form-item label="自习室">
                        <SigleSelect url="/Room/List" columnName="Name" :clearable="true" columnValue="Id"
                            v-model="searchForm.RoomId">
                        </SigleSelect>
                    </el-form-item>
                    <el-form-item label="是否维修">
                        <el-select v-model="searchForm.IsMaintain" :clearable="true" placeholder="请选择是否维修">
                            <el-option key="true" label="是" value="true">
                            </el-option>
                            <el-option key="false" label="否" value="false">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </div>

        </el-card>


        <el-dialog :title="formData.Id ? '修改座位' : '添加座位'" :visible.sync="editorShow" width="50%" :lock-scroll="true"
            height="800px">
            <el-form v-if="editorShow == true" ref="editModalForm" :rules="editModalFormRules" :model="formData"
                label-width="140px" size="mini">
                <el-row :gutter="10" class="EditFromBody">

                    <el-col :span="24">
                        <el-form-item label="自习室" prop="RoomId">
                            <SigleSelect url="/Room/List" columnName="Name" columnValue="Id" v-model="formData.RoomId">
                            </SigleSelect>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="是否维修" prop="IsMaintain">
                            <el-switch v-model="formData.IsMaintain">
                            </el-switch>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="行" prop="Row">
                            <el-input type="number" v-model.trim="formData.Row" placeholder="请输入行"
                                :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="列" prop="Col">
                            <el-input type="number" v-model.trim="formData.Col" placeholder="请输入列"
                                :clearable="true"></el-input>
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


        <el-dialog title="批量添加座位" :visible.sync="batchEditorShow" width="50%" :lock-scroll="true" height="800px">
            <el-form v-if="batchEditorShow == true" ref="editModalForm" :rules="editModalFormRules" :model="formData"
                label-width="140px" size="mini">
                <el-row :gutter="10" class="EditFromBody">

                    <el-col :span="24">
                        <el-form-item label="自习室" prop="RoomId">
                            <SigleSelect url="/Room/List" columnName="Name" columnValue="Id" v-model="formData.RoomId">
                            </SigleSelect>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="是否维修" prop="IsMaintain">
                            <el-switch v-model="formData.IsMaintain">
                            </el-switch>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="行" prop="Row">
                            <el-input type="number" v-model.trim="formData.Row" placeholder="请输入行"
                                :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="列" prop="Col">
                            <el-input type="number" v-model.trim="formData.Col" placeholder="请输入列"
                                :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>


                <el-row type="flex" justify="end" align="bottom">
                    <el-form-item>
                        <el-button type="primary" plain @click="BatchCreateOrEditForm()">确 定</el-button>
                        <el-button @click="batchEditorShow = false">取 消</el-button>
                    </el-form-item>
                </el-row>
            </el-form>

        </el-dialog>


        <PaginationTable ref="PaginationTableId" url="/Seat/List" :column="dataColum" :where="where">
            <template v-slot:header>
                <el-button type="primary" size="mini" plain icon="el-icon-edit" @click="ShowEditModal()">新 增</el-button>
                <el-button type="primary" size="mini" plain icon="el-icon-edit" @click="BatchShowEditModal()">批 量 新
                    增</el-button>
                <el-button type="danger" size="mini" icon="el-icon-delete" @click="ShowBatchDeleteModal()">批 量 删
                    除</el-button>
            </template>
            <template v-slot:Operate="scope">
                <el-button type="primary" size="mini" @click="ShowEditModal(scope.row.Id)">修 改</el-button>
                <el-button type="danger" size="mini" @click="ShowDeleteModal(scope.row.Id)">删 除</el-button>
            </template>
        </PaginationTable>
    </div>
</template>

<script>
import store from '@/store';
import { mapGetters } from 'vuex'
export default {
    name: "SeatList",
    computed: {
        ...mapGetters([
            'Token', 'UserInfo', 'RoleType', 'HasUserInfo', 'ColumnType', "UserId"
        ])
    },
    data() {

        return {
            where: {},
            searchForm: {},//搜索定义的数据对象
            dataColum: [
                {
                    key: "Id",
                    hidden: true,

                },
                {
                    key: "RoomDto.Name",
                    title: "自习室名称",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "No",
                    title: "编号",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "Row",
                    title: "行",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "Col",
                    title: "列",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "TotalCommentCount",
                    title: "评论数",
                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "TotalUseCount",
                    title: "使用次数",
                    type: store.getters.ColumnType.SHORTTEXT,
                },

                {
                    key: "AgvCommentScore",
                    title: "平均评分",

                    type: store.getters.ColumnType.SHORTTEXT,
                },
                {
                    key: "IsMaintain",
                    title: "是否维修",

                    type: store.getters.ColumnType.JUDGMENTTAG,
                },
                {
                    key: "RoomId",
                    hidden: true,
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
                "RoomId": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "IsMaintain": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "Row": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "Col": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
            },
            formData: {

            },//保存或者修改定义的数据对象
            editorShow: false,
            batchEditorShow: false,

        };
    },
    created() {

    },
    methods: {
        //修改属性
        async UpdateEntityAsync(Id, title, data) {
            let { Data } = await this.$PostSigleUpdate(`/Seat/Get`, `/Seat/CreateOrEdit`, Id, title, data);

            this.$refs.PaginationTableId.Reload(this.searchForm);
        },
        /**
         * 点击新增或者编辑的时候会触发
         */
        async ShowEditModal(Id) {

            let { Data } = await this.$Post(`/Seat/Get`, { Id: Id });
            this.formData = Data;
            if (!Id) {
                this.formData.IsMaintain = false;
            }
            this.editorShow = true;

        },


        /**
         * 点击保存的时候会触发
         */
        async CreateOrEditForm() {

            this.$refs.editModalForm.validate(async valid => {
                if (valid) {
                    var { Success } = await this.$Post(`/Seat/CreateOrEdit`, this.formData);

                    if (Success) {
                        this.editorShow = false;
                        this.$refs.PaginationTableId.Reload(this.searchForm);
                    }
                }
            })
        },
        //批量弹窗
        async BatchShowEditModal() {
            this.formData = {
                IsMaintain: false,
            };

            this.batchEditorShow = true;

        },
        //批量创建
        async BatchCreateOrEditForm() {

            this.$refs.editModalForm.validate(async valid => {
                if (valid) {
                    let body = [];
                    for (let row = 1; row <= this.formData.Row; row++) {
                        for (let col = 1; col <= this.formData.Col; col++) {

                            body.push({
                                Row: row,
                                Col: col,
                                IsMaintain: this.formData.IsMaintain,
                                RoomId: this.formData.RoomId,
                            })

                        }
                    }
                    var { Success } = await this.$Post(`/Seat/BatchCreate`, body);

                    if (Success) {
                        this.batchEditorShow = false;
                        this.$refs.PaginationTableId.Reload(this.searchForm);
                    }
                }
            })
        },
        /**
         * 单个删除的时候会触发
         */
        async ShowDeleteModal(Id) {
            await this.$PostDelete(`/Seat/Delete`, { Id: Id });
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
            await this.$PostDelete(`/Seat/BatchDelete`, { Ids: ids });

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