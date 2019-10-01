# JavaEventController

A simple to use, thread safe and lightweight Java event framework

<hr>

## Documentation

### Package paths
```java
import com.github.primumflaw.javaeventcontroller.Event;
import com.github.primumflaw.javaeventcontroller.EventCallback;
import com.github.primumflaw.javaeventcontroller.EventController;
import com.github.primumflaw.javaeventcontroller.EventReceiver;
```

### 1. Create an instance of EventController

The EventController class serves as the center of event management, from here Events can be send and
EventReceivers are registered/unregistered.  
Every instance of the EventController class serves as a enclosed event namespace, in most
cases one single centralized instance should be enough.

```java
EventController eventController = new EventController();
```

### 2. Create an instances of EventReceiver

The EventReceiver class serves as the name suggests for receiving events. One EventReceiver instance
can be used for multiple events.

```java
EventReceiver eventReceiver = new EventReceiver() {
	@Override
	public void onEvent(Event event, EventCallback eventCallback) {
		System.out.println(event.eventName); // <- name of the event
		System.out.println(event.eventProcessType); // <- describes how this event got processed
		System.out.println(eventCallback); // <- used for callbacks, can be null
	}
};
```

### 3. Register the EventReceiver instance in the EventController instance

The EventReceiver class serves as the name suggests for receiving events. One EventReceiver instance
can be used for multiple events.

```java
eventController.registerReceiver("event-name-1", eventReceiver);

eventController.registerReceiver(
	"event-name-2", // <- event name that can be freely chosen (used to map the different events)
	eventReceiver // < - event receiver instance that will receive events with "event-name-2"
);
```

### 4. Send an event

```java
Event event1 = new Event();
event.eventName = "event-name-1";
event.eventProcessType = EventController.event_process_type_non_threaded; // constructor type 1
eventController.sendEvent(event); // <- send event with the event controller instance

Event event2 = new Event("event-name-2");
event.eventProcessType = EventController.event_process_type_single_threaded; // constructor type 2
eventController.sendEvent(event); // <- send event with the event controller instance

Event event3 = new Event("event-name-2", EventController.event_process_type_multi_threaded); // constructor type 3
eventController.sendEvent(event); // <- send event with the event controller instance
```

### 5. Unregister event receiver

When you are done you can unregister the event receiver.
(This prevents RAM leakage because the event controller holds an reference to every registered EventReceiver instance, this blocks the Java garbage collector to delete non used receiver instances).

```java
eventController.unregisterReceiver("event-name-1", eventReceiver);
```

### 6. Working with callbacks

You can easily get a callback from called event receivers. This allows for responsive and event driven programming.

```java

// event receiver with callback response
EventReceiver eventReceiver = new EventReceiver() {
	@Override
	public void onEvent(Event event, EventCallback eventCallback) {
		System.out.println(event.eventName); // <- name of the event
		System.out.println(event.eventProcessType); // <- describes how this event got processed
		System.out.println(eventCallback); // <- used for callbacks, can be null
		if (eventCallback != null) {
			// in this case the event instance just serves as a data argument and can also be null
			eventCallback.onCallback(new Event("receiver-callback"));
		}
	}
};

// register event receiver
eventController.registerReceiver("event-name-1", eventReceiver);

// create event
Event event = new Event("event-name-1");  

// create event callback
EventCallback eventCallback = new EventCallback() {
	@Override
	public void onCallback(Event event) {
		System.out.println("callback with arg: " + event.eventName);
	}
};

// send event
eventController.sendEvent(event, eventCallback);
```

<hr>

## Class blueprints

### EventController Class

**Attributes:**

| attribute                          | access       | initial value                                   |
|------------------------------------|--------------|-------------------------------------------------|
| event_process_type_non_threaded    | final public | event_process_type_non_threaded                 |
| event_process_type_single_threaded | final public | event_process_type_single_threaded              |
| event_process_type_multi_threaded  | final public | event_process_type_multi_threaded               |
| default_event_process_type         | public       | EventController.event_process_type_non_threaded |

**Methods:**

| method             | arguments                                     |
|--------------------|-----------------------------------------------|
| sendEvent (type 1) | Event event                                   |
| sendEvent (type 2) | Event event, EventCallback eventCallback      |
| registerReceiver   | String eventName, EventReceiver eventReceiver |
| unregisterReceiver | String eventName, EventReceiver eventReceiver |

### Event Class

**Constructors:**

| constructor        | arguments                                 |
|--------------------|-------------------------------------------|
| constructor 1      | none                                      |
| constructor 2      | String eventName                          |
| constructor 3      | String eventName, String eventProcessType |

**Attributes:**

| attribute        | access | initial value                              |
|------------------|--------|--------------------------------------------|
| eventName        | public | empty string                               |
| eventProcessType | public | EventController.default_event_process_type |

### EventReceiver Interface

**Methods:**

| method  | arguments                                |
|---------|------------------------------------------|
| onEvent | Event event, EventCallback eventCallback |


### EventCallback Interface

**Methods:**

| method     | arguments   |
|------------|-------------|
| onCallback | Event event |

<hr>

## Recommendations or questions

If you have any ideas that would make the grid even greater, feel free to contact me:
[mailto](Mailto:wag96niklas@gmail.com)

Thank you for downloading, made with ❤️ and 🍺 in Munich
