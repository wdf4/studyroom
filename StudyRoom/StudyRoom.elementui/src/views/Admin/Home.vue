<template>
    <div>
        <el-card class="box-card margin-top-lg" v-loading="loadingDataCollect">
            <div slot="header" class="clearfix">
                <span>数据统计</span>

            </div>
            <div class="board-list " v-if="DataCollect != null">
                <div class="board-item">
                    <div class="tit">自习室个数</div>
                    <div class="num">{{ DataCollect.RoomCount }}</div>
                </div>
                <div class="board-item">
                    <div class="tit">总座位数</div>
                    <div class="num">{{ DataCollect.SeatCount }}</div>
                </div>
                <div class="board-item">
                    <div class="tit">总预约人次</div>
                    <div class="num">{{ DataCollect.AppointCount }}</div>
                </div>
                <div class="board-item">
                    <div class="tit">待使用次数</div>
                    <div class="num">{{ DataCollect.WaitAppointCount }}</div>
                </div>
                <div class="board-item">
                    <div class="tit">逾期人次</div>
                    <div class="num">{{ DataCollect.OverdueAppointCount }}</div>
                </div>


                <div class="board-item">
                    <div class="tit">总剩余积分</div>
                    <div class="num">{{ DataCollect.TotalIntegral }}</div>
                </div>
            </div>
            <el-empty v-if="DataCollect == null && !loadingDataCollect" description="暂无数据"></el-empty>
        </el-card>


        <el-card class="box-card margin-top-lg" v-loading="loadingChart">
            <div slot="header" class="clearfix">
                <span>今日每个自习室早中晚的使用率(%)</span>

            </div>
            <div class="echart" id="echartDiv" :style="{ float: 'left', width: '100%', height: '500px' }">
            </div>
        </el-card>

        <el-card class="box-card margin-top-lg">
            <div slot="header" class="clearfix">
                <span>欢迎您的使用</span>

            </div>
            <el-row :gutter="10">
                <el-col :span="12">
                    <el-calendar></el-calendar>
                </el-col>
                <el-col :span="12">
                    <el-descriptions title="系统介绍" direction="horizontal" :column="1" border>
                        <el-descriptions-item label="系统名称">基于SpringBoot+Vue的自习室管理系统设计与实现</el-descriptions-item>
                        <el-descriptions-item label="后端技术">SpringBoot3.3</el-descriptions-item>
                        <el-descriptions-item label="前端">Vue2</el-descriptions-item>
                        <el-descriptions-item label="数据库">Mysql</el-descriptions-item>
                    </el-descriptions>
                </el-col>
            </el-row>

        </el-card>



    </div>
</template>

<script>
import * as echarts from "echarts";
export default {
    name: 'Home',
    props: {

    },
    data() {
        return {
            DataCollect: null,
            loadingDataCollect: false,
            loadingChart: false,
        };
    },
    created() {

    },
    mounted() {
        this.GetAppointRoomUseRate();
        this.GetDataCollect();
    },
    methods: {
        //统计早中晚每个自习室的使用率&空闲率
        async GetAppointRoomUseRate() {
            this.loadingChart = true;
            let Data;
            try {
                let res = await this.$Post('/AppointRecord/GetAppointRoomUseRate', {});
                Data = res.Data;
            } finally {
                this.loadingChart = false;
            }

            let myChart = echarts.init(document.getElementById("echartDiv"));// 图标初始化

            const labelOption = {
                show: true,
                position: 'insideBottom',
                distance: 15,
                align: 'left',
                verticalAlign: 'middle',
                rotate: 90,
                formatter: '{c}  {name|{a}}',
                fontSize: 16,
                rich: {
                    name: {}
                }
            };
            let option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },
                legend: {
                    data: ['上午', '下午', '夜晚']
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    left: 'right',
                    top: 'center',
                    feature: {
                        mark: { show: true },
                        dataView: { show: true, readOnly: false },
                        magicType: { show: true, type: ['line', 'bar', 'stack'] },
                        restore: { show: true },
                        saveAsImage: { show: true }
                    }
                },
                xAxis: [
                    {
                        type: 'category',
                        axisTick: { show: false },
                        data: Data.map(x => x.Room.Name)
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value} %' // 在这里设置 y 轴标签的格式，添加单位
                        }
                    }
                ],
                series: [
                    {
                        name: '上午',
                        type: 'bar',
                        itemStyle: { borderRadius: [4, 4, 0, 0] },
                        barGap: 0,
                        label: labelOption,
                        emphasis: { focus: 'series' },
                        data: Data.map(x => x.AmUseRate)
                    },
                    {
                        name: '下午',
                        type: 'bar',
                        itemStyle: { borderRadius: [4, 4, 0, 0] },
                        label: labelOption,
                        emphasis: { focus: 'series' },
                        data: Data.map(x => x.PmUseRate)
                    },
                    {
                        name: '夜晚',
                        type: 'bar',
                        itemStyle: { borderRadius: [4, 4, 0, 0] },
                        label: labelOption,
                        emphasis: { focus: 'series' },
                        data: Data.map(x => x.NmUseRate)
                    },

                ]
            };

            myChart.setOption(option);// 渲染页面
            //随着屏幕大小调节图表
            window.addEventListener("resize", () => {
                myChart.resize();
            });
        },
        //统计各类数据到看板
        async GetDataCollect() {
            this.loadingDataCollect = true;
            try {
                let { Data } = await this.$Post('/AppointRecord/GetDataCollect', {});
                this.DataCollect = Data;
            } finally {
                this.loadingDataCollect = false;
            }
        }
    }
}       
</script>


<style scoped>
.board-list {
    display: flex;


}

.board-list .board-item {
    text-align: center;
    padding: 24px 20px;
    border-radius: 12px;
    color: white;
    margin-right: 20px;
    width: 180px;
    box-shadow: var(--shadow-md);
    transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.board-list .board-item:hover {
    transform: translateY(-4px);
    box-shadow: var(--shadow-lg);
}

.board-list .board-item:nth-child(1) { background: linear-gradient(135deg, #3b82f6, #2563eb); }
.board-list .board-item:nth-child(2) { background: linear-gradient(135deg, #8b5cf6, #7c3aed); }
.board-list .board-item:nth-child(3) { background: linear-gradient(135deg, #f59e0b, #d97706); }
.board-list .board-item:nth-child(4) { background: linear-gradient(135deg, #10b981, #059669); }
.board-list .board-item:nth-child(5) { background: linear-gradient(135deg, #ec4899, #db2777); }
.board-list .board-item:nth-child(6) { background: linear-gradient(135deg, #f43f5e, #e11d48); }
.board-list .board-item:nth-child(7) { background: linear-gradient(135deg, #6366f1, #4f46e5); }

.board-list .board-item .num {
    margin-top: 10px;
    font-weight: bolder;
}
</style>