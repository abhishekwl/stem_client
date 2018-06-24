package io.github.abhishekwl.stemclient.Models;

import com.google.gson.annotations.SerializedName;

public class Hospital {

    @SerializedName("uid") private String hospitalUid;
    @SerializedName("name") private String hospitalName;
    @SerializedName("latitude") private double hospitalLatitude;
    @SerializedName("longitude") private double hospitalLongitude;
    @SerializedName("city") private String hospitalCity;
    @SerializedName("image") private String hospitalImageUrl;

    public Hospital(String hospitalUid, String hospitalName, double hospitalLatitude, double hospitalLongitude, String hospitalCity) {
        this.hospitalUid = hospitalUid;
        this.hospitalName = hospitalName;
        this.hospitalLatitude = hospitalLatitude;
        this.hospitalLongitude = hospitalLongitude;
        this.hospitalCity = hospitalCity;
        this.hospitalImageUrl = "";
    }

    public Hospital(String hospitalUid, String hospitalName, double hospitalLatitude, double hospitalLongitude, String hospitalCity, String hospitalImageUrl) {
        this.hospitalUid = hospitalUid;
        this.hospitalName = hospitalName;
        this.hospitalLatitude = hospitalLatitude;
        this.hospitalLongitude = hospitalLongitude;
        this.hospitalCity = hospitalCity;
        this.hospitalImageUrl = hospitalImageUrl;
    }

    public String getHospitalUid() {
        return hospitalUid;
    }

    public void setHospitalUid(String hospitalUid) {
        this.hospitalUid = hospitalUid;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public double getHospitalLatitude() {
        return hospitalLatitude;
    }

    public void setHospitalLatitude(double hospitalLatitude) {
        this.hospitalLatitude = hospitalLatitude;
    }

    public double getHospitalLongitude() {
        return hospitalLongitude;
    }

    public void setHospitalLongitude(double hospitalLongitude) {
        this.hospitalLongitude = hospitalLongitude;
    }

    public String getHospitalCity() {
        return hospitalCity;
    }

    public void setHospitalCity(String hospitalCity) {
        this.hospitalCity = hospitalCity;
    }

    public String getHospitalImageUrl() {
        return hospitalImageUrl;
    }

    public void setHospitalImageUrl(String hospitalImageUrl) {
        this.hospitalImageUrl = hospitalImageUrl;
    }
}
