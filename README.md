# AI智能助手

一个现代化的AI智能助手应用，采用前后端分离架构，支持企业内部部署。

## 🚀 项目特性

- **现代化UI**: 基于Vue3 + TypeScript + Tailwind CSS，采用fin-tech风格设计
- **前后端分离**: 前端Vue3，后端Spring Boot
- **多模型支持**: 兼容OpenAI API的推理服务，支持多种大语言模型
- **企业级**: 支持离线环境下的企业内部部署
- **数据持久化**: 使用MySQL 8数据库存储聊天记录
- **响应式设计**: 适配各种屏幕尺寸

## 🏗️ 技术栈

### 前端 (frontend/)
- Vue 3.4+ with Composition API
- TypeScript 5.2+
- Tailwind CSS 3.3+
- Pinia (状态管理)
- Vue Router 4 (路由)
- Axios (HTTP客户端)
- Lucide Vue (图标库)

### 后端 (console/)
- Spring Boot 3.2
- Spring Data JPA
- MySQL 8
- Java 17
- Maven 3.6+

## 📋 环境要求

- Node.js 18+
- Java 17+
- Maven 3.6+
- MySQL 8.0+

## 🛠️ 快速开始

### 1. 数据库准备

```sql
CREATE DATABASE ai_assistant DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'ai_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON ai_assistant.* TO 'ai_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. 后端启动

```bash
cd console
mvn clean install
mvn spring-boot:run
```

后端服务默认启动在 `http://localhost:8080`

### 3. 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端应用默认启动在 `http://localhost:3000`

## ⚙️ 配置说明

### AI模型配置

在设置面板中可以配置：

- **API地址**: 兼容OpenAI API的推理服务地址
- **API密钥**: 访问推理服务的密钥
- **模型名称**: 要使用的模型名称
- **温度**: 控制回复的创造性 (0-2)
- **最大令牌数**: 单次回复的最大长度
- **流式输出**: 是否启用流式回复

### 支持的推理服务

- **OpenAI API**: https://api.openai.com/v1/chat/completions
- **Ollama**: http://localhost:11434/v1/chat/completions
- **vLLM**: http://localhost:8000/v1/chat/completions
- **Text Generation WebUI**: http://localhost:5000/v1/chat/completions
- **FastChat**: http://localhost:8000/v1/chat/completions

## 🎨 UI设计

采用现代化的fin-tech风格设计：

- **主色调**: 蓝色系 (#0ea5e9 - #0369a1)
- **设计语言**: 简洁、专业、现代
- **交互体验**: 流畅的动画和过渡效果
- **响应式**: 完美适配桌面和移动设备

## 📁 项目结构

```
├── frontend/                 # 前端应用
│   ├── src/
│   │   ├── components/       # Vue组件
│   │   ├── views/           # 页面视图
│   │   ├── stores/          # Pinia状态管理
│   │   ├── api/             # API接口
│   │   ├── types/           # TypeScript类型定义
│   │   └── assets/          # 静态资源
│   ├── package.json
│   └── vite.config.ts
├── console/                  # 后端应用
│   ├── src/main/java/
│   │   └── com/ai/assistant/
│   │       ├── controller/   # 控制器
│   │       ├── service/      # 业务服务
│   │       ├── entity/       # 实体类
│   │       ├── repository/   # 数据访问层
│   │       ├── dto/          # 数据传输对象
│   │       └── config/       # 配置类
│   ├── pom.xml
│   └── src/main/resources/
└── README.md
```

## 🔧 开发指南

### 添加新的AI模型支持

1. 在设置面板的模型选择中添加新选项
2. 如果API格式不同，在 `AIService` 中添加适配逻辑

### 自定义UI主题

1. 修改 `tailwind.config.js` 中的颜色配置
2. 更新 `src/assets/css/index.css` 中的CSS变量

### 扩展功能

- 添加插件系统支持
- 集成语音识别和合成
- 支持文件上传和处理
- 添加多用户支持

## 🐛 常见问题

### 1. 数据库连接失败
检查 `application.yml` 中的数据库配置是否正确。

### 2. AI服务连接失败
确保推理服务正在运行，并检查API地址和密钥配置。

### 3. 前端构建失败
删除 `node_modules` 和 `package-lock.json`，重新安装依赖。

## 📄 许可证

MIT License

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！
