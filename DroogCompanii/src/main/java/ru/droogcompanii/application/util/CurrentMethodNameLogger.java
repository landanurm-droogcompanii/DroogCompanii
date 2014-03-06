package ru.droogcompanii.application.util;

/**
 * Created by ls on 28.02.14.
 */
public class CurrentMethodNameLogger {
    private static final int INDEX_OF_INVOKER_METHOD = 4;

    private final String className;

    public CurrentMethodNameLogger(Class<?> invokerClass) {
        className = invokerClass.getSimpleName();
    }

    public void log(final Object... additional) {
        executeIfInvokerMethodNameIsKnown(new MethodNameHandler() {
            @Override
            public void handle(String methodName) {
                LogUtils.debug(className + "." + methodName + "()" + combine(additional));
            }
        });
    }

    private static interface MethodNameHandler {
        void handle(String methodName);
    }

    private static void executeIfInvokerMethodNameIsKnown(MethodNameHandler methodNameHandler) {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length > 0) {
            StackTraceElement invoker = stackTraceElements[INDEX_OF_INVOKER_METHOD];
            methodNameHandler.handle(invoker.getMethodName());
        }
    }

    private static String combine(Object... toCombine) {
        if (toCombine.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(": ");
        for (Object each : toCombine) {
            builder.append(each);
        }
        return builder.toString();
    }
}
