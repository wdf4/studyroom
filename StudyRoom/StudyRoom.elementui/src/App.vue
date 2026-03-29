<template>
  <div id="app">
    <router-view />
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
body {
  background-color: rgb(247, 247, 247);
}

.bg-header {
  background-color: #28b2d4;
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
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  height: 100%;
  border-radius: 10px;
  padding: 20px;
  background: white;
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
