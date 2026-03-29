<template>
    <div class="table-box" v-if="columnList.length > 0">

        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <template>
                    <slot name="header"></slot>
                </template>
            </div>
            <div class="tb-body">
                <el-table :show-summary="false" v-loading="listLoading" :data="datalist" @row-click="rowClick"
                    element-loading-text="加载数据中~~~" fit :stripe="true" highlight-current-row
                    @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="55"> </el-table-column>

                    <template v-for="(item, idx) in columnList">
                        <el-table-column v-if="item.type == '1'" v-bind="item" :key="'PHONE' + idx" align="center"
                            :sortable="item.sort" :prop="item.key" :label="item.title" :width="item.width || 'auto'">
                            <template slot-scope="{ row }">
                                <slot :name="item.key" :row="row"> {{ row[`${item.key}`] }}</slot>
                            </template>
                        </el-table-column>
                        <el-table-column v-else-if="item.type == '2'" v-bind="item" align="center" :key="'DATE' + idx"
                            :prop="item.key" :label="item.title" :sortable="item.sort" :width="item.width || 'auto'">
                            <template slot-scope="{ row }">
                                <slot :name="item.key" :row="row">
                                    {{ row[`${item.key}`] | YMDFormat }}</slot>
                            </template>
                        </el-table-column>
                        <el-table-column v-else-if="item.type == 4" v-bind="item" align="center"
                            :key="'USERDEFINED' + idx" :width="item.width || 100" :prop="item.key" :label="item.title"
                            fixed="right">
                            <template slot-scope="{ row }">
                                <slot :name="item.key" :row="row"></slot>
                            </template>
                        </el-table-column>
                        <el-table-column v-else-if="item.type == 8" v-bind="item" align="center"
                            :key="'SHORTTEXT' + idx" :prop="item.key" :label="item.title" :width="item.width || 'auto'">
                            <template slot-scope="{ row }">
                                <el-popover trigger="hover" placement="top">
                                    <p>{{ row[`${item.key}`] }}</p>
                                    <div slot="reference" class="text-cut">
                                        {{ row[`${item.key}`] }}
                                    </div>
                                </el-popover>
                            </template>
                        </el-table-column>
                        <el-table-column v-else-if="item.type == 9" v-bind="item" align="center" :key="item.type"
                            :prop="item.key" :label="item.title" :width="item.width || 'auto'">
                            <template slot-scope="{ row }">
                                <template v-for="(tagItem, tagIndex) in row[item.key]">
                                    <el-tag type="primary">{{ tagItem }}</el-tag>
                                </template>
                            </template>
                        </el-table-column>
                        <el-table-column v-else-if="item.type == 10" v-bind="item" align="center" :key="'POPOVER' + idx"
                            :prop="item.key" :label="item.title" :width="item.width || 'auto'">
                            <template slot-scope="{ row }">
                                <el-popover trigger="hover" placement="top">
                                    <p>{{ row[`${item.key}`] }}</p>
                                    <div slot="reference" class="name-wrapper">
                                        <el-tag size="medium">{{ row[`${item.key}`] }}</el-tag>
                                    </div>
                                </el-popover>
                            </template>
                        </el-table-column>
                        <template v-else-if="item.type == 11">
                        </template>
                        <el-table-column v-else-if="item.type == 12" v-bind="item" :key="'IMAGES' + idx"
                            :prop="item.key" :label="item.title" :width="item.width || 'auto'" align="center">
                            <template slot-scope="{ row }">

                                <el-image v-for="(image, idx) in row[`${item.key}`]" style="width: 50px; height: 40px"
                                    :src="image" :preview-src-list="row[`${item.key}`]" fit="scale-down">
                                    <div slot="error" class="image-slot">
                                        <i class="el-icon-picture-outline"></i>
                                    </div>
                                </el-image>

                            </template>
                        </el-table-column>
                        <el-table-column v-else-if="item.type == 13" v-bind="item" align="center" :key="'VIDEO' + idx"
                            :width="item.width || 'auto'" :prop="item.key" :label="item.title">
                            <template slot-scope="{ row }">
                                <VideoButton :url="row[`${item.key}`]"></VideoButton>
                            </template>
                        </el-table-column>
                        <el-table-column v-else-if="item.type == 14" v-bind="item" align="center" :key="'CUSTOM' + idx"
                            :width="item.width || 'auto'" :prop="item.key" :label="item.title">
                            <template slot-scope="{ row }">
                                <slot :name="item.key" :row="row"></slot>
                            </template>
                        </el-table-column>
                        <el-table-column v-else-if="item.type == 15" v-bind="item" align="center" :key="'AUDIO' + idx"
                            :width="item.width || 'auto'" :prop="item.key" :label="item.title">
                            <template slot-scope="{ row }">
                                <VideoButton :url="row[`${item.key}`]"></VideoButton>
                            </template>
                        </el-table-column>

                        <el-table-column v-else-if="item.type == 16" v-bind="item" align="center" :key="'LINK' + idx"
                            :width="item.width || 'auto'" :prop="item.key" :label="item.title">
                            <template slot-scope="{ row }">
                                <a v-if="row[`${item.key}`]" target="_blank"
                                    style="color:blue;text-decoration:underline" :href="row[`${item.key}`]">超链接</a>
                            </template>
                        </el-table-column>
                        <el-table-column v-else-if="item.type == 17" v-bind="item" :key="'IMAGE' + idx" :prop="item.key"
                            :label="item.title" :width="item.width || 'auto'" align="center">
                            <template slot-scope="{ row }">

                                <el-image style="width: 50px; height: 40px" :src="row[`${item.key}`]"
                                    :preview-src-list="[row[`${item.key}`]]" fit="scale-down">
                                    <div slot="error" class="image-slot">
                                        <i class="el-icon-picture-outline"></i>
                                    </div>
                                </el-image>

                            </template>
                        </el-table-column>
                        <el-table-column v-else-if="item.type == 18" v-bind="item" :key="'RICHTEXT' + idx"
                            :prop="item.key" :label="item.title" :width="item.width || 'auto'" align="center">
                            <template slot-scope="{ row }">
                                <RichButton :content="row[`${item.key}`]"></RichButton>
                            </template>
                        </el-table-column>
                        <el-table-column v-else-if="item.type == 19" v-bind="item" :key="'FILESLINK' + idx"
                            :prop="item.key" :label="item.title" :width="item.width || 'auto'" align="center">
                            <template slot-scope="{ row }">
                                <FilesLinkButton :url="row[`${item.key}`]"></FilesLinkButton>
                            </template>
                        </el-table-column>
                        <el-table-column v-else-if="item.type == 21" v-bind="item" :key="'JUDGMENTTAG' + idx"
                            :prop="item.key" :label="item.title" :width="item.width || 'auto'" align="center">
                            <template slot-scope="{ row }">
                                <el-tag v-if="row[`${item.key}`] == true" type="success">是</el-tag>
                                <el-tag v-else-if="row[`${item.key}`] == false" type="info">否</el-tag>

                            </template>
                        </el-table-column>

                        <el-table-column v-else v-bind="item" align="center" :key="idx" :prop="item.key"
                            :label="item.title">
                            <template slot-scope="{ row }">
                                <span>{{ row[`${item.key}`] }}</span>
                            </template>
                        </el-table-column>
                    </template>
                </el-table>
                <div class="custom-pagination">
                    <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
                        :current-page="pagination.page" :page-sizes="pagination.pageSizes" :page-size="pagination.limit"
                        layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
                    </el-pagination>
                </div>
            </div>
        </el-card>


    </div>
