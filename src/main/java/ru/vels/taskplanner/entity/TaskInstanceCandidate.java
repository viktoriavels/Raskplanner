package ru.vels.taskplanner.entity;

import javax.persistence.*;

@Entity
@Table(name = "task_instance_candidate")
public class TaskInstanceCandidate {
    public TaskInstanceCandidate() {
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "task_instance_id")
    private long taskInstanceId;
    @Column(name = "authority_name")
    private String authorityName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskInstanceId() {
        return taskInstanceId;
    }

    public void setTaskInstanceId(long taskInstanceId) {
        this.taskInstanceId = taskInstanceId;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
}
