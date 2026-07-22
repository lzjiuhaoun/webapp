package com.ankki.webapp.controller.config;

import com.ankki.webapp.common.WebResult;
import com.ankki.webapp.controller.BaseController;
import com.ankki.webapp.model.config.AccessList;
import com.ankki.webapp.service.AccessListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 黑白名单管理 REST 控制器.
 * <p>
 * 提供黑白名单的增删改查、状态切换等接口。
 * </p>
 *
 * @author AAS-SIMP
 */
@Slf4j
@RestController
@RequestMapping("/alert-access-list")
public class AccessListController extends BaseController {

    /** 黑白名单业务服务 */
    @Autowired
    private AccessListService accessListService;

    /**
     * 分页查询黑白名单列表.
     *
     * @param pageNo   页码，从1开始，默认1
     * @param pageSize 每页条数，默认10
     * @param keyword  名称关键词（模糊搜索）
     * @param type     类型筛选（0=白名单, 1=黑名单）
     * @return 分页结果
     */
    @GetMapping
    public WebResult page(@RequestParam(defaultValue = "1") Integer pageNo,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) Byte type) {
        log.info("分页查询黑白名单: pageNo={}, pageSize={}, keyword={}, type={}", pageNo, pageSize, keyword, type);
        Map<String, Object> result = accessListService.page(keyword, type, pageNo, pageSize);
        return WebResult.success(result);
    }

    /**
     * 查询黑白名单详情（含用户明细）.
     *
     * @param id 黑白名单ID
     * @return 黑白名单实体及关联用户列表
     */
    @GetMapping("/detail")
    public WebResult detail(@RequestParam Integer id) {
        log.info("查询黑白名单详情: id={}", id);
        AccessList record = accessListService.detail(id);
        if (record == null) {
            log.warn("黑白名单不存在: id={}", id);
            return WebResult.error("黑白名单不存在");
        }
        return WebResult.success(record);
    }

    /**
     * 获取所有已启用的黑白名单（供下拉选择）.
     *
     * @param type 类型筛选（可选：0=白名单, 1=黑名单）
     * @return 已启用的黑白名单列表
     */
    @GetMapping("/names")
    public WebResult names(@RequestParam(required = false) Byte type) {
        log.info("查询已启用黑白名单: type={}", type);
        List<AccessList> list = accessListService.getEnabledLists(type);
        return WebResult.success(list);
    }

    /**
     * 新增黑白名单.
     *
     * @param record 黑白名单实体（含用户列表）
     * @return 创建成功后的ID
     */
    @PostMapping
    public WebResult add(@RequestBody AccessList record) {
        log.info("新增黑白名单: name={}, type={}", record != null ? record.getName() : null, record != null ? record.getType() : null);
        if (record == null || !StringUtils.hasText(record.getName())) {
            log.warn("新增黑白名单失败: 名称为空");
            return WebResult.error("规则名称不能为空");
        }
        Integer id = accessListService.add(record);
        log.info("新增黑白名单成功: id={}, name={}", id, record.getName());
        return WebResult.success("创建成功", id);
    }

    /**
     * 更新黑白名单（含用户列表全量替换）.
     *
     * @param record 黑白名单实体（必须包含ID）
     * @return 更新后的ID
     */
    @PutMapping
    public WebResult update(@RequestBody AccessList record) {
        log.info("更新黑白名单: id={}, name={}", record != null ? record.getId() : null, record != null ? record.getName() : null);
        if (record == null || record.getId() == null) {
            log.warn("更新黑白名单失败: 参数错误");
            return WebResult.error("参数错误");
        }
        if (!StringUtils.hasText(record.getName())) {
            log.warn("更新黑白名单失败: 名称为空");
            return WebResult.error("规则名称不能为空");
        }
        accessListService.update(record);
        log.info("更新黑白名单成功: id={}", record.getId());
        return WebResult.success("更新成功", record.getId());
    }

    /**
     * 删除黑白名单（批量）.
     * <p>
     * 若黑白名单已被告警规则引用，则拒绝删除。
     * </p>
     *
     * @param ids 黑白名单ID列表
     * @return 成功删除的条数
     */
    @PostMapping("/delete")
    public WebResult delete(@RequestBody List<Integer> ids) {
        log.info("删除黑白名单: ids={}", ids);
        if (ids == null || ids.isEmpty()) {
            log.warn("删除黑白名单失败: 未选择条目");
            return WebResult.error("请选择要删除的条目");
        }
        try {
            int count;
            if (ids.size() == 1) {
                count = accessListService.delete(ids.get(0));
            } else {
                count = accessListService.batchDelete(ids);
            }
            log.info("删除黑白名单成功: count={}", count);
            return WebResult.success("成功删除" + count + "条", count);
        } catch (IllegalArgumentException e) {
            log.warn("删除黑白名单失败: {}", e.getMessage());
            return WebResult.error(e.getMessage());
        }
    }

    /**
     * 切换黑白名单启用/禁用状态.
     * <p>
     * 若黑白名单已被告警规则引用，则禁止禁用。
     * </p>
     *
     * @param body 包含 id 的JSON对象
     * @return 操作结果
     */
    @PutMapping("/status")
    public WebResult toggleStatus(@RequestBody Map<String, Integer> body) {
        Integer id = body != null ? body.get("id") : null;
        log.info("切换黑白名单状态: id={}", id);
        if (id == null) {
            log.warn("切换黑白名单状态失败: 参数错误");
            return WebResult.error("参数错误");
        }
        try {
            int count = accessListService.toggleStatus(id);
            if (count > 0) {
                log.info("切换黑白名单状态成功: id={}", id);
                return WebResult.success("状态已更新");
            } else {
                log.warn("切换黑白名单状态失败: 名单不存在, id={}", id);
                return WebResult.error("黑白名单不存在");
            }
        } catch (IllegalArgumentException e) {
            log.warn("切换黑白名单状态失败: {}", e.getMessage());
            return WebResult.error(e.getMessage());
        }
    }
}
