package com.unnamed.transformLink.admin.remote;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.unnamed.transformLink.admin.comoon.convention.result.Result;
import com.unnamed.transformLink.admin.remote.dto.req.LinkCreateReqDTO;
import com.unnamed.transformLink.admin.remote.dto.req.LinkPageReqDTO;
import com.unnamed.transformLink.admin.remote.dto.req.LinkUpdateReqDTO;
import com.unnamed.transformLink.admin.remote.dto.resp.LinkCountGroupQueryRespDTO;
import com.unnamed.transformLink.admin.remote.dto.resp.LinkCreateRespDTO;
import com.unnamed.transformLink.admin.remote.dto.resp.LinkPageRespDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短链接中台远程调用服务
 */
public interface LinkRemoteService {

    default Result<IPage<LinkPageRespDTO>> pageLink(LinkPageReqDTO requestParam){
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gid", requestParam.getGid());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String resultStr = HttpUtil.get("http://127.0.0.1:8001/api/transform-link/project/v1/link/page", requestMap);
        return JSON.parseObject(resultStr, new TypeReference<>() {
        });
    }

    default Result<LinkCreateRespDTO> createLink(LinkCreateReqDTO requestParam){
        String resultStr = HttpUtil.createPost("http://127.0.0.1:8001/api/transform-link/project/v1/link/create")
                .header("Content-Type", "application/json")
                .body(JSON.toJSONString(requestParam)).execute().body();
        return JSON.parseObject(resultStr, new TypeReference<>() {});
    }

    default Result<List<LinkCountGroupQueryRespDTO>> listGroupLinkCount(List<String> requestParam){
        HashMap<String, Object> request = new HashMap<>();
        request.put("requestParam", requestParam);
        String resultStr = HttpUtil.get("http://127.0.0.1:8001/api/transform-link/project/v1/link/count", request);
        return JSON.parseObject(resultStr, new TypeReference<>() {});
    }

    default void updateLink(LinkUpdateReqDTO requestParam){
        String resultStr = HttpUtil.createPost("http://127.0.0.1:8001/api/transform-link/project/v1/link/update")
                .header("Content-Type", "application/json")
                .body(JSON.toJSONString(requestParam)).execute().body();
    };
}
