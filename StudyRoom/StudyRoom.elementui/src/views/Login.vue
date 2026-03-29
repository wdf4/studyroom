<template>
    <div>
        <div style="height: 100vh; display: flex;align-items: center;justify-content: center;background-color: #28b2d4;">
            <div style="display: flex;justify-content: center;background-color: white;padding:20px;border-radius: 10px;">
                <div style="flex:1">
                    <img style="width:600px;border-radius: 5px;" src="@/assets/工作.png">

                </div>
                <div style="flex:1;margin-left: 35px;display: flex;flex-direction: column;justify-content: space-between;">
                    <div style="width:500px">
                        <h2>欢迎您来到自习室预约系统</h2>
                        <el-form style="margin-top:30px" ref="loginForm" :model="formData" label-width="70px"
                            label-position="left" :rules="rules">

                            <el-form-item label="账号" prop="UserName">
                                <el-input type="text" v-model.trim="formData.UserName" placeholder="请输入账号">
                                </el-input>
                            </el-form-item>

                            <el-form-item label="密码" prop="Password">
                                <el-input type="password" v-model.trim="formData.Password" placeholder="请输入密码"></el-input>
                            </el-form-item>

                            <el-form-item label="角色" prop="RoleType">
                                <el-radio-group v-model="formData.RoleType">
                                    <el-radio v-for="(item, index) in roleOptions" :key="item.Code" :label="item.Code">{{
                                        item.Label
                                    }}</el-radio>
                                </el-radio-group>
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
                                <el-button style="width: 100%;" type="primary" @click="LoginBtn">登 录</el-button>

                            </el-form-item>
                            <el-form-item>
                                <el-button style="width: 100%;" type="danger" @click="ToHome">前 台</el-button>

                            </el-form-item>
                        </el-form>
                    </div>
                    <div style="display: flex; justify-content: space-between;">
                        <div>
                            <span>如果没有账号可以</span>
                            <RouterLink :to="{ path: '/Register' }"><span style="padding-left:10px; color:red;"
                                    class="pointer">
                                    立即注册
                                </span>
                            </RouterLink>
                        </div>
                        <div><span style="color:#3366FF" class="pointer">忘记密码</span></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import ValidCode from '@/components/Code/canvas.vue';
import store from '@/store';
export default {
    components: {
        ValidCode: ValidCode
    },
    data() {
        return {
            formData: {
                UserName: '',
                Password: '',
                RoleType: "",
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
                RoleType: [
                    { required: true, message: '请选择角色', trigger: 'blur' },
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
            //就是写请求Select控制器以及RoleType具体方法的前端代码
            let { Data: { Items } } = await this.$Post("/Select/RoleType");

            this.roleOptions = Items

        },
        LoginBtn() {
            this.$refs.loginForm.validate(async (valid) => {
                if (valid) {

                    let res = await store.dispatch("Login", this.formData);
                    if (res.Success) {
                        this.$message.success("登录成功!");
                        this.$router.push({
                            path: "/Admin"
                        })

                    }
                    else {

                        this.$refs.ValidCode.refreshCode();
                    }
                } else {
                    this.$message.error("登录验证不通过")
                    this.$refs.ValidCode.refreshCode();
                    return false;
                }
            });
        },
        //去往前台
        ToHome() {
            this.$router.push({
                path: "/Front/Home"
            })
        },
    }
}
</script>

<style></style>