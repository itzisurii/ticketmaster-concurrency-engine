package edu.iCET.controller;

import edu.iCET.model.dto.EventDTO;
import edu.iCET.model.entity.Event;
import edu.iCET.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventDTO getEvent(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @PostMapping("/save")
    public EventDTO saveEvent(@RequestBody EventDTO eventDTO) {
        return eventService.saveEvent(eventDTO);
    }

}
