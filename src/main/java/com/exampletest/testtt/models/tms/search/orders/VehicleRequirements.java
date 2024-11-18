package com.exampletest.testtt.models.tms.search.orders;

public class VehicleRequirements {
    private String name;
    private BodySubtype bodySubtype;

    public BodySubtype getBodySubtype() {
        return bodySubtype;
    }

    public void setBodySubtype(BodySubtype bodySubtype) {
        this.bodySubtype = bodySubtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "VehicleRequirements{" +
                "name='" + name + '\'' +
                ", bodySubtype=" + bodySubtype +
                '}';
    }
}
