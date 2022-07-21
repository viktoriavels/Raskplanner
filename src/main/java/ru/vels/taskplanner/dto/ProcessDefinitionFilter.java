package ru.vels.taskplanner.dto;

import ru.vels.taskplanner.entity.User;

public class ProcessDefinitionFilter {

    private String title;

    private String description;

    private String definitionPlan;

    private User owner;


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

    public String getDefinitionPlan() {
        return definitionPlan;
    }

    public void setDefinitionPlan(String definitionPlan) {
        this.definitionPlan = definitionPlan;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }



}
