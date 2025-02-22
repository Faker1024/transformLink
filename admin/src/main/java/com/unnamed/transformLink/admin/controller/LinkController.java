package com.unnamed.transformLink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.unnamed.transformLink.admin.comoon.convention.result.Result;
import com.unnamed.transformLink.admin.comoon.convention.result.Results;
import com.unnamed.transformLink.admin.remote.LinkRemoteService;
import com.unnamed.transformLink.admin.remote.dto.req.LinkCreateReqDTO;
import com.unnamed.transformLink.admin.remote.dto.req.LinkPageReqDTO;
import com.unnamed.transformLink.admin.remote.dto.req.LinkUpdateReqDTO;
import com.unnamed.transformLink.admin.remote.dto.resp.LinkCountGroupQueryRespDTO;
import com.unnamed.transformLink.admin.remote.dto.resp.LinkCreateRespDTO;
import com.unnamed.transformLink.admin.remote.dto.resp.LinkPageRespDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LinkController {

    private final LinkRemoteService linkRemoteService = new LinkRemoteService() {
    };

    /**
     * 创建短链接
     */
    @PostMapping("/api/transform-link/project/v1/admin/create")
    public Result<LinkCreateRespDTO> createLink(@RequestBody LinkCreateReqDTO requestParam){
        return linkRemoteService.createLink(requestParam);
    }

    /**
     * 分页查询
     */
    @GetMapping("/api/transform-link/project/v1/admin/page")
    public Result<IPage<LinkPageRespDTO>> pageLink(LinkPageReqDTO requestParam){
        return linkRemoteService.pageLink(requestParam);
    }

    /**
     * 查询短链接分组数量
     */
    @GetMapping("/api/transform-link/project/v1/admin/count")
    public Result<List<LinkCountGroupQueryRespDTO>> listGroupLinkCount(@RequestParam List<String> requestParam){
        return linkRemoteService.listGroupLinkCount(requestParam);
    }


    @PostMapping("/api/transform-link/project/v1/link/update")
    public Result<Void> updateLink(@RequestBody LinkUpdateReqDTO requestParam){
        linkRemoteService.updateLink(requestParam);
        return Results.success();
    }

}
