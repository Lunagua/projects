package com.kkb.groupForOrder;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class OrderGroup extends WritableComparator {
    public OrderGroup() {
        super(OrderBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean A = (OrderBean) a;
        OrderBean B = (OrderBean) b;
        return A.getOrderNo().compareTo(B.getOrderNo());
    }
}
