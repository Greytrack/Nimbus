package com.cjc.nimbus.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: HttpUtils
 * @Description: HTTP工具类
 * @Author: DaiYaLu
 * @DateTime: 2022/10/9 17:45
 * @Version: 1.0
 */
@Slf4j
public class HttpUtil {

    //========================================HTTP工具类============================================================

    private HttpUtil() {
        throw new IllegalStateException("HttpUtils class");
    }

    /**
     * 冒号（英文）
     */
    private static final String COLON_EN = ":";
    /**
     * 斜杠（英文）
     */
    private static final String SCHEMA_SEPARATOR = "//";

    /**
     * 中括号（左）（中文）
     */
    private static final String MIDDLE_BRACKETS_LEFT_CN = "【";

    /**
     * 中括号（右）（中文）
     */
    private static final String MIDDLE_BRACKETS_RIGHT_CN = "】";

    /**
     * 分号（英文）
     */
    private static final String SEMICOLON_EN = ";";

    /**
     * post
     */
    private static final String WORD_POST = "post";

    /**
     * put
     */
    private static final String WORD_PUT = "put";

    /**
     * 请求返回异常状态码
     */
    private static final String WORD_ERROR_STATUS = "请求返回异常状态码";

    /**
     * url
     */
    private static final String WORD_URL = "url";

    /**
     * statusCode
     */
    private static final String WORD_STATUS_CODE = "statusCode";

    /**
     * reasonPhrase
     */
    private static final String WORD_REASON_PHRASE = "reasonPhrase";

    /**
     * 连接符
     */
    private static final String WORD_CONNECTOR = "-";

    /**
     * 获取日志打印前缀
     *
     * @param uuid       唯一标志
     * @param methodName 方法名
     * @return String
     */
    public static String getPrefixStr(String uuid, String methodName) {
        return mergeMiddleBrackets(uuid) + methodName + COLON_EN;
    }

    /**
     * 拼接信息_中括号（汉语）
     *
     * @param msg 信息
     * @return String
     */
    private static String mergeMiddleBrackets(String msg) {
        return MIDDLE_BRACKETS_LEFT_CN + msg + MIDDLE_BRACKETS_RIGHT_CN;
    }

    /**
     * 获取域名
     */
    public static String getDomainName(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        log.info("请求的url: {}", url);
        String scheme = request.getScheme();
        String domainName = request.getServerName();
        // 获取X-Forwarded-Host头信息，即原始域名
        String host = request.getHeader("X-Forwarded-Host");
        if (StringUtils.isBlank(host)) {
            host = domainName;
        }
        // 获取X-Forwarded-Port头信息，即原始端口
        String port = request.getHeader("X-Forwarded-Port");
        if (StringUtils.isBlank(port)) {
            port = request.getServerPort() + "";
        }
        // 拼接域名和端口
        String fullDomain = scheme + COLON_EN + SCHEMA_SEPARATOR + host + ("80".equals(port) || "443".equals(port) ? "" : ":" + port);
        log.info("请求的域名: {}", fullDomain);
        return fullDomain;
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @param requestConfig
     * @param uuid
     */
    public static String get(String url, List<BasicHeader> headers, RequestConfig requestConfig, String uuid)
            throws IOException {
        // 获取方法名
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        // 获取日志前缀
        String prefixStr = HttpUtil.getPrefixStr(uuid, methodName);
        // 创建http客户端
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // 通过httpget方式来实现我们的get请求
            HttpGet httpGet = new HttpGet(url);
            // 设置头信息
            if (CollectionUtils.isEmpty(headers)) {
                headers = initHttpHead();
            }
            for (BasicHeader bh : headers) {
                httpGet.setHeader(bh);
            }
            // 设置超时时间
            httpGet.setConfig(setRequestConfig(requestConfig));
            // 通过client调用execute方法，得到我们的执行结果就是一个response，所有的数据都封装在response里面了
            HttpResponse httpResponse = client.execute(httpGet);
            // 返回json串
            return returnJsonString(url, prefixStr, httpResponse);
        }
    }

