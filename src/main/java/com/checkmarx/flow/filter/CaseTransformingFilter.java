package com.checkmarx.flow.filter;

import com.google.common.base.CaseFormat;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Transforms kebab-case request params to lower camel case.
 */
public class CaseTransformingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Map<String, String[]> camelCaseParams = new ConcurrentHashMap<>();

        for (String param : request.getParameterMap().keySet()) {
            String formattedParam = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, param);
            camelCaseParams.put(formattedParam, request.getParameterValues(param));
        }

        ServletRequest requestWrapper = new ParameterOverridingWrapper(camelCaseParams, request);
        filterChain.doFilter(requestWrapper, response);
    }


}
