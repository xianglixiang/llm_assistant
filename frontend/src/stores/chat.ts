import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { ChatSession, Message, AIConfig } from '../types'
import { chatApi } from '../api/chat'

export const useChatStore = defineStore('chat', () => {
  const sessions = ref<ChatSession[]>([])
  const currentSessionId = ref<string | null>(null)
  const isLoading = ref(false)
  const aiConfig = ref<AIConfig>({
    baseUrl: 'http://localhost:8080/api/v1/chat/completions',
    apiKey: '',
    model: 'gpt-3.5-turbo',
    temperature: 0.7,
    maxTokens: 2048,
    stream: true
  })

  const currentSession = computed(() => 
    sessions.value.find(s => s.id === currentSessionId.value)
  )

  const createNewSession = () => {
    const newSession: ChatSession = {
      id: `session_${Date.now()}`,
      title: '新对话',
      messages: [],
      createdAt: new Date(),
      updatedAt: new Date()
    }
    sessions.value.unshift(newSession)
    currentSessionId.value = newSession.id
    return newSession
  }

  const deleteSession = (sessionId: string) => {
    const index = sessions.value.findIndex(s => s.id === sessionId)
    if (index > -1) {
      sessions.value.splice(index, 1)
      if (currentSessionId.value === sessionId) {
        currentSessionId.value = sessions.value[0]?.id || null
      }
    }
  }

  const sendMessage = async (content: string) => {
    if (!currentSession.value) {
      createNewSession()
    }

    const userMessage: Message = {
      id: `msg_${Date.now()}`,
      content,
      role: 'user',
      timestamp: new Date()
    }

    currentSession.value!.messages.push(userMessage)
    isLoading.value = true

    try {
      const response = await chatApi.sendMessage({
        message: content,
        sessionId: currentSession.value!.id,
        config: aiConfig.value
      })

      const aiMessage: Message = {
        id: response.id,
        content: response.content,
        role: 'assistant',
        timestamp: new Date()
      }

      currentSession.value!.messages.push(aiMessage)
      currentSession.value!.updatedAt = new Date()

      // 自动生成会话标题
      if (currentSession.value!.messages.length === 2) {
        currentSession.value!.title = content.slice(0, 20) + (content.length > 20 ? '...' : '')
      }
    } catch (error) {
      console.error('发送消息失败:', error)
    } finally {
      isLoading.value = false
    }
  }

  const updateAIConfig = (config: Partial<AIConfig>) => {
    aiConfig.value = { ...aiConfig.value, ...config }
  }

  return {
    sessions,
    currentSessionId,
    currentSession,
    isLoading,
    aiConfig,
    createNewSession,
    deleteSession,
    sendMessage,
    updateAIConfig
  }
})