package com.unnamed.transformLink.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unnamed.transformLink.project.comoon.convention.exception.ServiceException;
import com.unnamed.transformLink.project.dao.entity.LinkDO;
import com.unnamed.transformLink.project.dao.mapper.LinkMapper;
import com.unnamed.transformLink.project.dto.req.LinkCreateReqDTO;
import com.unnamed.transformLink.project.dto.resp.LinkCreateRespDTO;
import com.unnamed.transformLink.project.service.LinkService;
import com.unnamed.transformLink.project.toolkit.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * 连接接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDO> implements LinkService {

    private final RBloomFilter<String> bloomFilter;

    @Override
    public LinkCreateRespDTO createLink(LinkCreateReqDTO requestParam) {
        String shortLinkSuffix = HashUtil.hashToBase62(requestParam.getOriginUrl());
        String fullShortLink = requestParam.getDomain() + "/" + shortLinkSuffix;
        LinkDO linkDO = LinkDO.builder().originUrl(requestParam.getOriginUrl())
                .createdType(requestParam.getCreatedType())
                .describe(requestParam.getDescribe())
                .domain(requestParam.getDomain())
                .gid(requestParam.getGid())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .fullShortUrl(fullShortLink)
                .shortUri(shortLinkSuffix)
                .build();
        try {
            baseMapper.insert(linkDO);
        }catch (DuplicateKeyException es){
            log.error("短連接{}重複入庫", fullShortLink);
            throw new ServiceException("短連接生成重複");
        }
        bloomFilter.add(shortLinkSuffix);
        return LinkCreateRespDTO.builder()
                .originUrl(linkDO.getOriginUrl())
                .fullShortUrl(linkDO.getFullShortUrl())
                .gid(linkDO.getGid())
                .build();
    }

    public String generateLinkSuffix(LinkCreateReqDTO requestParam) {
        String linkSuffix = null;
        do {
            linkSuffix = HashUtil.hashToBase62(requestParam.getOriginUrl());
        }while (bloomFilter.contains(linkSuffix));
        return linkSuffix;
    }
}
