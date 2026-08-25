package com.dublin.feign;

import feign.Logger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeignConfigTest {

    @Test
    void requestLoggingIsDisabledBecauseApiKeysAreQueryParameters() {
        assertEquals(Logger.Level.NONE, new FeignConfig().feignLoggerLevel());
    }
}
