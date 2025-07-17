<template>
  <div :class="messageClasses">
    <div v-if="message.role === 'assistant'" class="flex-shrink-0 mr-3">
      <div class="w-8 h-8 bg-fintech-600 rounded-full flex items-center justify-center">
        <Bot class="w-4 h-4 text-white" />
      </div>
    </div>
    
    <div class="flex-1">
      <div :class="bubbleClasses">
        <div class="whitespace-pre-wrap" v-html="formattedContent"></div>
      </div>
      <div class="text-xs text-fintech-500 mt-1" :class="timeClasses">
        {{ formatTime(message.timestamp) }}
      </div>
    </div>
    
    <div v-if="message.role === 'user'" class="flex-shrink-0 ml-3">
      <div class="w-8 h-8 bg-fintech-500 rounded-full flex items-center justify-center">
        <User class="w-4 h-4 text-white" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Bot, User } from 'lucide-vue-next'
import type { Message } from '../types'

interface Props {
  message: Message
}

const props = defineProps<Props>()

const messageClasses = computed(() => [
  'flex items-start mb-4',
  props.message.role === 'user' ? 'justify-end' : 'justify-start'
])

const bubbleClasses = computed(() => [
  'max-w-xs lg:max-w-md xl:max-w-lg px-4 py-2 rounded-lg',
  props.message.role === 'user'
    ? 'bg-fintech-600 text-white rounded-br-sm'
    : 'bg-white border border-fintech-200 text-fintech-800 rounded-bl-sm shadow-sm'
])

const timeClasses = computed(() => [
  props.message.role === 'user' ? 'text-right' : 'text-left'
])

const formattedContent = computed(() => {
  // 简单的 markdown 处理
  return props.message.content
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
    .replace(/`(.*?)`/g, '<code class="bg-fintech-100 px-1 rounded">$1</code>')
    .replace(/\n/g, '<br>')
})

const formatTime = (date: Date) => {
  return date.toLocaleTimeString('zh-CN', { 
    hour: '2-digit', 
    minute: '2-digit' 
  })
}
</script>