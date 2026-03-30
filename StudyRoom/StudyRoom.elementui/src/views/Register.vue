<template>
  <div class="login-container">
    <div class="login-glass-card register-glass-card">
      <div class="login-header">
        <h2>注册账号</h2>
        <p>加入现代化智能学习空间</p>
      </div>

      <el-form class="login-form" ref="loginForm" :model="formData" :rules="rules">
        <el-form-item prop="UserName">
          <el-input prefix-icon="el-icon-user" type="text" v-model.trim="formData.UserName" placeholder="请输入账号"></el-input>
        </el-form-item>

        <el-form-item prop="Password">
          <el-input prefix-icon="el-icon-lock" type="password" v-model.trim="formData.Password" placeholder="请输入密码"></el-input>
        </el-form-item>

        <el-form-item prop="Email">
          <el-input prefix-icon="el-icon-message" type="email" v-model.trim="formData.Email" placeholder="请输入邮箱"></el-input>
        </el-form-item>

        <el-form-item prop="PhoneNumber">
          <el-input prefix-icon="el-icon-mobile-phone" type="tel" v-model.trim="formData.PhoneNumber" placeholder="请输入手机号"></el-input>
        </el-form-item>

        <el-form-item prop="Name">
          <el-input prefix-icon="el-icon-s-custom" type="text" v-model.trim="formData.Name" placeholder="请输入姓名"></el-input>
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
          <el-button class="submit-btn" type="primary" :loading="registering" @click="RegisterBtn">注 册</el-button>
        </div>

        <div class="extra-links">
          <span class="extra-hint">已有账号？ </span>
          <RouterLink :to="{ path: '/Login' }" class="link-register">立即登录</RouterLink>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
    data() {
        return {
            formData: {
                UserName: '',
                Password: '',
                RoleType: "2",
                Code: ""
            },
            captchaQuestion: '',
            registering: false,
            rules: {
                UserName: [
                    { required: true, message: '请输入账号', trigger: 'blur' },
                ],
                Password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                ],
                Email: [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            var reg = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
                            if (!value || !reg.test(value)) {
                                callback(new Error('请输入正确的邮箱'));
                            } else {
                                callback();
                            }
                        }, trigger: 'blur'
                    },
                ],
                Name: [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                ],
                PhoneNumber: [
                    { required: true, message: '该项为必填项', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            var reg = /^1[3-9]\d{9}$/;
                            if (!value || !reg.test(value)) {
                                callback(new Error('请输入正确的手机号'));
                            } else {
                                callback();
                            }
                        }, trigger: 'blur'
                    },
                ],
                Code: [
                    { required: true, message: '请输入验证码答案', trigger: 'blur' },
                ]
            }
        }
    },
    created() {
        this.loadCaptcha();
    },
    methods: {
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
        RegisterBtn() {
            this.$refs.loginForm.validate(async (valid) => {
                if (valid) {
                    this.registering = true;
                    try {
                        // 先向后端校验验证码
                        let captchaRes = await this.$Post("/Captcha/Verify", { Code: this.formData.Code });
                        if (!captchaRes || !captchaRes.Data) {
                            this.$message.error("验证码错误，请重新输入");
                            await this.loadCaptcha();
                            return;
                        }
                        let res = await this.$Post("/User/Register", this.formData);
                        if (res.Success) {
                            this.$message.success("注册成功!");
                            this.$router.push({ path: "/Login" });
                        } else {
                            await this.loadCaptcha();
                        }
                    } finally {
                        this.registering = false;
                    }
                } else {
                    this.$message.error("注册验证不通过");
                    return false;
                }
            });
        }
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

.register-glass-card {
  width: 480px; /* slightly wider for register */
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
  justify-content: center;
  margin-top: 20px;
  font-size: 14px;
}

.extra-hint {
  color: var(--slate-500);
}

.link-register {
  color: var(--primary);
  text-decoration: none;
  font-weight: 600;
}

.link-register:hover {
  text-decoration: underline;
}
</style>
