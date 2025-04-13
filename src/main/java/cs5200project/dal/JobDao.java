package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public Job create(Connection connection, String jobName) throws SQLException {
        String insertJob = "INSERT INTO Job(jobName) VALUES (?)";

        try (PreparedStatement insertStmt = connection.prepareStatement(insertJob,
                Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, jobName);
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

    public void delete(Connection connection, Job job) throws SQLException {
        String delete = "DELETE FROM Job WHERE jobID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(delete)) {
            stmt.setInt(1, job.getJobID());
            stmt.executeUpdate();   
        }
    }
}
