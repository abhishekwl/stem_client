package io.github.abhishekwl.stemclient.Models;

import android.os.Parcel;
import com.google.gson.annotations.SerializedName;

public class Hospital implements android.os.Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hospitalUid);
        dest.writeString(this.hospitalName);
        dest.writeDouble(this.hospitalLatitude);
        dest.writeDouble(this.hospitalLongitude);
        dest.writeString(this.hospitalCity);
        dest.writeString(this.hospitalImageUrl);
    }

    private Hospital(Parcel in) {
        this.hospitalUid = in.readString();
        this.hospitalName = in.readString();
        this.hospitalLatitude = in.readDouble();
        this.hospitalLongitude = in.readDouble();
        this.hospitalCity = in.readString();
        this.hospitalImageUrl = in.readString();
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel source) {
            return new Hospital(source);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };
}
