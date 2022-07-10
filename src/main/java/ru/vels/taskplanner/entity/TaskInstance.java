package ru.vels.taskplanner.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "Task_instance")
public class TaskInstance {
    public TaskInstance() {
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "process_instance_id")
    ProcessInstance processInstance;
    @ElementCollection
    @CollectionTable(name = "Task_instance_candidates", joinColumns = @JoinColumn(name = "Task_instance_id"))
    @Column(name = "candidates")
    List<String> candidates;
    @Column(name = "creation_date")
    Instant creationDate;
    @Column(name = "dueDate")
    Instant dueDate;
    @Column(name = "completion_date")
    Instant completionDate;
    @Column(name = "title")
    String title;
    @Column(name = "description")
    String description;
    @Column(name = "decision")
    String decision;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public List<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<String> candidates) {
        this.candidates = candidates;
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
