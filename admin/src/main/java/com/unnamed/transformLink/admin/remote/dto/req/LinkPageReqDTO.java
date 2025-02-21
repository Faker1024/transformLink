package com.unnamed.transformLink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class LinkPageReqDTO extends Page {

    /**
     * 分组标识
     */
    private String gid;


}
