package com.unnamed.transformLink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.unnamed.transformLink.project.dao.entity.LinkDO;
import com.unnamed.transformLink.project.dto.req.LinkCreateReqDTO;
import com.unnamed.transformLink.project.dto.req.LinkPageReqDTO;
import com.unnamed.transformLink.project.dto.req.LinkUpdateReqDTO;
import com.unnamed.transformLink.project.dto.resp.LinkCountGroupQueryRespDTO;
import com.unnamed.transformLink.project.dto.resp.LinkCreateRespDTO;
import com.unnamed.transformLink.project.dto.resp.LinkPageRespDTO;

import java.util.List;

public interface LinkService extends IService<LinkDO> {
    LinkCreateRespDTO createLink(LinkCreateReqDTO requestParam);

    IPage<LinkPageRespDTO> pageLink(LinkPageReqDTO requestParam);


    List<LinkCountGroupQueryRespDTO> listGroupLinkCount(List<String> gids);

    void updateLink(LinkUpdateReqDTO requestParam);
}
