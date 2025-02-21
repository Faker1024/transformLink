package com.unnamed.transformLink.admin.remote.dto.resp;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 短链接分组查询返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkCountGroupQueryRespDTO {

    /**
     * 分组id
     */
    private String gid;

    /**
     * 短链接数量
     */
    private Integer linkCount;
}
