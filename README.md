# 智聘通系统 (ZhiPinTong)

<p align="center">
  <a href="https://spring.io/projects/spring-boot">
    <img src="https://img.shields.io/badge/Spring%20Boot-2.7.x-brightgreen.svg" alt="Spring Boot">
  </a>
  <a href="https://www.mysql.com/">
    <img src="https://img.shields.io/badge/MySQL-8.0%2B-blue.svg" alt="MySQL">
  </a>
  <a href="https://redis.io/">
    <img src="https://img.shields.io/badge/Redis-6.x-red.svg" alt="Redis">
  </a>
  <a href="https://vuejs.org/">
    <img src="https://img.shields.io/badge/Vue.js-3.x-green.svg" alt="Vue.js">
  </a>
  <a href="https://www.docker.com/">
    <img src="https://img.shields.io/badge/Docker-Latest-blue.svg" alt="Docker">
  </a>
</p>

## 项目介绍
智聘通  是一个基于 Spring Boot 的现代化在线招聘系统，致力于为企业和求职者提供高效的招聘服务平台。系统集成了腾讯云对象存储、微信公众号等功能，提供完整的文件管理、用户交互等服务。

### 项目特点
![Java](https://img.shields.io/badge/Java-1.8%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.x-brightgreen)
![MyBatis](https://img.shields.io/badge/MyBatis-3.5.x-yellow)
![Vue.js](https://img.shields.io/badge/Vue.js-3.x-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-blue)
![Redis](https://img.shields.io/badge/Redis-6.x-red)
![Maven](https://img.shields.io/badge/Maven-3.6%2B-blue)
![Docker](https://img.shields.io/badge/Docker-Latest-blue)

### 项目状态
![Status](https://img.shields.io/badge/Status-Developing-green)
![License](https://img.shields.io/badge/License-MIT-blue)
![Release](https://img.shields.io/badge/Release-v1.0.0-blue)

## 核心特性
- 📝 完整的招聘流程管理
  - 职位发布与管理
  - 简历投递与筛选
  - 面试流程管理
  - 候选人评估系统
- 📱 微信公众号集成
  - 消息推送服务
  - 自动回复功能
  - 用户身份认证
  - 模板消息通知
- 🗃️ 文件存储与管理
  - 简历文件上传
  - 文件格式转换
  - 在线预览功能
  - 腾讯云 COS 存储
- 🔐 安全的用户认证
  - 多角色权限管理
  - JWT 令牌认证
  - 数据访问控制
  - 操作日志记录
- ⚡ 高性能的数据处理
  - 数据库优化
  - 缓存策略
  - 异步处理
- 📊 数据统计与分析
  - 招聘数据报表
  - 用户行为分析
  - 系统性能监控

## 技术栈
### 后端技术
- **核心框架**: 
  - Spring Boot 2.7.x
  - Spring Framework 5.3.x
  - Spring Security 5.7.x
- **ORM 框架**: 
  - MyBatis 3.5.x
  - MyBatis-Plus 3.5.x
- **数据库**: 
  - MySQL 8.0+
  - H2 Database (测试环境)
- **缓存**: 
  - Redis 6.x
  - Spring Cache
- **文件存储**: 
  - 腾讯云 COS
  - 本地文件系统
- **权限管理**: 
  - Spring Security
  - JWT Token
  - RBAC 权限模型
- **任务调度**: 
  - Spring Schedule
  - Quartz
- **API文档**: 
  - Swagger 3.0
  - OpenAPI
  - Knife4j
- **日志框架**:
  - Logback
  - Log4j2
- **工具库**:
  - Apache Commons
  - Hutool
  - Guava
- **测试框架**:
  - JUnit 5
  - Mockito
  - Spring Test

### 前端技术
- **构建工具**: 
  - Node.js 14+
  - npm/yarn
  - Webpack 5
  - Vite
- **前端框架**: 
  - Vue.js 3.x
  - Vue Router
  - Vuex/Pinia
- **UI 组件**: 
  - Element Plus
  - Ant Design Vue
  - TailwindCSS
- **HTTP 客户端**: 
  - Axios
  - Fetch API
- **工具库**:
  - Lodash
  - Moment.js/Day.js
  - ECharts

### 开发工具
- **IDE**: 
  - IntelliJ IDEA Ultimate
  - WebStorm
  - Visual Studio Code
- **构建工具**: 
  - Maven 3.6+
  - Gradle (可选)
- **版本控制**: 
  - Git
  - GitHub/GitLab
- **API 测试**: 
  - Postman
  - Insomnia
- **数据库工具**:
  - Navicat
  - DBeaver
- **其他工具**:
  - Docker
  - Jenkins
  - SonarQube

## 系统架构
- src/main/java/com/zhi/pin/
- ├── MainApplication.java # 项目启动入口
- ├── utils/
- │ └── CosUtils.java # 腾讯云 COS 工具类
- ├── wxmp/
- │ └── handler/
- │ └── EventHandler.java # 微信公众号事件处理
- └── mapper/ # MyBatis Mapper 接口


## 快速开始
1. 克隆项目

git clone [项目地址]


2. 配置文件

- 复制 `application.properties.example` 为 `application.properties`
- 填写您的配置信息

3. 运行项目

./mvnw spring-boot:run


## 注意事项
- 确保 `application.properties` ��经添加到 `.gitignore`
- 在提交代码前请删除所有敏感信息
- COS 配置信息请妥善保管，不要泄露

## 开发团队
[在这里添加开发团队信息]

## 许可证
[在这里添加许可证信息]