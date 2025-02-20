package com.unnamed.transformLink.project.service.impl;

import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 连接接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDO> implements LinkService {

    private final RBloomFilter<String> bloomFilter;

    @Value("${transform-link.domain.default}")
    private String createTransformLinkDefaultDomain;

    @Override
    public LinkCreateRespDTO createLink(LinkCreateReqDTO requestParam) {
        String shortLinkSuffix = generateLinkSuffix(requestParam);
        String fullShortLink = StrBuilder.create("http://")
                .append(createTransformLinkDefaultDomain)
                .append( "/")
                .append(shortLinkSuffix)
                .toString();
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
            LambdaQueryWrapper<LinkDO> eq = Wrappers.lambdaQuery(LinkDO.class)
                    .eq(LinkDO::getOriginUrl, linkDO.getOriginUrl())
                    .eq(LinkDO::getDelFlag, 0);
            if (baseMapper.selectCount(eq) > 0){
                log.error("短連接{}重複入庫", fullShortLink);
                throw new ServiceException("短連接生成重複");
            }
        }
        bloomFilter.add(fullShortLink);
        return LinkCreateRespDTO.builder()
                .originUrl(linkDO.getOriginUrl())
                .fullShortUrl(linkDO.getFullShortUrl())
                .gid(linkDO.getGid())
                .build();
    }

    public String generateLinkSuffix(LinkCreateReqDTO requestParam) {
        int generateCount = 0;
        String linkSuffix = null;
        while (true) {
            if (generateCount > 10) throw new ServiceException("短連接生成频繁，请稍候再试");
            String originUrl = requestParam.getOriginUrl();
            originUrl += UUID.randomUUID().toString();
            linkSuffix = HashUtil.hashToBase62(originUrl);
            if (!bloomFilter.contains(createTransformLinkDefaultDomain + "/" + linkSuffix)) break;
            generateCount++;
        }
        return linkSuffix;
    }
}
