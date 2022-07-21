package lk.ijse.pos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderDetailDTO {
    private String OrderID ;
    private String ItemCode ;
    private int OrderQTY;
    private BigDecimal DiscountPriceForOneItem;

    private String ItemDescription;
    private BigDecimal UnitePrice;
    private LocalDate OrderDate;
    private String CustID;

    private int nonUpdatedQTY;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(String orderID, String itemCode, int orderQTY, BigDecimal discountPriceForOneItem) {
        setOrderID(orderID);
        setItemCode(itemCode);
        setOrderQTY(orderQTY);
        setDiscountPriceForOneItem(discountPriceForOneItem);

    }

    public OrderDetailDTO(String OrderId, LocalDate orderDate, String CustomerId, String ItemCode, String Description, BigDecimal UnitrPrice, BigDecimal Discount, int OrderQty) {
        this.setOrderID(OrderId);
        this.setOrderDate(orderDate);
        this.setCustID(CustomerId);
        this.setItemCode(ItemCode);
        this.setItemDescription(Description);
        this.setUnitePrice(UnitrPrice);
        this.setDiscountPriceForOneItem(Discount);
        this.setOrderQTY(OrderQty);
    }

    public OrderDetailDTO(String selectedOrderId, String itemCode, int orderQty, BigDecimal discount, int nonUpdateQTY) {
        this.OrderID = selectedOrderId;
        this.ItemCode = itemCode;
        this.OrderQTY=orderQty;
        this.DiscountPriceForOneItem=discount;
        this.nonUpdatedQTY=nonUpdateQTY;
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

    public BigDecimal getDiscountPriceForOneItem() {
        return DiscountPriceForOneItem;
    }

    public void setDiscountPriceForOneItem(BigDecimal discountPriceForOneItem) {
        DiscountPriceForOneItem = discountPriceForOneItem;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public BigDecimal getUnitePrice() {
        return UnitePrice;
    }

    public void setUnitePrice(BigDecimal unitePrice) {
        UnitePrice = unitePrice;
    }

    public LocalDate getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        OrderDate = orderDate;
    }

    public String getCustID() {
        return CustID;
    }

    public void setCustID(String custID) {
        CustID = custID;
    }

    public int getNonUpdatedQTY() {
        return nonUpdatedQTY;
    }

    public void setNonUpdatedQTY(int nonUpdatedQTY) {
        this.nonUpdatedQTY = nonUpdatedQTY;
    }

    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "OrderID='" + OrderID + '\'' +
                ", ItemCode='" + ItemCode + '\'' +
                ", OrderQTY=" + OrderQTY +
                ", DiscountPriceForOneItem=" + DiscountPriceForOneItem +
                ", ItemDescription='" + ItemDescription + '\'' +
                ", UnitePrice=" + UnitePrice +
                ", OrderDate=" + OrderDate +
                ", CustID='" + CustID + '\'' +
                ", nonUpdatedQTY=" + nonUpdatedQTY +
                '}';
    }
}
