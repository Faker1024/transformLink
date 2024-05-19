package com.unnamed.transformLink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unnamed.transformLink.admin.dao.entity.GroupDO;
import com.unnamed.transformLink.admin.dto.req.GroupSaveReqDTO;
import com.unnamed.transformLink.admin.dto.req.GroupSortReqDTO;
import com.unnamed.transformLink.admin.dto.req.GroupUpdateReqDTO;
import com.unnamed.transformLink.admin.dto.resp.GroupSearchRespDTO;

import java.util.List;

/**
 * 短链接分组接口层
 */
public interface GroupService extends IService<GroupDO> {


    /**
     * 新增短链接分组名
     * @param groupName 组名
     */
    void saveGroup(GroupSaveReqDTO groupName);

    /**
     * 查询用户创建的分组
     * @return 短连接分组
     */
    List<GroupSearchRespDTO> searchGroup();

    void updateGroup(GroupUpdateReqDTO requestParam);

    Boolean deleteGroup(String gid);

    void sortGroup(List<GroupSortReqDTO> requestParam);
}
