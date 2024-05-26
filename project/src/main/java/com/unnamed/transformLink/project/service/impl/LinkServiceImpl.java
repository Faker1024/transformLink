package com.unnamed.transformLink.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unnamed.transformLink.project.dao.entity.LinkDO;
import com.unnamed.transformLink.project.dao.mapper.LinkMapper;
import com.unnamed.transformLink.project.dto.req.LinkCreateReqDTO;
import com.unnamed.transformLink.project.dto.resp.LinkCreateRespDTO;
import com.unnamed.transformLink.project.service.LinkService;
import com.unnamed.transformLink.project.toolkit.HashUtil;
import org.springframework.stereotype.Service;

/**
 * 连接接口实现层
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDO> implements LinkService {
    @Override
    public LinkCreateRespDTO createLink(LinkCreateReqDTO requestParam) {
        String shortLinkSuffix = HashUtil.hashToBase62(requestParam.getOriginUrl());
        LinkDO linkDO = LinkDO.builder().originUrl(requestParam.getOriginUrl())
                .createdType(requestParam.getCreatedType())
                .describe(requestParam.getDescribe())
                .domain(requestParam.getDomain())
                .gid(requestParam.getGid())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .fullShortUrl(requestParam.getDomain() +"/" + shortLinkSuffix)
                .shortUri(shortLinkSuffix)
                .build();
        baseMapper.insert(linkDO);
        return LinkCreateRespDTO.builder()
                .originUrl(linkDO.getOriginUrl())
                .fullShortUrl(linkDO.getFullShortUrl())
                .gid(linkDO.getGid())
                .build();
    }
}
