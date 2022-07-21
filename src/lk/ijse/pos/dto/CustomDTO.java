package lk.ijse.pos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomDTO {
    private String CID;
    private String CTitle;
    private String CName;
    private String CAddress;
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
    private String CustomerID;

    //private String OrderID ;
    //private String ItemCode ;
    private int OrderQTY;
    private BigDecimal DiscountPriceForOneItem;

    public CustomDTO() {
    }

    public CustomDTO(String ItemCode, String Description, BigDecimal UnitePrice, int QtyOnHand, int SoldItem) {
        this.ItemCode=ItemCode;
        this.Description=Description;
        this.UnitPrice=UnitePrice;
        this.QtyOnHand=QtyOnHand;
        this.OrderQTY=SoldItem;
    }

    public CustomDTO(LocalDate orderDate, BigDecimal income) {
        this.OrderDate = orderDate;
        this.UnitPrice = income;
    }


    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getCTitle() {
        return CTitle;
    }

    public void setCTitle(String CTitle) {
        this.CTitle = CTitle;
    }

    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public String getCAddress() {
        return CAddress;
    }

    public void setCAddress(String CAddress) {
        this.CAddress = CAddress;
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

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
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

    @Override
    public String toString() {
        return "CustomDTO{" +
                "CID='" + CID + '\'' +
                ", CTitle='" + CTitle + '\'' +
                ", CName='" + CName + '\'' +
                ", CAddress='" + CAddress + '\'' +
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
                ", CustomerID='" + CustomerID + '\'' +
                ", OrderQTY=" + OrderQTY +
                ", DiscountPriceForOneItem=" + DiscountPriceForOneItem +
                '}';
    }
}
