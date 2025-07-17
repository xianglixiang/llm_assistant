import axios from 'axios'
import type { ChatRequest, ChatResponse } from '../types'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000
})

export const chatApi = {
  async sendMessage(request: ChatRequest): Promise<ChatResponse> {
    const response = await api.post('/chat/send', request)
    return response.data
  },

  async getSessions() {
    const response = await api.get('/chat/sessions')
    return response.data
  },

  async deleteSession(sessionId: string) {
    await api.delete(`/chat/sessions/${sessionId}`)
  }
}