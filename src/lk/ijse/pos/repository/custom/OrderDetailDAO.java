package lk.ijse.pos.repository.custom;

import lk.ijse.pos.dto.OrderDetailDTO;
import lk.ijse.pos.entity.OrderDetail;
import lk.ijse.pos.repository.CrudDAO;
import lk.ijse.pos.repository.SuperDAO;

import java.sql.SQLException;

public interface OrderDetailDAO extends CrudDAO<OrderDetail,String> {
    public boolean removeOrderDetails(String oid,String itemCode) throws SQLException, ClassNotFoundException;

    public boolean updateOrderDetails(OrderDetail entity) throws SQLException, ClassNotFoundException;

    OrderDetail searchRemovedOrderDetail(String oid,String ItemCode) throws SQLException, ClassNotFoundException;
}
