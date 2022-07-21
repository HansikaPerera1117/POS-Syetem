package lk.ijse.pos.repository.custom.impl;

import lk.ijse.pos.dto.OrderDetailDTO;
import lk.ijse.pos.entity.Customer;
import lk.ijse.pos.entity.Item;
import lk.ijse.pos.entity.OrderDetail;
import lk.ijse.pos.repository.CrudUtil;
import lk.ijse.pos.repository.custom.OrderDetailDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public ArrayList<OrderDetail> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(OrderDetail entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO OrderDetail (OrderID, ItemCode, OrderQTY, Discount) VALUES (?,?,?,?)", entity.getOrderID(), entity.getItemCode(), entity.getOrderQTY(), entity.getDiscount());

    }

    @Override
    public boolean update(OrderDetail entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public OrderDetail search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM orderdetail WHERE OrderID=?", id);
        if (rst.next()) {
            return new OrderDetail(rst.getString(1), rst.getString(2), rst.getInt(3),rst.getBigDecimal(4));
        }
        return null;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean removeOrderDetails(String oid, String itemCode) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM orderdetail WHERE OrderID=?AND ItemCode=?",oid,itemCode);
    }

    @Override
    public boolean updateOrderDetails(OrderDetail entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE orderdetail SET OrderQTY=? WHERE OrderID=? AND ItemCode=?",entity.getOrderQTY(),entity.getOrderID(),entity.getItemCode());
    }

    @Override
    public OrderDetail searchRemovedOrderDetail(String oid, String ItemCode) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM orderdetail WHERE OrderID=? AND ItemCode=?", oid,ItemCode);
        if (rst.next()) {
            return new OrderDetail(rst.getString(1), rst.getString(2),rst.getInt(3),rst.getBigDecimal(4));
        }
        return null;
    }

}
