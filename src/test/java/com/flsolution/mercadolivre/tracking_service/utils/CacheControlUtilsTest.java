package com.flsolution.mercadolivre.tracking_service.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.http.CacheControl;

class CacheControlUtilsTest {

	@Test
    void testGetCacheControl() {
        CacheControl cacheControl = CacheControlUtils.getCacheControl();

        assertEquals(CacheControl.maxAge(5, TimeUnit.MINUTES).getHeaderValue(), cacheControl.getHeaderValue(),
            "Cache control header value should be 'max-age=300'");
	}

}
