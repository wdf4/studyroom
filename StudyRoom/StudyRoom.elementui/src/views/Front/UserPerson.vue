<template>
    <div>
        <el-page-header class="card margin-top-lg" @back="goBack" content="修改个人信息">
        </el-page-header>

        <el-card class="margin-top-lg">
            <div>
                <el-form ref="editModalForm" v-if="editShow == true" :model="formData" label-width="80px" size="mini"
                    :rules="rules">
                    <el-form-item label="账号" prop="UserName">
                        <el-input v-model="formData.UserName" clearable :disabled="true"></el-input>
                    </el-form-item>

                    <el-form-item label="邮箱" prop="Email">
                        <el-input v-model="formData.Email" clearable> </el-input>
                    </el-form-item>
                    <el-form-item label="名称" prop="Name">
                        <el-input v-model="formData.Name" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="手机号码" prop="PhoneNumber">
                        <el-input v-model="formData.PhoneNumber" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="头像" prop="ImageUrls">
                        <UploadImages v-model="formData.ImageUrls"></UploadImages>
                    </el-form-item>

                    <el-form-item label="出生年月" prop="Birth">
                        <el-date-picker type="date" value-format="yyyy-MM-dd 00:00:00" placeholder="选择日期"
                            v-model="formData.Birth" clearable></el-date-picker>
                    </el-form-item>



                </el-form>
                <div style="display: flex;justify-content: flex-end;">
                    <el-button type="primary" @click="CreateOrEdit">确 定</el-button>
                </div>
            </div>
        </el-card>

    </div>
</template>

<script>
import store from "@/store/index.js"
import { mapGetters } from "vuex";
export default {

    computed: {
        ...mapGetters(["UserInfo", 'UserId'])
    },
    data() {
        return {
            editShow: false,
            formData: {},
            rules: {
                UserName: [
                    { required: true, message: '请输入账号', trigger: 'blur' },
                ],
                Password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                ],
                Email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            var reg = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
                            if (!value || !reg.test(value)) {
                                callback(new Error('请输入正确邮箱'));
                            }
                            else {
                                callback();
                            }
                        }, trigger: 'blur'
                    },
                ],
                Name: [
                    { required: true, message: '请输入名称', trigger: 'blur' },
                ],
                PhoneNumber: [
                    { required: true, message: '请输入手机号码', trigger: 'blur' },
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
                Birth: [
                    { required: true, message: '请输入出生年月', trigger: 'blur' },
                ],
                ImageUrls: [
                    { required: true, message: '请输入头像', trigger: 'blur' },
                ],
            },

        }
    },
    created() {
        this.ShowEditModal();
    },
    methods: {

        //获取用户信息
        async ShowEditModal() {

            let { Data } = await this.$Post("/User/Get", { Id: this.UserId })
            this.formData = Data;
            this.editShow = true;

        },
        //创建或者修改
        async CreateOrEdit() {
            this.$refs.editModalForm.validate(async (valid) => {
                if (valid) {
                    //保存数据到数据库
                    let { Success, Msg, Data } = await this.$Post("/User/CreateOrEdit", this.formData);
                    if (Success) {

                        this.$message.success("修改成功!");

                        store.dispatch("GetInfo");


                    }
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        }, //返回上一个页面
        goBack() {
            this.$router.go(-1)
        }

    }
}
</script>

<style></style>