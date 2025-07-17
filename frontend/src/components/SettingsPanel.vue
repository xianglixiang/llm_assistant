<template>
  <div class="w-96 h-full bg-white/90 backdrop-blur-sm border-l border-fintech-200 flex flex-col">
    <!-- 头部 -->
    <div class="p-4 border-b border-fintech-200 flex items-center justify-between">
      <h2 class="text-lg font-semibold text-fintech-800">设置</h2>
      <Button variant="ghost" size="sm" @click="$emit('close')">
        <X class="w-4 h-4" />
      </Button>
    </div>
    
    <!-- 设置内容 -->
    <div class="flex-1 overflow-y-auto p-4 space-y-6">
      <!-- AI模型配置 -->
      <div class="space-y-4">
        <h3 class="text-md font-medium text-fintech-800 flex items-center">
          <Bot class="w-4 h-4 mr-2" />
          AI模型配置
        </h3>
        
        <div class="space-y-3">
          <div>
            <label class="block text-sm font-medium text-fintech-700 mb-1">
              API地址
            </label>
            <Input
              v-model="localConfig.baseUrl"
              placeholder="http://localhost:8080/api/v1/chat/completions"
              class="w-full"
            />
            <p class="text-xs text-fintech-500 mt-1">
              兼容OpenAI API的推理服务地址
            </p>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-fintech-700 mb-1">
              API密钥
            </label>
            <Input
              v-model="localConfig.apiKey"
              type="password"
              placeholder="sk-..."
              class="w-full"
            />
          </div>
          
          <div>
            <label class="block text-sm font-medium text-fintech-700 mb-1">
              模型名称
            </label>
            <select
              v-model="localConfig.model"
              class="w-full px-3 py-2 border border-fintech-300 rounded-md focus:outline-none focus:ring-2 focus:ring-fintech-500"
            >
              <option value="gpt-3.5-turbo">GPT-3.5 Turbo</option>
              <option value="gpt-4">GPT-4</option>
              <option value="claude-3-sonnet">Claude 3 Sonnet</option>
              <option value="llama-2-70b">Llama 2 70B</option>
              <option value="qwen-72b">通义千问 72B</option>
              <option value="baichuan-53b">百川 53B</option>
            </select>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-fintech-700 mb-1">
              温度 ({{ localConfig.temperature }})
            </label>
            <input
              v-model.number="localConfig.temperature"
              type="range"
              min="0"
              max="2"
              step="0.1"
              class="w-full"
            />
            <div class="flex justify-between text-xs text-fintech-500 mt-1">
              <span>精确</span>
              <span>创造性</span>
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-fintech-700 mb-1">
              最大令牌数
            </label>
            <Input
              v-model.number="localConfig.maxTokens"
              type="number"
              min="1"
              max="8192"
              class="w-full"
            />
          </div>
          
          <div class="flex items-center justify-between">
            <label class="text-sm font-medium text-fintech-700">
              流式输出
            </label>
            <button
              @click="localConfig.stream = !localConfig.stream"
              :class="[
                'relative inline-flex h-6 w-11 items-center rounded-full transition-colors',
                localConfig.stream ? 'bg-fintech-600' : 'bg-gray-200'
              ]"
            >
              <span
                :class="[
                  'inline-block h-4 w-4 rounded-full bg-white transition-transform',
                  localConfig.stream ? 'translate-x-6' : 'translate-x-1'
                ]"
              />
            </button>
          </div>
        </div>
      </div>
      
      <!-- 测试连接 -->
      <div>
        <Button
          @click="testConnection"
          :disabled="isTestingConnection"
          variant="outline"
          class="w-full"
        >
          <Wifi v-if="!isTestingConnection" class="w-4 h-4 mr-2" />
          <div v-else class="w-4 h-4 mr-2 animate-spin rounded-full border-2 border-fintech-600 border-t-transparent" />
          {{ isTestingConnection ? '测试中...' : '测试连接' }}
        </Button>
        
        <div v-if="connectionStatus" class="mt-2 p-2 rounded text-sm" :class="connectionStatusClass">
          {{ connectionStatus }}
        </div>
      </div>
      
      <!-- 界面设置 -->
      <div class="space-y-4">
        <h3 class="text-md font-medium text-fintech-800 flex items-center">
          <Palette class="w-4 h-4 mr-2" />
          界面设置
        </h3>
        
        <div class="flex items-center justify-between">
          <label class="text-sm font-medium text-fintech-700">
            深色模式
          </label>
          <button
            @click="toggleDarkMode"
            :class="[
              'relative inline-flex h-6 w-11 items-center rounded-full transition-colors',
              isDarkMode ? 'bg-fintech-600' : 'bg-gray-200'
            ]"
          >
            <span
              :class="[
                'inline-block h-4 w-4 rounded-full bg-white transition-transform',
                isDarkMode ? 'translate-x-6' : 'translate-x-1'
              ]"
            />
          </button>
        </div>
      </div>
    </div>
    
    <!-- 底部操作 -->
    <div class="p-4 border-t border-fintech-200 space-y-2">
      <Button @click="saveSettings" class="w-full">
        <Save class="w-4 h-4 mr-2" />
        保存设置
      </Button>
      <Button @click="resetSettings" variant="outline" class="w-full">
        <RotateCcw class="w-4 h-4 mr-2" />
        重置为默认
      </Button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { X, Bot, Wifi, Palette, Save, RotateCcw } from 'lucide-vue-next'
