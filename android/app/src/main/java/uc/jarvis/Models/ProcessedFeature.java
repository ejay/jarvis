package uc.jarvis.Models;

/**
 * Model for database usage contains processed features of accelerometer data
 */
public class ProcessedFeature {

    public Integer id;
    public long timestamp;
    public double avgX;
    public double avgY;
    public double avgZ;

    public double minX;
    public double minY;
    public double minZ;

    public double maxX;
    public double maxY;
    public double maxZ;

    public double rmsX;
    public double rmsY;
    public double rmsZ;
}
