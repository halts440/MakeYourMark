package com.humayunafzal.makeyourmark;

public class CollectionItem {
    private String collectionId;
    private String imagePath;
    private String userId;

    public CollectionItem() {
    }

    public CollectionItem(String collectionId, String imagePath, String userId) {
        this.collectionId = collectionId;
        this.imagePath = imagePath;
        this.userId = userId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
