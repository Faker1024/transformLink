package com.unnamed.transformLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unnamed.transformLink.admin.dao.entity.GroupDO;
import com.unnamed.transformLink.admin.dao.mapper.GroupMapper;
import com.unnamed.transformLink.admin.dto.req.GroupSaveReqDTO;
import com.unnamed.transformLink.admin.dto.resp.GroupSearchRespDTO;
import com.unnamed.transformLink.admin.service.GroupService;
import com.unnamed.transformLink.admin.toolkit.RandomGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    @Override
    public void saveGroup(GroupSaveReqDTO requestParam) {
        String gid = RandomGenerator.generateRandom();
        while (hasGid(gid)) gid = RandomGenerator.generateRandom();
        GroupDO groupDO = GroupDO.builder()
                .name(requestParam.getName())
                .sortOrder(0)
                .gid(gid)
                .build();
        baseMapper.insert(groupDO);
    }

    @Override
    public List<GroupSearchRespDTO> searchGroup() {
        // TODO 获取用户名
        List<GroupDO> result = baseMapper.selectList(Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, "测试用户")
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime)
        );
        return BeanUtil.copyToList(result, GroupSearchRespDTO.class);
    }

    private boolean hasGid(String gid) {
        GroupDO one = baseMapper.selectOne(Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 1)
                .eq(GroupDO::getGid, gid));
        return one != null;
    }
}
