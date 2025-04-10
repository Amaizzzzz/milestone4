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

    protected JobDao() {
    }

    public static JobDao getInstance() {
        if (instance == null) {
            instance = new JobDao();
        }
        return instance;
    }

    public Job create(Connection conn, String jobName) throws SQLException {
        String sql = "INSERT INTO Job(jobName) VALUES(?);";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, jobName);
            ps.executeUpdate();
            
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Job(keys.getInt(1), jobName);
                }
                throw new SQLException("Failed to get generated key.");
            }
        }
    }

    public Job getById(Connection conn, int jobId) throws SQLException {
        String sql = "SELECT jobID, jobName FROM Job WHERE jobID = ?;";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Job(
                        rs.getInt("jobID"),
                        rs.getString("jobName")
                    );
                }
                return null;
            }
        }
    }

    public List<Job> getAll(Connection conn) throws SQLException {
        String sql = "SELECT jobID, jobName FROM Job;";
        List<Job> jobs = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                jobs.add(new Job(
                    rs.getInt("jobID"),
                    rs.getString("jobName")
                ));
            }
        }
        return jobs;
    }

    public static void delete(Connection cxn, Job job) throws SQLException {
        final String delete = """
            DELETE FROM Job 
            WHERE jobID = ?;
        """;
        try (PreparedStatement stmt = cxn.prepareStatement(delete)) {
            stmt.setInt(1, job.getJobID());
            stmt.executeUpdate();   
        }
    }
}
