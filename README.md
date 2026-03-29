# 自习室预约系统

一个基于 Spring Boot + Vue2 的自习室预约管理系统，支持用户预约座位、管理员管理自习室等功能。

## 技术栈

### 后端
- Spring Boot 3.3.1
- MyBatis-Plus
- MySQL
- JWT 认证

### 前端
- Vue 2
- Element UI
- Axios
- Vuex
- Vue Router

## 功能特性

- 用户登录/注册
- 座位预约
- 预约记录管理
- 积分系统
- 轮播图管理
- 自习室管理
- 座位管理
- 数据统计

## 环境要求

- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/wdf4/studyroom.git
cd studyroom
```

### 2. 配置数据库

1. 创建数据库：
```sql
CREATE DATABASE studyroom DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 导入数据：
```bash
mysql -u root -p studyroom < StudyRoom/studyroom.sql
```

3. 修改数据库配置（如需要）：
编辑 `StudyRoom/StudyRoom.springboot/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    username: root    # 数据库用户名
    password: 123456  # 数据库密码
    url: jdbc:mysql://localhost:3306/studyroom
```

### 3. 启动后端

```bash
cd StudyRoom/StudyRoom.springboot
./mvnw spring-boot:run
```

后端启动成功后会运行在 http://localhost:8088

### 4. 启动前端

```bash
cd StudyRoom/StudyRoom.elementui
npm install
npm run serve
```

前端启动成功后会运行在 http://localhost:8080

### 5. 登录账号

- 管理员账号：`admin` / `admin`
- 普通用户账号：`test0001` / `test0001`

## 项目结构

```
StudyRoom/
├── StudyRoom.elementui/     # 前端项目 (Vue2 + Element UI)
│   ├── src/
│   │   ├── api/            # API 接口
│   │   ├── components/     # 公共组件
│   │   ├── router/         # 路由配置
│   │   ├── store/          # 状态管理
│   │   ├── utils/          # 工具函数
│   │   └── views/          # 页面组件
│   │       ├── Admin/      # 后台管理页面
│   │       └── Front/      # 用户前端页面
│   └── package.json
│
├── StudyRoom.springboot/    # 后端项目 (Spring Boot)
│   ├── src/main/java/com/studyroom/
│   │   ├── controller/     # 控制器
│   │   ├── service/        # 业务逻辑
│   │   ├── mapper/         # 数据访问层
│   │   ├── entity/         # 实体类
│   │   ├── dto/            # 数据传输对象
│   │   └── tools/          # 工具类
│   └── pom.xml
│
└── studyroom.sql           # 数据库脚本
```

## 部署说明

### 前端打包

```bash
cd StudyRoom/StudyRoom.elementui
npm run build
```

打包后的文件在 `dist` 目录

### 后端打包

```bash
cd StudyRoom/StudyRoom.springboot
./mvnw clean package
```

打包后的 jar 文件在 `target` 目录

## 注意事项

1. 首次运行需要在后台管理系统中添加轮播图和自习室封面图片
2. 数据库配置中的用户名密码需要根据实际情况修改
3. 前端默认连接后端地址为 http://localhost:8088，如需修改请编辑 `.env.development`

## License

MIT
