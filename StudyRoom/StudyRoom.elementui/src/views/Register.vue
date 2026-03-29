<template>
    <div>
        <div
            style="height: 100vh; display: flex;align-items: center;justify-content: center;background-color: #28b2d4;">
            <div
                style="display: flex;justify-content: center;background-color: white;padding:20px;border-radius: 10px;">
                <div style="flex:1">
                    <img style="width:500px;border-radius: 5px;" src="@/assets/工作.png">

                </div>
                <div
                    style="flex:1;margin-left: 35px;display: flex;flex-direction: column;justify-content: space-between;">
                    <div style="width:500px">
                        <h2>注册信息填写</h2>
                        <el-form style="margin-top:30px" ref="loginForm" :model="formData" label-width="90px"
                            label-position="left" :rules="rules">

                            <el-form-item label="账号" prop="UserName">
                                <el-input type="text" v-model.trim="formData.UserName" placeholder="请输入账号">
                                </el-input>
                            </el-form-item>

                            <el-form-item label="密码" prop="Password">
                                <el-input type="password" v-model.trim="formData.Password"
                                    placeholder="请输入密码"></el-input>
                            </el-form-item>

                            <el-form-item label="邮箱" prop="Email" placeholder="请输入邮箱">
                                <el-input v-model.trim="formData.Email"></el-input>
                            </el-form-item>
                            <el-form-item label="联系方式" prop="PhoneNumber" placeholder="请输入联系方式">
                                <el-input v-model.trim="formData.PhoneNumber"></el-input>
                            </el-form-item>
                            <el-form-item label="姓名" prop="Name" placeholder="请输入姓名">
                                <el-input v-model.trim="formData.Name"></el-input>
                            </el-form-item>
                            <el-form-item label="验证码" prop="Code">
                                <div style="display: flex;">
                                    <el-input text="Code" v-model.trim="formData.Code" placeholder="请输入验证码"></el-input>
                                    <div style="margin-left:10px">
                                        <ValidCode ref="ValidCode"></ValidCode>
                                    </div>
                                </div>
                            </el-form-item>
                            <el-form-item>
                                <el-button style="width: 100%;" type="primary" @click="RegisterBtn">注 册</el-button>

                            </el-form-item>
                        </el-form>
                    </div>
                    <div style="display: flex; justify-content: space-between;">
                        <div>
                            <span>如果有账号 </span>
                            <RouterLink :to="{ path: '/Login' }"><span style="padding-left:10px; color:red;"
                                    class="pointer">去登录</span></RouterLink>


                        </div>
                        <div><span style="color:#3366FF" class="pointer">忘记密码</span></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import ValidCode from '@/components/Code/canvas.vue'
import store from '@/store';
import { settings } from 'nprogress';
export default {
    components: {
        ValidCode: ValidCode
    },
    data() {
        return {
            formData: {
                UserName: '',
                Password: '',
                RoleType: "2",
                Code: ""
            },
            roleOptions: [],
            rules: {
                UserName: [
                    { required: true, message: '请输入账号', trigger: 'blur' },
                ],
                Password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
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
                Code: [
                    { required: true, message: '请输入验证码', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            let identifyCode = this.$refs.ValidCode.getCode();

                            if (value != identifyCode) {
                                callback(new Error('请输入正确的验证码'));
                            } else {
                                callback();
                            }
                        }, trigger: 'blur'
                    }
                ]
            }
        }

    },
    created() {
        this.GetRoleTypeApi();
    },
    methods: {
        async GetRoleTypeApi() {
            let { Data: { Items } } = await this.$Post("/Select/RoleType");

            this.roleOptions = Items
            console.log(this.roleOptions)
        },
        RegisterBtn() {
            this.$refs.loginForm.validate(async (valid) => {
                if (valid) {

                    let res = await this.$Post("/User/Register", this.formData)
                    console.log(res);
                    if (res.Success) {
                        this.$message.success("注册成功!");
                        this.$router.push({
                            path: "/Login"
                        })
                    }
                    else {

                        this.$refs.ValidCode.refreshCode();
                    }
                } else {
                    this.$message.error("注册验证不通过")
                    this.$refs.ValidCode.refreshCode();
                    return false;
                }
            });
        }
    }
}
</script>

<style></style>