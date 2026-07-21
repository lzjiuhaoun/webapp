<template>
  <div class="alert-layout">
    <aside class="alert-sidebar">
      <div class="sidebar-title">告警配置</div>
      <el-menu
        :default-active="activeMenu"
        router
        :default-openeds="['1']"
      >
        <el-menu-item index="/alert/rule">
          <i class="el-icon-warning" />
          <span slot="title">告警规则</span>
        </el-menu-item>
        <el-menu-item index="/alert/access-list">
          <i class="el-icon-document" />
          <span slot="title">告警黑白名单</span>
        </el-menu-item>
      </el-menu>
    </aside>
    <main class="alert-main">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: 'AlertLayout',
  computed: {
    activeMenu() {
      return this.$route.path
    }
  }
}
</script>

<style scoped>
.alert-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
}
.alert-sidebar {
  width: 220px;
  background: #304156;
  overflow-y: auto;
  flex-shrink: 0;
}
.sidebar-title {
  height: 56px;
  line-height: 56px;
  text-align: center;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid rgba(255,255,255,0.1);
  background: #263445;
}
.alert-sidebar .el-menu {
  border-right: none;
  background: transparent;
}
.alert-sidebar .el-menu-item {
  color: #bfcbd9;
}
.alert-sidebar .el-menu-item:hover {
  background: #263445;
}
.alert-sidebar .el-menu-item.is-active {
  color: #409eff;
  background: #1f2d3d;
}
.alert-main {
  flex: 1;
  overflow-y: auto;
  background: #f0f2f5;
}
</style>
