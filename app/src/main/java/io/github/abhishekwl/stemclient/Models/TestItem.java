package io.github.abhishekwl.stemclient.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class TestItem implements Parcelable {

    private String testName;
    private String hospitalName;
    private int testPrice;
    private String hospitalImageUrl;
    private boolean testSelected;
    private String hospitalUid;
    private String testId;
    private double hospitalLatitude, hospitalLongitude;
    private int testPopularity;

    public TestItem(String testName, String hospitalName, int testPrice, String hospitalUid, String testId, String hospitalImageUrl, double hospitalLatitude, double hospitalLongitude) {
        this.testName = testName;
        this.hospitalName = hospitalName;
        this.testPrice = testPrice;
        this.hospitalImageUrl = hospitalImageUrl;
        this.testSelected = false;
        this.hospitalUid = hospitalUid;
        this.testId = testId;
        this.hospitalLatitude = hospitalLatitude;
        this.hospitalLongitude = hospitalLongitude;
        this.testPopularity = 0;
    }

    public TestItem(String testName, String hospitalName, int testPrice, String hospitalUid, String testId, double hospitalLatitude, double hospitalLongitude) {
        this.testName = testName;
        this.hospitalName = hospitalName;
        this.testPrice = testPrice;
        this.hospitalImageUrl = "";
        this.hospitalUid = hospitalUid;
        this.testId = testId;
        this.hospitalLatitude = hospitalLatitude;
        this.hospitalLongitude = hospitalLongitude;
        this.testPopularity = 0;
    }

    public TestItem() {
        this.testSelected = false;
    }

    public static final Creator<TestItem> CREATOR = new Creator<TestItem>() {
        @Override
        public TestItem createFromParcel(Parcel in) {
            return new TestItem(in);
        }

        @Override
        public TestItem[] newArray(int size) {
            return new TestItem[size];
        }
    };

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getTestPrice() {
        return testPrice;
    }

    public void setTestPrice(int testPrice) {
        this.testPrice = testPrice;
    }

    public String getHospitalImageUrl() {
        return hospitalImageUrl;
    }

    public void setHospitalImageUrl(String hospitalImageUrl) {
        this.hospitalImageUrl = hospitalImageUrl;
    }

    public boolean isTestSelected() {
        return testSelected;
    }

    public void setTestSelected(boolean testSelected) {
        this.testSelected = testSelected;
    }

    public String getHospitalUid() {
        return hospitalUid;
    }

    public void setHospitalUid(String hospitalUid) {
        this.hospitalUid = hospitalUid;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
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

    public int getTestPopularity() {
        return testPopularity;
    }

    public void setTestPopularity(int testPopularity) {
        this.testPopularity = testPopularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(testName);
        dest.writeString(hospitalName);
        dest.writeInt(testPrice);
        dest.writeString(hospitalImageUrl);
        dest.writeString(hospitalUid);
        dest.writeString(testId);
        dest.writeDouble(hospitalLatitude);
        dest.writeDouble(hospitalLongitude);
        dest.writeInt(testPopularity);
    }

    private TestItem(Parcel source) {
        this.testName = source.readString();
        this.hospitalName = source.readString();
        this.testPrice = source.readInt();
        this.hospitalImageUrl = source.readString();
        this.hospitalUid = source.readString();
        this.testId = source.readString();
        this.hospitalLatitude = source.readDouble();
        this.hospitalLongitude = source.readDouble();
        this.testPopularity = source.readInt();
    }
}
