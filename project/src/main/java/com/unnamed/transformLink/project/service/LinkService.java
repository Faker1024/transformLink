package com.unnamed.transformLink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.unnamed.transformLink.project.dao.entity.LinkDO;
import com.unnamed.transformLink.project.dto.req.LinkCreateReqDTO;
import com.unnamed.transformLink.project.dto.req.LinkPageReqDTO;
import com.unnamed.transformLink.project.dto.resp.LinkCreateRespDTO;
import com.unnamed.transformLink.project.dto.resp.LinkPageRespDTO;

public interface LinkService extends IService<LinkDO> {
    LinkCreateRespDTO createLink(LinkCreateReqDTO requestParam);

    IPage<LinkPageRespDTO> pageLink(LinkPageReqDTO requestParam);
}
