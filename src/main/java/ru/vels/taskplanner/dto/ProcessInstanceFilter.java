package ru.vels.taskplanner.dto;

import java.time.Instant;

public class ProcessInstanceFilter {

    private String definition;
    private Instant fromCreationDate;
    private Instant toCreationDate;
    private Instant fromDueDate;
    private Instant toDueDate;
    private Instant fromCompletionDate;
    private Instant toCompletionDate;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
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
}