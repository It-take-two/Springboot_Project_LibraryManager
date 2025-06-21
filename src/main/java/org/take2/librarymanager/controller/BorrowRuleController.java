package org.take2.librarymanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.take2.librarymanager.model.BorrowRule;
import org.take2.librarymanager.service.IBorrowRuleService;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/borrowRule")
public class BorrowRuleController {

    private final IBorrowRuleService borrowRuleService;

    /**
     * GET /borrowRule/list
     * 该接口返回所有借阅规则数据，前端可以点击“查看借阅规则”按钮时调用此接口，
     * 将数据传入前端进行展示。
     */
    @GetMapping("/list")
    public List<BorrowRule> getBorrowRules() {
        return borrowRuleService.list();
    }
}
