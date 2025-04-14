package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.Gear;
import cs5200project.model.GearJob;
import cs5200project.model.Job;

public class GearJobDao {
    private static GearJobDao instance = null;
    private final GearDao gearDao;
    private final JobDao jobDao;
    
    protected GearJobDao() {
        gearDao = GearDao.getInstance();
        jobDao = JobDao.getInstance();
    }
    
    public static GearJobDao getInstance() {
        if(instance == null) {
            instance = new GearJobDao();
        }
        return instance;
    }

    public GearJob create(Connection cxn, int itemID, int jobID) throws SQLException {
        String insertGearJob = "INSERT INTO GearJob(itemID, jobID) VALUES(?,?)";
        try (PreparedStatement stmt = cxn.prepareStatement(insertGearJob)) {
            stmt.setInt(1, itemID);
            stmt.setInt(2, jobID);
            stmt.executeUpdate();
            
            Gear gear = gearDao.getGearByItemID(cxn, itemID);
            Job job = jobDao.getJobById(cxn, jobID);
            return new GearJob(gear, job);
        }
    }

    public List<GearJob> getGearJobsByGear(Connection cxn, Gear gear) throws SQLException {
        List<GearJob> gearJobs = new ArrayList<>();
        String selectGearJobs = "SELECT * FROM GearJob WHERE itemID=?";
        try (PreparedStatement stmt = cxn.prepareStatement(selectGearJobs)) {
            stmt.setInt(1, gear.getItemId());
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    Gear gearRs = gearDao.getGearByItemID(cxn, gear.getItemId());
                    Job jobRs = jobDao.getJobById(cxn, results.getInt("jobID"));
                    gearJobs.add(new GearJob(gearRs, jobRs));
                }
            }
        }
        return gearJobs;
    }

    public List<GearJob> getGearJobsByJob(Connection cxn, Job job) throws SQLException {
        List<GearJob> gearJobs = new ArrayList<>();
        String selectGearJobs = "SELECT * FROM GearJob WHERE jobID=?";
        try (PreparedStatement stmt = cxn.prepareStatement(selectGearJobs)) {
            stmt.setInt(1, job.getJobID());
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    Gear gear = gearDao.getGearByItemID(cxn, results.getInt("itemID"));
                    Job jobRs = jobDao.getJobById(cxn, job.getJobID());
                    gearJobs.add(new GearJob(gear, jobRs));
                }
            }
        }
        return gearJobs;
    }

    public List<GearJob> getAllGearJobs(Connection cxn) throws SQLException {
        List<GearJob> gearJobs = new ArrayList<>();
        String selectGearJobs = "SELECT * FROM GearJob";
        try (PreparedStatement stmt = cxn.prepareStatement(selectGearJobs)) {
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    Gear gear = gearDao.getGearByItemID(cxn, results.getInt("itemID"));
                    Job job = jobDao.getJobById(cxn, results.getInt("jobID"));
                    gearJobs.add(new GearJob(gear, job));
                }
            }
        }
        return gearJobs;
    }
}
