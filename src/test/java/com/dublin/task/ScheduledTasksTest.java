package com.dublin.task;

import com.dublin.client.BikeApiClient;
import com.dublin.client.WeatherApiClient;
import com.dublin.entity.CurrentWeather;
import com.dublin.entity.ForecastWeather;
import com.dublin.repository.BikeStationRepository;
import com.dublin.repository.BikeStationStatusRepository;
import com.dublin.repository.CurrentWeatherRepository;
import com.dublin.repository.ForecastWeatherRepository;
import com.dublin.service.BikeService;
import com.dublin.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduledTasksTest {

    @Mock
    private BikeApiClient bikeApiClient;
    @Mock
    private WeatherApiClient weatherApiClient;
    @Mock
    private BikeService bikeService;
    @Mock
    private WeatherService weatherService;
    @Mock
    private BikeStationRepository bikeStationRepository;
    @Mock
    private BikeStationStatusRepository bikeStationStatusRepository;
    @Mock
    private CurrentWeatherRepository currentWeatherRepository;
    @Mock
    private ForecastWeatherRepository forecastWeatherRepository;

    @InjectMocks
    private ScheduledTasks scheduledTasks;

    @Test
    void bikeProviderFailureDoesNotPreventWeatherRefreshes() {
        CurrentWeather currentWeather = new CurrentWeather();
        ForecastWeather forecastWeather = new ForecastWeather();

        when(bikeStationRepository.findAll()).thenReturn(Collections.emptyList());
        when(bikeApiClient.fetchAllStations()).thenThrow(new RuntimeException("provider unavailable"));
        when(bikeApiClient.fetchAllStationStatus()).thenThrow(new RuntimeException("provider unavailable"));
        when(weatherApiClient.fetchCurrentWeather()).thenReturn(currentWeather);
        when(weatherApiClient.fetchForecastWeather()).thenReturn(Collections.singletonList(forecastWeather));

        assertDoesNotThrow(() -> scheduledTasks.run());

        verify(bikeService, never()).saveBikeStationStatus(any());
        verify(weatherService).saveCurrentWeather(currentWeather);
        verify(weatherService).saveForecastWeather(Collections.singletonList(forecastWeather));
    }
}
