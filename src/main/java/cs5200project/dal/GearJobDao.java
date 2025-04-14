package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.Gear;
import cs5200project.model.GearJob;
import cs5200project.model.Job;

public class GearJobDao {
    private GearJobDao() {
    }

    public static GearJob create(Connection cxn, Gear gearItem, Job job) throws SQLException {
        return create(cxn, gearItem.getItemId(), job.getJobID());
    }

    public static GearJob create(Connection cxn, int itemID, int jobID) throws SQLException {
        final String insertGearJob = "INSERT INTO GearJob(itemID, jobID) " +
                "VALUES(?,?)";
        try (PreparedStatement statement = cxn.prepareStatement(insertGearJob,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, itemID);
            statement.setInt(2, jobID);
            statement.executeUpdate();

            Gear gear = GearDao.getGearByItemID(cxn, itemID);
            Job job = JobDao.getJobById(cxn, jobID);
            
            return new GearJob(gear, job);
        }
    }

    public static GearJob getGearJobByItemIdAndJobId(Connection cxn, Gear gearItem, Job job) throws SQLException {
        final String selectGearJob = "SELECT gj.itemID, gj.jobID, " +
                "i.name AS itemName, j.name AS jobName " +
                "FROM GearJob gj " +
                "JOIN Item i ON gj.itemID = i.itemID " +
                "JOIN Job j ON gj.jobID = j.jobID " +
                "WHERE gj.itemID = ? AND gj.jobID = ?";
        try (PreparedStatement statement = cxn.prepareStatement(selectGearJob)) {
            statement.setInt(1, gearItem.getItemId());
            statement.setInt(2, job.getJobID());

            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
					Gear gearRs = GearDao.getGearByItemID(cxn,
							results.getInt("itemID"));
					Job jobRs = JobDao.getJobById(cxn, job.getJobID());
					return new GearJob(gearRs, jobRs);
                } else {
                    return null;
                }
            }
        }
    }

    public static List<GearJob> getGearJobsByItemId(Connection cxn, Gear gearItem) throws SQLException {
        final String selectGearJob = "SELECT gj.itemID, gj.jobID, " +
                "i.name AS itemName, j.name AS jobName " +
                "FROM GearJob gj " +
                "JOIN Item i ON gj.itemID = i.itemID " +
                "JOIN Job j ON gj.jobID = j.jobID " +
                "WHERE gj.itemID = ?";
        List<GearJob> gearJobList = new ArrayList<>();
        try (PreparedStatement statement = cxn.prepareStatement(selectGearJob)) {
			statement.setInt(1, gearItem.getItemId());
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
					Gear gear = GearDao.getGearByItemID(cxn,
							results.getInt("itemID"));
                    Job job = JobDao.getJobById(cxn, results.getInt("jobID"));
                    gearJobList.add(new GearJob(gear, job));
                }
                return gearJobList;
            }
        }
    }

    public static List<GearJob> getGearJobsByJobId(Connection cxn, int jobId) throws SQLException {
        final String selectGearJob = "SELECT gj.itemID, gj.jobID, " +
                "i.name AS itemName, j.name AS jobName " +
                "FROM GearJob gj " +
                "JOIN Item i ON gj.itemID = i.itemID " +
                "JOIN Job j ON gj.jobID = j.jobID " +
                "WHERE gj.jobID = ?";
        List<GearJob> gearJobList = new ArrayList<>();
        try (PreparedStatement statement = cxn.prepareStatement(selectGearJob)) {
            statement.setInt(1, jobId);
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
					Gear gear = GearDao.getGearByItemID(cxn,
							results.getInt("itemID"));
                    Job job = JobDao.getJobById(cxn, results.getInt("jobID"));
                    gearJobList.add(new GearJob(gear, job));
                }
                return gearJobList;
            }
        }
    }

    public static void delete(Connection cxn, int itemId, int jobId) throws SQLException {
        final String delete = "DELETE FROM GearJob " +
                "WHERE itemID = ? AND jobID = ?";
        try (PreparedStatement statement = cxn.prepareStatement(delete)) {
			statement.setInt(1, itemId);
			statement.setInt(2, jobId);
            statement.executeUpdate();
        }
    }
}
