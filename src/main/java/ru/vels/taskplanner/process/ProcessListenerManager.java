package ru.vels.taskplanner.process;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessListenerManager {

    private List<ProcessListener> listeners = new ArrayList<>();

    public void register(ProcessListener processListener) {
        listeners.add(processListener);

    }

    public void unregister(ProcessListener processListener) {
        listeners.remove(processListener);
    }

    public void notify(ProcessEvent processEvent) {
        for (ProcessListener p : listeners) {
            p.notify(processEvent);
        }
    }
}
