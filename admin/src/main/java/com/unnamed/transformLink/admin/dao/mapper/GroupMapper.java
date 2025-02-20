package com.unnamed.transformLink.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unnamed.transformLink.admin.dao.entity.GroupDO;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 分组持久层
 */
public interface GroupMapper extends BaseMapper<GroupDO> {

    void batchUpdate(List<GroupDO> list);

}
