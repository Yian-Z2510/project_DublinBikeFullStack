package com.dublin.controller;

import com.dublin.service.BikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BikeControllerTest {

    private BikeService bikeService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        bikeService = mock(BikeService.class);
        BikeController controller = new BikeController();
        ReflectionTestUtils.setField(controller, "bikeService", bikeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void historyReturnsNotFoundForUnknownStation() throws Exception {
        when(bikeService.getStationByNumber(999)).thenReturn(null);

        mockMvc.perform(get("/api/bike/stations/999/history"))
                .andExpect(status().isNotFound());

        verify(bikeService, never()).getStationHistory(999, 24);
    }
}
