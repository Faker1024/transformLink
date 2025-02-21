package com.unnamed.transformLink.admin.remote.dto.resp;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 连接创建响应实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkCreateRespDTO {
    /**
     * 分组信息
     */
    private String gid;

    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 短链接
     */
    private String fullShortUrl;
}
