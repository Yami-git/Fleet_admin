package com.logistics.fleet_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "anomaly")
public class AnomalyConfig {

    private Deviation deviation = new Deviation();

    public Deviation getDeviation() {
        return deviation;
    }

    public void setDeviation(Deviation deviation) {
        this.deviation = deviation;
    }

    public static class Deviation {
        private double thresholdMeters = 500;
        private int checkIntervalSeconds = 30;
        private double highThresholdMeters = 1000;
        private double mediumThresholdMeters = 500;
        private double lowThresholdMeters = 200;

        public double getThresholdMeters() {
            return thresholdMeters;
        }

        public void setThresholdMeters(double thresholdMeters) {
            this.thresholdMeters = thresholdMeters;
        }

        public int getCheckIntervalSeconds() {
            return checkIntervalSeconds;
        }

        public void setCheckIntervalSeconds(int checkIntervalSeconds) {
            this.checkIntervalSeconds = checkIntervalSeconds;
        }

        public double getHighThresholdMeters() {
            return highThresholdMeters;
        }

        public void setHighThresholdMeters(double highThresholdMeters) {
            this.highThresholdMeters = highThresholdMeters;
        }

        public double getMediumThresholdMeters() {
            return mediumThresholdMeters;
        }

        public void setMediumThresholdMeters(double mediumThresholdMeters) {
            this.mediumThresholdMeters = mediumThresholdMeters;
        }

        public double getLowThresholdMeters() {
            return lowThresholdMeters;
        }

        public void setLowThresholdMeters(double lowThresholdMeters) {
            this.lowThresholdMeters = lowThresholdMeters;
        }
    }
}
