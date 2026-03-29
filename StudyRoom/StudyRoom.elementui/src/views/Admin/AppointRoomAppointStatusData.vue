<template>
    <div>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <el-button type="primary" size="mini" icon="el-icon-search" @click="SearchClick">搜 索</el-button>
                <el-button type="warning" size="mini" @click="ResetClick" icon="el-icon-s-promotion">重 置</el-button>
            </div>
            <div class="tb-body">
                <el-form ref="searchFormRef" :model="searchForm" :inline="true" label-width="120px" :style="`flex:1;`">
                    <el-form-item label="时间范围">
                        <el-date-picker v-model="searchForm.DateRange" type="datetimerange" start-placeholder="起始时间"
                            end-placeholder="结束时间" :default-time="['00:00:00']" value-format="yyyy-MM-dd HH:mm:ss">
                        </el-date-picker>
                    </el-form-item>
                </el-form>
            </div>

        </el-card>

        <el-row :gutter="10">
            <el-col :span="6" v-for="(item, index) in DataList" :ley="index">
                <el-card class="box-card margin-top-lg">
                    <div slot="header" class="clearfix">
                        <span>{{ item.Room.Name }}</span>

                    </div>
                    <div class="echart" :id="'echartDiv' + item.Room.Id"
                        :style="{ float: 'left', width: '100%', height: '500px' }">
                    </div>
                </el-card>
            </el-col>

        </el-row>



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
            searchForm: {
                DateRange: []
            },
            DataList: [],

        };
    },
    created() {

    },
    mounted() {
        this.GetAppointRoomAppointStatusData();


    },
    methods: {
        //统计早中晚每个自习室的使用率&空闲率
        async GetAppointRoomAppointStatusData() {
            let { Data } = await this.$Post('/AppointRecord/GetAppointRoomAppointStatusData', this.searchForm);
            this.DataList = Data;

            this.$nextTick(() => {
                for (let item of this.DataList) {
                    let myChart = echarts.init(document.getElementById(`echartDiv${item.Room.Id}`));// 图标初始化

                    var option = option = {
                        title: {

                            left: 'center'
                        },
                        tooltip: {
                            trigger: 'item'
                        },
                        legend: {
                            orient: 'vertical',
                            left: 'left'
                        },
                        series: [
                            {
                                name: '预约次数统计',
                                type: 'pie',
                                radius: '50%',
                                data: item.DataList.map(x => { return { "name": x.Name, 'value': x.Value } }),
                                emphasis: {
                                    itemStyle: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ]
                    };

                    myChart.setOption(option);// 渲染页面
                    //随着屏幕大小调节图表
                    window.addEventListener("resize", () => {
                        myChart.resize();
                    });
                }
            })
        },
        /**
       * 点击表格搜索按钮会触发
       */
        async SearchClick() {
            if (this.searchForm.DateRange.length > 0) {
                this.searchForm.BeginDate = this.searchForm.DateRange[0];
                this.searchForm.EndDate = this.searchForm.DateRange[1];
            }
            this.GetAppointRoomAppointStatusData();

        },
        /**
         * 点击清空表单会触发
         */
        async ResetClick() {
            this.searchForm = { DateRange: [] };
            this.GetAppointRoomAppointStatusData();
        },



    }
}       
</script>


<style scoped></style>