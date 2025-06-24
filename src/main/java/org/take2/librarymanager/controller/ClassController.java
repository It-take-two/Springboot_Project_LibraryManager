package org.take2.librarymanager.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.take2.librarymanager.model.Class;
import org.take2.librarymanager.service.IClassService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/class")
public class ClassController {

    private final IClassService classService;

    @GetMapping("/{id}")
    public Class getById(@PathVariable("id") String id) {
        return classService.getById(id);
    }

}
