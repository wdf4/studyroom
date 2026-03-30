<template>
  <div class="login-container">
    <div class="login-glass-card">
      <div class="login-header">
        <h2>自习室预约系统</h2>
        <p>现代化智能学习空间</p>
      </div>
      
      <el-form class="login-form" ref="loginForm" :model="formData" :rules="rules">
        <el-form-item prop="UserName">
          <el-input prefix-icon="el-icon-user" type="text" v-model.trim="formData.UserName" placeholder="请输入账号"></el-input>
        </el-form-item>

        <el-form-item prop="Password">
          <el-input prefix-icon="el-icon-lock" type="password" v-model.trim="formData.Password" placeholder="请输入密码"></el-input>
        </el-form-item>

        <el-form-item prop="RoleType">
          <el-radio-group v-model="formData.RoleType" class="role-group">
            <el-radio v-for="item in roleOptions" :key="item.Code" :label="item.Code">{{ item.Label }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="Code">
          <div class="captcha-wrapper">
            <el-input prefix-icon="el-icon-key" v-model.trim="formData.Code" placeholder="请输入答案" class="captcha-input"></el-input>
            <div class="captcha-image">
              <span>{{ captchaQuestion }}</span>
              <el-button type="text" icon="el-icon-refresh" @click="loadCaptcha" title="换一题"></el-button>
            </div>
          </div>
        </el-form-item>

        <div class="action-buttons">
          <el-button class="submit-btn" type="primary" :loading="logging" @click="LoginBtn">登 录</el-button>
        </div>
        
        <div class="extra-links">
          <RouterLink :to="{ path: '/Register' }" class="link-register">立即注册</RouterLink>
          <span class="link-forgot pointer" @click="ToHome">返回前台主页</span>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import store from '@/store';
export default {
    data() {
        return {
            formData: {
                UserName: '',
                Password: '',
                RoleType: "",
                Code: ""
            },
            captchaQuestion: '',
            logging: false,
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
                    { required: true, message: '请输入验证码答案', trigger: 'blur' },
                ]
            }
        }
    },
    created() {
        this.GetRoleTypeApi();
        this.loadCaptcha();
    },
    methods: {
        async GetRoleTypeApi() {
            let { Data: { Items } } = await this.$Post("/Select/RoleType");
            this.roleOptions = Items;
        },
        async loadCaptcha() {
            try {
                let res = await this.$Post("/Captcha/Generate", {});
                if (res && res.Data) {
                    this.captchaQuestion = res.Data.Question;
                }
            } catch (e) {
                this.captchaQuestion = '加载失败，请刷新';
            }
            this.formData.Code = '';
        },
        LoginBtn() {
            this.$refs.loginForm.validate(async (valid) => {
                if (valid) {
                    this.logging = true;
                    try {
                        // 先向后端校验验证码
                        let captchaRes = await this.$Post("/Captcha/Verify", { Code: this.formData.Code });
                        if (!captchaRes || !captchaRes.Data) {
                            this.$message.error("验证码错误，请重新输入");
                            await this.loadCaptcha();
                            return;
                        }
                        let res = await store.dispatch("Login", this.formData);
                        if (res.Success) {
                            this.$message.success("登录成功!");
                            this.$router.push({ path: "/Admin" });
                        } else {
                            await this.loadCaptcha();
                        }
                    } finally {
                        this.logging = false;
                    }
                } else {
                    this.$message.error("登录验证不通过");
                    return false;
                }
            });
        },
        ToHome() {
            this.$router.push({ path: "/Front/Home" });
        },
    }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url('~@/assets/login_bg_modern.png');
  background-size: cover;
  background-position: center;
}

.login-glass-card {
  width: 420px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  margin: 0;
  font-size: 26px;
  color: var(--navy-mid);
  font-weight: 700;
  letter-spacing: -0.5px;
}

.login-header p {
  margin: 8px 0 0;
  color: var(--slate-500);
  font-size: 14px;
}

.role-group {
  display: flex;
  justify-content: center;
  width: 100%;
}

.captcha-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
}

.captcha-input {
  flex: 1;
}

.captcha-image {
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.9);
  padding: 0 15px;
  height: 40px;
  border-radius: var(--radius-md);
  border: 1px solid var(--border-color);
  font-weight: 800;
  color: var(--navy);
  box-shadow: inset 0 2px 4px 0 rgba(0, 0, 0, 0.03);
}

.submit-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 600;
  margin-top: 10px;
}

.extra-links {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  font-size: 14px;
}

.link-register {
  color: var(--primary);
  text-decoration: none;
  font-weight: 600;
}

.link-forgot {
  color: var(--slate-500);
}

.link-forgot:hover, .link-register:hover {
  text-decoration: underline;
}
</style>
