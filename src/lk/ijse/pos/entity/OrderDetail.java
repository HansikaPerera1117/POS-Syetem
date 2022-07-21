package lk.ijse.pos.entity;

import java.math.BigDecimal;

public class OrderDetail {

    private String OrderID ;
    private String ItemCode ;
    private int OrderQTY;
    private BigDecimal Discount;


    public OrderDetail() {
    }

    public OrderDetail(String orderID, String itemCode, int orderQTY, BigDecimal discount) {
        setOrderID(orderID);
        setItemCode(itemCode);
        setOrderQTY(orderQTY);
        setDiscount(discount);
    }


    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public int getOrderQTY() {
        return OrderQTY;
    }

    public void setOrderQTY(int orderQTY) {
        OrderQTY = orderQTY;
    }

    public BigDecimal getDiscount() {
        return Discount;
    }

    public void setDiscount(BigDecimal discount) {
        Discount = discount;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "OrderID='" + OrderID + '\'' +
                ", ItemCode='" + ItemCode + '\'' +
                ", OrderQTY=" + OrderQTY +
                ", Discount=" + Discount +
                '}';
    }
}
