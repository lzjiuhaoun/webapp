package com.ankki.webapp.controller.config;

import com.ankki.webapp.common.WebResult;
import com.ankki.webapp.controller.BaseController;
import com.ankki.webapp.model.config.AccessList;
import com.ankki.webapp.service.AccessListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alert-access-list")
public class AccessListController extends BaseController {

    @Autowired
    private AccessListService accessListService;

    @GetMapping
    public WebResult page(@RequestParam(defaultValue = "1") Integer pageNo,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) Byte type) {
        Map<String, Object> result = accessListService.page(keyword, type, pageNo, pageSize);
        return WebResult.success(result);
    }

    @GetMapping("/detail")
    public WebResult detail(@RequestParam Integer id) {
        AccessList record = accessListService.detail(id);
        if (record == null) {
            return WebResult.error("黑白名单不存在");
        }
        return WebResult.success(record);
    }

    @GetMapping("/names")
    public WebResult names(@RequestParam(required = false) Byte type) {
        List<AccessList> list = accessListService.getEnabledLists(type);
        return WebResult.success(list);
    }

    @PostMapping
    public WebResult add(@RequestBody AccessList record) {
        if (record == null || !StringUtils.hasText(record.getName())) {
            return WebResult.error("规则名称不能为空");
        }
        Integer id = accessListService.add(record);
        return WebResult.success("创建成功", id);
    }

    @PutMapping
    public WebResult update(@RequestBody AccessList record) {
        if (record == null || record.getId() == null) {
            return WebResult.error("参数错误");
        }
        if (!StringUtils.hasText(record.getName())) {
            return WebResult.error("规则名称不能为空");
        }
        accessListService.update(record);
        return WebResult.success("更新成功", record.getId());
    }

    @PostMapping("/delete")
    public WebResult delete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return WebResult.error("请选择要删除的条目");
        }
        int count;
        if (ids.size() == 1) {
            count = accessListService.delete(ids.get(0));
        } else {
            count = accessListService.batchDelete(ids);
        }
        return WebResult.success("成功删除" + count + "条", count);
    }

    @PutMapping("/status")
    public WebResult toggleStatus(@RequestBody Map<String, Integer> body) {
        Integer id = body != null ? body.get("id") : null;
        if (id == null) {
            return WebResult.error("参数错误");
        }
        int count = accessListService.toggleStatus(id);
        return count > 0 ? WebResult.success("状态已更新") : WebResult.error("黑白名单不存在");
    }
}
