package com.unnamed.transformLink.admin.dto.req;

import lombok.Data;

/**
 * 分组排序请求
 */
@Data
public class GroupSortReqDTO {
    private String gid;
    private Integer sortOrder;
}