    /**
     * http请求（raw格式） 【常用】参数传json，请使用本方法
     *
     * @param postUrl        请求地址
     * @param requestJsonStr 请求json字符串
     * @param headers        请求头信息
     * @param requestConfig  请求配置信息
     * @param uuid           请求唯一标志
     * @return String
     * @throws IOException 抛出自定义异常
     */
    public static String post(String postUrl, String requestJsonStr, List<BasicHeader> headers,
                              RequestConfig requestConfig, String uuid) throws IOException {
        // 获取方法名
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        // 获取日志前缀
        String prefixStr = HttpUtil.getPrefixStr(uuid, methodName);
        // 创建http客户端
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建post对象
            HttpPost httpPost = new HttpPost(postUrl);
            // 设置头信息
            setHeaders(headers, httpPost);
            // 设置超时时间
            requestConfig = setRequestConfig(requestConfig);
            httpPost.setConfig(requestConfig);
            // 设置消息实体
            StringEntity se = setStringEntity(requestJsonStr);
            httpPost.setEntity(se);
            // 执行post请求
            HttpResponse httpResponse = httpClient.execute(httpPost);
            // 返回json串
            return returnJsonString(postUrl, prefixStr, httpResponse);
        }
    }

    /**
     * 发送post请求（form格式）
     *
     * @param postUrl       请求地址
     * @param formParam     请求表单数据
     * @param headers       请求头信息
     * @param requestConfig 请求配置信息
     * @param multipartFile 上传的File文件路径
     * @param uuid          请求唯一标志
     * @return String
     * @throws IOException 抛出自定义异常
     */
    public static String post(String postUrl, Map<String, Object> formParam, List<BasicHeader> headers,
                              RequestConfig requestConfig, String fileKey, MultipartFile multipartFile, String uuid) throws IOException {
        // 获取方法名
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        // 获取日志前缀
        String prefixStr = HttpUtil.getPrefixStr(uuid, methodName);
        // 创建http客户端
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建post对象
            HttpPost httpPost = new HttpPost(postUrl);
            // 设置头信息
            setHeaders(headers, httpPost);
            // 设置超时时间
            requestConfig = setRequestConfig(requestConfig);
            httpPost.setConfig(requestConfig);
            /*文件上传*/
            if (multipartFile != null) {
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
                multipartEntityBuilder.setMode(HttpMultipartMode.RFC6532);
                String fileName = multipartFile.getOriginalFilename();
                multipartEntityBuilder.addBinaryBody(fileKey, multipartFile.getInputStream(),
                        ContentType.APPLICATION_OCTET_STREAM, fileName);
                for (Map.Entry<String, Object> e : formParam.entrySet()) {
                    multipartEntityBuilder.addTextBody(e.getKey(), String.valueOf(e.getValue()),
                            ContentType.create("text/plain", Consts.UTF_8));
                }
                HttpEntity reqEntity = multipartEntityBuilder.build();
                httpPost.setEntity(reqEntity);
            } else {
                // 参数转换
                List<NameValuePair> list = new ArrayList<>();
                for (Map.Entry<String, Object> e : formParam.entrySet()) {
                    list.add(new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())));
                }
                // 设置实体
                if (!CollectionUtils.isEmpty(list)) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                    httpPost.setEntity(entity);
                }
            }
            // 执行post请求
            HttpResponse httpResponse = httpClient.execute(httpPost);
            // 返回json串
            return returnJsonString(postUrl, prefixStr, httpResponse);
        }
    }

    /**
     * 发送put请求（raw格式）
     *
     * @param putUrl         请求地址
     * @param requestJsonStr 入参Json
     * @param headers        请求头信息
     * @param requestConfig  请求配置信息
     * @param uuid           唯一标识
     * @return 返参Json
     * @throws IOException 抛出IO异常
     */
    public static String put(String putUrl, String requestJsonStr, List<BasicHeader> headers,
                             RequestConfig requestConfig, String uuid) throws IOException {
        // 获取方法名
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        // 获取日志前缀
        String prefixStr = HttpUtil.getPrefixStr(uuid, methodName);
        // 创建http客户端
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建put对象
            HttpPut httpPut = new HttpPut(putUrl);
            // 设置头信息
            if (CollectionUtils.isEmpty(headers)) {
                headers = initHttpHead();
            }
            for (BasicHeader bh : headers) {
                httpPut.setHeader(bh);
            }
            // 设置超时时间
            requestConfig = setRequestConfig(requestConfig);
            httpPut.setConfig(requestConfig);
            // 设置消息实体
            StringEntity se = setStringEntity(requestJsonStr);
            httpPut.setEntity(se);
            // 执行put请求
            HttpResponse httpResponse = httpClient.execute(httpPut);
            // 返回json串
            return returnPutJsonString(putUrl, prefixStr, httpResponse);
        }
    }

    /**
     * 获取HTTP消息头
     *
     * @return java.util.List
     */
    public static List<BasicHeader> getHttpHead() {
        return initHttpHead();
    }

    /**
     * 初始化HTTP消息头
     *
     * @return java.util.List
     */
    public static List<BasicHeader> initHttpHead() {
        List<BasicHeader> basicHeaderList = new ArrayList<>();
        basicHeaderList.add(new BasicHeader(HTTP.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        basicHeaderList.add(new BasicHeader(HTTP.CONTENT_ENCODING, StandardCharsets.UTF_8.toString()));
        basicHeaderList.add(new BasicHeader(HTTP.DATE_HEADER, new Date().toString()));
        return basicHeaderList;
    }

    /**
     * 设置头信息
     *
     * @param headers  headers
     * @param httpPost httpPost
     */
    private static void setHeaders(List<BasicHeader> headers, HttpPost httpPost) {
        if (CollectionUtils.isEmpty(headers)) {
            headers = initHttpHead();
        }
        for (BasicHeader bh : headers) {
            httpPost.setHeader(bh);
        }
    }

    /**
     * 组装超时时间
     *
     * @param connectionrequesttimeout 获取请求连接超时时间（毫秒）
     * @param connecttimeout           设置请求连接超时时间（毫秒）
     * @param sockettimeout            获取等待响应超时时间（毫秒）
     * @return RequestConfig
     */
    private static RequestConfig initTimeoutConfig(Integer connectionrequesttimeout, Integer connecttimeout,
                                                   Integer sockettimeout) {
        return RequestConfig.custom().setConnectionRequestTimeout(connectionrequesttimeout)
                .setConnectTimeout(connecttimeout).setSocketTimeout(sockettimeout).build();
    }

    /**
     * 设置超时时间
     *
     * @param requestConfig 配置对象
     * @return RequestConfig
     */
    private static RequestConfig setRequestConfig(RequestConfig requestConfig) {
        if (requestConfig == null) {
            requestConfig = initTimeoutConfig(10000, 10000, 10000);
        }
        return requestConfig;
    }

    /**
     * 设置消息实体
     *
     * @param requestJsonStr json串
     * @return StringEntity
     */
    private static StringEntity setStringEntity(String requestJsonStr) {
        if (ObjectUtils.isEmpty(requestJsonStr)) {
            return null;
        }
        StringEntity se = new StringEntity(requestJsonStr, StandardCharsets.UTF_8);
        se.setContentType(MediaType.APPLICATION_JSON_VALUE);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        return se;
    }

    /**
     * 返回json串
     *
     * @param postUrl      请求地址
     * @param prefixStr    日志前缀
     * @param httpResponse http返参
     * @return 返参json
     * @throws IOException 抛出IO异常
     */
    private static String returnJsonString(String postUrl, String prefixStr, HttpResponse httpResponse)
            throws IOException {
        /*
         * 目前只认为返参头为HTTP_200、HTTP_202时是正常返回，其余情况均视为失败。
         * 若外部接口存在其他情况，请在此处按实际修改。
         */
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_ACCEPTED) {
            // 解析返参
            return EntityUtils.toString(httpResponse.getEntity());
        } else {
            // 异常打印
            printError(HttpUtil.WORD_POST, postUrl, prefixStr, httpResponse, statusCode);
            // 抛出异常
            throw new IOException(prefixStr + "HTTP返回异常状态码" + HttpUtil.WORD_CONNECTOR +
                    statusCode);
        }
    }

    /**
     * 返回json串 put
     *
     * @param postUrl      请求地址
     * @param prefixStr    日志前缀
     * @param httpResponse http返参
     * @return 返参json
     * @throws IOException 抛出IO异常
     * @throws IOException 抛出自定义异常
     */
    private static String returnPutJsonString(String postUrl, String prefixStr, HttpResponse httpResponse)
            throws IOException {
        /*
         * 目前只认为返参头为HTTP_200、HTTP_202时是正常返回，其余情况均视为失败。
         * 若外部接口存在其他情况，请在此处按实际修改。
         */
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_ACCEPTED) {
            // 解析返参
            return EntityUtils.toString(httpResponse.getEntity());
        } else {
            // 异常打印
            printError(HttpUtil.WORD_PUT, postUrl, prefixStr, httpResponse, statusCode);
            // 抛出异常
            throw new IOException(prefixStr + "HTTP返回异常状态码" + HttpUtil.WORD_CONNECTOR +
                    statusCode);
        }
    }

    /**
     * 异常信息打印
     *
     * @param method       方法名
     * @param url          地址
     * @param prefixStr    日志前缀
     * @param httpResponse 原因
     * @param statusCode   状态码
     */
    private static void printError(String method, String url, String prefixStr, HttpResponse httpResponse,
                                   int statusCode) {
        log.error(prefixStr + method + HttpUtil.WORD_ERROR_STATUS +
                HttpUtil.mergeMiddleBrackets(method + HttpUtil.WORD_URL) + url + SEMICOLON_EN +
                HttpUtil.mergeMiddleBrackets(HttpUtil.WORD_STATUS_CODE) + statusCode + SEMICOLON_EN +
                HttpUtil.mergeMiddleBrackets(HttpUtil.WORD_REASON_PHRASE) +
                httpResponse.getStatusLine().getReasonPhrase() + SEMICOLON_EN);
    }

    /**
     * 发送post请求
     *
     * @param url      路径
     * @param str      参数(json类型)
     * @param encoding 编码格式
     */
    public static String send(String url, String str, String encoding) {
        String body = "";

        // 创建httpclient对象
        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
            // 创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);

            // 装填参数
            StringEntity s = new StringEntity(str, "utf-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()));
            // 设置参数到请求对象中
            httpPost.setEntity(s);

            // 设置header信息
            // 指定报文头【Content-type】、【User-Agent】
            httpPost.setHeader("Content-type", ContentType.APPLICATION_JSON.getMimeType());
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            // 执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpPost);
            // 获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, encoding);
            }
            EntityUtils.consume(entity);
            // 释放链接
            response.close();
        } catch (Exception e) {
            log.info("请求错误 : {}", e.getMessage());
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                log.info("请求错误 : {}", e.getMessage());
            }
        }
        return body;
    }
}
