#!/bin/bash

# AI智能助手启动脚本

echo "🚀 启动AI智能助手..."

# 检查环境
check_requirements() {
    echo "📋 检查环境要求..."
    
    # 检查Node.js
    if ! command -v node &> /dev/null; then
        echo "❌ Node.js 未安装，请安装 Node.js 18+"
        exit 1
    fi
    
    # 检查Java
    if ! command -v java &> /dev/null; then
        echo "❌ Java 未安装，请安装 Java 17+"
        exit 1
    fi
    
    # 检查Maven
    if ! command -v mvn &> /dev/null; then
        echo "❌ Maven 未安装，请安装 Maven 3.6+"
        exit 1
    fi
    
    echo "✅ 环境检查通过"
}

# 启动后端服务
start_backend() {
    echo "🔧 启动后端服务..."
    cd console
    
    # 构建项目
    echo "📦 构建后端项目..."
    mvn clean install -DskipTests
    
    if [ $? -ne 0 ]; then
        echo "❌ 后端构建失败"
        exit 1
    fi
    
    # 启动后端
    echo "🚀 启动后端服务 (端口: 8080)..."
    mvn spring-boot:run &
    BACKEND_PID=$!
    
    echo "后端进程ID: $BACKEND_PID"
    cd ..
}

# 启动前端服务
start_frontend() {
    echo "🎨 启动前端服务..."
    cd frontend
    
    # 安装依赖
    if [ ! -d "node_modules" ]; then
        echo "📦 安装前端依赖..."
        npm install
    fi
    
    # 启动前端
    echo "🚀 启动前端服务 (端口: 3000)..."
    npm run dev &
    FRONTEND_PID=$!
    
    echo "前端进程ID: $FRONTEND_PID"
    cd ..
}

# 等待服务启动
wait_for_services() {
    echo "⏳ 等待服务启动..."
    sleep 10
    
    # 检查后端
    if curl -s http://localhost:8080/api/chat/health > /dev/null; then
        echo "✅ 后端服务启动成功"
    else
        echo "⚠️  后端服务可能未完全启动，请稍等片刻"
    fi
    
    echo "🌐 应用访问地址:"
    echo "   前端: http://localhost:3000"
    echo "   后端: http://localhost:8080/api"
}

# 停止服务函数
cleanup() {
    echo ""
    echo "🛑 正在停止服务..."
    
    if [ ! -z "$BACKEND_PID" ]; then
        kill $BACKEND_PID 2>/dev/null
        echo "后端服务已停止"
    fi
    
    if [ ! -z "$FRONTEND_PID" ]; then
        kill $FRONTEND_PID 2>/dev/null
        echo "前端服务已停止"
    fi
    
    echo "👋 服务已全部停止"
    exit 0
}

# 捕获中断信号
trap cleanup SIGINT SIGTERM

# 主流程
main() {
    check_requirements
    start_backend
    start_frontend
    wait_for_services
    
    echo ""
    echo "🎉 AI智能助手启动完成！"
    echo "按 Ctrl+C 停止所有服务"
    
    # 保持脚本运行
    wait
}

# 运行主函数
main