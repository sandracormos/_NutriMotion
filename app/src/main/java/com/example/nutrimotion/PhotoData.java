package com.example.nutrimotion;

public class PhotoData {
    private String photoPath;
    private String photoDate;

    public PhotoData() {
        // Default constructor required for Firestore
    }

    public PhotoData(String photoPath, String photoDate) {
        this.photoPath = photoPath;
        this.photoDate = photoDate;
    }

    // Add public getters and setters for each field
    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoDate() {
        return photoDate;
    }

    public void setPhotoDate(String photoDate) {
        this.photoDate = photoDate;
    }
}
