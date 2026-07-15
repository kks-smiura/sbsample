import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  server: {
    proxy: {
      '/useredit': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
    // この配列に .ts が含まれていることを確認します
    extensions: ['.mjs', '.js', '.mts', '.ts', '.jsx', '.tsx', '.json', '.vue']    
  },
  build: {
    rollupOptions: {
      output: {
        entryFileNames: 'assets/[name].js',   // メインのJSファイル
        chunkFileNames: 'assets/[name].js',   // 分割されたJSファイル
        assetFileNames: 'assets/[name].[ext]' // CSSや画像などのファイル
      }
    }
  },  
})
