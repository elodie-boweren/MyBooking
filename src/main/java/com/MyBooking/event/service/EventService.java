package com.mybooking.event.service;

import com.mybooking.event.dto.*;
import java.util.List;

public interface EventService {
    List<EventDto> getEvents(String from, String to, String eventType);
    EventDto getEvent(Long id);
    List<EventDto> getAllEvents();
    EventDto createEvent(CreateOrUpdateEventRequest request);
    EventDto updateEvent(Long id, CreateOrUpdateEventRequest request);
    void deleteEvent(Long id);
    void openEvent(Long id);
    void closeEvent(Long id);
}
