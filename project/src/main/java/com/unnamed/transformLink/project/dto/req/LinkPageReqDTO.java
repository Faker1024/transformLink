package com.unnamed.transformLink.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.unnamed.transformLink.project.dao.entity.LinkDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 连接分页请求实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkPageReqDTO extends Page<LinkDO> {

    /**
     * 分组标识
     */
    private String gid;


}
