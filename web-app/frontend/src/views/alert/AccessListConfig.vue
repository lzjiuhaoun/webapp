<template>
  <div class="access-list-config">
    <div class="operate-bar">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-input
        v-model="searchKey"
        placeholder="请输入规则名称"
        clearable
        style="width: 280px; margin-left: 12px;"
        @keyup.enter="handleSearch"
      />
      <el-button @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="name" label="规则名称" min-width="160" />
      <el-table-column label="类别" width="100">
        <template slot-scope="{ row }">
          <el-tag :type="row.type === 0 ? 'success' : 'danger'" size="small">
            {{ row.typeName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="用户数量" width="100" align="center">
        <template slot-scope="{ row }">
          <span @click="handleDetail(row)" class="link-type">{{ row.userCount || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template slot-scope="{ row }">
          <el-switch
            :value="row.status === 1"
            active-color="#52c41a"
            inactive-color="#ff4d4f"
            @change="handleToggle(row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
      <el-table-column label="更新时间" width="170">
        <template slot-scope="{ row }">{{ fmtTime(row.updateTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" @click="handleDetail(row)">详情</el-button>
          <el-button type="text" @click="handleEdit(row)">编辑</el-button>
          <el-button type="text" style="color: #ff4d4f;" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page="pageNo"
      :page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      background
      style="margin-top: 16px; text-align: right;"
      @current-change="handlePageChange"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      :title="formMode === 'add' ? '新增黑白名单' : '编辑黑白名单'"
      :visible.sync="dialogVisible"
      width="560px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" :rules="formRules" ref="formRef" label-width="90px">
        <el-form-item label="规则名称" prop="name">
          <el-input v-model="form.name" maxlength="32" placeholder="32个字符以内" />
        </el-form-item>
        <el-form-item label="类别" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio :label="0">白名单</el-radio>
            <el-radio :label="1">黑名单</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="用户列表">
          <div v-for="(user, idx) in form.users" :key="idx" class="user-row">
            <el-input v-model="user.userName" placeholder="用户名" style="width: 180px; margin-right: 8px;" />
            <el-input v-model="user.userAccount" placeholder="账号" style="width: 180px; margin-right: 8px;" />
            <el-button type="danger" size="small" icon="el-icon-delete" @click="removeUser(idx)" circle />
          </div>
          <el-button type="default" size="small" icon="el-icon-plus" @click="addUser" style="margin-top: 8px;">
            添加用户
          </el-button>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" maxlength="255" rows="3" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </span>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog title="名单详情" :visible.sync="detailVisible" width="480px">
      <div v-if="detailData">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="规则名称">{{ detailData.name }}</el-descriptions-item>
          <el-descriptions-item label="类别">
            <el-tag :type="detailData.type === 0 ? 'success' : 'danger'" size="small">
              {{ detailData.typeName }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="detailData.status === 1 ? 'success' : 'danger'" size="small">
              {{ detailData.status === 1 ? '已启用' : '已禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="备注">{{ detailData.remark || '-' }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ fmtTime(detailData.updateTime) }}</el-descriptions-item>
        </el-descriptions>
        <h4 style="margin: 16px 0 8px; color: #606266;">用户列表</h4>
        <el-table :data="detailData.users || []" border size="small">
          <el-table-column prop="userName" label="用户名" />
          <el-table-column prop="userAccount" label="账号" />
        </el-table>
        <div v-if="!detailData.users || detailData.users.length === 0" style="color: #999; text-align: center; padding: 16px;">
          暂无用户数据
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page, detail, add, update, remove, toggleStatus } from '@/api/accessList'

export default {
  name: 'AccessListConfig',
  data() {
    return {
      loading: false,
      tableData: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      searchKey: '',
      dialogVisible: false,
      formMode: 'add',
      form: this.createEmptyForm(),
      formRules: {
        name: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
        type: [{ required: true, message: '请选择类别', trigger: 'change' }]
      },
      detailVisible: false,
      detailData: null
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    createEmptyForm() {
      return {
        name: '',
        type: 0,
        users: [{ userName: '', userAccount: '' }],
        remark: ''
      }
    },
    async fetchData() {
      this.loading = true
      try {
        const res = await page({
          pageNo: this.pageNo,
          pageSize: this.pageSize,
          keyword: this.searchKey || undefined
        })
        this.tableData = res.list || []
        this.total = res.total || 0
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pageNo = 1
      this.fetchData()
    },
    handleReset() {
      this.searchKey = ''
      this.pageNo = 1
      this.fetchData()
    },
    handlePageChange(p) {
      this.pageNo = p
      this.fetchData()
    },
    handleAdd() {
      this.formMode = 'add'
      this.form = this.createEmptyForm()
      this.dialogVisible = true
      this.$nextTick(() => { if (this.$refs.formRef) this.$refs.formRef.clearValidate() })
    },
    handleEdit(row) {
      this.formMode = 'edit'
      this.form = {
        id: row.id,
        name: row.name,
        type: row.type,
        users: (row.users || []).length > 0
          ? row.users.map(u => ({ userName: u.userName, userAccount: u.userAccount }))
          : [{ userName: '', userAccount: '' }],
        remark: row.remark || ''
      }
      this.dialogVisible = true
      this.$nextTick(() => { if (this.$refs.formRef) this.$refs.formRef.clearValidate() })
    },
    async handleDetail(row) {
      const res = await detail(row.id)
      this.detailData = res
      this.detailVisible = true
    },
    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除黑白名单【${row.name}】？删除后无法恢复`, '操作确认', {
          confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'
        })
        await remove(row.id)
        this.$message.success(`黑白名单【${row.name}】已删除`)
        this.fetchData()
      } catch {}
    },
    async handleToggle(row) {
      try {
        await toggleStatus(row.id)
        this.$message.success(`黑白名单【${row.name}】已${row.status === 1 ? '禁用' : '启用'}`)
        this.fetchData()
      } catch {}
    },
    fmtTime(ts) {
      if (!ts) return '-'
      const d = new Date(ts)
      const pad = n => String(n).padStart(2, '0')
      return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
    },
    addUser() {
      this.form.users.push({ userName: '', userAccount: '' })
    },
    removeUser(idx) {
      if (this.form.users.length > 1) {
        this.form.users.splice(idx, 1)
      } else {
        this.$message.warning('至少保留一个用户行')
      }
    },
    async handleSave() {
      this.$refs.formRef.validate(async valid => {
        if (!valid) return
        const hasEmptyUser = this.form.users.some(u => !u.userName || !u.userAccount)
        if (hasEmptyUser) {
          this.$message.warning('请填写完整的用户信息（用户名和账号）')
          return
        }
        try {
          if (this.formMode === 'add') {
            await add(this.form)
            this.$message.success('黑白名单创建成功')
          } else {
            await update(this.form)
            this.$message.success('黑白名单更新成功')
          }
          this.dialogVisible = false
          this.fetchData()
        } catch (e) {
          // handled by interceptor
        }
      })
    }
  }
}
</script>

<style scoped>
.access-list-config {
  padding: 20px;
}
.operate-bar {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}
.link-type {
  color: #409eff;
  cursor: pointer;
}
.link-type:hover {
  text-decoration: underline;
}
.user-row {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}
</style>
