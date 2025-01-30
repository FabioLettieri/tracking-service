package com.flsolution.mercadolivre.tracking_service.services;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ETagService {

    public String generateETag(Object object) {
        return String.valueOf(object.hashCode());
    }

    public boolean isNotModified(HttpServletRequest request, String eTag) {
        String ifNoneMatch = request.getHeader(HttpHeaders.IF_NONE_MATCH);
        return ifNoneMatch != null && ifNoneMatch.equals(eTag);
    }
}
