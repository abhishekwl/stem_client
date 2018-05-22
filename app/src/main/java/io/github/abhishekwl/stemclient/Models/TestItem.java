package io.github.abhishekwl.stemclient.Models;

public class TestItem {

    private String testName;
    private String hospitalName;
    private int testPrice;
    private String hospitalImageUrl;
    private boolean testSelected;
    private String hospitalUid;
    private String testId;

    public TestItem(String testName, String hospitalName, int testPrice, String hospitalUid, String testId, String hospitalImageUrl) {
        this.testName = testName;
        this.hospitalName = hospitalName;
        this.testPrice = testPrice;
        this.hospitalImageUrl = hospitalImageUrl;
        this.testSelected = false;
        this.hospitalUid = hospitalUid;
        this.testId = testId;
    }

    public TestItem(String testName, String hospitalName, int testPrice, String hospitalUid, String testId) {
        this.testName = testName;
        this.hospitalName = hospitalName;
        this.testPrice = testPrice;
        this.hospitalImageUrl = "";
        this.hospitalUid = hospitalUid;
        this.testId = testId;
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
}
