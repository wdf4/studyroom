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
                            <el-tab-pane :label="item" :name="item" v-for="item in DateList" :key="item"></el-tab-pane>

                        </el-tabs>

                        <template v-if="SeatArrange">
                            <h4>上午</h4>
                            <div class="legend-area">
                                <span class="legend-item"><div class="modern-seat seat-available legend-box"></div> 可预约空气位</span>
                                <span class="legend-item"><div class="modern-seat seat-occupied legend-box"></div> 已被预约沉浸位</span>
                            </div>
                            <em class="tip">如果有人提前离开了,座位是会被释放的</em>
                            <el-empty v-if="SeatArrange.AmSeatDtoList.length == 0" description="暂时没有数据"></el-empty>
                            <div class="seat-list">
                                <div class="seat-row" v-for="(row, rowIndex) in SeatArrange.AmSeatDtoList" :key="rowIndex">
                                    <div class="seat-col" v-for="(col, colIndex) in row" :key="colIndex">
                                        <div v-if="col.Id" @click="ToAppoint(col, 1)" class="modern-seat" :class="{ 'seat-occupied': col.IsOccupy, 'seat-available': !col.IsOccupy }">
                                            <div class="tit">{{ col.No }}</div>
                                        </div>

                                    </div>

                                </div>

                            </div>

                            <h4>下午</h4>
                            <em class="tip">如果有人提前离开了,座位是会被释放的</em>
                            <el-empty v-if="SeatArrange.PmSeatDtoList.length == 0" description="暂时没有数据"></el-empty>
                            <div class="seat-list">
                                <div class="seat-row" v-for="(row, rowIndex) in SeatArrange.PmSeatDtoList" :key="rowIndex">
                                    <div class="seat-col" v-for="(col, colIndex) in row" :key="colIndex">
                                        <div v-if="col.Id" @click="ToAppoint(col, 2)" class="modern-seat" :class="{ 'seat-occupied': col.IsOccupy, 'seat-available': !col.IsOccupy }">
                                            <div class="tit">{{ col.No }}</div>
                                        </div>

                                    </div>

                                </div>

                            </div>
                            <h4>夜晚</h4>
                            <em class="tip">如果有人提前离开了,座位是会被释放的</em>
                            <el-empty v-if="SeatArrange.NmSeatDtoList.length == 0" description="暂时没有数据"></el-empty>
                            <div class="seat-list">
                                <div class="seat-row" v-for="(row, rowIndex) in SeatArrange.NmSeatDtoList" :key="rowIndex">
                                    <div class="seat-col" v-for="(col, colIndex) in row" :key="colIndex">
                                        <div v-if="col.Id" @click="ToAppoint(col, 3)" class="modern-seat" :class="{ 'seat-occupied': col.IsOccupy, 'seat-available': !col.IsOccupy }">
                                            <div class="tit">{{ col.No }}</div>
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
        async DateHandleClick() {
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
    font-family: 'Inter', 'Microsoft YaHei', -apple-system, BlinkMacSystemFont, sans-serif;
    font-size: 14px;
    line-height: 2.5;
    color: var(--slate-700, #334155);
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
    margin-top: 15px;
    margin-left: 15px;
    width: 60px;
    height: 60px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.modern-seat {
    width: 44px;
    height: 48px;
    border-radius: 10px 10px 6px 6px;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 4px 6px rgba(0,0,0,0.05);
}

.modern-seat::after {
    content: '';
    position: absolute;
    bottom: 4px;
    left: 4px;
    right: 4px;
    height: 6px;
    border-radius: 4px;
    background: rgba(255,255,255,0.25);
}

.seat-available {
    background: linear-gradient(145deg, #3b82f6, #2563eb);
    color: white;
}

.seat-available:hover {
    transform: translateY(-6px) scale(1.08);
    box-shadow: 0 10px 20px rgba(37, 99, 235, 0.4);
}

.seat-occupied {
    background: linear-gradient(145deg, #f1f5f9, #e2e8f0);
    color: #94a3b8;
    cursor: not-allowed;
    border: 1px solid #cbd5e1;
    box-shadow: inset 0 2px 4px rgba(0,0,0,0.05);
}

.seat-occupied::after {
    background: rgba(0,0,0,0.05);
}

.modern-seat .tit {
    font-weight: 700;
    font-size: 13px;
    z-index: 10;
}

.legend-area {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 20px;
    margin-top: 10px;
    padding: 12px 18px;
    background: #f8fafc;
    border-radius: 8px;
}

.legend-item {
    display: flex;
    align-items: center;
    font-size: 13px;
    font-weight: 600;
    color: #475569;
}

.legend-box {
    transform: scale(0.65);
    margin-right: 8px;
}
.legend-box:hover {
    transform: scale(0.65) !important;
    box-shadow: none !important;
}



.comment-list {
    width: 100%;
}

.comment-list .comment-item {
    display: flex;
    margin-bottom: 15px;
    border-bottom: 1px solid var(--slate-200, #e2e8f0);

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
    color: var(--slate-500, #64748b);
}

.tip {
    color: #ef4444;
    font-size: 13px;
}
</style>