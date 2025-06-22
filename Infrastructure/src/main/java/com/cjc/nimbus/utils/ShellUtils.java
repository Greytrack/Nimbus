package com.cjc.nimbus.utils;

import cn.hutool.core.util.RuntimeUtil;
import com.cjc.nimbus.exception.AppException;
import com.cjc.nimbus.result.GlobalResultCode;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ShellUtils {
    /**
     * @param scriptPath 执行的脚本路径
     * @param timeout    超时时间，毫秒数
     */
    public static ScriptResult executeScriptWithTimeout(String scriptPath, long timeout) {
        File scriptFile = new File(scriptPath);
        if (!scriptFile.exists()) {
            log.error("脚本{}不存在,停止执行", scriptPath);
            throw new AppException(GlobalResultCode.UTIL_EXECUTE_SCRIPT_ERROR);
        }
        if (!scriptFile.canExecute() && !scriptFile.setExecutable(true)) {
            log.error("脚本{}无执行权限且设置执行权限失败,停止执行", scriptPath);
            throw new AppException(GlobalResultCode.UTIL_EXECUTE_SCRIPT_ERROR);
        }

        // 构建命令
        String command = "/bin/bash " + scriptPath;
        Process process = RuntimeUtil.exec(command);
        int exitValue = -1;
        String output = "";
        boolean timedOut = false;
        // 等待命令执行完成，设置超时
        boolean completed;
        try {
            completed = process.waitFor(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AppException(GlobalResultCode.UTIL_EXECUTE_SCRIPT_ERROR);
        }

        if (!completed) {
            // 超时，终止进程
            process.destroyForcibly();
            timedOut = true;
            output = "脚本执行超时，已终止\n";
            log.error("脚本{}执行超时，已终止进程", scriptPath);
        } else {
            // 正常完成，获取退出码和输出
            exitValue = process.exitValue();
            output = RuntimeUtil.getResult(process);
            log.info("脚本{}执行完成，退出码: {}", scriptPath, process.exitValue());
        }
        return new ScriptResult(exitValue, output, timedOut);
    }

    public record ScriptResult(int exitCode, String output, boolean timedOut) {
    }
}
