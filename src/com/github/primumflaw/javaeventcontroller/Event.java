package com.github.primumflaw.javaeventcontroller;

public class Event {

    // constructor type 1
    public Event() {}

    // constructor type 2
    public Event(String eventName) {
        this.eventName = eventName;
    }

	// constructor type 3
    public Event(String eventName, String eventProcessType) {
        this.eventName = eventName;
		this.eventProcessType = eventProcessType;
    }

    // event name
    public String eventName = "";

    // event process type
    public String eventProcessType = EventController.default_event_process_type;
}
