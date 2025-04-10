package cs5200project.model;

public class Statistic {
    private int statID;
    private int statTypeID;
    private int baseValue;

    public Statistic(int statID, int statTypeID, int baseValue) {
        this.statID = statID;
        this.statTypeID = statTypeID;
        this.baseValue = baseValue;
    }

    public int getStatID() {
        return statID;
    }

    public void setStatID(int statID) {
        this.statID = statID;
    }

    public int getStatTypeID() {
        return statTypeID;
    }

    public void setStatTypeID(int statTypeID) {
        this.statTypeID = statTypeID;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    @Override
    public String toString() {
        return String.format("Statistic[statID=%d, statTypeID=%d, baseValue=%d]",
            statID, statTypeID, baseValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Statistic other = (Statistic) obj;
        return statID == other.statID &&
               statTypeID == other.statTypeID &&
               baseValue == other.baseValue;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + statID;
        result = 31 * result + statTypeID;
        result = 31 * result + baseValue;
        return result;
    }
}
