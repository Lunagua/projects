package com.kkb.groupForOrder;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class OrderBean implements WritableComparable<OrderBean> {
    private String orderNo;
    private Double price;
    @Override
    public int compareTo(OrderBean o) {
        int orderNoCompare = this.orderNo.compareTo(o.orderNo);
        if (orderNoCompare == 0) {
            return  -this.price.compareTo(o.price);
        }
        return orderNoCompare;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(orderNo);
        out.writeDouble(price);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.orderNo = in.readUTF();
        this.price = in.readDouble();
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "orderNo='" + orderNo + '\'' +
                ", price=" + price +
                '}';
    }

    public OrderBean() {
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
