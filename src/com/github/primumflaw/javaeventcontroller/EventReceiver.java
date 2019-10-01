package com.github.primumflaw.javaeventcontroller;

public interface EventReceiver {
    void onEvent(Event event, EventCallback eventCallback);
}