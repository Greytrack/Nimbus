package com.cjc.nimbus.utils;


import com.cjc.nimbus.exception.AppException;
import com.cjc.nimbus.result.GlobalResultCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public final class RestTemplateUtils {

    private static RestTemplate restTemplate = new RestTemplate();

    private HttpEntity<?> httpEntity;

    /**
     * 构造函数已经私有化，只能通过create方法构造HttpContent
     *
     * @return HttpContent
     */
    public static RestTemplateUtils create() {
        return new RestTemplateUtils();
    }

    /**
     * get请求返回模板对象
     *
     * @param url   url
     * @param clazz T.class
     * @param <T>   T
     * @return T
     */
    public static <T> T getForObj(String url, Class<T> clazz) {
        return RestTemplateUtils.restTemplate.getForObject(url, clazz);
    }

    /**
     * get请求返回模板对象
     *
     * @param url   url
     * @param clazz t.class
     * @param <T>   模板
     * @return T
     */
    public <T> T postForObj(String url, Class<T> clazz) {
        this.build();
        log.info("url:{}, httpEntity:{}", url, this.httpEntity);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.POST, this.httpEntity, clazz);
        log.info("responseEntity:{}", responseEntity);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            log.error("Unexpected status code: {}", responseEntity.getStatusCode());
            throw new AppException(GlobalResultCode.UNKNOWN_ERROR);
        }
    }

    /**
     * header
     */
    private HttpHeaders httpHeaders;

    private String contentType;

    /**
     * body
     */
    private String body;

    private HashMap<String, Object> params = new HashMap<>();

    private RestTemplateUtils() {
    }

    /**
     * 添加Header内容
     *
     * @param headerName  key
     * @param headerValue value
     * @return HttpContent
     */
    public RestTemplateUtils addHeader(String headerName, String headerValue) {
        if (Objects.isNull(httpHeaders)) {
            httpHeaders = new HttpHeaders();
        }
        httpHeaders.add(headerName, headerValue);
        return this;
    }

    /**
     * 设置请求体
     *
     * @param key   key
     * @param value value
     * @return HttpContent
     */
    public RestTemplateUtils addBody(String key, Object value) {
        if (Objects.isNull(params)) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }

    /**
     * 设置请求体
     *
     * @param value value
     * @return HttpContent
     */
    public RestTemplateUtils addBody(Object value) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(value);
        this.body = jsonNode.toString();
        return this;
    }

    /**
     * 设置content-Type
     *
     * @param contentType string
     * @return HttpContent
     */
    public RestTemplateUtils setContentType(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            throw new RuntimeException("content-Type can not be empty,please check your mediaType and try again ~");
        }
        this.contentType = contentType;
        if (Objects.isNull(httpHeaders)) {
            httpHeaders = new HttpHeaders();
        }
        httpHeaders.setContentType(MediaType.parseMediaType(contentType));
        return this;
    }

    /**
     * 构建HttpEntity对象
     * 不设置Content-Type默认application/json
     */
    private void build() {

        if (StringUtils.isNotBlank(this.body)) {
            this.httpEntity = new HttpEntity<>(this.body, httpHeaders);
            return;
        }

        if (Objects.isNull(this.params)) {
            throw new RuntimeException("body must be not empty,please add body and try again ~");
        }
        if (Objects.isNull(contentType) || ContentType.APPLICATION_JSON.getMimeType().equals(contentType)) {
            this.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(params);
            String httpBody = jsonNode.toString();
            this.httpEntity = new HttpEntity<>(httpBody, httpHeaders);
        } else if (ContentType.APPLICATION_FORM_URLENCODED.getMimeType().equals(contentType)) {
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                formData.add(key, value);
            }
            this.httpEntity = new HttpEntity<>(formData, httpHeaders);
        } else {
            throw new RuntimeException("sorry this tool only support content-Type include {application/json,multipart/form-data}");
        }
    }

}

