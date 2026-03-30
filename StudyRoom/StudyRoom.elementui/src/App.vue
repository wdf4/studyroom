<template>
  <div id="app">
    <transition name="fade" mode="out-in">
      <router-view />
    </transition>
  </div>
</template>

<script>

export default {
  name: 'App',

}
</script>

<style>
@import './css/index.css';
@import "./css/font/iconfont.css";



html,
body,
#app {
  background-color: var(--bg-color-main, #f8fafc);
  margin: 0;
  padding: 0;
  min-height: 100vh;
}

.bg-header {
  background-color: #0f172a;
  display: flex;
  height: 60px;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  color: white;
}

/* -xs -sm  -md  -lg*/

.pointer {
  cursor: pointer;
}


.card {
  box-shadow: var(--shadow-sm, 0 2px 4px rgba(0,0,0,.06), 0 1px 2px rgba(0,0,0,.04));
  height: 100%;
  border-radius: var(--radius-lg, 12px);
  padding: 20px;
  background: var(--white, #ffffff);
  border: 1px solid var(--border-color, #e2e8f0);
}


.margin-top-xs {
  margin-top: 5px;
}

.margin-top-lg {
  margin-top: 20px;
}

.margin-bottom-xs {
  margin-bottom: 5px;
}

.margin-bottom-lg {
  margin-bottom: 20px;
}
</style>
