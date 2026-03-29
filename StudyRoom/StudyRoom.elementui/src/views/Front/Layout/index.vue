<template>
    <div>
        <el-container>
            <el-header style="background-color: white;">
                <el-menu :default-active="active" class="main-container" mode="horizontal" text-color="black"
                    active-text-color="#28b2d4">
                    <el-menu-item index="">
                        <img style="height: 50px;object-fit: contain;" :src="require('@/assets/logo.jpg')">
                    </el-menu-item>

                    <el-menu-item index="/Front/Home" @click="ToPath('/Front/Home')">志高自习室预约</el-menu-item>

                    <el-menu-item style="float: right;" v-if="!Token" @click="ToRegister()">注册</el-menu-item>
                    <el-menu-item style="float: right;" v-if="!Token" @click="ToLogin()">登录</el-menu-item>
                    <el-submenu style="float: right;" v-if="Token">
                        <template slot="title">{{ UserInfo.UserName }}</template>
                        <el-menu-item @click="ToUserInfo()">个人信息</el-menu-item>
                        <el-menu-item @click="ToEditPassword()">修改密码</el-menu-item>
                        <el-menu-item @click="LoginOut()">退出</el-menu-item>
                    </el-submenu>
                    <el-menu-item style="float: right;" index="/Front/AppointRecordList"
                        @click="ToPath('/Front/AppointRecordList')" v-if="Token">我的预约记录</el-menu-item>
                    <el-menu-item style="float: right;" index="/Front/IntegralList"
                        @click="ToPath('/Front/IntegralList')" v-if="Token">我的积分</el-menu-item>



                </el-menu>

            </el-header>
            <el-main class="main-container main-box">
                <router-view></router-view>
            </el-main>
            <div style="height: 20px;"></div>
            <el-footer style="background-color:#28b2d4">
                <div style="text-align: center;color:white;font-weight: bolder;">系统归 志高自习室预约所有</div>
            </el-footer>
        </el-container>


    </div>
</template>

<script>

import store from '@/store'
import { mapGetters } from 'vuex'
export default {
    data() {
        return {
            active: 'home',

        }
    },
    created() {

    },
    computed: {
        ...mapGetters(["UserInfo", "Token"])
    },
    methods: {
        //前往登录
        ToLogin() {
            this.$router.push({
                path: "/Login"
            })
        },
        //前往注册
        ToRegister() {
            this.$router.push({
                path: "/Register"
            })
        },
        //退出
        async LoginOut() {
            await this.$store.dispatch('Logout')
            this.$router.push(`/Login`);
        },
        //跳转
        async ToPath(url) {
            this.$router.push({
                path: url
            })
        },
        //跳转到用户信息
        async ToUserInfo() {
            this.$router.push({
                path: "/Front/UserPerson"
            })
        },
        //跳转到修改密码
        async ToEditPassword() {
            this.$router.push({
                path: "/Front/PasswordEdit"
            })
        },
        //返回上一个页面
        goBack() {
            this.$router.go(-1)
        }

    },


}
</script>

<style scoped>
.main-container {
    width: 1300px;
    margin: 0 auto;
}


.main-box {
    min-height: calc(100vh - 120px);
}

.el-main {
    padding: 0px !important;
}
</style>