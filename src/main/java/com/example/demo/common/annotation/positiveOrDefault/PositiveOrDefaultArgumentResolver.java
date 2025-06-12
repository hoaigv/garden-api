package com.example.demo.common.annotation.positiveOrDefault;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PositiveOrDefaultArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PositiveOrDefault.class)
                && (parameter.getParameterType().equals(int.class)
                || parameter.getParameterType().equals(Integer.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        PositiveOrDefault annotation = parameter.getParameterAnnotation(PositiveOrDefault.class);
        String paramName = parameter.getParameterName();
        if (annotation == null || paramName == null) {
            return null;
        }

        String value = webRequest.getParameter(paramName);

        if (value == null || value.trim().isEmpty()) {
            return annotation.defaultValue();
        }

        try {
            int parsed = Integer.parseInt(value);
            return (parsed > 0) ? parsed : annotation.defaultValue();
        } catch (NumberFormatException e) {
            return annotation.defaultValue();
        }
    }
}
