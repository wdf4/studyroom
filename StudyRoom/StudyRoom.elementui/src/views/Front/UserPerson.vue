<template>
  <div class="person-container margin-top-lg">
    <el-page-header class="page-back" @back="goBack" content="个人信息＆头像"></el-page-header>

    <div class="profile-card">
      <div class="profile-header">
        <div class="avatar-area">
          <img v-if="formData.ImageUrls" :src="formData.ImageUrls" class="user-avatar" />
          <div v-else class="user-avatar avatar-placeholder">
            <i class="el-icon-user"></i>
          </div>
        </div>
        <div class="profile-meta">
          <h2 class="username">{{ formData.Name || formData.UserName }}</h2>
          <p class="usersub">{{ formData.UserName }}</p>
        </div>
      </div>

      <el-divider></el-divider>

      <el-form ref="editModalForm" v-if="editShow == true" :model="formData" label-width="90px" size="medium" :rules="rules" class="profile-form">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="账号" prop="UserName">
              <el-input v-model="formData.UserName" clearable :disabled="true" prefix-icon="el-icon-user"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="Name">
              <el-input v-model="formData.Name" clearable prefix-icon="el-icon-s-custom"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="Email">
              <el-input v-model="formData.Email" clearable prefix-icon="el-icon-message"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="PhoneNumber">
              <el-input v-model="formData.PhoneNumber" clearable prefix-icon="el-icon-mobile-phone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生年月" prop="Birth">
              <el-date-picker type="date" value-format="yyyy-MM-dd 00:00:00" placeholder="选择日期" v-model="formData.Birth" clearable style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="头像" prop="ImageUrls">
              <UploadImages v-model="formData.ImageUrls"></UploadImages>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div class="form-actions">
        <el-button type="primary" @click="CreateOrEdit" icon="el-icon-check">保 存 修 改</el-button>
      </div>
    </div>
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
                    let { Success } = await this.$Post("/User/CreateOrEdit", this.formData);
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

<style scoped>
.person-container {
  max-width: 900px;
  margin: 0 auto;
  padding-bottom: 40px;
}

.page-back {
  margin-bottom: 24px;
  background: transparent;
}

.profile-card {
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 4px 6px -1px rgba(0,0,0,0.07), 0 2px 4px -1px rgba(0,0,0,0.04);
  border: 1px solid #f1f5f9;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 8px;
}

.user-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #e2e8f0;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.avatar-placeholder {
  background: linear-gradient(135deg, #3b82f6, #2563eb);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 36px;
}

.username {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  color: #1e293b;
}

.usersub {
  margin: 4px 0 0;
  color: #94a3b8;
  font-size: 14px;
}

.profile-form {
  margin-top: 8px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #f1f5f9;
}
</style>