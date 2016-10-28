package org.automobile.models.masters;

/**
 * @author Kushal
 * @Date 27-Oct-16
 */
public class YearMaster {
    private int yearId;
    private int year;

    public YearMaster(int yearId, int year) {
        this.yearId = yearId;
        this.year = year;
    }

    public YearMaster() {
    }

    public int getYearId() {
        return yearId;
    }

    public void setYearId(int yearId) {
        this.yearId = yearId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
