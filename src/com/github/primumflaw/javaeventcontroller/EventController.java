package com.github.primumflaw.javaeventcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class EventController {

    // receiver list map
    private HashMap<String, ArrayList<EventReceiver>> receiverListMap = new HashMap<>();

    // event process types
    public static String event_process_type_non_threaded = "event_process_type_non_threaded";
    public static String event_process_type_single_threaded = "event_process_type_single_threaded";
    public static String event_process_type_multi_threaded = "event_process_type_multi_threaded";

    // default event process type
    public static String default_event_process_type = event_process_type_non_threaded;

    // lock
    private Object lock = new Object();

    // ==========================================================================================
    // public methods

    // send event (event)
    public void sendEvent(Event event) {
        this.sendEvent(event, null);
    }

    // send event (event, event callback)
    public void sendEvent(Event event, EventCallback eventCallback) {
        synchronized (this.lock) {
            // on event process type non threaded
            if (event.eventProcessType.equals(event_process_type_non_threaded)) {
                this.processSendEventTypeNonThreaded(event, eventCallback);
            }
            // on event process type singe threaded
            if (event.eventProcessType.equals(event_process_type_single_threaded)) {
                this.processSendEventTypeSingleThreaded(event, eventCallback);
            }
            // on event process type multi threaded
            if (event.eventProcessType.equals(event_process_type_multi_threaded)) {
                this.processSendEventTypeMultiThreaded(event, eventCallback);
            }
        }
    }

    // register receiver
    public void registerReceiver(String eventName, EventReceiver eventReceiver) {
        synchronized (this.lock) {
            this.processRegisterReceiver(eventName, eventReceiver);
            this.cleanReceiverMap();
        }
    }

    // unregister receiver
    public void unregisterReceiver(String eventName, EventReceiver eventReceiver) {
        synchronized (this.lock) {
            this.processUnregisterReceiver(eventName, eventReceiver);
            this.cleanReceiverMap();
        }
    }

    // ==========================================================================================
    // process methods

    // process register receiver
    private void processRegisterReceiver(String eventName, EventReceiver eventReceiver) {
        ArrayList<EventReceiver> receiverList = this.getReceiverList(eventName);
        if (!receiverList.contains(eventReceiver)) {
            receiverList.add(eventReceiver);
        }
    }
    // process unregister receiver
    private void processUnregisterReceiver(String eventName, EventReceiver eventReceiver) {
        ArrayList<EventReceiver> receiverList = this.getReceiverList(eventName);
        receiverList.remove(eventReceiver);
    }

    // process send event type non threaded
    private void processSendEventTypeNonThreaded(final Event event, final EventCallback callback) {
        // get receiver list
        final ArrayList<EventReceiver> tempReceiverList = this.getReceiverList(event.eventName);
        final ArrayList<EventReceiver> receiverList = this.copyReceiverList(tempReceiverList);
        // send event
        for (final EventReceiver receiver : receiverList) {
            receiver.onEvent(event, callback);
        }
    }

    // process send event type single threaded
    private void processSendEventTypeSingleThreaded(final Event event, final EventCallback callback) {
        // get receiver list
        final ArrayList<EventReceiver> tempReceiverList = this.getReceiverList(event.eventName);
        final ArrayList<EventReceiver> receiverList = this.copyReceiverList(tempReceiverList);
        // send event
        new Thread(){
            @Override
            public void run() {
                super.run();
                for (final EventReceiver receiver : receiverList) {
                    receiver.onEvent(event, callback);
                }
            }
        }.start();
    }

    // process send event type multi threaded
    private void processSendEventTypeMultiThreaded(final Event event, final EventCallback callback) {
        // get receiver list
        final ArrayList<EventReceiver> tempReceiverList = this.getReceiverList(event.eventName);
        final ArrayList<EventReceiver> receiverList = this.copyReceiverList(tempReceiverList);
        // send event
        for (final EventReceiver receiver : receiverList) {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    receiver.onEvent(event, callback);
                }
            }.start();
        }
    }

    // ==========================================================================================
    // private helper methods

    // get receiver list
    private ArrayList<EventReceiver> getReceiverList(String eventName) {
        if (this.receiverListMap.containsKey(eventName)) {
            return this.receiverListMap.get(eventName);
        } else {
            this.receiverListMap.put(eventName, new ArrayList<EventReceiver>());
            return this.receiverListMap.get(eventName);
        }
    }

    // copy receiver list
    private ArrayList<EventReceiver> copyReceiverList(ArrayList<EventReceiver> receiverList) {
        return new ArrayList<>(receiverList);
    }

    // clean receiver map
    private void cleanReceiverMap() {
        Set<String> keySet = this.receiverListMap.keySet();
        for (String entry:keySet) {
            if (this.receiverListMap.get(entry).isEmpty()) {
                this.receiverListMap.remove(entry);
            }
        }
    }
}