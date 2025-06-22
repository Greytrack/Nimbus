package com.cjc.nimbus.utils;

import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CJC
 * @version 1.0
 * @description JDBC工具类，用于原生查询，绕过mybatis的拦截
 * @date 2024/7/20 下午4:58
 */
public class JdbcUtil {

    public static <T> List<T> query(Connection connection, String sql, ResultSetMapper<T> mapper, Object... params) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(sql);
            setParameters(statement, params);
            rs = statement.executeQuery();
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapper.mapRow(rs));
            }
            return result;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(statement);
        }
    }

    private static void setParameters(PreparedStatement statement, Object... params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
        }
    }

    @FunctionalInterface
    public interface ResultSetMapper<T> {
        T mapRow(ResultSet resultSet) throws SQLException;
    }
}
