package com.unnamed.transformLink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.unnamed.transformLink.project.comoon.convention.result.Result;
import com.unnamed.transformLink.project.comoon.convention.result.Results;
import com.unnamed.transformLink.project.dto.req.LinkCreateReqDTO;
import com.unnamed.transformLink.project.dto.req.LinkPageReqDTO;
import com.unnamed.transformLink.project.dto.resp.LinkCreateRespDTO;
import com.unnamed.transformLink.project.dto.resp.LinkPageRespDTO;
import com.unnamed.transformLink.project.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 连接控制层
 */
@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping("/api/transform-link/project/v1/link/create")
    public Result<LinkCreateRespDTO> createLink(@RequestBody LinkCreateReqDTO requestParam){
        return Results.success(linkService.createLink(requestParam));
    }

    /**
     * 分页查询
     */
    @GetMapping("/api/transform-link/project/v1/link/page")
    public Result<IPage<LinkPageRespDTO>> pageLink(LinkPageReqDTO requestParam){
        return Results.success(linkService.pageLink(requestParam));
    }


}
