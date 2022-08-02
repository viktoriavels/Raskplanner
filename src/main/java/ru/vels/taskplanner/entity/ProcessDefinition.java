package ru.vels.taskplanner.entity;

import javax.persistence.*;

@Entity
@Table(name = "process_definitions")
public class ProcessDefinition {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "definition_plan")
    private String definitionPlan;
    @Column(name = "owner_username")
    private String owner;
    @Column(name = "deleted")
    private boolean deleted;

    public ProcessDefinition() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}

