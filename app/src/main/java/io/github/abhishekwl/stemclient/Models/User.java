package io.github.abhishekwl.stemclient.Models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("uid") private String userId;
    @SerializedName("name") private String userName;
    @SerializedName("age") private int userAge;
    @SerializedName("blood") private String userBloodGroup;
    @SerializedName("gender") private boolean userGender;
    @SerializedName("contact") private String userContactNumber;
    @SerializedName("email") private String userEmailId;
    @SerializedName("image") private String userImageUrl;
    @SerializedName("additional") private String userAdditionalInfo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public String getUserBloodGroup() {
        return userBloodGroup;
    }

    public void setUserBloodGroup(String userBloodGroup) {
        this.userBloodGroup = userBloodGroup;
    }

    public boolean isUserGender() {
        return userGender;
    }

    public void setUserGender(boolean userGender) {
        this.userGender = userGender;
    }

    public String getUserContactNumber() {
        return userContactNumber;
    }

    public void setUserContactNumber(String userContactNumber) {
        this.userContactNumber = userContactNumber;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUserAdditionalInfo() {
        return userAdditionalInfo;
    }

    public void setUserAdditionalInfo(String userAdditionalInfo) {
        this.userAdditionalInfo = userAdditionalInfo;
    }

    public User(String userId, String userName, int userAge, String userBloodGroup, boolean userGender, String userContactNumber, String userImageUrl, String userEmailId) {
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.userBloodGroup = userBloodGroup;
        this.userGender = userGender;
        this.userContactNumber = userContactNumber;
        this.userImageUrl = userImageUrl;
        this.userEmailId = userEmailId;
    }

    public User(String userId, String userName, int userAge, String userBloodGroup, boolean userGender, String userContactNumber, String userEmailId) {
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.userBloodGroup = userBloodGroup;
        this.userGender = userGender;
        this.userContactNumber = userContactNumber;
        this.userEmailId = userEmailId;
    }

    public User(String userId, String userName, int userAge, String userBloodGroup, boolean userGender, String userContactNumber, String userImageUrl, String userAdditionalInfo, String userEmailId) {
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.userBloodGroup = userBloodGroup;
        this.userGender = userGender;
        this.userContactNumber = userContactNumber;
        this.userImageUrl = userImageUrl;
        this.userAdditionalInfo = userAdditionalInfo;
        this.userEmailId = userEmailId;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }
}