</template>

<script>
import { ReplaceImageHttp } from "@/utils/comm";
import store from "@/store";
import VideoButton from "./VideoButton.vue"
import RichButton from "./RichButton.vue"
import AudioButton from "./AudioButton.vue"
import FilesLinkButton from "./FilesLinkButton.vue"



export default {
    components: {
        VideoButton,
        RichButton,
        AudioButton,
        FilesLinkButton
    },
    props: {
        url: {
            type: String,//默认的请求路径
            default: "",
        },
        column: {//默认的列属性
            type: Array,
        },

        where: {//默认的过滤条件
            type: Object,
            default: () => { }
        },

    },

    data() {
        return {
            selectRow: [],
            datalist: [],
            listLoading: true,
            columnList: [],
            where_: {},//默认的条件
            searchWhere: {},//搜索带过来的条件
            pagination: {
                limit: 10,
                page: 1,
                total: 0,
                pageSizes: [10, 20, 50, 100, 200, 500, 1000],
            },
        };
    },

    created() {
        this.where_ = this.$props.where;
        this.columnList = this.column.filter((x) => !x.hidden);
        this.fetchDataList();

    },
    methods: {

        //分页当前页改变
        handleCurrentChange(event) {
            this.pagination.page = event;
            this.fetchDataList(this.searchWhere);
        },
        //分页大小改变
        handleSizeChange(event) {
            this.pagination.limit = event;
            this.pagination.page = 1;

            this.fetchDataList(this.searchWhere);
        },
        //重新加载
        async Reload(where) {
            this.pagination.page = 1;
            this.pagination.total = 0;
            var searchWhere = {};
            if (where) {
                searchWhere = Object.assign(searchWhere, where);
            }
            this.searchWhere = searchWhere;
            this.fetchDataList(searchWhere);
        },

        //查询数据
        async fetchDataList(searchWhere = {}) {
            this.listLoading = true;

            let { Data: { Items, TotalCount } } = await this.$Post(this.$props.url, {
                Limit: this.pagination.limit,
                Page: this.pagination.page,
                ...this.where_,
                ...searchWhere
            });
            let dataList = [];
            Items.forEach((item, index) => {
                let dataItem = {};
                this.$props.column.forEach((element) => {

                    if (element.template) {
                        dataItem[`${element.key}`] = element.template(item, index);
                    }
                    else {
                        dataItem[`${element.key}`] = this.GetObjectValue(item, element.key);
                    }

                    if (element.type == store.getters.ColumnType.IMAGES) {
                        if (element.template) {
                            dataItem[`${element.key}`] = ReplaceImageHttp(dataItem[`${element.key}`]?.split(","));
                        }
                        else {
                            dataItem[`${element.key}`] = ReplaceImageHttp(this.GetObjectValue(item, element.key)?.split(","));
                        }
                    }
                });
                dataItem.OrginValue = item;
                dataList.push(dataItem);
            });
            this.datalist = dataList;
            this.selectRow = [];
            this.pagination.total = parseInt(TotalCount);
            this.listLoading = false;
            this.$emit("fetchDataSuccess", { items: Items, totalCount: TotalCount })
        },
        //点击行
        rowClick(row) {
            this.$emit("rowClick", { selectRow: row })
        },
        //选择行改变回调事件
        async handleSelectionChange(rows) {

            this.selectRow = rows;
            this.$emit("rowSelectionChange", { selectRow: rows })
        },
        //得到选中的行
        GetSelectionRow() {
            return this.selectRow;
        },
        /**
         * 获取对象中指定key的值
         */
        GetObjectValue(obj, name) {

            if (!name) {
                return undefined;
            }
            if (!obj) { return undefined };
            if (name.indexOf(".") != -1) {
                var array = name.split(".");

                var value = obj;
                array.forEach((item) => {
                    if (value != null) {
                        value = value[`${item}`];
                    }
                })
                return value;
            }
            else {
                return obj[`${name}`];
            }
        }
    },
};
</script>

<style scoped>
.custom-pagination {
    margin-top: 20px;
    display: flex;
    flex-direction: row-reverse;
}



.text-cut {

    white-space: nowrap;
    overflow: hidden;
    text-align: center;
    text-overflow: ellipsis;
}

.table-box {
    background-color: white;

    margin-top: 20px;

}

/* .table-box {
    .table-search {
        display: flex;
        margin-bottom: 10px;

        .search-operation {
            margin-left: 15px;
            white-space: nowrap;

            .search-isOpen {
                margin-left: 20px;
            }
        }
    }
} */
</style>