package com.unnamed.transformLink.admin.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分组更新请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupUpdateReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名
     */
    private String name;
}
