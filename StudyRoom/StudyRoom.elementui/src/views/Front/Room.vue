<template>
    <div v-if="RoomDetail">
        <el-page-header class="card margin-top-lg" @back="goBack" :content="RoomDetail.Name">
        </el-page-header>
        <div class=" card margin-top-lg margin-bottom-lg">
            <div>
                <img :src="RoomDetail.Cover" style="width: 100%;border-radius: 10px;">
            </div>
            <div class="container">
                <el-tabs v-model="activeName">
                    <el-tab-pane label="详情介绍" name="详情介绍">
                        <div class="content" v-html="RoomDetail.Content"></div>
                    </el-tab-pane>
                    <el-tab-pane label="选座列表" name="选座列表">

                        <h4>先选择日期</h4>
                        <el-tabs v-model="SelectDate" @tab-click="DateHandleClick">
                            <el-tab-pane :label="item" :name="item" v-for="(item, index) in DateList"></el-tab-pane>

                        </el-tabs>

                        <template v-if="SeatArrange">
                            <h4>上午</h4>
                            <em class="tip">如果有人提前离开了,座位是会被释放的</em>
                            <el-empty v-if="SeatArrange.AmSeatDtoList.length == 0" description="暂时没有数据"></el-empty>
                            <div class="seat-list">
                                <div class="seat-row" v-for="(row, rowIndex) in SeatArrange.AmSeatDtoList">
                                    <div class="seat-col" v-for="(col, colIndex) in row">
                                        <div v-if="col.Id" @click="ToAppoint(col, 1)">
                                            <img v-if="col.IsOccupy == false" :src="require('@/assets/seat.png')">
                                            <img v-else :src="require('@/assets/seatcked.png')">
                                            <div class="tit"><span>{{ col.No }}</span></div>
                                        </div>

                                    </div>

                                </div>

                            </div>

                            <h4>下午</h4>
                            <em class="tip">如果有人提前离开了,座位是会被释放的</em>
                            <el-empty v-if="SeatArrange.PmSeatDtoList.length == 0" description="暂时没有数据"></el-empty>
                            <div class="seat-list">
                                <div class="seat-row" v-for="(row, rowIndex) in SeatArrange.PmSeatDtoList">
                                    <div class="seat-col" v-for="(col, colIndex) in row">
                                        <div v-if="col.Id" @click="ToAppoint(col, 2)">
                                            <img v-if="col.IsOccupy == false" :src="require('@/assets/seat.png')">
                                            <img v-else :src="require('@/assets/seatcked.png')">
                                            <div class="tit"><span>{{ col.No }}</span></div>
                                        </div>

                                    </div>

                                </div>

                            </div>
                            <h4>夜晚</h4>
                            <em class="tip">如果有人提前离开了,座位是会被释放的</em>
                            <el-empty v-if="SeatArrange.NmSeatDtoList.length == 0" description="暂时没有数据"></el-empty>
                            <div class="seat-list">
                                <div class="seat-row" v-for="(row, rowIndex) in SeatArrange.NmSeatDtoList">
                                    <div class="seat-col" v-for="(col, colIndex) in row">
                                        <div v-if="col.Id" @click="ToAppoint(col, 3)">
                                            <img v-if="col.IsOccupy == false" :src="require('@/assets/seat.png')">
                                            <img v-else :src="require('@/assets/seatcked.png')">
                                            <div class="tit"><span>{{ col.No }}</span></div>
                                        </div>

                                    </div>

                                </div>

                            </div>
                        </template>
                    </el-tab-pane>
                    <el-tab-pane label="使用评价" name="使用评价">
                        <Pagination url="/AppointRecord/List" :where="{ RoomId: RoomId, AppointStatus: 3 }">
                            <template v-slot:content="{ data }">
                                <div class="comment-list">
                                    <div class="comment-item" v-for="(item, index) in data" :key="index">
                                        <div>
                                            <img class="head"
                                                :src="item.UserDto.ImageUrls ? item.UserDto.ImageUrls : require('@/assets/emptyheadimg.png')">
                                        </div>
                                        <div>
                                            <div>
                                                <span class="name">{{ item.UserDto.Name }}</span>
                                                <em class="seat">它当时的选座{{ item.SeatDto.No }}</em>
                                            </div>
                                            <div>
                                                <el-rate disabled v-model="item.CommentScore"></el-rate>
                                            </div>
                                            <div class="content">
                                                {{ item.Comment }}
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </template>
                        </Pagination>



                    </el-tab-pane>

                </el-tabs>
            </div>
        </div>

    </div>
