package lk.ijse.pos.entity;

import java.math.BigDecimal;

public class Item {
    private String ItemCode;
    private String Description;
    private String PackSize;
    private BigDecimal UnitPrice;
    private int QtyOnHand;
    private BigDecimal Discount;

    public Item() {
    }

    public Item(String itemCode, String description, String packSize, BigDecimal unitPrice, int qtyOnHand, BigDecimal discount) {
        setItemCode(itemCode);
        setDescription(description);
        setPackSize(packSize);
        setUnitPrice(unitPrice);
        setQtyOnHand(qtyOnHand);
        setDiscount(discount);
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPackSize() {
        return PackSize;
    }

    public void setPackSize(String packSize) {
        PackSize = packSize;
    }

    public BigDecimal getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        UnitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return QtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        QtyOnHand = qtyOnHand;
    }

    public BigDecimal getDiscount() {
        return Discount;
    }

    public void setDiscount(BigDecimal discount) {
        Discount = discount;
    }

    @Override
    public String toString() {
        return "Item{" +
                "ItemCode='" + ItemCode + '\'' +
                ", Description='" + Description + '\'' +
                ", PackSize='" + PackSize + '\'' +
                ", UnitPrice=" + UnitPrice +
                ", QtyOnHand=" + QtyOnHand +
                ", Discount=" + Discount +
                '}';
    }
}
