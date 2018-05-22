package io.github.abhishekwl.stemclient.Models;

public class TestItem {

    private String testName;
    private String hospitalName;
    private int testPrice;
    private String hospitalImageUrl;

    public TestItem(String testName, String hospitalName, int testPrice, String hospitalImageUrl) {
        this.testName = testName;
        this.hospitalName = hospitalName;
        this.testPrice = testPrice;
        this.hospitalImageUrl = hospitalImageUrl;
    }


    public TestItem(String testName, String hospitalName, int testPrice) {
        this.testName = testName;
        this.hospitalName = hospitalName;
        this.testPrice = testPrice;
        this.hospitalImageUrl = "";
    }

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
}
