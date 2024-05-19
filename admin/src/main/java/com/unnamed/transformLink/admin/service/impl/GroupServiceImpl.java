package com.unnamed.transformLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unnamed.transformLink.admin.comoon.biz.user.UserContext;
import com.unnamed.transformLink.admin.dao.entity.GroupDO;
import com.unnamed.transformLink.admin.dao.mapper.GroupMapper;
import com.unnamed.transformLink.admin.dto.req.GroupSaveReqDTO;
import com.unnamed.transformLink.admin.dto.req.GroupSortReqDTO;
import com.unnamed.transformLink.admin.dto.req.GroupUpdateReqDTO;
import com.unnamed.transformLink.admin.dto.resp.GroupSearchRespDTO;
import com.unnamed.transformLink.admin.service.GroupService;
import com.unnamed.transformLink.admin.toolkit.RandomGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * 用户接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    private GroupMapper groupMapper;

    @Override
    public void saveGroup(GroupSaveReqDTO requestParam) {
        String gid = RandomGenerator.generateRandom();
        String username = UserContext.getUsername();
        while (hasGid(gid)) {gid = RandomGenerator.generateRandom();}
        GroupDO groupDO = GroupDO.builder()
                .name(requestParam.getName())
                .username(username)
                .sortOrder(0)
                .gid(gid)
                .build();
        baseMapper.insert(groupDO);
    }

    @Override
    public List<GroupSearchRespDTO> searchGroup() {
        String username = UserContext.getUsername();
        List<GroupDO> result = baseMapper.selectList(Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, username)
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime)
        );
        return BeanUtil.copyToList(result, GroupSearchRespDTO.class);
    }

    @Override
    public void updateGroup(GroupUpdateReqDTO requestParam) {
        LambdaUpdateWrapper<GroupDO> wrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, requestParam.getGid())
                .eq(GroupDO::getDelFlag, 0);
        GroupDO groupDO = GroupDO.builder().name(requestParam.getName()).build();
        baseMapper.update(groupDO, wrapper);
    }

    @Override
    public Boolean deleteGroup(String gid) {
        String username = UserContext.getUsername();
        LambdaUpdateWrapper<GroupDO> wrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, username)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDelFlag, 0);
        GroupDO groupDO = (GroupDO) new GroupDO().setDelFlag(1).setUpdateTime(new Date());
        return baseMapper.update(groupDO, wrapper) > 0;
    }

    @Override
    public void sortGroup(List<GroupSortReqDTO> requestParam) {
        requestParam.stream().forEach(param -> {
            GroupDO groupDO = GroupDO.builder()
                    .gid(param.getGid())
                    .sortOrder(param.getSortOrder())
                    .build();
            LambdaUpdateWrapper<GroupDO> wrapper = Wrappers.lambdaUpdate(GroupDO.class)
                    .eq(GroupDO::getUsername, UserContext.getUsername())
                    .eq(GroupDO::getGid, groupDO.getGid())
                    .set(GroupDO::getDelFlag, 1);
            baseMapper.update(groupDO, wrapper);
        });
    }

    private boolean hasGid(String gid) {
        GroupDO one = baseMapper.selectOne(Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 1)
                .eq(GroupDO::getGid, gid));
        return one != null;
    }
}
