package com.exampletest.testtt.models.tms.search.orders;

public class UnloadingPlaces {
    private StoragePoint storagePoint;

    public StoragePoint getStoragePoint() {
        return storagePoint;
    }

    public void setStoragePoint(StoragePoint storagePoint) {
        this.storagePoint = storagePoint;
    }

    @Override
    public String toString() {
        return "UnloadingPlaces{" +
                "storagePoint=" + storagePoint +
                '}';
    }
}
