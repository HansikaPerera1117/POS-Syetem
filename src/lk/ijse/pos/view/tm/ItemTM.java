package lk.ijse.pos.view.tm;

import javafx.scene.control.Button;

import java.math.BigDecimal;

public class ItemTM {
    private String ItemCode;
    private String Description;
    private String PackSize;
    private BigDecimal UnitPrice;
    private int QtyOnHand;
    private BigDecimal Discount;
    private Button btn;

    public ItemTM() {
    }

    public ItemTM(String itemCode, String description, String packSize, BigDecimal unitPrice, int qtyOnHand, BigDecimal discount, Button btn) {
        setItemCode(itemCode);
        setDescription(description);
        setPackSize(packSize);
        setUnitPrice(unitPrice);
        setQtyOnHand(qtyOnHand);
        setDiscount(discount);
        this.setBtn(btn);
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "ItemTM{" +
                "ItemCode='" + ItemCode + '\'' +
                ", Description='" + Description + '\'' +
                ", PackSize='" + PackSize + '\'' +
                ", UnitPrice=" + UnitPrice +
                ", QtyOnHand=" + QtyOnHand +
                ", Discount=" + Discount +
                ", btn=" + btn +
                '}';
    }
}
