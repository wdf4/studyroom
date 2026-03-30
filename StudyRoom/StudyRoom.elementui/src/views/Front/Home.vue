<template>
    <div>

        <div class="margin-top-lg card" v-loading="loadingBanner">
            <el-carousel v-if="BannerList.length > 0" :interval="5000" arrow="always" height="400px">
                <el-carousel-item v-for="item in BannerList" :key="item.Id">
                    <div>
                        <img :src="item.Cover" style="width: 100%;">
                    </div>
                </el-carousel-item>
            </el-carousel>
            <el-empty v-if="BannerList.length === 0 && !loadingBanner" description="暂无轮播图"></el-empty>
        </div>

        <div class="card margin-top-lg">
            <div class="item-header">自习室列表</div>
            <Pagination url="/Room/List">
                <template v-slot:content="{ data }">
                    <div class="room-list">
                        <div class="room-item" v-for="item in data" :key="item.Id" @click="ToRoom(item)">
                            <div class="room-cover-wrapper">
                                <img class="room-cover" :src="item.Cover">
                            </div>
                            <div class="room-info">
                                <span class="tit">{{ item.Name }}</span>
                            </div>
                        </div>

                    </div>
                </template>
            </Pagination>
        </div>


    </div>
</template>


<script>
import Pagination from "@/components/Pagination/PaginationBox.vue"
export default {
    components: {
        Pagination
    },
    data() {
        return {
            BannerList: [],
            RoomList: [],
            loadingBanner: false
        }
    },
    created() {
        this.BannerListApi();
    },
    methods: {
        async BannerListApi() {
            this.loadingBanner = true;
            try {
                let { Data: { Items } } = await this.$Post("/Banner/List", {});
                this.BannerList = Items;
            } finally {
                this.loadingBanner = false;
            }
        },
        //跳转到自习室详情
        async ToRoom(item) {
            this.$router.push({
                path: "/Front/Room",
                query: {
                    RoomId: item.Id
                }
            })
        }
    },


}
</script>


<style scoped>
.room-list {
    display: flex;
    flex-wrap: wrap;
    margin-left: 10px;
}

.room-list .room-item {
    width: 260px;
    cursor: pointer;
    margin-bottom: 25px;
    margin-left: 30px;
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
    overflow: hidden;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.room-list .room-item:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

.room-cover-wrapper {
    width: 100%;
    height: 180px;
    overflow: hidden;
}

.room-list .room-item .room-cover {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.5s ease;
}

.room-list .room-item:hover .room-cover {
    transform: scale(1.08);
}

.room-info {
    padding: 16px;
    text-align: center;
}



.room-list .room-item .tit {
    font-weight: bold;
    width: 100%;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    line-clamp: 2;
    overflow: hidden;
    text-align: center;
    text-overflow: ellipsis;
}

.item-header {
    border-left: 5px solid #2563eb;
    padding-left: 12px;
    font-size: 18px;
    color: #1e293b;
    font-weight: 700;
    margin-bottom: 20px;
}
</style>