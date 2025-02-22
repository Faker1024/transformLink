package com.unnamed.transformLink.admin.controller;


import com.unnamed.transformLink.admin.comoon.convention.result.Result;
import com.unnamed.transformLink.admin.comoon.convention.result.Results;
import com.unnamed.transformLink.admin.dto.req.GroupSaveReqDTO;
import com.unnamed.transformLink.admin.dto.req.GroupSortReqDTO;
import com.unnamed.transformLink.admin.dto.req.GroupUpdateReqDTO;
import com.unnamed.transformLink.admin.dto.resp.GroupSearchRespDTO;
import com.unnamed.transformLink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接分组控制器
 */
@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * 新增组名
     */
    @PostMapping("/api/transform-link/admin/v1/group")
    public Result<Void> save(@RequestBody GroupSaveReqDTO requestParam){
        groupService.saveGroup(requestParam.getName());
        return Results.success();
    }

    /**
     *  查询个人所拥有的分组名称
     */
    @GetMapping("/api/transform-link/admin/v1/group")
    public Result<List<GroupSearchRespDTO>> search(){
        return Results.success(groupService.searchGroup());
    }

    /**
     * 更新分组信息
     */
    @PutMapping("/api/transform-link/admin/v1/group")
    public Result<Void> update(@RequestBody GroupUpdateReqDTO requestParam){
        groupService.updateGroup(requestParam);
        return Results.success();
    }

    /**
     * 删除分组
     */
    @DeleteMapping("/api/transform-link/admin/v1/group")
    public Result<Boolean> delete(@RequestParam String gid){
        return Results.success(groupService.deleteGroup(gid));
    }

    /**
     * 短连接分组排序
     */
    @PostMapping("/api/transform-link/admin/v1/group/sort")
    public  Result<Void> sortGroup(@RequestBody List<GroupSortReqDTO> requestParam){
        groupService.sortGroup(requestParam);
        return Results.success();
    }



}
