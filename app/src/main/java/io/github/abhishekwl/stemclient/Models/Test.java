package io.github.abhishekwl.stemclient.Models;

import com.google.gson.annotations.SerializedName;

public class Test {

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
}