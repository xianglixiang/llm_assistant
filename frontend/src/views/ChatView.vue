<template>
  <div class="flex h-screen">
    <!-- 侧边栏 -->
    <ChatSidebar @toggle-settings="showSettings = !showSettings" />
    
    <!-- 主聊天区域 -->
    <div class="flex-1 flex flex-col">
      <!-- 聊天头部 -->
      <div class="p-4 border-b border-fintech-200 bg-white/80 backdrop-blur-sm">
        <div class="flex items-center justify-between">
          <div>
            <h2 class="text-lg font-semibold text-fintech-800">
              {{ chatStore.currentSession?.title || '选择或创建新对话' }}
            </h2>
            <p class="text-sm text-fintech-600">
              AI智能助手为您服务
            </p>
          </div>
          <div class="flex items-center space-x-2">
            <span class="flex items-center text-sm text-fintech-600">
              <div class="w-2 h-2 bg-green-500 rounded-full mr-2"></div>
              在线
            </span>
          </div>
        </div>
      </div>
      
      <!-- 聊天消息区域 -->
      <div ref="messagesContainer" class="flex-1 overflow-y-auto p-4 space-y-4">
        <!-- 欢迎消息 -->
        <div v-if="!chatStore.currentSession?.messages.length" class="text-center py-12">
          <div class="fintech-gradient w-16 h-16 rounded-full mx-auto mb-4 flex items-center justify-center">
            <Bot class="w-8 h-8 text-white" />
          </div>
          <h3 class="text-xl font-semibold text-fintech-800 mb-2">
            中午好，String.Li
          </h3>
          <p class="text-fintech-600 mb-6">
            我是您的AI智能助手，有什么可以帮助您的吗？
          </p>
          <div class="grid grid-cols-2 gap-4 max-w-md mx-auto">
            <div
              v-for="example in examples"
              :key="example.title"
              class="fintech-card p-4 cursor-pointer hover:shadow-lg transition-all"
              @click="handleExampleClick(example.prompt)"
            >
              <component :is="example.icon" class="w-6 h-6 text-fintech-600 mb-2" />
              <h4 class="font-medium text-fintech-800 mb-1">{{ example.title }}</h4>
              <p class="text-sm text-fintech-600">{{ example.description }}</p>
            </div>
          </div>
        </div>
        
        <!-- 消息列表 -->
        <ChatMessage
          v-for="message in chatStore.currentSession?.messages"
          :key="message.id"
          :message="message"
        />
        
        <!-- 加载中指示器 -->
        <div v-if="chatStore.isLoading" class="flex items-start mb-4">
          <div class="w-8 h-8 bg-fintech-600 rounded-full flex items-center justify-center mr-3">
            <Bot class="w-4 h-4 text-white" />
          </div>
          <div class="bg-white border border-fintech-200 rounded-lg rounded-bl-sm px-4 py-2">
            <div class="flex space-x-1">
              <div class="w-2 h-2 bg-fintech-400 rounded-full animate-bounce"></div>
              <div class="w-2 h-2 bg-fintech-400 rounded-full animate-bounce" style="animation-delay: 0.1s"></div>
              <div class="w-2 h-2 bg-fintech-400 rounded-full animate-bounce" style="animation-delay: 0.2s"></div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 输入区域 -->
      <ChatInput
        :is-loading="chatStore.isLoading"
        @send="handleSendMessage"
      />
    </div>
    
    <!-- 设置面板 -->
    <SettingsPanel v-if="showSettings" @close="showSettings = false" />
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick, watch } from 'vue'
import { Bot, MessageSquare, Code, BookOpen, Lightbulb } from 'lucide-vue-next'
import ChatSidebar from '../components/ChatSidebar.vue'
import ChatMessage from '../components/ChatMessage.vue'
import ChatInput from '../components/ChatInput.vue'
import SettingsPanel from '../components/SettingsPanel.vue'
import { useChatStore } from '../stores/chat'

const chatStore = useChatStore()
const showSettings = ref(false)
const messagesContainer = ref<HTMLElement>()

const examples = [
  {
    icon: MessageSquare,
    title: '智能对话',
    description: '与AI进行自然对话',
    prompt: '你好，请介绍一下你自己'
  },
  {
    icon: Code,
    title: '代码助手',
    description: '编程问题解答',
    prompt: '帮我写一个计算斐波那契数列的Python函数'
  },
  {
    icon: BookOpen,
    title: '知识问答',
    description: '获取各种知识',
    prompt: '请解释一下区块链技术的基本原理'
  },
  {
    icon: Lightbulb,
    title: '创意灵感',
    description: '激发创造力',
    prompt: '给我一些关于环保的创新想法'
  }
]

const handleSendMessage = (message: string) => {
  chatStore.sendMessage(message)
}

const handleExampleClick = (prompt: string) => {
  handleSendMessage(prompt)
}

// 自动滚动到底部
watch(() => chatStore.currentSession?.messages.length, () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
})

watch(() => chatStore.isLoading, () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
})
</script>