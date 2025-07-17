<template>
  <div class="p-4 border-t border-fintech-200 bg-white/80 backdrop-blur-sm">
    <div class="flex items-end space-x-2">
      <div class="flex-1">
        <textarea
          ref="textareaRef"
          v-model="message"
          placeholder="输入您的问题..."
          class="w-full px-4 py-3 border border-fintech-300 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-fintech-500 focus:border-transparent"
          rows="1"
          @keydown="handleKeydown"
          @input="adjustHeight"
        />
      </div>
      <Button
        :disabled="!message.trim() || isLoading"
        @click="sendMessage"
        class="flex-shrink-0"
      >
        <Send v-if="!isLoading" class="w-4 h-4" />
        <div v-else class="w-4 h-4 animate-spin rounded-full border-2 border-white border-t-transparent" />
      </Button>
    </div>
    
    <!-- 快捷操作 -->
    <div class="flex items-center justify-between mt-3">
      <div class="flex space-x-2">
        <Button
          v-for="suggestion in suggestions"
          :key="suggestion"
          variant="ghost"
          size="sm"
          @click="message = suggestion"
          class="text-xs"
        >
          {{ suggestion }}
        </Button>
      </div>
      
      <div class="flex items-center space-x-2 text-xs text-fintech-600">
        <Lightbulb class="w-3 h-3" />
        <span>按 Shift+Enter 换行</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue'
import { Send, Lightbulb } from 'lucide-vue-next'
import Button from './ui/Button.vue'

interface Props {
  isLoading?: boolean
}

defineProps<Props>()

const emit = defineEmits<{
  send: [message: string]
}>()

const message = ref('')
const textareaRef = ref<HTMLTextAreaElement>()

const suggestions = [
  '解释一下人工智能',
  '写一个Python函数',
  '总结一下今天的新闻',
  '推荐几本好书'
]

const sendMessage = () => {
  if (message.value.trim()) {
    emit('send', message.value.trim())
    message.value = ''
    nextTick(() => {
      adjustHeight()
    })
  }
}

const handleKeydown = (event: KeyboardEvent) => {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    sendMessage()
  }
}

const adjustHeight = () => {
  if (textareaRef.value) {
    textareaRef.value.style.height = 'auto'
    textareaRef.value.style.height = Math.min(textareaRef.value.scrollHeight, 120) + 'px'
  }
}
</script>