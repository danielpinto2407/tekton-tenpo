package com.tekton.tenpo.infrastructure.adapters.in.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.tekton.tenpo.application.port.in.SaveCallPort;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CreateCallHistoryRequest;
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
    public void afterCompletion(HttpServletRequest request, 
                                HttpServletResponse response,
                                Object handler, 
                                Exception ex) {

        try {
            String endpoint = request.getRequestURI();
            String params = getRequestBody(request);
            String message = ex != null ? ex.getMessage() : ApiConstants.SUCCESS;
            int statusCode = ex != null ? 500 : response.getStatus();

            CreateCallHistoryRequest dto = new CreateCallHistoryRequest(endpoint, params, message, statusCode);
            historyService.saveCallHistory(dto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRequestBody(HttpServletRequest request) {
        try {
            if (request instanceof ContentCachingRequestWrapper wrapper) {
                byte[] buf = wrapper.getContentAsByteArray();
                if (buf.length > 0) {
                    return new String(buf, wrapper.getCharacterEncoding());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request.getQueryString() != null ? request.getQueryString() : ApiConstants.EMPTY_BODY_RESPONSE;
    }
}
