package ru.vels.taskplanner.dto.definition;

import java.util.List;

public class DefinitionPlan {
    private List<DefinitionActivity> activities;
    private List<DefinitionTransition> transitions;

    public List<DefinitionActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<DefinitionActivity> activities) {
        this.activities = activities;
    }

    public List<DefinitionTransition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<DefinitionTransition> transitions) {
        this.transitions = transitions;
    }
}
