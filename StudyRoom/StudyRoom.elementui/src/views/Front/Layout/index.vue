<template>
    <div>
        <el-container>
            <el-header class="front-header-wrap">
                <el-menu :default-active="active" class="main-container front-nav" mode="horizontal"
                    text-color="#334155" active-text-color="#2563eb">
                    <el-menu-item index="">
                        <img class="nav-logo" :src="require('@/assets/logo.jpg')">
                    </el-menu-item>

                    <el-menu-item index="/Front/Home" @click="ToPath('/Front/Home')">小鹏自习室预约</el-menu-item>

                    <el-menu-item class="nav-right" v-if="!Token" @click="ToRegister()">注册</el-menu-item>
                    <el-menu-item class="nav-right" v-if="!Token" @click="ToLogin()">登录</el-menu-item>
                    <el-submenu class="nav-right" index="userMenu" v-if="Token">
                        <template slot="title">{{ UserInfo.UserName }}</template>
                        <el-menu-item @click="ToUserInfo()">个人信息</el-menu-item>
                        <el-menu-item @click="ToEditPassword()">修改密码</el-menu-item>
                        <el-menu-item @click="LoginOut()">退出</el-menu-item>
                    </el-submenu>
                    <el-menu-item class="nav-right" index="/Front/AppointRecordList"
                        @click="ToPath('/Front/AppointRecordList')" v-if="Token">我的预约记录</el-menu-item>
                    <el-menu-item class="nav-right" index="/Front/IntegralList"
                        @click="ToPath('/Front/IntegralList')" v-if="Token">我的积分</el-menu-item>
                </el-menu>
            </el-header>

            <el-main class="main-container main-box">
                <transition name="fade" mode="out-in">
                    <router-view></router-view>
                </transition>
            </el-main>

            <el-footer class="front-footer">
                <div class="footer-text">© 2026 小鹏自习室预约系统 · 现代化智能学习空间</div>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
    data() {
        return {
            active: 'home',
        }
    },
    computed: {
        ...mapGetters(["UserInfo", "Token"])
    },
    methods: {
        ToLogin() {
            this.$router.push({ path: "/Login" })
        },
        ToRegister() {
            this.$router.push({ path: "/Register" })
        },
        async LoginOut() {
            await this.$store.dispatch('Logout')
            this.$router.push(`/Login`);
        },
        async ToPath(url) {
            this.$router.push({ path: url })
        },
        async ToUserInfo() {
            this.$router.push({ path: "/Front/UserPerson" })
        },
        async ToEditPassword() {
            this.$router.push({ path: "/Front/PasswordEdit" })
        },
    },
}
</script>

<style scoped>
.front-header-wrap {
    background-color: var(--white, #ffffff);
    border-bottom: 1px solid var(--border-color, #e2e8f0);
    padding: 0 !important;
}

.main-container {
    width: 1300px;
    margin: 0 auto;
}

.front-nav {
    border-bottom: none !important;
}

.nav-logo {
    height: 46px;
    object-fit: contain;
}

.nav-right {
    float: right;
}

.main-box {
    min-height: calc(100vh - 120px);
    padding: 0 !important;
}

.front-footer {
    background-color: var(--navy, #0f172a);
    display: flex;
    align-items: center;
    justify-content: center;
    height: 56px !important;
    line-height: 56px !important;
}

.footer-text {
    text-align: center;
    color: var(--slate-400, #94a3b8);
    font-size: 13px;
}
</style>
