package uc.jarvis;

public class ProcessedSensorDataObject {

    private double avgX = 0.0;
    private double avgY = 0.0;
    private double avgZ = 0.0;

    private double minX = 0.0;
    private double minY = 0.0;
    private double minZ = 0.0;

    private double maxX = 0.0;
    private double maxY = 0.0;
    private double maxZ = 0.0;

    private double rmsX = 0.0;
    private double rmsY = 0.0;
    private double rmsZ = 0.0;

    public double getAvgX() {
        return avgX;
    }

    public void setAvgX(double avgX) {
        this.avgX = avgX;
    }

    public double getAvgY() {
        return avgY;
    }

    public void setAvgY(double avgY) {
        this.avgY = avgY;
    }

    public double getAvgZ() {
        return avgZ;
    }

    public void setAvgZ(double avgZ) {
        this.avgZ = avgZ;
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMinZ() {
        return minZ;
    }

    public void setMinZ(double minZ) {
        this.minZ = minZ;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(double maxZ) {
        this.maxZ = maxZ;
    }

    public double getRmsX() {
        return rmsX;
    }

    public void setRmsX(double rmsX) {
        this.rmsX = rmsX;
    }

    public double getRmsY() {
        return rmsY;
    }

    public void setRmsY(double rmsY) {
        this.rmsY = rmsY;
    }

    public double getRmsZ() {
        return rmsZ;
    }

    public void setRmsZ(double rmsZ) {
        this.rmsZ = rmsZ;
    }
}
