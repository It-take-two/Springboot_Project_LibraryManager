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

    @GetMapping("/list")
    public List<BorrowRule> getBorrowRules() {
        return borrowRuleService.list();
    }
}
