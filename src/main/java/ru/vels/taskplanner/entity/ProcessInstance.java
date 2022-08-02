package ru.vels.taskplanner.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "process_instances")
public class ProcessInstance {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "definition_id")
    private String definition;
    @Column(name = "owner")
    private String owner;
    @Column(name = "creation_date")
    private   Instant creationDate;
    @Column(name = "due_date")
    private  Instant dueDate;
    @Column(name = "completion_date")
    private   Instant completionDate;
    @OneToMany(mappedBy = "processInstance")
    private List<TaskInstance> taskInstanceList;

    public ProcessInstance() {
    }

    public List<TaskInstance> getTaskInstanceList() {
        return taskInstanceList;
    }

    public void setTaskInstanceList(List<TaskInstance> taskInstanceList) {
        this.taskInstanceList = taskInstanceList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
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
