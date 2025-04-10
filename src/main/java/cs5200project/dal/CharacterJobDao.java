package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.CharacterJob;
import cs5200project.model.GameCharacter;
import cs5200project.model.Job;

public class CharacterJobDao {
    private static CharacterJobDao instance = null;
    private JobDao jobDao;

    protected CharacterJobDao() {
        jobDao = JobDao.getInstance();
    }

    public static CharacterJobDao getInstance() {
        if (instance == null) {
            instance = new CharacterJobDao();
        }
        return instance;
    }

    public List<CharacterJob> getByCharacterID(Connection conn, GameCharacter character) throws SQLException {
        String sql = 
            "SELECT cj.characterID, cj.jobID, cj.isUnlocked, cj.jobLevel, " +
            "j.jobName " +
            "FROM CharacterJob cj " +
            "JOIN Job j ON cj.jobID = j.jobID " +
            "WHERE cj.characterID = ?;";

        List<CharacterJob> jobs = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, character.getCharacterID());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Job job = new Job(
                        rs.getInt("jobID"),
                        rs.getString("jobName")
                    );
                    CharacterJob characterJob = new CharacterJob(
                        character,
                        job,
                        rs.getBoolean("isUnlocked"),
                        rs.getInt("jobLevel")
                    );
                    jobs.add(characterJob);
                }
            }
        }
        return jobs;
    }

    public CharacterJob create(Connection conn, GameCharacter character, Job job, boolean isUnlocked, int jobLevel) throws SQLException {
        String sql = "INSERT INTO CharacterJob(characterID, jobID, isUnlocked, jobLevel) VALUES(?, ?, ?, ?);";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, character.getCharacterID());
            ps.setInt(2, job.getJobID());
            ps.setBoolean(3, isUnlocked);
            ps.setInt(4, jobLevel);
            ps.executeUpdate();
            return new CharacterJob(character, job, isUnlocked, jobLevel);
        }
    }
}
