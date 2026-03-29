<template>
    <el-select style="width: 100%;" :disabled="disabled" v-model="selectValue" filterable placeholder="请选择"
        :filterable="true" :loading="loading" :clearable="true" :remote="true" @change="Change">
        <el-option v-for="item in options" :key="item.value" :label="item.name" :value="item.value">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 14px">{{ item.label }}</span>
        </el-option>
    </el-select>
</template>

<script>
import store from "@/store";
export default {

    props: {

        value: {
            type: [Number, String],
            default: ''
        },
        url: {
            type: String, //默认的请求路径
            default: "",
        },
        columnName: {
            type: String, //需要绑定Name的值
            default: "",
        },
        columnValue: {
            type: String, //需要绑定Value的值
            default: "",
        },
        columnLabel: {
            type: String, //需要绑定Label的值
            default: "",
        },
        where: {},
        filterValue: {
            type: Array, //需要绑定Value的值
            default: () => []
        },
        disabled: {
            type: Boolean,
            default: false
        },


    },
    watch: {
        "value": {
            immediate: true, //该回调将会在侦听开始之后被立即调用
            handler: function (n, o) {
                if (n?.toString()?.length > 0) {
                    this.selectValue = n.toString();
                }
                else {
                    this.selectValue = n;
                }
            }
        }
    },
    data() {
        return {
            loading: false, //是否正在获取数据中
            options: [],
            where_: {},//默认
            searchWhere: {},//搜索带过来的条件
            selectValue: undefined,

        }
    },
    created() {


        if (this.$props.value?.toString()?.length > 0) {
            this.selectValue = this.$props.value.toString();
        }
        this.where_ = this.$props.where;
        this.FetchDataList();

    },
    methods: {

        async Reload(where) {

            var searchWhere = {};
            if (where) {
                searchWhere = Object.assign(searchWhere, where);
            }
            this.searchWhere = searchWhere;
            this.FetchDataList(searchWhere);
        },
        async FetchDataList(searchWhere = {}) {
            this.loading = true;
            let {
                Data: {
                    Items,

                }
            } = await this.$Post(this.$props.url, {
                Limit: 999,
                ...this.where_,
                ...searchWhere
            });
            this.loading = false;
            let dataList = [];
            Items.forEach((item, index) => {
                if (this.filterValue.find(x => x == item[`${this.columnValue}`]?.toString()) == null) {
                    dataList.push({
                        name: item[`${this.columnName}`],
                        value: item[`${this.columnValue}`]?.toString(),
                        label: item[`${this.columnLabel}`]?.toString(),
                    });

                }

            });

            if (this.filterValue.filter(x => x.toString() == this.selectValue).length > 0) {
                this.selectValue = undefined;
            }

            this.options = dataList;
        },

        Change(value) {
            this.$emit('input', value);
            this.$emit("Change", value);
        },

    }
}

</script>
