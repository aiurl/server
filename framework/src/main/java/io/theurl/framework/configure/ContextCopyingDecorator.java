package io.theurl.framework.configure;

import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestContextHolder;

@SuppressWarnings("NullableProblems")
public class ContextCopyingDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        var attributes = RequestContextHolder.getRequestAttributes();

        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(attributes);
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }
}
