<template>
  <div class="w-80 h-full bg-white/80 backdrop-blur-sm border-r border-fintech-200 flex flex-col">
    <!-- 头部 -->
    <div class="p-4 border-b border-fintech-200">
      <div class="flex items-center justify-between mb-4">
        <h1 class="text-xl font-bold text-fintech-800">AI智能助手</h1>
        <Button variant="ghost" size="sm" @click="$emit('toggle-settings')">
          <Settings class="w-4 h-4" />
        </Button>
      </div>
      <Button @click="chatStore.createNewSession()" class="w-full">
        <Plus class="w-4 h-4 mr-2" />
        新对话
      </Button>
    </div>

    <!-- 会话列表 -->
    <div class="flex-1 overflow-y-auto p-2">
      <div
        v-for="session in chatStore.sessions"
        :key="session.id"
        :class="[
          'p-3 mb-2 rounded-lg cursor-pointer transition-colors group',
          session.id === chatStore.currentSessionId
            ? 'bg-fintech-100 border border-fintech-300'
            : 'hover:bg-fintech-50'
        ]"
        @click="chatStore.currentSessionId = session.id"
      >
        <div class="flex items-center justify-between">
          <div class="flex-1 min-w-0">
            <h3 class="text-sm font-medium text-fintech-800 truncate">
              {{ session.title }}
            </h3>
            <p class="text-xs text-fintech-600 mt-1">
              {{ formatDate(session.updatedAt) }}
            </p>
          </div>
          <Button
            variant="ghost"
            size="sm"
            class="opacity-0 group-hover:opacity-100 transition-opacity"
            @click.stop="deleteSession(session.id)"
          >
            <Trash2 class="w-3 h-3" />
          </Button>
        </div>
      </div>
    </div>

    <!-- 底部用户信息 -->
    <div class="p-4 border-t border-fintech-200">
      <div class="flex items-center space-x-3">
        <div class="w-8 h-8 bg-fintech-600 rounded-full flex items-center justify-center">
          <User class="w-4 h-4 text-white" />
        </div>
        <div>
          <p class="text-sm font-medium text-fintech-800">用户</p>
          <p class="text-xs text-fintech-600">在线</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Plus, Settings, Trash2, User } from 'lucide-vue-next'
import Button from './ui/Button.vue'
import { useChatStore } from '../stores/chat'

defineEmits<{
  'toggle-settings': []
}>()

const chatStore = useChatStore()

const formatDate = (date: Date) => {
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  
  if (days === 0) {
    return '今天'
  } else if (days === 1) {
    return '昨天'
  } else if (days < 7) {
    return `${days}天前`
  } else {
    return date.toLocaleDateString('zh-CN')
  }
}

const deleteSession = (sessionId: string) => {
  if (confirm('确定要删除这个对话吗？')) {
    chatStore.deleteSession(sessionId)
  }
}
</script>