package org.take2.librarymanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.take2.librarymanager.model.Clc;
import org.take2.librarymanager.service.IClcService;

@RestController
@RequestMapping("/clc")
public class ClcController {

    @Autowired
    private IClcService clcService;

    /**
     * 内部 record：返回前端级联选择器所需的分类数据格式
     * value：分类代码
     * label：分类名称
     * children：子分类列表（若有）
     */
    public record ClcOption(String value, String label, List<ClcOption> children) {}

    /**
     * 获取分类树，返回给前端用于筛选。
     * GET /clc/tree
     */
    @GetMapping("/tree")
    public List<ClcOption> getClcTree() {
        // 查询所有分类记录
        List<Clc> list = clcService.list();
        // 用于保存所有节点，以代码作为 key
        Map<String, ClcOption> map = new HashMap<>();
        // 用于存放根节点（没有父分类）的记录
        List<ClcOption> roots = new ArrayList<>();

        // 先将所有 Clc 转换为 ClcOption，其中 children 初始为一个空集合
        for (Clc clc : list) {
            ClcOption option = new ClcOption(clc.getCode(), clc.getName(), new ArrayList<>());
            map.put(clc.getCode(), option);
        }
        // 根据 parentCode 组织成树形结构
        for (Clc clc : list) {
            String parentCode = clc.getParentCode();
            if (parentCode != null && !parentCode.isEmpty()) {
                ClcOption parent = map.get(parentCode);
                if (parent != null) {
                    parent.children().add(map.get(clc.getCode()));
                } else {
                    // 若未找到父节点，则当作根节点
                    roots.add(map.get(clc.getCode()));
                }
            } else {
                // 没有父分类，则直接作为根节点
                roots.add(map.get(clc.getCode()));
            }
        }
        return roots;
    }

    /**
     * 新增根据 id 查询分类的方法
     * GET /clc/{id}
     *
     * 该接口通过分类 id（即 clc 表中的 code）查询单个分类记录，
     * 前端调用该接口即可获取单个分类的详细信息。
     */
    @GetMapping("/{id}")
    public Clc getClcById(@PathVariable("id") String id) {
        return clcService.getById(id);
    }
}
