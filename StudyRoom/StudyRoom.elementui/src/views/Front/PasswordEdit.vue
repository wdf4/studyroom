<template>
  <div class="pwd-container margin-top-lg">
    <el-page-header class="page-back" @back="goBack" content="修改密码"></el-page-header>

    <div class="pwd-card">
      <div class="pwd-header">
        <div class="icon-shield">
          <i class="el-icon-lock"></i>
        </div>
        <div>
          <h2>安全修改密码</h2>
          <p>为确保账号安全，请输入原始密码后进行修改</p>
        </div>
      </div>

      <el-divider></el-divider>

      <el-form ref="editModalForm" :model="formData" label-width="100px" size="medium" :rules="rules" class="pwd-form">
        <el-form-item label="原始密码" prop="OrginPassword">
          <el-input type="password" v-model.trim="formData.OrginPassword" placeholder="请输入原始密码" prefix-icon="el-icon-unlock" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="NewPassword">
          <el-input type="password" v-model.trim="formData.NewPassword" placeholder="请输入新密码" prefix-icon="el-icon-lock" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="TwoPassword">
          <el-input type="password" v-model.trim="formData.TwoPassword" placeholder="请再次输入新密码" prefix-icon="el-icon-lock" show-password></el-input>
        </el-form-item>
      </el-form>

      <div class="form-actions">
        <el-button type="primary" :loading="saving" @click="CreateOrEdit" icon="el-icon-check">确 认 修 改</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import store from "@/store/index.js";
import { mapGetters } from "vuex";
export default {
    computed: {
        ...mapGetters(["UserInfo", "UserId"])
    },
    data() {
        return {
            formData: {},
            saving: false,
            rules: {
                OrginPassword: [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                NewPassword: [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                TwoPassword: [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
            },
        }
    },
    methods: {
        async CreateOrEdit() {
            this.$refs.editModalForm.validate(async (valid) => {
                if (valid) {
                    if (this.formData.TwoPassword !== this.formData.NewPassword) {
                        this.$message.error("确认密码和新密码不一致");
                        return;
                    }
                    if (this.formData.OrginPassword === this.formData.NewPassword) {
                        this.$message.error("原始密码和新密码不能相同");
                        return;
                    }
                    this.saving = true;
                    try {
                        let { Success } = await this.$Post("/User/ChangePassword", {
                            Id: this.UserId,
                            OrginPassword: this.formData.OrginPassword,
                            Password: this.formData.NewPassword
                        });
                        if (Success) {
                            this.$message.success("修改成功!");
                            store.dispatch("Logout");
                            this.$router.push({ path: "/Login" });
                        }
                    } finally {
                        this.saving = false;
                    }
                } else {
                    return false;
                }
            });
        },
        goBack() {
            this.$router.go(-1);
        }
    }
}
</script>

<style scoped>
.pwd-container {
  max-width: 560px;
  margin: 0 auto;
  padding-bottom: 40px;
}

.page-back {
  margin-bottom: 24px;
  background: transparent;
}

.pwd-card {
  background: var(--white);
  border-radius: var(--radius-xl);
  padding: 36px;
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-color);
}

.pwd-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 8px;
}

.icon-shield {
  width: 56px;
  height: 56px;
  border-radius: var(--radius-lg);
  background: linear-gradient(135deg, var(--primary-light), var(--primary));
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26px;
  color: white;
  flex-shrink: 0;
}

.pwd-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: var(--navy-mid);
}

.pwd-header p {
  margin: 4px 0 0;
  color: var(--slate-400);
  font-size: 13px;
}

.pwd-form {
  margin-top: 8px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid var(--slate-100);
}
</style>
