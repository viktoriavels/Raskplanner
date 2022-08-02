package ru.vels.taskplanner.process;

import ru.vels.taskplanner.exception.ActivityException;

public interface ProcessListener {
    void init();

    void notify(ProcessEvent processEvent) throws ActivityException;

    int getOrder();
}
