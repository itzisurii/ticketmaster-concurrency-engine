package edu.iCET.service;

import edu.iCET.model.dto.EventDTO;
import edu.iCET.model.entity.Event;
import edu.iCET.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    private EventDTO toDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getName(),
                event.getBasePrice(),
                event.isHighDemand(),
                event.getEventDate()
        );
    }


}
