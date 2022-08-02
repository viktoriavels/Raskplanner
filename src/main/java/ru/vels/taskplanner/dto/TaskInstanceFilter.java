package ru.vels.taskplanner.dto;

import java.time.Instant;

public class TaskInstanceFilter {

    private String taskDefinitionId;
    private Long processInstanceId;
    private String candidate;
    private Instant fromCreationDate;
    private Instant toCreationDate;
    private Instant fromDueDate;
    private Instant toDueDate;
    private Instant fromCompletionDate;
    private Instant toCompletionDate;
    private String title;
    private String description;
    private String decision;

    public String getTaskDefinitionId() {
        return taskDefinitionId;
    }

    public void setTaskDefinitionId(String taskDefinitionId) {
        this.taskDefinitionId = taskDefinitionId;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public Instant getFromCreationDate() {
        return fromCreationDate;
    }

    public void setFromCreationDate(Instant fromCreationDate) {
        this.fromCreationDate = fromCreationDate;
    }

    public Instant getToCreationDate() {
        return toCreationDate;
    }

    public void setToCreationDate(Instant toCreationDate) {
        this.toCreationDate = toCreationDate;
    }

    public Instant getFromDueDate() {
        return fromDueDate;
    }

    public void setFromDueDate(Instant fromDueDate) {
        this.fromDueDate = fromDueDate;
    }

    public Instant getToDueDate() {
        return toDueDate;
    }

    public void setToDueDate(Instant toDueDate) {
        this.toDueDate = toDueDate;
    }

    public Instant getFromCompletionDate() {
        return fromCompletionDate;
    }

    public void setFromCompletionDate(Instant fromCompletionDate) {
        this.fromCompletionDate = fromCompletionDate;
    }

    public Instant getToCompletionDate() {
        return toCompletionDate;
    }

    public void setToCompletionDate(Instant toCompletionDate) {
        this.toCompletionDate = toCompletionDate;
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

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
