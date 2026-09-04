<script setup>
import { ref } from 'vue'

const url = ref('')
const result = ref(null)
const error = ref('')
const loading = ref(false)
const copied = ref(false)
const apiBase = import.meta.env.VITE_API_URL || 'http://localhost:8080'

async function shorten() {
  error.value = ''
  result.value = null
  copied.value = false
  if (!url.value.trim()) { error.value = 'Add a URL to get started.'; return }
  loading.value = true
  try {
    const response = await fetch(`${apiBase}/api/urls`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ url: url.value.trim() }) })
    const data = await response.json()
    if (!response.ok) throw new Error(data.error || 'Something went wrong.')
    result.value = data
  } catch (exception) { error.value = exception.message || 'Unable to connect to LinkNest.' }
  finally { loading.value = false }
}

async function copyLink() {
  await navigator.clipboard.writeText(result.value.shortUrl)
  copied.value = true
  setTimeout(() => { copied.value = false }, 1800)
}
</script>

<template>
  <main class="page-shell">
    <nav class="nav"><a class="brand" href="/"><span class="brand-mark">↗</span><span>linknest</span></a><span class="nav-note">A quieter way to share</span></nav>
    <section class="hero">
      <div class="hero-copy"><p class="eyebrow"><span class="pulse"></span> LINK SHORTENER</p><h1>Make every link<br><em>feel lighter.</em></h1><p class="intro">Turn long, unwieldy URLs into clean links that are easy to remember, share, and trust.</p></div>
      <div class="form-wrap">
        <form class="shorten-form" @submit.prevent="shorten">
          <label for="url">Paste your long URL</label>
          <div class="input-row"><input id="url" v-model="url" type="url" placeholder="https://your-next-big-idea.com/..." autocomplete="url"><button :disabled="loading" type="submit">{{ loading ? 'Creating...' : 'Shorten link' }} <span>→</span></button></div>
          <p v-if="error" class="error" role="alert">{{ error }}</p>
        </form>
        <Transition name="result"><div v-if="result" class="result-card"><div><p class="result-label">YOUR SHORT LINK</p><a class="short-link" :href="result.shortUrl" target="_blank">{{ result.shortUrl.replace('http://', '') }}</a><p class="destination">{{ result.originalUrl }}</p></div><button class="copy-button" @click="copyLink">{{ copied ? 'Copied!' : 'Copy link' }} <span>⧉</span></button></div></Transition>
      </div>
    </section>
    <section class="features"><div><span class="feature-number">01</span><h2>One click<br>to share</h2><p>Clean, memorable links made for the moments that matter.</p></div><div><span class="feature-number">02</span><h2>Built to<br>be reliable</h2><p>Your links stay available and redirect instantly, every time.</p></div><div><span class="feature-number">03</span><h2>Nothing<br>to install</h2><p>No accounts, no friction. Just paste, shorten, and go.</p></div></section>
    <footer><span>© 2025 LinkNest</span><span>Short links, thoughtfully made.</span></footer>
  </main>
</template>