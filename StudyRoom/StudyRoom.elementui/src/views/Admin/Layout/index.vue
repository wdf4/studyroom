<template>
    <div>
        <el-container style="height: 100vh">

            <el-header>
                <div class="bg-header">
                    <div style="display: flex;align-items: center;">
                        <img style="height: 30px;object-fit: contain;" :src="require('@/assets/logo2.png')">
                        <span style="margin-left:10px">自习室管理端</span>
                    </div>
                    <div style="display: flex;align-items: center">
                        <el-avatar :size="60" :src="UserInfo.ImageUrls" size="medium">
                            <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png" />
                        </el-avatar>
                        <div style="margin-left:10px">
                            <el-dropdown>
                                <span class="el-dropdown-link" style="color:white">
                                    {{ UserInfo.Name }}<i class="el-icon-arrow-down el-icon--right"></i>
                                </span>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item>
                                        <RouterLink :to="{ path: '/Admin/UserPerson' }"> 个人信息</RouterLink>
                                    </el-dropdown-item>
                                    <el-dropdown-item>
                                        <RouterLink :to="{ path: '/Admin/PasswordEdit' }"> 修改密码</RouterLink>
                                    </el-dropdown-item>
                                    <el-dropdown-item>
                                        <div @click="LoginOut()">退出</div>
                                    </el-dropdown-item>

                                </el-dropdown-menu>
                            </el-dropdown>
                        </div>

                    </div>
                </div>


            </el-header>


            <el-container>
                <el-aside width="200px">

                    <el-menu class="menu-list" :router="true" active-text-color="#28b2d4">
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

                    <transition>
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
            console.log("点击退出")
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
    padding: 0px;
}

.el-aside {

    color: #333;
    text-align: center;
    line-height: 200px;
}


.menu-list {
    height: calc(100vh - 60px);
}

.el-submenu__title {
    text-align: left
}

.el-menu-item {
    text-align: left;
}

.admin-main {
    height: calc(100vh - 60px) !important;
}
</style>