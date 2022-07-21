package ru.vels.taskplanner.dto.definition;

public class DefinitionTransition {
    private String id;
    private String fromActivityId;
    private String toActivityId;
    private String decision;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromActivityId() {
        return fromActivityId;
    }

    public void setFromActivityId(String fromActivityId) {
        this.fromActivityId = fromActivityId;
    }

    public String getToActivityId() {
        return toActivityId;
    }

    public void setToActivityId(String toActivityId) {
        this.toActivityId = toActivityId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
