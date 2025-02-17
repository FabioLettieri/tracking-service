package com.flsolution.mercadolivre.tracking_service.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;

public class CacheControlUtils {
	
	public static CacheControl getCacheControl() {
        return CacheControl.maxAge(5, TimeUnit.MINUTES);
    }

}
