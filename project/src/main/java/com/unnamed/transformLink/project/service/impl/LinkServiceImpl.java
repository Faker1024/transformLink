package com.unnamed.transformLink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unnamed.transformLink.project.comoon.convention.exception.ClientException;
import com.unnamed.transformLink.project.comoon.convention.exception.ServiceException;
import com.unnamed.transformLink.project.comoon.enums.VailDateTypeEnum;
import com.unnamed.transformLink.project.dao.entity.LinkDO;
import com.unnamed.transformLink.project.dao.mapper.LinkMapper;
import com.unnamed.transformLink.project.dto.req.LinkCreateReqDTO;
import com.unnamed.transformLink.project.dto.req.LinkPageReqDTO;
import com.unnamed.transformLink.project.dto.req.LinkUpdateReqDTO;
import com.unnamed.transformLink.project.dto.resp.LinkCountGroupQueryRespDTO;
import com.unnamed.transformLink.project.dto.resp.LinkCreateRespDTO;
import com.unnamed.transformLink.project.dto.resp.LinkPageRespDTO;
import com.unnamed.transformLink.project.service.LinkService;
import com.unnamed.transformLink.project.toolkit.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 连接接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDO> implements LinkService {

    private final RBloomFilter<String> bloomFilter;

    private LinkMapper linkMapper;

    @Value("${transform-link.domain.default}")
    private String createTransformLinkDefaultDomain;

    @Override
    public LinkCreateRespDTO createLink(LinkCreateReqDTO requestParam) {
        String shortLinkSuffix = generateLinkSuffix(requestParam);
        String fullShortLink = StrBuilder.create("http://")
                .append(createTransformLinkDefaultDomain)
                .append("/")
                .append(shortLinkSuffix)
                .toString();
        LinkDO linkDO = LinkDO.builder().originUrl(requestParam.getOriginUrl())
                .createdType(requestParam.getCreatedType())
                .describe(requestParam.getDescribe())
                .domain(requestParam.getDomain())
                .gid(requestParam.getGid())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .enableStatus(1)
                .fullShortUrl(fullShortLink)
                .shortUri(shortLinkSuffix)
                .build();
        try {
            baseMapper.insert(linkDO);
        } catch (DuplicateKeyException es) {
            LambdaQueryWrapper<LinkDO> eq = Wrappers.lambdaQuery(LinkDO.class)
                    .eq(LinkDO::getOriginUrl, linkDO.getOriginUrl())
                    .eq(LinkDO::getDelFlag, 0);
            if (baseMapper.selectCount(eq) > 0) {
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

    @Override
    public IPage<LinkPageRespDTO> pageLink(LinkPageReqDTO requestParam) {
        LambdaQueryWrapper<LinkDO> wrapper = Wrappers.lambdaQuery(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getDelFlag, 0)
                .eq(LinkDO::getEnableStatus, 1)
                .orderByDesc(LinkDO::getCreateTime);
        IPage<LinkDO> resultPage = baseMapper.selectPage(requestParam, wrapper);
        return resultPage.convert(each -> BeanUtil.copyProperties(each, LinkPageRespDTO.class));
    }

    @Override
    public List<LinkCountGroupQueryRespDTO> listGroupLinkCount(List<String> gids) {
        QueryWrapper<LinkDO> wrapper = Wrappers.query(new LinkDO())
                .select("gid as gid, count(*) as linkCount")
                .in("gid", gids)
                .eq("enable_status", 1)
                .groupBy("gid");
        List<Map<String, Object>> linkCountList = baseMapper.selectMaps(wrapper);
        return BeanUtil.copyToList(linkCountList, LinkCountGroupQueryRespDTO.class);
    }

    @Override
    @Transactional
    public void updateLink(LinkUpdateReqDTO requestParam) {
        LambdaUpdateWrapper<LinkDO> queryWrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(LinkDO::getDelFlag, 0)
                .eq(LinkDO::getEnableStatus, 1);
        LinkDO linkDO = baseMapper.selectOne(queryWrapper);
        if (Objects.isNull(linkDO)) throw new ClientException("短链接记录不存在");
        LinkDO updateDO = LinkDO.builder()
                .domain(linkDO.getDomain())
                .shortUri(linkDO.getShortUri())
                .clickNum(linkDO.getClickNum())
                .favicon(linkDO.getFavicon())
                .createdType(linkDO.getCreatedType())
                .gid(requestParam.getGid())
                .originUrl(requestParam.getOriginUrl())
                .describe(requestParam.getDescribe())
                .validDate(requestParam.getValidDate())
                .validDateType(requestParam.getValidDateType()).build();
        LambdaUpdateWrapper<LinkDO> updateWrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortUrl, requestParam.getOriginUrl())
                .eq(LinkDO::getDelFlag, 0)
                .eq(LinkDO::getEnableStatus, 1)
                .set(Objects.equals(requestParam.getValidDateType(), VailDateTypeEnum.PERMANENT.getType()), LinkDO::getValidDate, null);
        baseMapper.update(updateDO, updateWrapper);
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
