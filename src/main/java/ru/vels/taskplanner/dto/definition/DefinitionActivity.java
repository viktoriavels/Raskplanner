package ru.vels.taskplanner.dto.definition;

public abstract class DefinitionActivity {
    private String id;
    private DefinitionActivityType activityType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DefinitionActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(DefinitionActivityType activityType) {
        this.activityType = activityType;
    }
}
