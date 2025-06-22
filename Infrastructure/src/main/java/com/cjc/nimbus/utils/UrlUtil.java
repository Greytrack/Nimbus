package com.cjc.nimbus.utils;

import com.cjc.nimbus.exception.AppException;
import com.cjc.nimbus.result.GlobalResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * url处理工具类
 *
 * @author jiangkun@airedgesoft.com
 */
@Slf4j
public class UrlUtil extends UriUtils {

    /**
     * url 编码，同js decodeURIComponent
     *
     * @param source  url
     * @param charset 字符集
     * @return 编码后的url
     */
    public static String encodeURL(String source, Charset charset) {
        return UriUtils.encode(source, charset.name());
    }

    /**
     * url 解码
     *
     * @param source  url
     * @param charset 字符集
     * @return 解码url
     */
    public static String decodeURL(String source, Charset charset) {
        return UriUtils.decode(source, charset.name());
    }

    /**
     * 获取url路径
     *
     * @param uriStr 路径
     * @return url路径
     */
    public static String getPath(String uriStr) {
        URI uri;

        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException var3) {
            log.error("uriStr:{} is error", uriStr);
            throw new AppException(GlobalResultCode.INTERNAL_SERVER_ERROR);
        }

        return uri.getPath();
    }


    /**
     * 参数过滤
     *
     * @param joinPoint 切点
     * @return 参数列表
     */
    public static List<Object> filterArgs(JoinPoint joinPoint) {
        List<Object> argList = new ArrayList<>();
        Object[] args = joinPoint.getArgs();
        for (Object obj : args) {
            if (addCondition(obj)) {
                argList.add(obj);
            }
        }
        return argList;
    }

    /**
     * 条件过滤
     *
     * @param obj 参数
     * @return 是否添加
     */
    private static boolean addCondition(Object obj) {
        return !(obj instanceof HttpServletRequest) && !(obj instanceof HttpServletResponse) && !(obj instanceof MultipartFile);
    }

}
