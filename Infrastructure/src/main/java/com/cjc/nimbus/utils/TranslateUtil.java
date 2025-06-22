package com.cjc.nimbus.utils;

import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 翻译工具类
 *
 * @author jiangkun@airedgesoft.com
 * @date 2024/7/18
 */
@Slf4j
@Component
public class TranslateUtil {

    private static final String TRANS_API_HOST = "https://fanyi-api.baidu.com/api/trans/vip/translate";
    private static final String APP_ID = "20240717002102140";
    private static final String SECURITY_KEY = "LJI1KuQcwkNszKG0YLQ7";
    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    /**
     * 翻译
     *
     * @param text 待翻译文本
     * @return 翻译结果
     */
    public String translate(String text) {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
        String result = null;
        try {
            // 标准版翻译，每秒请求1次
            Thread.sleep(1000);
            result = HttpUtil.get(TRANS_API_HOST + StringPool.QUESTION_MARK + UrlQuery.of(buildParams(text, "zh", "en"), true).build(Charset.defaultCharset()),
                    null, config, IdUtil.randomUUID());
            JsonNode jsonNode = JsonUtil.readTree(result);
            result = jsonNode.get("trans_result").get(0).get("dst").asText();
        } catch (Exception e) {
            log.error("翻译失败", e);
            Thread.currentThread().interrupt();
            return text;
        }
        return result;
    }

    /**
     * 构建请求参数
     *
     * @param query 待翻译文本
     * @param from  源语言
     * @param to    目标语言
     * @return 请求参数
     */
    private static Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", APP_ID);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = APP_ID + query + salt + SECURITY_KEY;
        params.put("sign", md5(src));

        return params;
    }

    /**
     * 获得一个字符串的MD5值
     *
     * @param input 输入的字符串
     * @return 输入字符串的MD5值
     */
    public static String md5(String input) {
        if (input == null) {
            return null;
        }

        try {

            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 输入的字符串转换成字节数组
            byte[] inputByteArray = input.getBytes(StandardCharsets.UTF_8);
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray);
            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 字符数组转换成字符串返回
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String byteArrayToHex(byte[] byteArray) {
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        // 字符数组组合成字符串返回
        return new String(resultCharArray);

    }
}
