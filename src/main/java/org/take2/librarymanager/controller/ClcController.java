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

    public record ClcOption(String value, String label, List<ClcOption> children) {}

    @GetMapping("/tree")
    public List<ClcOption> getClcTree() {
        List<Clc> list = clcService.list();
        Map<String, ClcOption> map = new HashMap<>();
        List<ClcOption> roots = new ArrayList<>();

        for (Clc clc : list) {
            ClcOption option = new ClcOption(clc.getCode(), clc.getName(), new ArrayList<>());
            map.put(clc.getCode(), option);
        }
        for (Clc clc : list) {
            String parentCode = clc.getParentCode();
            if (parentCode != null && !parentCode.isEmpty()) {
                ClcOption parent = map.get(parentCode);
                if (parent != null) {
                    parent.children().add(map.get(clc.getCode()));
                } else {
                    roots.add(map.get(clc.getCode()));
                }
            } else {
                roots.add(map.get(clc.getCode()));
            }
        }
        return roots;
    }

    @GetMapping("/{id}")
    public Clc getClcById(@PathVariable("id") String id) {
        return clcService.getById(id);
    }
}
