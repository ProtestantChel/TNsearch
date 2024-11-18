package com.exampletest.testtt.models.tms.search.orders;

public class OrderAttributes {
   private String firstGoodsName;
   private Integer goodsCount;

    public String getFirstGoodsName() {
        return firstGoodsName;
    }

    public void setFirstGoodsName(String firstGoodsName) {
        this.firstGoodsName = firstGoodsName;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    @Override
    public String toString() {
        return "OrderAttributes{" +
                "firstGoodsName='" + firstGoodsName + '\'' +
                ", goodsCount=" + goodsCount +
                '}';
    }
}
