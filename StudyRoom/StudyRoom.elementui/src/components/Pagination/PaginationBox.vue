<template>
    <div style="margin: 15px 0px;">
        <div v-if="loading"><el-skeleton :rows="12" animated /></div>
        <div v-else>
            <el-empty v-if="options.length == 0" description="看官，数据正在维护中"></el-empty>
            <div v-else class="box">
                <slot name="content" :data="options"></slot>
            </div>
        </div>
        <div class="custom-pagination">
            <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="pagination.page" :page-sizes="pagination.pageSizes" :page-size="pagination.limit"
                layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
            </el-pagination>
        </div>
    </div>
</template>

<script>

import store from "@/store";
import { mapGetters } from "vuex";
export default {

    props: {



        url: {
            type: String, //默认的请求路径
            default: "",
        },
        where: {
            type: [Number, String, Object],
            default: ""
        },

    },
    watch: {

    },
    data() {
        return {
            loading: false, //是否正在获取数据中
            options: [],
            where_: {},
            pagination: {
                limit: 10,
                page: 1,
                total: 0,
                pageSizes: [10, 20, 50, 100],
            },
            selectOption: {},

        }
    },
    created() {
        this.where_ = this.$props.where;
        this.FetchDataList();
    },
    methods: {

        InitData() {
            this.pagination.limit = 10;
            this.pagination.page = 1;
            this.pagination.total = 0;

            this.where_ = {};
        },
        async Reload(where) {
            this.pagination.page = 1;
            this.pagination.total = 0;
            var searchWhere = {};
            if (where) {
                searchWhere = Object.assign(searchWhere, where);
            }
            this.FetchDataList(searchWhere);
        },
        async FetchDataList(searchWhere) {
            this.loading = true;
            let {
                Data: {
                    Items,
                    TotalCount
                }
            } = await this.$Post(this.$props.url, {
                Limit: this.pagination.limit,
                Size: this.pagination.limit,
                Page: this.pagination.page,
                ...this.where_,
                ...searchWhere
            });
            this.loading = false;
            this.options = Items;
            this.pagination.total = TotalCount;
        },

        handleCurrentChange(event) {
            this.pagination.page = event;
            this.FetchDataList();
        },
        handleSizeChange(event) {
            this.pagination.limit = event;
            this.pagination.page = 1;

            this.FetchDataList();
        },

    }
}

</script>
<style scoped>
.box {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    box-sizing: border-box;

}

.item {
    width: 100%;
    padding: 5px;
    box-sizing: border-box;

}

.content-cut {

    overflow: hidden;
    text-overflow: ellipsis;
    word-break: break-all;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 4;

    line-height: 1.2em;
}

.card {
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    height: 100%;
    border-radius: 10px;


}

.custom-pagination {
    margin-top: 20px;
    padding-bottom: 20px;
    display: flex;
    flex-direction: row-reverse;
}
</style>
