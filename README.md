<div align="center">

# 🏫 小鹏自习室预约系统

**现代化智能学习空间管理平台**

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.1-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-2.6-4FC08D?logo=vuedotjs)](https://v2.vuejs.org/)
[![Element UI](https://img.shields.io/badge/Element%20UI-2.x-409EFF?logo=element)](https://element.eleme.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

[功能特性](#功能特性) · [技术栈](#技术栈) · [快速开始](#快速开始) · [项目结构](#项目结构) · [接口文档](#接口说明) · [部署指南](#部署指南)

</div>

---

## 功能特性

### 用户端
| 功能 | 说明 |
|------|------|
| 账号注册/登录 | 含数学题验证码防刷，BCrypt 密码加密 |
| 自习室浏览 | 查看自习室详情、封面、介绍 |
| 可视化选座 | 日期选择 + 上午/下午/夜晚三个时段，座位状态实时展示 |
| 预约确认 | 填写姓名手机号后提交预约，防重复提交 |
| 预约记录 | 查看历史预约，支持取消、完成后评价评分 |
| 积分系统 | 每日签到获积分，逾期扣分，可用积分兑换清零逾期次数 |
| 个人中心 | 修改头像、个人信息、密码 |

### 管理端
| 功能 | 说明 |
|------|------|
| 数据大屏 | 用户总数、预约总数、自习室数量、积分总量统计 |
| 用户管理 | 增删改查用户，支持 Excel 导出 |
| 自习室管理 | 维护自习室信息、封面、富文本介绍 |
| 座位管理 | 按行列批量生成座位，支持维修状态标记 |
| 预约记录管理 | 全量预约记录查询，支持多维度筛选 |
| 轮播图管理 | 首页轮播图增删改 |
| 积分管理 | 查看全量积分流水 |
| 数据统计 | 预约状态饼图、积分收支折线图、实时在场人数 |

---

## 技术栈

### 后端
| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.3.1 | 核心框架 |
| MyBatis-Plus | 3.5.7 | ORM 框架 |
| MySQL | 8.0+ | 数据库 |
| JWT (jjwt) | 0.12.x | 接口鉴权 |
| BCrypt | Spring Security Crypto | 密码加密 |
| EasyPoi | — | Excel 导入导出 |
| Lombok | — | 简化实体代码 |
| Spring Devtools | — | 热重载 |

### 前端
| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 2.6.14 | 核心框架 |
| Element UI | 2.15.x | UI 组件库 |
| Vue Router | 3.x | 路由管理 |
| Vuex | 3.x | 状态管理 |
| Axios | 0.18.x | HTTP 请求 |
| ECharts | 5.x | 数据图表 |
| vue-quill-editor | — | 富文本编辑器 |

---

## 快速开始

### 环境要求

- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 1. 克隆项目

```bash
git clone https://github.com/wdf4/studyroom.git
cd studyroom
```

### 2. 初始化数据库

```sql
-- 创建数据库
CREATE DATABASE studyroom DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

```bash
# 导入表结构和初始数据
mysql -u root -p studyroom < StudyRoom/studyroom.sql
```

### 3. 配置后端

编辑 `StudyRoom/StudyRoom.springboot/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    username: ${DB_USERNAME:root}      # 数据库账号（可用环境变量覆盖）
    password: ${DB_PASSWORD:123456}    # 数据库密码（可用环境变量覆盖）
    url: jdbc:mysql://localhost:3306/studyroom?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai

studyroom:
  jwt:
    secret: ${JWT_SECRET:StudyRoom@Dev2024}   # JWT 密钥（生产环境务必修改）
    expiry-days: 7
```

### 4. 启动后端

```bash
cd StudyRoom/StudyRoom.springboot
mvn spring-boot:run
# 或使用 Maven Wrapper
./mvnw spring-boot:run
```

> 启动成功：`Started StudyRoomApplication ... Tomcat started on port 8088`

### 5. 启动前端

```bash
cd StudyRoom/StudyRoom.elementui
npm install
npm run serve
```

> 启动成功：`App running at: http://localhost:8080`

### 6. 测试账号

| 角色 | 账号 | 密码 | 入口 |
|------|------|------|------|
| 管理员 | `admin` | `admin` | http://localhost:8080/#/Login |
| 普通用户 | `test0001` | `test0001` | http://localhost:8080/#/Login |

> 登录时角色选择对应选项（管理员 / 普通用户）。

---

## 项目结构

```
studyroom/
├── StudyRoom/
│   ├── StudyRoom.elementui/          # 前端项目（Vue 2 + Element UI）
│   │   ├── public/
│   │   └── src/
│   │       ├── assets/               # 静态资源（图片、字体）
│   │       ├── components/           # 全局公共组件
│   │       │   ├── Tables/           # PaginationTable 分页表格
│   │       │   ├── Upload/           # 图片/文件上传
│   │       │   ├── Select/           # 远程下拉选择
│   │       │   └── RichText/         # 富文本编辑器
│   │       ├── css/
│   │       │   └── modern-theme.css  # 全局 CSS 设计系统（变量/工具类）
│   │       ├── router/               # 路由配置
│   │       ├── store/                # Vuex 状态管理
│   │       ├── utils/                # 工具函数（request、cache、comm）
│   │       └── views/
│   │           ├── Admin/            # 管理后台页面（9个页面）
│   │           ├── Front/            # 用户前台页面（7个页面）
│   │           ├── Login.vue
│   │           └── Register.vue
│   │
│   ├── StudyRoom.springboot/         # 后端项目（Spring Boot 3）
│   │   └── src/main/java/com/studyroom/
│   │       ├── controller/           # REST 控制器（8个）
│   │       ├── service/              # 业务逻辑接口 + 实现
│   │       ├── mapper/               # MyBatis-Plus Mapper
│   │       ├── entity/               # 数据库实体（继承 BaseEntity）
│   │       ├── dto/                  # 数据传输对象
│   │       │   └── query/            # 分页查询输入参数
│   │       ├── enums/                # 枚举（角色/预约状态/时间段）
│   │       ├── job/                  # 定时任务（自动逾期/完成/积分）
│   │       └── tools/                # 工具（JWT、拦截器、全局异常）
│   │
│   └── studyroom.sql                 # 数据库建表脚本 + 初始数据
│
└── README.md
```

---

## 接口说明

后端基础路径：`http://localhost:8088`

所有需要鉴权的接口请在请求头携带：
```
Authorization: Bearer <token>
```

### 核心接口列表

| 模块 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户 | POST | `/User/SignIn` | 登录，返回 JWT Token |
| 用户 | POST | `/User/Register` | 注册新用户 |
| 用户 | POST | `/User/GetByToken` | 通过 Token 获取用户信息 |
| 用户 | POST | `/User/ChangePassword` | 修改密码 |
| 验证码 | POST | `/Captcha/Generate` | 生成数学题验证码 |
| 验证码 | POST | `/Captcha/Verify` | 验证答案 |
| 自习室 | POST | `/Room/List` | 分页查询自习室 |
| 座位 | POST | `/Seat/GetArrange` | 获取指定日期座位排布及占用状态 |
| 座位 | POST | `/Seat/GetSevenDays` | 获取最近可预约的 7 天日期 |
| 预约 | POST | `/AppointRecord/CheckIsAbleAppoint` | 检查是否可预约 |
| 预约 | POST | `/AppointRecord/ToOrder` | 提交预约 |
| 预约 | POST | `/AppointRecord/CancelAppoint` | 取消预约 |
| 预约 | POST | `/AppointRecord/Comment` | 评价预约记录 |
| 积分 | POST | `/Integral/GetMyIntegralData` | 获取我的积分汇总 |
| 积分 | POST | `/Integral/OverdueTimesClear` | 积分兑换清零逾期次数 |
| 文件 | POST | `/File/Upload` | 上传图片/文件，返回 URL |

---

## 部署指南

### 前端打包

```bash
cd StudyRoom/StudyRoom.elementui
npm run build
# 产物在 dist/ 目录，部署到 Nginx 的 root 即可
```

**Nginx 配置示例（处理 Vue Router history 模式）：**
```nginx
server {
    listen 80;
    root /var/www/studyroom/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://localhost:8088/;
    }
}
```

### 后端打包

```bash
cd StudyRoom/StudyRoom.springboot
mvn clean package -DskipTests
java -jar target/StudyRoom-*.jar
```

**使用环境变量覆盖配置（生产环境推荐）：**
```bash
export DB_USERNAME=prod_user
export DB_PASSWORD=strong_password
export JWT_SECRET=your-secret-key-at-least-32-chars
java -jar target/StudyRoom-*.jar
```

### Docker 一键启动（可选）

```bash
# 启动 MySQL
docker run -d --name studyroom-mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -e MYSQL_DATABASE=studyroom \
  -p 3306:3306 mysql:8.0

# 导入数据
docker exec -i studyroom-mysql mysql -uroot -p123456 studyroom < StudyRoom/studyroom.sql
```

---

## 定时任务

系统内置两个定时任务，随后端启动自动运行：

| 任务类 | 周期 | 作用 |
|--------|------|------|
| `AppointRecordJob` | 每10秒 | 检查逾期预约 → 累加逾期次数；检查已完成预约 → 更新状态 |
| `IntegralMockJob` | 每10秒 | 模拟积分数据（演示用） |

---

## 注意事项

1. **密码安全**：初始数据库中密码为明文，系统使用 BCrypt 加密。生产环境请通过注册或修改密码接口更新为加密密码
2. **JWT 密钥**：生产环境务必通过环境变量 `JWT_SECRET` 设置强密钥（建议32位以上随机字符串）
3. **文件上传**：上传的图片默认存储在服务器本地，生产环境建议改为对象存储（OSS/MinIO）
4. **首次运行**：建议先在管理后台添加自习室、座位和轮播图，再体验前台功能

---

## License

[MIT](LICENSE) © 2026 小鹏自习室预约系统
