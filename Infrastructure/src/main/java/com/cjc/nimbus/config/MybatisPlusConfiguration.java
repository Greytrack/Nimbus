package com.cjc.nimbus.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.AllArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;


/**
 * @author jiangkun@airedgesoft.com
 */
@Configuration
@AllArgsConstructor
@MapperScan(value = {
        "com.cjc.nimbus.**"

})
public class MybatisPlusConfiguration {

    /**
     * mybatis-plus 拦截器集合
     */
    @Bean
    @ConditionalOnMissingBean(value = {MybatisPlusInterceptor.class})
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 配置分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    @Bean
    @Primary
    public AirEdgeSqlInjector airEdgeSqlInjector() {
        return new AirEdgeSqlInjector();
    }


    public static class AirEdgeSqlInjector extends DefaultSqlInjector {

        @Override
        public List<AbstractMethod> getMethodList(org.apache.ibatis.session.Configuration configuration, Class<?> mapperClass, TableInfo tableInfo) {
            List<AbstractMethod> methodList = super.getMethodList(configuration, mapperClass, tableInfo);
            methodList.add(new InsertBatchSomeColumn(x -> x.getFieldFill() != FieldFill.UPDATE));
            return methodList;
        }
    }

}

