package com.cjc.nimbus.utils;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 异常处理工具类
 * @author jiangkun@airedgesoft.com
 */
public class Exceptions {
	private Exceptions() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * 将CheckedException转换为UncheckedException.
	 *
	 * @param e Throwable
	 * @return {RuntimeException}
	 */
	public static RuntimeException unchecked(Throwable e) {
		Map<Class<? extends Throwable>, Function<Throwable, RuntimeException>> exceptionMappings = new HashMap<>();
		exceptionMappings.put(IllegalAccessException.class, IllegalArgumentException::new);
		exceptionMappings.put(IllegalArgumentException.class, IllegalArgumentException::new);
		exceptionMappings.put(NoSuchMethodException.class, IllegalArgumentException::new);
		exceptionMappings.put(InvocationTargetException.class, ex -> new RuntimeException(((InvocationTargetException) ex).getTargetException()));
		exceptionMappings.put(RuntimeException.class, RuntimeException.class::cast);

		return Optional.ofNullable(exceptionMappings.get(e.getClass()))
				.map(exceptionFunction -> exceptionFunction.apply(e))
				.orElseGet(() -> new RuntimeException(e));
	}

	/**
	 * 代理异常解包
	 *
	 * @param wrapped 包装过得异常
	 * @return 解包后的异常
	 */
	public static Throwable unwrap(Throwable wrapped) {
		Throwable unwrapped = wrapped;
		while (unwrapped instanceof InvocationTargetException || unwrapped instanceof UndeclaredThrowableException) {
			unwrapped = decideThrowable(unwrapped);
		}
		return unwrapped;
	}

	private static Throwable decideThrowable(Throwable unwrapped) {
		if (unwrapped instanceof InvocationTargetException invocationTargetException) {
			unwrapped = invocationTargetException.getTargetException();
		} else {
			unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
		}
		return unwrapped;
	}

	/**
	 * 将ErrorStack转化为String.
	 *
	 * @param ex Throwable
	 * @return {String}
	 */
	public static String getStackTraceAsString(Throwable ex) {
		FastStringWriter stringWriter = new FastStringWriter();
		ex.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

}
