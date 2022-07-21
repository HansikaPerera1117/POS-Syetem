package lk.ijse.pos.repository.custom;

import lk.ijse.pos.dto.OrderDTO;
import lk.ijse.pos.entity.Orders;
import lk.ijse.pos.repository.CrudDAO;
import lk.ijse.pos.repository.SuperDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDAO extends CrudDAO<Orders,String> {
    ArrayList<Orders> getAllOrdersAccordingToCustomerID(String id) throws SQLException, ClassNotFoundException;

}
