<template>
  <div class="alert-rule-config">
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
      <el-table-column prop="name" label="规则名称" min-width="140" show-overflow-tooltip />
      <el-table-column label="日志类型" width="110">
        <template slot-scope="{ row }">
          {{ logTypeMap[row.logType] || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="规则类型" width="100">
        <template slot-scope="{ row }">
          {{ row.ruleType === 0 ? '普通规则' : '组合规则' }}
        </template>
      </el-table-column>
      <el-table-column label="告警等级" width="110">
        <template slot-scope="{ row }">
          <el-tag v-if="row.level" :type="levelType(row.level)" size="small">
            {{ row.levelName }}
          </el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="关联方式" width="80" align="center">
        <template slot-scope="{ row }">
          {{ row.ruleType === 0 ? (row.linkType === 0 ? '与' : '或') : '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" min-width="150" show-overflow-tooltip />
      <el-table-column label="黑名单" width="100">
        <template slot-scope="{ row }">
          {{ row.blacklistId ? '已关联' : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="白名单" width="100">
        <template slot-scope="{ row }">
          {{ row.whitelistId ? '已关联' : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center">
        <template slot-scope="{ row }">
          <el-switch
            :value="row.status === 1"
            active-color="#52c41a"
            inactive-color="#ff4d4f"
            @change="handleToggle(row)"
          />
        </template>
      </el-table-column>
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
      :title="formMode === 'add' ? '新增告警规则' : '编辑告警规则'"
      :visible.sync="dialogVisible"
      width="680px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="规则名称" prop="name">
          <el-input v-model="form.name" maxlength="32" placeholder="32个字符以内" />
        </el-form-item>
        <el-form-item label="日志类型" prop="logType">
          <el-select v-model="form.logType" placeholder="请选择" style="width: 200px;">
            <el-option v-for="(label, val) in logTypeMap" :key="val" :label="label" :value="Number(val)" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" maxlength="255" rows="2" type="textarea" />
        </el-form-item>
        <el-form-item label="规则类型" prop="ruleType">
          <el-radio-group v-model="form.ruleType">
            <el-radio :label="0">普通规则</el-radio>
            <el-radio :label="1">组合规则</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="告警等级" prop="level">
          <el-select v-model="form.level" placeholder="请选择" style="width: 200px;">
            <el-option label="一级（红）" :value="1" />
            <el-option label="二级（橙）" :value="2" />
            <el-option label="三级（黄）" :value="3" />
          </el-select>
        </el-form-item>

        <!-- 普通规则字段 -->
        <template v-if="form.ruleType === 0">
          <el-form-item label="关联方式" prop="linkType">
            <el-radio-group v-model="form.linkType">
              <el-radio :label="0">与（全部满足）</el-radio>
              <el-radio :label="1">或（满足其一）</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="匹配条件">
            <div v-for="(cond, idx) in form.conditions" :key="idx" class="condition-row">
              <el-select v-model="cond.field" placeholder="字段" style="width: 140px; margin-right: 6px;" @change="onFieldChange(cond)">
                <el-option v-for="f in availableFields" :key="f.value" :label="f.label" :value="f.value" />
              </el-select>
              <el-select v-model="cond.operator" placeholder="运算符" style="width: 120px; margin-right: 6px;">
                <el-option v-for="op in operatorOptions(cond.field)" :key="op" :label="operatorLabel(op)" :value="op" />
              </el-select>
              <el-time-picker
                v-if="cond.field === 'loginTime'"
                v-model="cond.value"
                format="HH:mm"
                value-format="HHmm"
                placeholder="选择时间"
                style="width: 200px; margin-right: 6px;"
              />
              <el-input
                v-else
                v-model="cond.value"
                :placeholder="cond.field === 'ipAddress' ? '如 192.168.1.1' : cond.operator === 'crossCamp' ? '红方|蓝方、白方|蓝方' : '值'"
                style="width: 200px; margin-right: 6px;"
              />
              <el-button type="danger" size="small" icon="el-icon-delete" @click="removeCondition(idx)" circle />
            </div>
            <el-button type="default" size="small" icon="el-icon-plus" @click="addCondition" style="margin-top: 8px;">
              添加条件
            </el-button>
          </el-form-item>
        </template>

        <!-- 组合规则字段 -->
        <template v-if="form.ruleType === 1">
          <el-form-item label="引用规则" prop="combineRuleId">
            <el-select v-model="form.combineRuleId" placeholder="请选择普通规则" style="width: 300px;">
              <el-option v-for="r in normalRules" :key="r.id" :label="r.name" :value="r.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间范围" prop="combineDuration">
            <el-input-number v-model="form.combineDuration" :min="1" :max="525600" :step="5" />
            <span style="margin-left: 8px; color: #999;">分钟</span>
          </el-form-item>
          <el-form-item label="触发次数" prop="combineCount">
            <el-input-number v-model="form.combineCount" :min="2" :max="9999" />
            <span style="margin-left: 8px; color: #999;">次（组合规则备用，当前不做校验）</span>
          </el-form-item>
        </template>

        <template v-if="form.ruleType === 0">
          <el-form-item label="黑名单">
            <el-select v-model="form.blacklistId" placeholder="请选择" clearable style="width: 300px;">
              <el-option v-for="l in blacklists" :key="l.id" :label="l.name" :value="l.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="白名单">
            <el-select v-model="form.whitelistId" placeholder="请选择" clearable style="width: 300px;">
              <el-option v-for="l in whitelists" :key="l.id" :label="l.name" :value="l.id" />
            </el-select>
          </el-form-item>
        </template>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </span>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog title="规则详情" :visible.sync="detailVisible" width="600px">
      <div v-if="detailData">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="规则名称">{{ detailData.name }}</el-descriptions-item>
          <el-descriptions-item label="日志类型">{{ logTypeMap[detailData.logType] || '-' }}</el-descriptions-item>
          <el-descriptions-item label="规则类型">{{ detailData.ruleType === 0 ? '普通规则' : '组合规则' }}</el-descriptions-item>
          <el-descriptions-item v-if="detailData.level" label="告警等级">
            <el-tag :type="levelType(detailData.level)" size="small">{{ detailData.levelName }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item v-if="detailData.ruleType === 0" label="关联方式">
            {{ detailData.linkType === 0 ? '与' : '或' }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="detailData.status === 1 ? 'success' : 'danger'" size="small">
              {{ detailData.status === 1 ? '已启用' : '已禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="描述">{{ detailData.description || '-' }}</el-descriptions-item>
          <el-descriptions-item v-if="detailData.ruleType === 1 && detailData.combineRuleId" label="引用规则ID">
            {{ detailData.combineRuleId }}
          </el-descriptions-item>
          <el-descriptions-item v-if="detailData.ruleType === 1" label="触发次数">
            {{ detailData.combineCount || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="黑名单">{{ detailData.blacklistId ? '已关联' : '-' }}</el-descriptions-item>
          <el-descriptions-item label="白名单">{{ detailData.whitelistId ? '已关联' : '-' }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ fmtTime(detailData.updateTime) }}</el-descriptions-item>
        </el-descriptions>
        <h4 v-if="detailData.ruleType === 0 && parsedConditions.length > 0" style="margin: 16px 0 8px; color: #606266;">
          匹配条件
        </h4>
        <el-table v-if="parsedConditions.length > 0" :data="parsedConditions" border size="small">
          <el-table-column prop="field" label="字段" />
          <el-table-column prop="operator" label="运算符" />
          <el-table-column prop="value" label="值" show-overflow-tooltip />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page, detail, add, update, remove, toggleStatus } from '@/api/alertRule'
import { names as getAccessLists } from '@/api/accessList'

export default {
  name: 'AlertRuleConfig',
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
        logType: [{ required: true, message: '请选择日志类型', trigger: 'change' }],
        ruleType: [{ required: true, message: '请选择规则类型', trigger: 'change' }],
        level: [{ required: true, message: '请选择告警等级', trigger: 'change' }],
        combineRuleId: [{ required: true, message: '请选择引用的普通规则', trigger: 'change' }],
        combineDuration: [{ required: true, message: '请配置时间范围', trigger: 'change' }],
        combineCount: [{ required: true, message: '请输入触发次数', trigger: 'blur' }]
      },
      detailVisible: false,
      detailData: null,
      logTypeMap: { 0: '平台登录日志', 1: 'IM登录日志', 2: 'DLP日志' },
      availableFields: [
        { label: '用户名称', value: 'operatorName' },
        { label: '用户账号', value: 'operatorAccount' },
        { label: '席位IP', value: 'ipAddress' },
        { label: '登录时间', value: 'loginTime' },
        { label: '发送方式', value: 'sendMethod' },
        { label: '文件名', value: 'fileName' },
        { label: '参与方', value: 'senderParty' },
        { label: '日志类型', value: 'logType' },
        { label: '匹配结果', value: 'operationResult' }
      ],
      normalRules: [],
      whitelists: [],
      blacklists: []
    }
  },
  computed: {
    parsedConditions() {
      if (!this.detailData || !this.detailData.conditions) return []
      try {
        const conds = JSON.parse(this.detailData.conditions)
        const fieldLabelMap = {}
        this.availableFields.forEach(f => { fieldLabelMap[f.value] = f.label })
        return conds.map(c => ({ ...c, field: fieldLabelMap[c.field] || c.field }))
      } catch {
        return []
      }
    }
  },
  created() {
    this.fetchData()
    this.fetchReferences()
  },
  methods: {
    operatorOptions(field) {
      if (field === 'loginTime') {
        return ['gte', 'lte']
      }
      if (field === 'senderParty') {
        return ['crossCamp']
      }
      return ['equals', 'notEquals', 'gte', 'lte', 'lt', 'gt', 'contains', 'notContains']
    },
    operatorLabel(op) {
      const labels = {
        equals: '等于', notEquals: '不等于', gte: '大于等于', lte: '小于等于',
        lt: '小于', gt: '大于', contains: '包含', notContains: '不包含', crossCamp: '跨阵营'
      }
      return labels[op] || op
    },
    onFieldChange(cond) {
      const valid = this.operatorOptions(cond.field)
      if (valid.indexOf(cond.operator) === -1) {
        cond.operator = valid[0] || 'equals'
      }
    },
    createEmptyForm() {
      return {
        name: '',
        description: '',
        logType: null,
        ruleType: 0,
        level: null,
        linkType: 0,
        conditions: [{ field: '', operator: 'equals', value: '' }],
        combineDuration: null,
        combineCount: null,
        combineRuleId: null,
        whitelistId: null,
        blacklistId: null,
        status: 1
      }
    },
    levelType(level) {
      if (level === 1) return 'danger'
      if (level === 2) return 'warning'
      return 'info'
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
    async fetchReferences() {
      try {
        const [allLists, normalList] = await Promise.all([
          getAccessLists(),
          page({ pageSize: 9999, ruleType: 0 })
        ])
        this.whitelists = (allLists || []).filter(l => l.type === 0)
        this.blacklists = (allLists || []).filter(l => l.type === 1)
        this.normalRules = normalList.list || []
      } catch {
        // ignore
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
    async handleEdit(row) {
      const res = await detail(row.id)
      this.formMode = 'edit'
      let conditions = res.conditions
      try {
        const parsed = JSON.parse(conditions)
        if (Array.isArray(parsed) && parsed.length > 0) {
          conditions = parsed
        }
      } catch {}
      const conds = Array.isArray(conditions) ? conditions : [{ field: '', operator: '等于', value: '' }]
      this.form = {
        id: res.id,
        name: res.name,
        description: res.description || '',
        logType: res.logType,
        ruleType: res.ruleType,
        level: res.level,
        linkType: res.linkType != null ? res.linkType : 0,
        conditions: conds,
        combineDuration: res.combineDuration,
        combineCount: res.combineCount,
        combineRuleId: res.combineRuleId,
        whitelistId: res.whitelistId,
        blacklistId: res.blacklistId,
        status: res.status
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
        await this.$confirm(`确认删除告警规则【${row.name}】？删除后无法恢复`, '操作确认', {
          confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'
        })
        await remove(row.id)
        this.$message.success(`告警规则【${row.name}】已删除`)
        this.fetchData()
      } catch {}
    },
    async handleToggle(row) {
      try {
        await toggleStatus(row.id)
        this.$message.success(`告警规则【${row.name}】已${row.status === 1 ? '禁用' : '启用'}`)
        this.fetchData()
      } catch {}
    },
    fmtTime(ts) {
      if (!ts) return '-'
      const d = new Date(ts)
      const pad = n => String(n).padStart(2, '0')
      return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
    },
    addCondition() {
      this.form.conditions.push({ field: '', operator: 'equals', value: '' })
    },
    removeCondition(idx) {
      if (this.form.conditions.length > 1) {
        this.form.conditions.splice(idx, 1)
      } else {
        this.$message.warning('至少保留一个条件')
      }
    },
    validateConditions() {
      if (this.form.ruleType === 1) return ''
      const conds = this.form.conditions
      if (!conds || conds.length === 0) return ''
      for (const c of conds) {
        if (!c.field) return '请选择匹配字段'
        if (!c.value) return `【${c.field}】的条件值不能为空`
      }
      return ''
    },
    async handleSave() {
      const condErr = this.validateConditions()
      if (condErr) {
        this.$message.warning(condErr)
        return
      }
      this.$refs.formRef.validate(async valid => {
        if (!valid) return
        const payload = { ...this.form }
        if (!payload.combineCount) payload.combineCount = null
        if (!payload.combineRuleId) payload.combineRuleId = null
        if (!payload.whitelistId) payload.whitelistId = null
        if (!payload.blacklistId) payload.blacklistId = null
        if (payload.ruleType === 0) {
          payload.conditions = JSON.stringify(payload.conditions)
        } else {
          payload.conditions = null
          payload.linkType = null
        }
        try {
          if (this.formMode === 'add') {
            await add(payload)
            this.$message.success('告警规则创建成功')
          } else {
            await update(payload)
            this.$message.success('告警规则更新成功')
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
.alert-rule-config {
  padding: 20px;
}
.operate-bar {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}
.condition-row {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}
</style>
