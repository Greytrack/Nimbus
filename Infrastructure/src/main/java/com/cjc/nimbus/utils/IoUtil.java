package com.cjc.nimbus.utils;

import com.cjc.nimbus.exception.AppException;
import com.cjc.nimbus.result.GlobalResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.util.List;

/**
 * 流工具类
 *
 * @author jiangkun@airedgesoft.com
 */
@Slf4j
public class IoUtil extends StreamUtils {

    /**
     * closeQuietly
     *
     * @param closeable 自动关闭
     */
    public static void closeQuietly(@Nullable Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    /**
     * InputStream to String utf-8
     *
     * @param input the <code>InputStream</code> to read from
     * @return the requested String
     */
    public static String toString(InputStream input) {
        return toString(input, java.nio.charset.StandardCharsets.UTF_8);
    }

    /**
     * InputStream to String
     *
     * @param input   the <code>InputStream</code> to read from
     * @param charset the <code>Charsets</code>
     * @return the requested String
     */
    public static String toString(@Nullable InputStream input, java.nio.charset.Charset charset) {
        try {
            return StreamUtils.copyToString(input, charset);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        } finally {
            IoUtil.closeQuietly(input);
        }
    }

    public static byte[] toByteArray(@Nullable InputStream input) {
        try {
            return StreamUtils.copyToByteArray(input);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        } finally {
            IoUtil.closeQuietly(input);
        }
    }

    /**
     * Writes chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     *
     * @param data     the <code>String</code> to write, null ignored
     * @param output   the <code>OutputStream</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws IOException if an I/O error occurs
     */
    public static void write(@Nullable final String data, final OutputStream output, final java.nio.charset.Charset encoding) throws IOException {
        if (data != null) {
            output.write(data.getBytes(encoding));
        }
    }

    /**
     * Writes chars from a <code>String</code> to bytes on an
     *
     * @param input  the <code>InputStream</code> to read from
     * @param output the <code>OutputStream</code> to write to
     * @return the number of characters written
     */
    public static List<String> readLines(Reader input, List<String> output) {
        try {
            BufferedReader reader = new BufferedReader(input);
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
            return output;
        } catch (IOException e) {
            log.error("readLines error", e);
            throw new AppException(GlobalResultCode.INTERNAL_SERVER_ERROR);
        }
    }
}
