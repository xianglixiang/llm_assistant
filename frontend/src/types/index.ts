export interface Message {
  id: string
  content: string
  role: 'user' | 'assistant'
  timestamp: Date
}

export interface ChatSession {
  id: string
  title: string
  messages: Message[]
  createdAt: Date
  updatedAt: Date
}

export interface AIModel {
  id: string
  name: string
  provider: string
  endpoint: string
  apiKey?: string
  isDefault: boolean
}

export interface AIConfig {
  baseUrl: string
  apiKey: string
  model: string
  temperature: number
  maxTokens: number
  stream: boolean
}

export interface ChatRequest {
  message: string
  sessionId?: string
  config?: Partial<AIConfig>
}

export interface ChatResponse {
  id: string
  content: string
  sessionId: string
}