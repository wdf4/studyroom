<template>
    <div>

        <div class="margin-top-lg card">
            <el-carousel :interval="5000" arrow="always" height="400px">
                <el-carousel-item v-for="(item, index) in BannerList" :key="item">
                    <div>
                        <img :src="item.Cover" style="width: 100%;">
                    </div>
                </el-carousel-item>
            </el-carousel>
        </div>

        <div class="card margin-top-lg">
            <div class="item-header">自习室列表</div>
            <Pagination url="/Room/List">
                <template v-slot:content="{ data }">
                    <div class="room-list">
                        <div class="room-item" v-for="(item, index) in data" key="index" @click="ToRoom(item)">
                            <img class="room-cover" :src="item.Cover">
                            <div>
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
            RoomList: []
        }
    },
    created() {
        this.BannerListApi();
    },
    methods: {
        async BannerListApi() {
            let { Data: { Items } } = await this.$Post("/Banner/List", {});
            this.BannerList = Items;
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
    margin-bottom: 20px;
    margin-left: 40px;

}

.room-list .room-item:hover {
    transform: scale(1.05);
    transition: all 0.5s;
}

.room-list .room-item .room-cover {
    width: 260px;
    height: 260px;
    border-radius: 20px;

}



.room-list .room-item .tit {
    font-weight: bold;
    width: 100%;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    overflow: hidden;
    text-align: center;
    text-overflow: ellipsis;
}

.item-header {
    border-left: 5px solid #28b2d4;
    padding-left: 5px;
    font-weight: bolder;
}
</style>