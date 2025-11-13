package com.tekton.tenpo.infrastructure.adapters.in.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.tekton.tenpo.application.port.in.SaveCallPort;
import com.tekton.tenpo.domain.model.CreateCallHistoryInterceptor;
import com.tekton.tenpo.infrastructure.constants.ApiConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CallHistoryInterceptor implements HandlerInterceptor {

    private final SaveCallPort historyService;

    public CallHistoryInterceptor(SaveCallPort historyService) {
        this.historyService = historyService;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            int statusCode = response.getStatus();
            String message = ex != null ? ex.getMessage() : 
            (statusCode >= 400 ? ApiConstants.ERROR : ApiConstants.SUCCESS);
            String params = extractRequestBody(request);

            historyService.saveCallHistory(
                    new CreateCallHistoryInterceptor(request.getRequestURI(), params, message, statusCode)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractRequestBody(HttpServletRequest request) {
        try {
            if (request instanceof ContentCachingRequestWrapper wrapper) {
                byte[] buf = wrapper.getContentAsByteArray();
                if (buf.length > 0) return new String(buf, wrapper.getCharacterEncoding());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request.getQueryString() != null ? request.getQueryString() : ApiConstants.EMPTY_BODY_RESPONSE;
    }
}
