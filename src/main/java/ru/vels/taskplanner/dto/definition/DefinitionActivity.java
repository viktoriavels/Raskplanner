package ru.vels.taskplanner.dto.definition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "activityType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DefinitionUserTask.class, name = "USER_TASK")
})
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
