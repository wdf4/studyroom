<template>
    <div>



        <div class="echart card" id="GetAppointRoomRealTimeData"
            :style="{ float: 'left', width: '100%', height: '500px' }">
        </div>

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



        };
    },
    created() {

    },
    mounted() {

        this.GetAppointRoomRealTimeData();

    },
    methods: {
        //统计每个自习室实时在场的人数
        async GetAppointRoomRealTimeData() {
            let { Data } = await this.$Post('/AppointRecord/GetAppointRoomRealTimeData', this.searchForm);


            this.$nextTick(() => {

                let myChart = echarts.init(document.getElementById(`GetAppointRoomRealTimeData`));// 图标初始化

                var option = {
                    title: {
                        text: '统计自习室实时在场的人数'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            dataZoom: {
                                yAxisIndex: 'none'
                            },
                            dataView: { readOnly: false },

                            restore: {},
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        data: Data.map(x => x.Room.Name)
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [
                        {
                            data: Data.map(x => x.Value),
                            type: 'bar'
                        }
                    ]
                };

                myChart.setOption(option);// 渲染页面
                //随着屏幕大小调节图表
                window.addEventListener("resize", () => {
                    myChart.resize();
                });

            })
        },




    }
}       
</script>


<style scoped></style>