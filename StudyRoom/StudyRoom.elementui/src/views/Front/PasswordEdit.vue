<template>
    <div>
        <el-page-header class="card margin-top-lg" @back="goBack" content="修改密码">
        </el-page-header>
        <el-card class="margin-top-lg">
            <div slot="header" class="clearfix">
                <h2>修改密码</h2>
            </div>
            <div>
                <el-form ref="editModalForm" :model="formData" label-width="80px" size="mini" :rules="rules">


                    <el-form-item label="原始密码" prop="OrginPassword" placeholder="请输入原始密码">
                        <el-input type="password" v-model.trim="formData.OrginPassword"></el-input>
                    </el-form-item>

                    <el-form-item label="新密码" prop="NewPassword" placeholder="请输入原始密码">
                        <el-input type="password" v-model.trim="formData.NewPassword"></el-input>
                    </el-form-item>

                    <el-form-item label="确认密码" prop="TwoPassword" placeholder="请输入原始密码">
                        <el-input type="password" v-model.trim="formData.TwoPassword"></el-input>
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

            formData: {},
            rules: {
                "TwoPassword": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "OrginPassword": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                "NewPassword": [
                    { required: true, message: '该项为必填项', trigger: 'blur' },

                ],
            },

        }
    },
    created() {

    },
    methods: {


        //创建或者修改
        async CreateOrEdit() {
            this.$refs.editModalForm.validate(async (valid) => {
                if (valid) {

                    if (this.UserInfo.Password != this.formData.OrginPassword) {
                        this.$message.error("原始密码错误");
                        return;
                    }
                    if (this.formData.TwoPassword != this.formData.NewPassword) {
                        this.$message.error("确认密码和新密码不一致");
                        return;
                    }
                    if (this.formData.Password == this.formData.TwoPassword) {
                        this.$message.error("原始密码和新密码不能相同");
                    }

                    this.UserInfo.Password = this.formData.TwoPassword;

                    //保存数据到数据库
                    let { Success, Msg, Data } = await this.$Post("/User/CreateOrEdit", this.UserInfo);
                    if (Success) {

                        this.$message.success("修改成功!");
                        store.dispatch("Logout");
                        this.$router.push({
                            path: "/Login"
                        })

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