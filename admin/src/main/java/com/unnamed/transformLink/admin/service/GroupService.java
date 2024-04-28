package com.unnamed.transformLink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unnamed.transformLink.admin.dao.entity.GroupDO;
import com.unnamed.transformLink.admin.dto.req.GroupSaveReqDTO;

/**
 * 短链接分组接口层
 */
public interface GroupService extends IService<GroupDO> {


    /**
     * 新增短链接分组名
     * @param groupName
     */
    void saveGroup(GroupSaveReqDTO groupName);
}
