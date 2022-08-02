package ru.vels.taskplanner.process;

import ru.vels.taskplanner.entity.ProcessInstance;

public class ProcessEvent<T> {
    private ProcessInstance instance;
    private T eventSubject;
    private Class<? extends T> eventSubjectType;
    private ProcessEventType eventType;

    public ProcessInstance getInstance() {
        return instance;
    }

    public void setInstance(ProcessInstance instance) {
        this.instance = instance;
    }

    public T getEventSubject() {
        return eventSubject;
    }

    public void setEventSubject(T eventSubject) {
        this.eventSubject = eventSubject;
    }

    public Class<? extends T> getEventSubjectType() {
        return eventSubjectType;
    }

    public void setEventSubjectType(Class<? extends T> eventSubjectType) {
        this.eventSubjectType = eventSubjectType;
    }

    public ProcessEventType getEventType() {
        return eventType;
    }

    public void setEventType(ProcessEventType eventType) {
        this.eventType = eventType;
    }
}
