package com.exampletest.testtt.models.tms.search.orders;

import java.io.Serializable;

public class LoadingPlaces implements Serializable {
    private String department;
    private StoragePoint storagePoint;
    private String id;
    private String name;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public StoragePoint getStoragePoint() {
        return storagePoint;
    }

    public void setStoragePoint(StoragePoint storagePoint) {
        this.storagePoint = storagePoint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LoadingPlaces{" +
                "department='" + department + '\'' +
                ", storagePoint='" + storagePoint + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

