package com.cjc.nimbus.utils;

import com.cjc.nimbus.exception.AppException;
import com.cjc.nimbus.result.GlobalResultCode;
import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author: jiangkun@airedgesoft.com
 * @date: 2024/9/4 11:16
 */
@Slf4j
public class FreeMakerUtil {


    /**
     * 渲染模板
     *
     * @param path   模板路径
     * @param params 模板参数
     * @return 渲染后的html文本
     */
    public String render(String path, Map<String, Object> params) {
        String htmlText = "";
        try {
            //获取模板实例
            Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            //设置模板文件的目录
            configuration.setClassForTemplateLoading(this.getClass(), path.substring(0, path.lastIndexOf("/")));
            configuration.setDefaultEncoding("UTF-8");
            //空值报错设置
            configuration.setClassicCompatible(true);
            configuration.setNewBuiltinClassResolver(TemplateClassResolver.SAFER_RESOLVER);
            Template template = configuration.getTemplate(path.substring(path.lastIndexOf("/")));
            //解析模板文件
            htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
        } catch (IOException | TemplateException e) {
            log.error("Failed to render html template", e);
            throw new AppException(GlobalResultCode.INTERNAL_SERVER_ERROR);
        }
        return htmlText;
    }
}
