package ru.vels.taskplanner.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "Process_instance")
public class ProcessInstance {
    public ProcessInstance() {
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @ManyToOne
    @JoinColumn(name = "process_definition_id")
    private ProcessDefinition definition;

    @ManyToOne
    @JoinColumn(name = "owner")
    User owner;
    @Column(name = "creation_date")
    Instant creationDate;
    @Column(name = "due_date")
    Instant dueDate;
    @Column(name = "completion_date")
    Instant completionDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProcessDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(ProcessDefinition definition) {
        this.definition = definition;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Instant completionDate) {
        this.completionDate = completionDate;
    }
}
