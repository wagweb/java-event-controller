package test;

import com.github.primumflaw.javaeventcontroller.Event;
import com.github.primumflaw.javaeventcontroller.EventCallback;
import com.github.primumflaw.javaeventcontroller.EventController;
import com.github.primumflaw.javaeventcontroller.EventReceiver;

public class Showcase{

    public static void main(String[] args) {

        // event controller
        EventController eventController = new EventController();

        // event receiver 1
        EventReceiver eventReceiver1 = new EventReceiver() {
            @Override
            public void onEvent(Event event, EventCallback eventCallback) {
                System.out.println("receiver 1 event | name: " + event.eventName);
                if (eventCallback != null) {
                    eventCallback.onCallback(new Event("receiver-1-callback"));
                }
            }
        };

        // event receiver 2
        EventReceiver eventReceiver2 = new EventReceiver() {
            @Override
            public void onEvent(Event event, EventCallback eventCallback) {
                System.out.println("receiver 2 event | name: " + event.eventName);
                if (eventCallback != null) {
                    eventCallback.onCallback(new Event("receiver-2-callback"));
                }
            }
        };

        // event receiver 3
        EventReceiver eventReceiver3 = new EventReceiver() {
            @Override
            public void onEvent(Event event, EventCallback eventCallback) {
                System.out.println("receiver 3 event | name: " + event.eventName);
                if (eventCallback != null) {
                    eventCallback.onCallback(new Event("receiver-3-callback"));
                }
            }
        };

        // register receivers
        eventController.registerReceiver("test-event-1", eventReceiver1);
        eventController.registerReceiver("test-event-1", eventReceiver2);
        eventController.registerReceiver("test-event-1", eventReceiver3);
        eventController.registerReceiver("test-event-2", eventReceiver1);
        eventController.registerReceiver("test-event-2", eventReceiver2);
        eventController.registerReceiver("test-event-2", eventReceiver3);
        eventController.registerReceiver("test-event-3", eventReceiver1);
        eventController.registerReceiver("test-event-3", eventReceiver2);
        eventController.registerReceiver("test-event-3", eventReceiver3);

        // event emitter 1
        Thread eventEmitter1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(200);
                        Event event = new Event();
                        event.eventName = "test-event-1";
                        event.eventProcessType = EventController.event_process_type_non_threaded;
                        eventController.sendEvent(event);
                    } catch (Exception e) {
                        System.out.println("emitter 1 interrupt");
                    }
                }
            }
        };

        // event emitter 2
        Thread eventEmitter2 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(400);
                        Event event = new Event();
                        event.eventName = "test-event-2";
                        event.eventProcessType = EventController.event_process_type_single_threaded;
                        eventController.sendEvent(event);
                    } catch (Exception e) {
                        System.out.println("emitter 2 interrupt");
                    }
                }
            }
        };

        // event emitter 3
        Thread eventEmitter3 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(650);
                        Event event = new Event();
                        event.eventName = "test-event-3";
                        event.eventProcessType = EventController.event_process_type_multi_threaded;
                        eventController.sendEvent(event);
                    } catch (Exception e) {
                        System.out.println("emitter 3 interrupt");
                    }
                }
            }
        };

        // event emitter 4 (with callback)
        Thread eventEmitter4 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(650);
                        Event event = new Event();
                        event.eventName = "test-event-3";
                        event.eventProcessType = EventController.event_process_type_single_threaded;
                        // event callback
                        EventCallback eventCallback = new EventCallback() {
                            @Override
                            public void onCallback(Event event) {
                                System.out.println("event emitter 4 callback | name: " + event.eventName);
                            }
                        };
                        eventController.sendEvent(event, eventCallback);
                    } catch (Exception e) {
                        System.out.println("emitter 3 interrupt");
                    }
                }
            }
        };

        // start event emitters
        eventEmitter1.start();
        eventEmitter2.start();
        eventEmitter3.start();
        eventEmitter4.start();
    }
}
