package cs5200project.model;

public class CharacterJob {
    private GameCharacter character;
    private Job job;
    private boolean isUnlocked;
    private int jobLevel;

    public CharacterJob(GameCharacter character, Job job, boolean isUnlocked, int jobLevel) {
        this.character = character;
        this.job = job;
        this.isUnlocked = isUnlocked;
        this.jobLevel = jobLevel;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public void setCharacter(GameCharacter character) {
        this.character = character;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean isUnlocked) {
        this.isUnlocked = isUnlocked;
    }

    public int getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(int jobLevel) {
        this.jobLevel = jobLevel;
    }

    @Override
    public String toString() {
        return String.format("CharacterJob[character=%s, job=%s, isUnlocked=%b, jobLevel=%d]",
            character, job, isUnlocked, jobLevel);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CharacterJob other = (CharacterJob) obj;
        return character.equals(other.character) &&
               job.equals(other.job) &&
               isUnlocked == other.isUnlocked &&
               jobLevel == other.jobLevel;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + character.hashCode();
        result = 31 * result + job.hashCode();
        result = 31 * result + (isUnlocked ? 1 : 0);
        result = 31 * result + jobLevel;
        return result;
    }
}
