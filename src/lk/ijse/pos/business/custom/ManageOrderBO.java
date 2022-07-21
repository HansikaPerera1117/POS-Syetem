package lk.ijse.pos.business.custom;

import lk.ijse.pos.business.SuperBO;
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.dto.ItemDTO;
import lk.ijse.pos.dto.OrderDTO;
import lk.ijse.pos.dto.OrderDetailDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ManageOrderBO extends SuperBO {

    boolean removeOrder(String id) throws SQLException, ClassNotFoundException;

    boolean updateOrderDetails(OrderDetailDTO dto) throws SQLException, ClassNotFoundException;

    boolean removeOrderDetails(String oid,String itemCode) throws SQLException, ClassNotFoundException;

    boolean orderExist(String id) throws SQLException, ClassNotFoundException;

    ArrayList<OrderDetailDTO> searchOrderDetail(String id) throws SQLException, ClassNotFoundException;

    OrderDetailDTO searchRemovedOrderDetail(String oid,String ItemCode) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String code) throws SQLException, ClassNotFoundException;

    ArrayList<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException;

    ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException;

    ArrayList<OrderDTO> getAllOrdersAccordingToCustomerID(String id) throws SQLException, ClassNotFoundException;
}
