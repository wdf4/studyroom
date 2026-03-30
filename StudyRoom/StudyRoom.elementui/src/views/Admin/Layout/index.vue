<template>
    <div>
        <el-container style="height: 100vh">

            <el-header class="layout-header-wrap">
                <div class="bg-header">
                    <div class="header-brand">
                        <img class="brand-logo" :src="require('@/assets/logo2.png')">
                        <span class="brand-title">自习室管理端</span>
                    </div>
                    <div class="header-user" v-if="UserInfo">
                        <el-avatar :size="36" :src="UserInfo.ImageUrls">
                            <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png" />
                        </el-avatar>
                        <el-dropdown>
                            <span class="el-dropdown-link user-name">
                                {{ UserInfo.Name }}<i class="el-icon-arrow-down el-icon--right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item>
                                    <RouterLink :to="{ path: '/Admin/UserPerson' }">个人信息</RouterLink>
                                </el-dropdown-item>
                                <el-dropdown-item>
                                    <RouterLink :to="{ path: '/Admin/PasswordEdit' }">修改密码</RouterLink>
                                </el-dropdown-item>
                                <el-dropdown-item divided>
                                    <div @click="LoginOut()">退出登录</div>
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                </div>
            </el-header>

            <el-container>
                <el-aside width="200px">
                    <el-menu class="menu-list" :router="true" background-color="#0f172a" text-color="#cbd5e1" active-text-color="#60a5fa">
                        <el-menu-item index="/Admin/Home">
                            <i class="el-icon-shujufenxi"></i>
                            <span>控制台</span>
                        </el-menu-item>
                        <el-submenu index="/Admin/UserList">
                            <template slot="title">
                                <i class="el-icon-s-home"></i>
                                <span>用户管理</span>
                            </template>
                            <el-menu-item index="/Admin/UserList">
                                <span>用户信息</span>
                            </el-menu-item>
                        </el-submenu>
                        <el-menu-item index="/Admin/RoomList">
                            <i class="el-icon-AIzixishi"></i>
                            <span>自习室</span>
                        </el-menu-item>
                        <el-menu-item index="/Admin/SeatList">
                            <i class="el-icon-zuowei"></i>
                            <span>座位</span>
                        </el-menu-item>
                        <el-menu-item index="/Admin/AppointRecordList">
                            <i class="el-icon-yuyuejilu"></i>
                            <span>预约记录</span>
                        </el-menu-item>
                        <el-menu-item index="/Admin/BannerList">
                            <i class="el-icon-wenanfengmian"></i>
                            <span>封面</span>
                        </el-menu-item>
                        <el-menu-item index="/Admin/IntegralList">
                            <i class="el-icon-jifen"></i>
                            <span>积分</span>
                        </el-menu-item>
                        <el-submenu index="/Admin/AppointRoomAppointStatusData">
                            <template slot="title">
                                <i class="el-icon-shujutongji"></i>
                                <span>数据统计</span>
                            </template>
                            <el-menu-item index="/Admin/AppointRoomAppointStatusData">
                                <span>预约状态分析</span>
                            </el-menu-item>
                            <el-menu-item index="/Admin/GetIntegralConsumeAndGainChart">
                                <span>最近30天积分分析</span>
                            </el-menu-item>
                            <el-menu-item index="/Admin/GetAppointRoomRealTimeData">
                                <span>统计实时在场人数</span>
                            </el-menu-item>
                        </el-submenu>
                    </el-menu>
                </el-aside>

                <el-main class="admin-main">
                    <el-breadcrumb separator-class="el-icon-arrow-right" class="margin-bottom-lg">
                        <el-breadcrumb-item v-for="(item, index) in breadList" :key="index" :to="item.path">{{
                            item.meta.title
                        }}</el-breadcrumb-item>
                    </el-breadcrumb>

                    <transition name="fade" mode="out-in">
                        <router-view></router-view>
                    </transition>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
import { adminRouters } from "@/router/index"
import { mapGetters } from "vuex";
export default {
    name: 'Layout',
    data() {
        return {
            routerMenu: [],
            breadList: [],
        }
    },
    computed: {
        ...mapGetters(["UserInfo"])
    },
    watch: {
        $route() {
            this.getBreadcrumb();
        },
    },
    created() {
        this.routerMenu = adminRouters;
        this.getBreadcrumb();
    },
    methods: {
        async LoginOut() {
            await this.$store.dispatch('Logout')
            this.$router.push(`/Login`);
        },
        isHome(route) {
            return route.path === "/Admin";
        },
        getBreadcrumb() {
            let matched = this.$route.matched;
            if (!this.isHome(matched[0])) {
                matched = [{ path: "/Admin", meta: { title: "控制台" } }].concat(matched);
            }
            this.breadList = matched;
        }
    }
}
</script>

<style>
.el-header,
.el-footer {
    text-align: center;
    line-height: 60px;
    padding: 0;
}

.el-aside {
    color: var(--slate-400, #94a3b8);
    text-align: center;
    line-height: 200px;
}

.menu-list {
    height: calc(100vh - 60px);
    border-right: none !important;
}

.el-submenu__title {
    text-align: left;
}

.el-menu-item {
    text-align: left;
}

.admin-main {
    height: calc(100vh - 60px) !important;
    background-color: var(--slate-50, #f8fafc);
}

/* Header layout */
.layout-header-wrap {
    padding: 0 !important;
}

.header-brand {
    display: flex;
    align-items: center;
    gap: 10px;
}

.brand-logo {
    height: 28px;
    object-fit: contain;
}

.brand-title {
    font-size: 16px;
    font-weight: 600;
    letter-spacing: 0.01em;
}

.header-user {
    display: flex;
    align-items: center;
    gap: 12px;
}

.user-name {
    color: rgba(255, 255, 255, 0.9);
    font-size: 14px;
    cursor: pointer;
}

.user-name:hover {
    color: #ffffff;
}
</style>
