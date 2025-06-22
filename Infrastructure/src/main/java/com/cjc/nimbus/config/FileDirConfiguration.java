package com.cjc.nimbus.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class FileDirConfiguration {

    @Value("${file.temp-dir:temp}")
    private String tempDir;


    /**
     * 生成临时文件路径
     * @param fileName 文件名
     * @return 文件路径
     */
    public String generateTempFilePath(String fileName) {
        return tempDir + "/" + fileName;
    }
}
