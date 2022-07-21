package lk.ijse.pos.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public class CustomEntity {
    private String CustID;
    private String CustTitle;
    private String CustName;
    private String CustAddress;
    private String City;
    private String Province;
    private String PostalCode;


    private String ItemCode;
    private String Description;
    private String PackSize;
    private BigDecimal UnitPrice;
    private int QtyOnHand;
    private BigDecimal Discount;


    private String OrderID;
    private LocalDate OrderDate;
   // private String CustID;


    //private String OrderID ;
    // private String ItemCode ;
    private int OrderQTY;
    // private BigDecimal Discount;


    public CustomEntity() {
    }

    public CustomEntity(String ItemCode, String Description, BigDecimal UnitePrice, int QtyOnHand, int SoldItem) {
        this.ItemCode=ItemCode;
        this.Description=Description;
        this.UnitPrice=UnitePrice;
        this.QtyOnHand=QtyOnHand;
        this.OrderQTY=SoldItem;
    }

    public CustomEntity(String OrderId, LocalDate orderDate, String CustomerId, String ItemCode, String Description, BigDecimal UnitrPrice, BigDecimal Discount, int OrderQty) {
        this.OrderID = OrderId;
        this.OrderDate = orderDate;
        this.CustID = CustomerId;
        this.ItemCode = ItemCode;
        this.Description = Description;
        this.UnitPrice = UnitrPrice;
        this.Discount = Discount;
        this.OrderQTY = OrderQty;
    }

    public CustomEntity(LocalDate OrderDate, BigDecimal Income) {
        this.OrderDate = OrderDate;
        this.UnitPrice = Income;
    }

    public String getCustID() {
        return CustID;
    }

    public void setCustID(String custID) {
        CustID = custID;
    }

    public String getCustTitle() {
        return CustTitle;
    }

    public void setCustTitle(String custTitle) {
        CustTitle = custTitle;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getCustAddress() {
        return CustAddress;
    }

    public void setCustAddress(String custAddress) {
        CustAddress = custAddress;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
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

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public LocalDate getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        OrderDate = orderDate;
    }

    public int getOrderQTY() {
        return OrderQTY;
    }

    public void setOrderQTY(int orderQTY) {
        OrderQTY = orderQTY;
    }

    @Override
    public String toString() {
        return "CustomEntity{" +
                "CustID='" + CustID + '\'' +
                ", CustTitle='" + CustTitle + '\'' +
                ", CustName='" + CustName + '\'' +
                ", CustAddress='" + CustAddress + '\'' +
                ", City='" + City + '\'' +
                ", Province='" + Province + '\'' +
                ", PostalCode='" + PostalCode + '\'' +
                ", ItemCode='" + ItemCode + '\'' +
                ", Description='" + Description + '\'' +
                ", PackSize='" + PackSize + '\'' +
                ", UnitPrice=" + UnitPrice +
                ", QtyOnHand=" + QtyOnHand +
                ", Discount=" + Discount +
                ", OrderID='" + OrderID + '\'' +
                ", OrderDate=" + OrderDate +
                ", OrderQTY=" + OrderQTY +
                '}';
    }
}