</template>

<script>
import store from '@/store';
import { mapGetters } from 'vuex';
import Pagination from "@/components/Pagination/PaginationBox.vue"
export default {
    components: {
        Pagination
    },
    name: "Room",
    computed: {
        ...mapGetters(["UserInfo", 'Token', 'UserId'])
    },
    data() {
        return {
            activeName: "选座列表",
            RoomId: undefined,
            RoomDetail: null,
            DateList: [],
            SeatArrange: null,
            SelectDate: null,


        }
    },
    created() {
        this.RoomId = this.$route.query.RoomId
        this.GetRoomApi()
        this.GetSevenDaysApi()

    },
    methods: {
        //得到自习室详情接口
        async GetRoomApi() {
            let { Data } = await this.$Post("/Room/Get", { Id: this.RoomId })
            this.RoomDetail = Data;
        },
        //得到最近7天
        async GetSevenDaysApi() {
            let { Data } = await this.$Post("/Seat/GetSevenDays", {})
            this.DateList = Data;
            this.SelectDate = this.DateList[0]
            this.GetArrange();
        },
        //得到选中日期的座位信息 
        async GetArrange() {
            let { Data } = await this.$Post("/Seat/GetArrange", { RoomId: this.RoomId, SelectDate: this.SelectDate + " 00:00:00" })
            this.SeatArrange = Data;
        },
        //选中日期
        async DateHandleClick(e) {
            console.log(e)
            this.GetArrange();
        },
        //返回上一个页面
        goBack() {
            this.$router.go(-1)
        },
        //去预约
        async ToAppoint(col, type) {
            if (col.IsOccupy) {
                return;
            }
            if (!this.Token) {
                this.$message.warning("请先登录后,在操作");
                return;
            }
            let body = {
                RoomId: this.RoomId,
                UserId: this.UserId,
                SeatId: col.Id,
                AppointDateType: type,
                AppointDate: this.SelectDate + " 00:00:00"
            };

            let { Success } = await this.$Post("/AppointRecord/CheckIsAbleAppoint", body)

            let tick = new Date().getTime();
            localStorage.setItem(tick, JSON.stringify(body));

            if (Success) {
                this.$router.push({
                    path: "/Front/ToOrder",
                    query: {
                        tick: tick
                    }
                })
            }


        },

    }
}
</script>

<style scoped>
.content {
    font-family: Arial, sans-serif;
    font-size: 14px;
    line-height: 2.5;
    color: #333;
}

.seat-list {
    margin: 0 auto;
}

.seat-list .seat-row {
    display: flex;
    margin: 0 auto;
    width: fit-content;
    cursor: pointer;

}

.seat-list .seat-row .seat-col {
    margin-top: 20px;
    margin-left: 20px;
    width: 60px;
    height: 70px;
}

.seat-list .seat-row .seat-col img {
    width: 50px;
    height: 50px;
    pointer-events: none;
}

.seat-list .seat-row .seat-col .tit {
    font-weight: bold;
}



.comment-list {
    width: 100%;
}

.comment-list .comment-item {
    display: flex;
    margin-bottom: 15px;
    border-bottom: 1px dashed rgb(129, 127, 127);

}

.comment-list .comment-item .head {
    margin-right: 10px;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    overflow: hidden;
}

.comment-list .comment-item .name {
    font-weight: bolder;

}

.comment-list .comment-item .seat {
    font-size: 13px;
    margin-left: 10px;
    color: #333;


}

.tip {
    color: red;
    font-size: 12px;
}
</style>