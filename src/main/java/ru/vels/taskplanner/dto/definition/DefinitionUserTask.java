package ru.vels.taskplanner.dto.definition;

import java.time.Duration;
import java.util.List;

public class DefinitionUserTask extends DefinitionActivity {
    private String title;
    private String description;
    private List<String> candidates;
    private List<String> availableDecisions;
    private Duration dueDateInterval;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<String> candidates) {
        this.candidates = candidates;
    }

    public List<String> getAvailableDecisions() {
        return availableDecisions;
    }

    public void setAvailableDecisions(List<String> availableDecisions) {
        this.availableDecisions = availableDecisions;
    }

    public Duration getDueDateInterval() {
        return dueDateInterval;
    }

    public void setDueDateInterval(Duration dueDateInterval) {
        this.dueDateInterval = dueDateInterval;
    }
}
