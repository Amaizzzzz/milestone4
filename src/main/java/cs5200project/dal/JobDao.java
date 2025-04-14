package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.Job;

public class JobDao {
    private static JobDao instance = null;
    
    private JobDao() {}
    
    public static JobDao getInstance() {
        if (instance == null) {
            instance = new JobDao();
        }
        return instance;
    }

    public Job create(Connection connection, String jobName, String description, int minLevel, int maxLevel) throws SQLException {
        String insertJob = "INSERT INTO Job(jobName, description, minLevel, maxLevel) VALUES (?, ?, ?, ?)";

        try (PreparedStatement insertStmt = connection.prepareStatement(insertJob,
                Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, jobName);
            insertStmt.setString(2, description);
            insertStmt.setInt(3, minLevel);
            insertStmt.setInt(4, maxLevel);
            insertStmt.executeUpdate();

            return new Job(Utils.getAutoIncrementKey(insertStmt), jobName);
        }
    }

    public Job getJobById(Connection connection, int jobId)
            throws SQLException {
        String selectJob = "SELECT * FROM Job WHERE jobID = ?";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectJob)) {
            selectStmt.setInt(1, jobId);
            try (ResultSet results = selectStmt.executeQuery()) {
                if (results.next()) {
                    String jobName = results.getString("jobName");
                    return new Job(jobId, jobName);
                } else {
                    return null;
                }
            }
        }
    }

    public List<Job> getAllJobs(Connection connection) throws SQLException {
        List<Job> jobs = new ArrayList<>();
        String selectJobs = "SELECT * FROM Job";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectJobs);
             ResultSet results = selectStmt.executeQuery()) {
            while (results.next()) {
                int jobId = results.getInt("jobID");
                String jobName = results.getString("jobName");
                jobs.add(new Job(jobId, jobName));
            }
        }
        return jobs;
    }

    public List<Job> getJobsForWeapon(Connection connection, int weaponId) throws SQLException {
        List<Job> jobs = new ArrayList<>();
        String selectJobs = 
            "SELECT j.* FROM Job j " +
            "JOIN Weapon w ON j.jobID = w.jobID " +
            "WHERE w.itemID = ?";
        
        try (PreparedStatement selectStmt = connection.prepareStatement(selectJobs)) {
            selectStmt.setInt(1, weaponId);
            try (ResultSet results = selectStmt.executeQuery()) {
                while (results.next()) {
                    int jobId = results.getInt("jobID");
                    String jobName = results.getString("jobName");
                    jobs.add(new Job(jobId, jobName));
                }
            }
        }
        return jobs;
    }

    public void update(Connection connection, int jobId, String jobName, String description, int minLevel, int maxLevel) throws SQLException {
        String update = "UPDATE Job SET jobName = ?, description = ?, minLevel = ?, maxLevel = ? WHERE jobID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(update)) {
            stmt.setString(1, jobName);
            stmt.setString(2, description);
            stmt.setInt(3, minLevel);
            stmt.setInt(4, maxLevel);
            stmt.setInt(5, jobId);
            stmt.executeUpdate();
        }
    }

    public void delete(Connection connection, Job job) throws SQLException {
        String delete = "DELETE FROM Job WHERE jobID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(delete)) {
            stmt.setInt(1, job.getJobID());
            stmt.executeUpdate();   
        }
    }
}
