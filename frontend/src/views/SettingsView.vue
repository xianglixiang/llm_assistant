<template>
  <div class="min-h-screen bg-gradient-to-br from-fintech-50 to-fintech-100 p-8">
    <div class="max-w-4xl mx-auto">
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-fintech-800 mb-2">设置</h1>
        <p class="text-fintech-600">配置您的AI智能助手</p>
      </div>
      
      <div class="fintech-card p-6">
        <SettingsPanel @close="$router.push('/')" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import SettingsPanel from '../components/SettingsPanel.vue'
</script>