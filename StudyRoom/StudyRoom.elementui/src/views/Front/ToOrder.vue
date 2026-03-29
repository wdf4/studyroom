<template>
    <div>
        <el-page-header class="card margin-top-lg" @back="goBack" content="确定选座">
        </el-page-header>

        <div class="margin-top-lg card">
            <el-form v-if="editorShow == true" ref="editModalForm" :rules="editModalFormRules" :model="formData"
                label-width="140px" size="mini">
                <el-row :gutter="10" class="EditFromBody">


                    <el-col :span="24">
                        <el-form-item label="自习室" prop="RoomId">
                            <SigleSelect url="/Room/List" :disabled="true" columnName="Name" columnValue="Id"
                                v-model="formData.RoomId">
                            </SigleSelect>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="座位" prop="SeatId">
                            <SigleSelect url="/Seat/List" :disabled="true" columnName="No" columnValue="Id"
                                v-model="formData.SeatId">
                            </SigleSelect>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="预约账号" prop="UserId">
                            <SigleSelect url="/User/List" :disabled="true" columnName="UserName" columnValue="Id"
                                v-model="formData.UserId">
                            </SigleSelect>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="预约时间" prop="AppointDateType">
                            <SigleSelect url="/Select/AppointDateTypeEnum" :disabled="true" columnName="Name"
                                columnValue="Code" v-model="formData.AppointDateType">
                            </SigleSelect>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="姓名" prop="Name">
                            <el-input type="text" v-model.trim="formData.Name" placeholder="请输入姓名"
                                :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="手机号" prop="Phone">
                            <el-input type="text" v-model.trim="formData.Phone" placeholder="请输入手机号"
                                :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>

                </el-row>


                <el-row type="flex" justify="end" align="bottom">
                    <el-form-item>
                        <el-button type="primary" size="large" @click="ToOrder()">确 定</el-button>
                    </el-form-item>
                </el-row>
            </el-form>
        </div>

    </div>
</template>

<script>
import store from '@/store';
import { mapGetters } from 'vuex'
export default {
    computed: {
        ...mapGetters([
            'Token', 'UserInfo', 'RoleType', 'HasUserInfo', 'ColumnType', "UserId"
        ])
    },
    data() {
        return {
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
                "Comment": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "CommentScore": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
            },
            formData: {

            },//保存或者修改定义的数据对象
            editorShow: true,
        }
    },
    created() {
        let tick = this.$route.query.tick;
        let jsonData = localStorage.getItem(tick);
        let data = JSON.parse(jsonData);
        this.formData = {
            RoomId: data.RoomId,
            SeatId: data.SeatId,
            UserId: data.UserId,
            AppointDateType: data.AppointDateType,
            AppointDate: data.AppointDate,
            AppointStatus: 1,
        };

    },
    methods: {
        //订单确认
        async ToOrder() {
            this.$refs.editModalForm.validate(async (valid) => {
                if (valid) {
                    let { Success } = await this.$Post("/AppointRecord/ToOrder", this.formData);
                    if (Success) {
                        //跳转到下一个页面
                        this.$router.push({
                            path: "/Front/AppointRecordList"
                        })
                    }
                } else {

                    return false;
                }
            });
        },
        //返回上一个页面
        goBack() {
            this.$router.go(-1)
        }
    }
}
</script>

<style></style>