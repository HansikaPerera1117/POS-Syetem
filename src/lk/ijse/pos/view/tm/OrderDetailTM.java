package lk.ijse.pos.view.tm;

import javafx.scene.control.Button;

import java.math.BigDecimal;

public class OrderDetailTM {
    private String ItemCode;
    private String Description;
    private int Qty;
    private BigDecimal UnitPrice;
    private BigDecimal Total;
    private BigDecimal Discount;
    private Button btn;

    public OrderDetailTM() {
    }

    public OrderDetailTM(String itemCode, String description, int qty, BigDecimal unitPrice, BigDecimal total, BigDecimal discount, Button btn) {
        ItemCode = itemCode;
        Description = description;
        Qty = qty;
        UnitPrice = unitPrice;
        Total = total;
        Discount = discount;
        this.btn = btn;
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

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public BigDecimal getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        UnitPrice = unitPrice;
    }

    public BigDecimal getTotal() {
        return Total;
    }

    public void setTotal(BigDecimal total) {
        Total = total;
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
        return "OrderDetailTM{" +
                "ItemCode='" + ItemCode + '\'' +
                ", Description='" + Description + '\'' +
                ", Qty=" + Qty +
                ", UnitPrice=" + UnitPrice +
                ", Total=" + Total +
                ", Discount=" + Discount +
                ", btn=" + btn +
                '}';
    }
}
