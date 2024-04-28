package com.unnamed.transformLink.admin.controller;


import com.unnamed.transformLink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接分组控制器
 */
@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;


}
