# 系统开发文档

> 小鹏自习室预约系统 · 技术开发参考手册

---

## 目录

1. [系统架构](#1-系统架构)
2. [数据库设计](#2-数据库设计)
3. [后端开发说明](#3-后端开发说明)
4. [前端开发说明](#4-前端开发说明)
5. [安全机制](#5-安全机制)
6. [核心业务流程](#6-核心业务流程)
7. [定时任务](#7-定时任务)
8. [前后端通信规范](#8-前后端通信规范)
9. [CSS 设计系统](#9-css-设计系统)
10. [常见问题](#10-常见问题)

---

## 1. 系统架构

### 整体架构图

```
┌─────────────────────────────────────────────────────────┐
│                        浏览器                            │
│          Vue 2 + Element UI + Vuex + Vue Router          │
│    ┌──────────────────┐  ┌──────────────────────────┐   │
│    │    用户前台        │  │       管理后台             │   │
│    │  /Front/*        │  │      /Admin/*             │   │
│    └──────────────────┘  └──────────────────────────┘   │
└───────────────────────┬─────────────────────────────────┘
                        │ HTTP/Axios (JWT Bearer Token)
                        ▼
┌─────────────────────────────────────────────────────────┐
│                    Spring Boot 3.3.1                      │
│                                                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐  │
│  │ Interceptor │  │ Controller  │  │ GlobalException  │  │
│  │ (JWT验证)   │  │  (REST API) │  │    Handler       │  │
│  └──────┬──────┘  └──────┬──────┘  └─────────────────┘  │
│         │                │                                │
│         └────────────────▼                               │
│                  ┌─────────────┐                         │
│                  │   Service   │  ← @Transactional        │
│                  └──────┬──────┘                         │
│                         │                                │
│                  ┌──────▼──────┐                         │
│                  │   Mapper    │  ← MyBatis-Plus          │
│                  └──────┬──────┘                         │
└─────────────────────────┼───────────────────────────────┘
                          │ JDBC
                          ▼
                    ┌──────────┐
                    │  MySQL   │
                    │   8.0    │
                    └──────────┘
```

### 目录约定

| 层次 | 包路径 | 说明 |
|------|--------|------|
| 入口 | `com.studyroom` | 启动类、全局常量 |
| 控制器 | `com.studyroom.controller` | 接收 HTTP 请求，参数校验 |
| 服务接口 | `com.studyroom.service` | 业务逻辑接口定义 |
| 服务实现 | `com.studyroom.service.impl` | 业务逻辑实现，持有事务 |
| 数据访问 | `com.studyroom.mapper` | MyBatis-Plus Mapper |
| 实体 | `com.studyroom.entity` | 数据库表映射，继承 `BaseEntity` |
| DTO | `com.studyroom.dto` | 接口输入/输出数据结构 |
| 查询 | `com.studyroom.dto.query` | 分页查询条件封装 |
| 枚举 | `com.studyroom.enums` | 业务枚举值定义 |
| 定时任务 | `com.studyroom.job` | Spring Scheduled 定时任务 |
| 工具 | `com.studyroom.tools` | JWT、拦截器、全局异常、扩展方法 |

---

## 2. 数据库设计

### ER 关系

```
appuser ──── integral (1:N, UserId)
appuser ──── appointrecord (1:N, UserId)
room    ──── seat (1:N, RoomId)
room    ──── appointrecord (1:N, RoomId)
seat    ──── appointrecord (1:N, SeatId)
```

### 表结构详解

#### `appuser` — 用户表

| 字段 | 类型 | 说明 |
|------|------|------|
| Id | int PK | 用户主键（自增） |
| CreationTime | timestamp | 创建时间 |
| CreatorId | int | 创建人 ID |
| UserName | varchar(20) | 登录账号（唯一） |
| Password | varchar(255) | BCrypt 加密密码 |
| Name | varchar(20) | 用户姓名 |
| RoleType | int | 角色（1=管理员, 2=普通用户） |
| PhoneNumber | varchar(20) | 手机号码 |
| Email | varchar(20) | 邮箱 |
| Birth | datetime | 出生年月 |
| ImageUrls | varchar(256) | 头像 URL |
| OverdueTimes | int | 累计逾期次数（影响预约资格） |

#### `room` — 自习室表

| 字段 | 类型 | 说明 |
|------|------|------|
| Id | int PK | 自习室主键 |
| Name | varchar(128) | 自习室名称 |
| Cover | varchar(256) | 封面图片 URL |
| Address | varchar(256) | 地址 |
| Content | text | 富文本介绍 |
| EveryMonCancelCount | int | 每月允许取消次数 |

#### `seat` — 座位表

| 字段 | 类型 | 说明 |
|------|------|------|
| Id | int PK | 座位主键 |
| No | varchar(128) | 座位编号（如 A-01） |
| SRow | int | 所在行 |
| SCol | int | 所在列 |
| RoomId | int FK | 所属自习室 |
| IsMaintain | int | 是否维修中（0=正常, 1=维修） |

#### `appointrecord` — 预约记录表

| 字段 | 类型 | 说明 |
|------|------|------|
| Id | int PK | 预约主键 |
| No | varchar(128) | 预约流水号（UUID） |
| RoomId | int FK | 自习室 |
| SeatId | int FK | 座位 |
| UserId | int FK | 预约人 |
| Phone | varchar(128) | 联系手机号 |
| Name | varchar(128) | 预约人姓名 |
| AppointDate | datetime | 预约日期 |
| AppointDateType | int | 时段（1=上午, 2=下午, 3=夜晚） |
| BeginTime | datetime | 实际开始时间 |
| EndTime | datetime | 实际结束时间 |
| AppointStatus | int | 预约状态（见枚举） |
| CommentScore | double | 评分（1-5分） |
| SComment | varchar(128) | 评价内容 |

**预约状态枚举 `AppointStatusEnum`：**

| 值 | 含义 |
|----|------|
| 1 | 待使用（已预约，等待到时间入座） |
| 2 | 使用中（已到时间，正在使用） |
| 3 | 已完成（离场或时间到期） |
| 4 | 已取消（用户主动取消） |
| 5 | 已逾期（超时未使用，系统自动标记） |

#### `integral` — 积分流水表

| 字段 | 类型 | 说明 |
|------|------|------|
| Id | int PK | 积分主键 |
| UserId | int FK | 用户 |
| Title | varchar(128) | 积分说明（如"每日签到+10"） |
| IntegralValue | int | 积分变化量（正数=获得, 负数=消耗） |
| Source | varchar(128) | 来源标识 |
| RelativeCode | varchar(128) | 关联业务编码 |

#### `banner` — 轮播图表

| 字段 | 类型 | 说明 |
|------|------|------|
| Id | int PK | 轮播图主键 |
| Cover | varchar(128) | 图片 URL |
| Remark | varchar(128) | 备注描述 |

---

## 3. 后端开发说明

### 3.1 BaseEntity — 基础实体

所有实体均继承 `BaseEntity`，统一持有公共字段：

```java
@Data
public class BaseEntity {
    @TableId(type = IdType.AUTO)
    private Integer Id;
    private Date CreationTime;
    private Integer CreatorId;
}
```

### 3.2 分页查询模式

所有列表接口统一使用 `PagedResult<T>` 返回：

```java
// 查询输入（继承 BasePagedInput）
public class RoomPagedInput extends BasePagedInput {
    private String NameLike;      // 模糊搜索
    private String AddressLike;
}

// 返回结构
public class PagedResult<T> {
    private List<T> Items;
    private long Total;
    private int PageIndex;
    private int PageSize;
}
```

Service 实现中使用 MyBatis-Plus 条件构造器：

```java
@Override
public PagedResult<RoomDto> List(RoomPagedInput input) {
    Page<Room> page = new Page<>(input.getPageIndex(), input.getPageSize());
    LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
    if (StringUtils.hasText(input.getNameLike())) {
        wrapper.like(Room::getName, input.getNameLike());
    }
    Page<Room> roomPage = roomMapper.selectPage(page, wrapper);
    // DTO 转换...
    return PagedResult.of(roomPage, roomDtoList);
}
```

### 3.3 统一响应格式

```java
public class ResponseData<T> {
    private T Data;
    private String Msg;
    private Boolean Success;

    public static <T> ResponseData<T> GetResponseDataInstance(T data, String msg, Boolean success) {
        ResponseData<T> r = new ResponseData<>();
        r.Data = data; r.Msg = msg; r.Success = success;
        return r;
    }
}
```

成功响应示例：
```json
{
  "Data": "eyJhbGciOiJIUzI1...",
  "Msg": "登录成功",
  "Success": true
}
```

### 3.4 请求拦截器

`CurrentUserInterceptor` 在每次请求中：
1. 从 `Authorization` 头提取 JWT Token
2. 解析 Token 获取用户 ID 和角色
3. 存入 `BaseContext`（ThreadLocal）供后续使用
4. 白名单路径（`/User/SignIn`, `/User/Register`, `/Captcha/**` 等）不校验 Token

```java
// 在 Service 中获取当前用户
Integer userId = BaseContext.getCurrentUserDto().getUserId();
```

### 3.5 全局异常处理

`GlobalExceptionHandler` 统一捕获：

| 异常类型 | HTTP状态 | 处理方式 |
|----------|----------|----------|
| `MethodArgumentNotValidException` | 400 | 返回第一条字段校验错误信息 |
| `RuntimeException` | 500 | 返回 `Success:false` + 错误消息 |
| `Exception` | 500 | 记录 `log.error`，返回通用错误 |

### 3.6 文件上传

`FileController` 处理图片上传，文件保存到：
```
{project}/src/main/resources/static/upload/{filename}
```
返回可访问的相对 URL，前端拼接域名使用。

---

## 4. 前端开发说明

### 4.1 路由结构

```
/Login               登录页
/Register            注册页
/Front
  /Home              用户首页（自习室列表）
  /Room?RoomId=x     自习室详情 + 选座
  /ToOrder?tick=x    预约确认
  /AppointRecordList 我的预约记录
  /IntegralList      我的积分
  /UserPerson        个人信息
  /PasswordEdit      修改密码
/Admin
  /Home              管理控制台
  /UserList          用户管理
  /RoomList          自习室管理
  /SeatList          座位管理
  /AppointRecordList 预约记录管理
  /BannerList        轮播图管理
  /IntegralList      积分管理
  /AppointRoomAppointStatusData  预约状态分析
  /GetIntegralConsumeAndGainChart 积分分析
  /GetAppointRoomRealTimeData    实时在场统计
  /UserPerson        个人信息
  /PasswordEdit      修改密码
```

**路由守卫**：`router/index.js` 中，访问 `/Admin/**` 时检查 Token 和角色，未登录跳转 `/Login`，非管理员跳转首页。

### 4.2 Vuex Store

```javascript
state: {
  Token: '',          // JWT Token（持久化到 localStorage）
  UserInfo: null,     // 当前用户信息对象
}

actions: {
  Login(context, formData)   // 调用登录接口，存储 Token + 用户信息
  Logout(context)            // 清除 Token + 用户信息，跳转登录页
  GetInfo(context)           // 通过 Token 刷新用户信息
}
```

### 4.3 HTTP 请求封装

`utils/request.js` 封装 Axios：

- **请求拦截器**：自动在 Header 注入 `Authorization: Bearer <token>`
- **响应拦截器**：
  - `Success === false` 时弹出 `this.$message.error(Msg)`
  - HTTP 401 时自动跳转登录页
  - HTTP 500 时提示服务器错误

组件内使用：
```javascript
// 全局挂载的 $Post 方法
let { Data } = await this.$Post("/Room/List", { PageIndex: 1, PageSize: 10 })
```

### 4.4 公共组件

#### `PaginationTable` — 分页数据表格

最核心的公共组件，所有列表页都基于它构建。

```vue
<PaginationTable
  ref="PaginationTableId"
  url="/Room/List"          <!-- 接口地址 -->
  :column="dataColum"       <!-- 列定义 -->
  :where="searchParams"     <!-- 额外查询参数 -->
>
  <template v-slot:header>  <!-- 表格上方操作按钮区 -->
    <el-button @click="add">新增</el-button>
  </template>
  <template v-slot:Operate="scope">  <!-- 每行操作按钮 -->
    <el-button @click="edit(scope.row.Id)">修改</el-button>
  </template>
</PaginationTable>
```

列定义 `column` 支持的类型（`ColumnType` 枚举）：

| 类型常量 | 渲染方式 |
|----------|----------|
| `SHORTTEXT` | 纯文本截断显示 |
| `LONGTEXT` | 多行文本 |
| `IMAGE` | 缩略图 |
| `VIDEO` | 视频播放按钮 |
| `FILE` | 文件链接 |
| `RICHTEXT` | HTML 富文本弹窗查看 |
| `TAG` | 标签数组 |
| `DATE` | 格式化日期 |

#### `SigleSelect` — 远程下拉选择

```vue
<SigleSelect
  url="/Room/List"      <!-- 数据来源接口 -->
  columnName="Name"     <!-- 显示字段 -->
  columnValue="Id"      <!-- 值字段 -->
  v-model="formData.RoomId"
  :clearable="true"
  :where="{ Status: 1 }"  <!-- 附加查询条件 -->
/>
```

#### `UploadImages` — 图片上传

```vue
<UploadImages v-model="formData.ImageUrls" />
```

自动调用 `/File/Upload`，上传成功后回填 URL。

### 4.5 选座流程详解

1. 用户进入 `Front/Room.vue`，选择日期 Tab
2. 调用 `/Seat/GetArrange` 获取座位二维数组（`SeatArrange.AmSeatDtoList` / `PmSeatDtoList` / `NmSeatDtoList`）
3. 每个座位含 `IsOccupy` 字段，决定渲染颜色（蓝色=可选/灰色=已占）
4. 点击可用座位 → 调用 `/AppointRecord/CheckIsAbleAppoint` 校验是否可预约
5. 预约信息序列化存入 `localStorage`（key=当前时间戳）
6. 跳转至 `Front/ToOrder.vue?tick=<timestamp>`，从 localStorage 读取数据回显
7. 用户填写姓名手机号 → 提交 `/AppointRecord/ToOrder`
8. 成功后跳转至预约记录列表

---

## 5. 安全机制

### 5.1 JWT 认证流程

```
客户端                              服务端
  │  POST /User/SignIn              │
  │  {UserName, Password, RoleType} │
  ├─────────────────────────────────►
  │                                 │ BCrypt.matches(input, dbHash)
  │                                 │ JWTUtils.createToken(userId, roleType)
  │◄────────────────────────────────┤
  │  { Data: "eyJhbGciOiJ...",     │
  │    Success: true }              │
  │                                 │
  │  GET /User/GetByToken           │
  │  Authorization: Bearer eyJ...  │
  ├─────────────────────────────────►
  │                                 │ JWTUtils.parseToken(token)
  │                                 │ BaseContext.setCurrentUser(...)
  │◄────────────────────────────────┤
  │  { Data: { UserInfo... } }      │
```

### 5.2 密码加密

注册时：
```java
String encodedPwd = PASSWORD_ENCODER.encode(input.getPassword());
// 存入数据库 $2a$10$xxxxx...（60位 BCrypt hash）
```

登录时：
```java
boolean match = PASSWORD_ENCODER.matches(inputPassword, user.getPassword());
```

修改密码时：
```java
// 先验证原密码
if (!PASSWORD_ENCODER.matches(input.getOrginPassword(), user.getPassword())) {
    throw new RuntimeException("原始密码错误");
}
// 再 encode 新密码后保存
```

### 5.3 验证码机制

`CaptchaController` 生成数学题：
1. 随机生成两个1-9的整数，随机选加/减/乘运算
2. 计算正确答案，存入 `HttpSession`（有效期5分钟，一次性消费）
3. 返回题目字符串（如 `"3 + 5 = ?"`）给前端展示
4. 登录/注册提交前先调 `/Captcha/Verify` 校验答案
5. 答案错误则刷新题目，禁止继续提交

### 5.4 接口白名单

以下路径无需 JWT，在 `InterceptorConfig` 中配置：
```java
"/User/SignIn"
"/User/Register"
"/User/Export"
"/File/Upload"
"/Captcha/**"
"/Select/**"
```

---

## 6. 核心业务流程

### 6.1 预约流程状态机

```
             提交预约
               │
               ▼
          ┌─────────┐
          │ 待使用(1)│
          └────┬────┘
               │ 到达预约时间（定时任务）
               ▼
          ┌─────────┐
          │ 使用中(2)│◄──── 用户可在此阶段取消（扣减取消次数）
          └────┬────┘
               │
        ┌──────┴──────┐
        │             │
        ▼             ▼
  ┌──────────┐  ┌──────────┐
  │ 已完成(3)│  │ 已逾期(5)│
  └────┬─────┘  └──────────┘
       │
       ▼
  用户可发表评价（评分+评论）
```

### 6.2 积分系统规则

| 触发事件 | 积分变化 | 说明 |
|----------|----------|------|
| 每日首次登录（签到） | +10 | `SignGiveIntegral()` |
| 逾期次数清零 | -50 | `OverdueTimesClear()` |
| 模拟积分（演示） | 随机 | `IntegralMockJob` |

积分消耗：用户主动调用清零接口，扣除50积分，将 `OverdueTimes` 清零。

### 6.3 座位排布计算

`SeatService.GetArrange()` 逻辑：
1. 查出该自习室所有座位，按 `SRow`, `SCol` 排序
2. 构建 `maxRow × maxCol` 的二维数组（空位用空对象填充）
3. 查询该日期该时段所有 `AppointStatus IN (1,2)` 的预约
4. 将已被预约的 `SeatId` 标记 `IsOccupy = true`
5. 分别返回上午/下午/夜晚三个二维数组（`SeatArrange`）

---

## 7. 定时任务

### `AppointRecordJob`

每 10 秒执行两个检查：

**自动逾期检查：**
```
查询所有 AppointStatus=1（待使用）且 EndTime < 当前时间的记录
→ 更新状态为 5（逾期）
→ 对应用户 OverdueTimes + 1
```

**自动完成检查：**
```
查询所有 AppointStatus=2（使用中）且 EndTime < 当前时间的记录
→ 使用 updateBatchById 批量更新状态为 3（已完成）
```

### `IntegralMockJob`

每 10 秒为随机用户生成一条随机积分流水，仅用于演示效果，生产环境可禁用。

---

## 8. 前后端通信规范

### 请求格式

所有业务接口统一 **POST** + **JSON Body**：

```http
POST /Room/List HTTP/1.1
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

{
  "PageIndex": 1,
  "PageSize": 10,
  "NameLike": "图书馆"
}
```

### 响应格式

**业务响应（非分页）：**
```json
{
  "Data": { ... },
  "Msg": "操作成功",
  "Success": true
}
```

**分页响应：**
```json
{
  "Data": {
    "Items": [ {...}, {...} ],
    "Total": 42,
    "PageIndex": 1,
    "PageSize": 10
  },
  "Msg": null,
  "Success": true
}
```

**错误响应：**
```json
{
  "Data": null,
  "Msg": "原始密码错误",
  "Success": false
}
```

### 字段命名约定

后端统一使用 **UpperCamelCase（大驼峰）**，通过 Jackson 配置：
```yaml
spring:
  jackson:
    property-naming-strategy: UPPER_CAMEL_CASE
```

前端字段访问也使用大驼峰（如 `item.UserName`、`item.RoomId`）。

---

## 9. CSS 设计系统

所有页面通过 `src/css/modern-theme.css` 共享统一的设计令牌。

### CSS 变量（Design Tokens）

```css
:root {
  /* 主色调 */
  --primary:       #2563eb;   /* 主蓝色 */
  --primary-light: #3b82f6;   /* 浅蓝 */
  --primary-glow:  rgba(37, 99, 235, 0.15); /* 蓝色光晕 */

  /* 中性色阶 */
  --navy:       #0f172a;  /* 深导航背景 */
  --navy-mid:   #1e293b;  /* 主文本色 */
  --slate-700:  #334155;  /* 次级文本 */
  --slate-500:  #64748b;  /* 辅助文本 */
  --slate-400:  #94a3b8;  /* 占位符/禁用 */
  --slate-200:  #e2e8f0;  /* 分隔线 */
  --slate-100:  #f1f5f9;  /* 悬停背景 */
  --slate-50:   #f8fafc;  /* 页面背景 */
  --white:      #ffffff;

  /* 语义色 */
  --border-color: #e2e8f0;
  --success:      #16a34a;
  --danger:       #dc2626;
  --warning:      #d97706;

  /* 圆角 */
  --radius-sm: 4px;  --radius-md: 8px;
  --radius-lg: 12px; --radius-xl: 16px;

  /* 阴影 */
  --shadow-xs: 0 1px 2px rgba(0,0,0,.05);
  --shadow-sm: 0 2px 4px rgba(0,0,0,.06), 0 1px 2px rgba(0,0,0,.04);
  --shadow-md: 0 4px 6px -1px rgba(0,0,0,.08), 0 2px 4px -2px rgba(0,0,0,.04);
  --shadow-lg: 0 10px 15px -3px rgba(0,0,0,.08), 0 4px 6px -4px rgba(0,0,0,.04);
  --shadow-xl: 0 20px 25px -5px rgba(0,0,0,.08), 0 8px 10px -6px rgba(0,0,0,.04);
}
```

### 工具类

```css
/* 搜索卡片头部（所有列表页统一使用） */
.filter-header  { display: flex; align-items: center; justify-content: space-between; }
.filter-title   { font-size: 15px; font-weight: 600; color: var(--navy-mid); }
.filter-actions { display: flex; gap: 8px; }

/* 页面标题区 */
.page-header-bar { display: flex; align-items: center; justify-content: space-between; }
.page-title      { font-size: 20px; font-weight: 700; color: var(--navy-mid); }
```

### 新增页面规范

1. **Scoped 样式**：所有组件使用 `<style scoped>`，避免全局污染
2. **颜色禁止硬编码**：必须引用 CSS 变量（`var(--primary)` 而非 `#2563eb`）
3. **卡片结构**：统一使用 `.card` 全局类或 `el-card` + `filter-header` 搭配
4. **内联样式**：禁止在模板中写 `style="color:red"`，全部移入 scoped CSS

---

## 10. 常见问题

### Q: 登录后跳转不对（管理员跳到前台）？

确认登录时选择了正确角色。`RoleType=1` 为管理员（跳转 `/Admin`），`RoleType=2` 为普通用户（跳转 `/Front/Home`）。

### Q: 密码加密后旧账号无法登录？

初始 SQL 中密码为明文，BCrypt 加密后无法匹配。解决方法：
- 通过注册页创建新账号
- 或在数据库直接更新为 BCrypt hash（可用项目根目录的 `BCryptGen.java` 临时工具生成）

### Q: 前端请求后端 404/跨域？

检查 `utils/request.js` 中 `baseURL` 是否为 `http://localhost:8088`，确认后端已正常启动。

### Q: 上传图片后显示不出来？

确认后端 `application.yml` 中静态资源路径配置正确，并且 `InterceptorConfig` 中已排除 `/upload/**` 路径的 JWT 拦截。

### Q: 定时任务执行太频繁影响演示？

修改 `AppointRecordJob` 中 `@Scheduled(fixedDelay = 10000)` 的值（单位毫秒），或在 `application.yml` 中通过配置控制。

### Q: 如何添加新的列表页？

1. 后端：添加 `Entity` → `Mapper` → `Service/ServiceImpl` → `Controller` → `Dto/QueryInput`
2. 前端：复制任意现有列表页（如 `RoomList.vue`），修改 `url`、`dataColum`、`formData`
3. 路由：在 `router/index.js` 对应的 `adminRouters` 或 `frontRouters` 中注册新路由
4. 导航：在对应 Layout 的 `el-menu` 中添加菜单项

---

*文档最后更新：2026-03-30*
