package cs5200project.model;

import java.util.Objects;

public class Job {
    private int jobID;         // Primary key
    private String jobName;
    private String description;
    private int baseHP;
    private int baseMP;

    // constructor
    public Job(int jobID, String jobName, String description, int baseHP, int baseMP) {
        this.jobID = jobID;
        this.jobName = jobName;
        this.description = description;
        this.baseHP = baseHP;
        this.baseMP = baseMP;
    }

    // Constructor without description and stats for backward compatibility
    public Job(int jobID, String jobName) {
        this(jobID, jobName, "", 0, 0);
    }

    // Getter of jobID
    public int getJobID() {
        return jobID;
    }

    // Setter of jobID
    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    // Getter of jobName
    public String getJobName() {
        return jobName;
    }

    // Setter of jobName
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    // Getter of description
    public String getDescription() {
        return description;
    }

    // Setter of description
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter of baseHP
    public int getBaseHP() {
        return baseHP;
    }

    // Setter of baseHP
    public void setBaseHP(int baseHP) {
        this.baseHP = baseHP;
    }

    // Getter of baseMP
    public int getBaseMP() {
        return baseMP;
    }

    // Setter of baseMP
    public void setBaseMP(int baseMP) {
        this.baseMP = baseMP;
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(jobID, jobName, description, baseHP, baseMP);
    }

    // equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Job job = (Job) obj;
        return jobID == job.jobID && 
               baseHP == job.baseHP && 
               baseMP == job.baseMP && 
               Objects.equals(jobName, job.jobName) && 
               Objects.equals(description, job.description);
    }

    // toString method
    @Override
    public String toString() {
        return String.format("Job[id=%d, name=%s, description=%s, baseHP=%d, baseMP=%d]", 
                           jobID, jobName, description, baseHP, baseMP);
    }
}