import Button from './ui/Button.vue'
import Input from './ui/Input.vue'
import { useChatStore } from '../stores/chat'
import type { AIConfig } from '../types'

defineEmits<{
  close: []
}>()

const chatStore = useChatStore()
const isTestingConnection = ref(false)
const connectionStatus = ref('')
const isDarkMode = ref(false)

const localConfig = reactive<AIConfig>({
  baseUrl: '',
  apiKey: '',
  model: 'gpt-3.5-turbo',
  temperature: 0.7,
  maxTokens: 2048,
  stream: true
})

const connectionStatusClass = ref('')

onMounted(() => {
  // 从store加载当前配置
  Object.assign(localConfig, chatStore.aiConfig)
})

const testConnection = async () => {
  isTestingConnection.value = true
  connectionStatus.value = ''
  
  try {
    // 模拟API测试
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    if (localConfig.baseUrl && localConfig.apiKey) {
      connectionStatus.value = '连接成功！'
      connectionStatusClass.value = 'bg-green-100 text-green-800 border border-green-200'
    } else {
      connectionStatus.value = '请填写完整的API地址和密钥'
      connectionStatusClass.value = 'bg-yellow-100 text-yellow-800 border border-yellow-200'
    }
  } catch (error) {
    connectionStatus.value = '连接失败，请检查配置'
    connectionStatusClass.value = 'bg-red-100 text-red-800 border border-red-200'
  } finally {
    isTestingConnection.value = false
  }
}

const saveSettings = () => {
  chatStore.updateAIConfig(localConfig)
  connectionStatus.value = '设置已保存'
  connectionStatusClass.value = 'bg-fintech-100 text-fintech-800 border border-fintech-200'
  
  setTimeout(() => {
    connectionStatus.value = ''
  }, 2000)
}

const resetSettings = () => {
  if (confirm('确定要重置所有设置吗？')) {
    localConfig.baseUrl = 'http://localhost:8080/api/v1/chat/completions'
    localConfig.apiKey = ''
    localConfig.model = 'gpt-3.5-turbo'
    localConfig.temperature = 0.7
    localConfig.maxTokens = 2048
    localConfig.stream = true
  }
}

const toggleDarkMode = () => {
  isDarkMode.value = !isDarkMode.value
  document.documentElement.classList.toggle('dark', isDarkMode.value)
}
</script>