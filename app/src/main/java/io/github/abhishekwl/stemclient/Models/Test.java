package io.github.abhishekwl.stemclient.Models;

import android.os.Parcel;
import com.google.gson.annotations.SerializedName;

public class Test implements android.os.Parcelable {

    @SerializedName("name") private String testName;
    @SerializedName("price") private double testPrice;
    @SerializedName("hospital") private Hospital testHospital;
    private boolean testSelected;

    public Test(String testName, double testPrice, Hospital testHospital) {
        this.testName = testName;
        this.testPrice = testPrice;
        this.testHospital = testHospital;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public double getTestPrice() {
        return testPrice;
    }

    public void setTestPrice(double testPrice) {
        this.testPrice = testPrice;
    }

    public Hospital getTestHospital() {
        return testHospital;
    }

    public void setTestHospital(Hospital testHospital) {
        this.testHospital = testHospital;
    }

    public boolean isTestSelected() {
        return testSelected;
    }

    public void setTestSelected(boolean testSelected) {
        this.testSelected = testSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.testName);
        dest.writeDouble(this.testPrice);
        dest.writeParcelable(this.testHospital, flags);
        dest.writeByte(this.testSelected ? (byte) 1 : (byte) 0);
    }

    private Test(Parcel in) {
        this.testName = in.readString();
        this.testPrice = in.readDouble();
        this.testHospital = in.readParcelable(Hospital.class.getClassLoader());
        this.testSelected = in.readByte() != 0;
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel source) {
            return new Test(source);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };
}