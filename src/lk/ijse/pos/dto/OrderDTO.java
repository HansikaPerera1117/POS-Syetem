package lk.ijse.pos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class OrderDTO {

    private String OrderID;
    private LocalDate OrderDate;
    private String CustomerID;

    private List<OrderDetailDTO> orderDetails;

    private String customerName;
    private BigDecimal orderTotal;

    public OrderDTO() {
    }

    public OrderDTO(String orderID, LocalDate orderDate, String customerID) {
        setOrderID(orderID);
        setOrderDate(orderDate);
        setCustomerID(customerID);
    }

    public OrderDTO(String orderId, LocalDate orderDate, String customerId, List<OrderDetailDTO> orderDetails) {
        this.OrderID = orderId;
        this.OrderDate = orderDate;
        this.CustomerID = customerId;
        this.orderDetails = orderDetails;
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

    public List<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "OrderID='" + OrderID + '\'' +
                ", OrderDate=" + OrderDate +
                ", CustomerID='" + CustomerID + '\'' +
                ", orderDetails=" + orderDetails +
                ", customerName='" + customerName + '\'' +
                ", orderTotal=" + orderTotal +
                '}';
    }
}
