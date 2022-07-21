package lk.ijse.pos.repository.custom.impl;

import lk.ijse.pos.entity.Customer;
import lk.ijse.pos.entity.Orders;
import lk.ijse.pos.repository.CrudUtil;
import lk.ijse.pos.repository.custom.OrderDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public ArrayList<Orders> getAll() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM orders");
        ArrayList<Orders> allOrders =new ArrayList<>();
        while (result.next()) {
            allOrders.add(new Orders(result.getString(1),result.getDate(2).toLocalDate(),result.getString(3)));
        }
        return allOrders;
    }

    @Override
    public boolean save(Orders entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO `Orders` (orderid, orderdate, custid) VALUES (?,?,?)", entity.getOrderID(), entity.getOrderDate(), entity.getCustID());

    }

    @Override
    public boolean update(Orders entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Orders search(String id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        ResultSet result =  CrudUtil.execute("SELECT OrderID FROM `Orders` WHERE OrderID=?", id);
        return result.next();
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM orders WHERE OrderID=?", id);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT OrderID FROM `Orders` ORDER BY OrderID DESC LIMIT 1;");
        if (rst.next()) {
            String id = rst.getString("OrderID");
            int co=id.length();
            String text = id.substring(0,2);
            String num= id.substring(2,co);
            int n=Integer.parseInt(num);
            n++;
            String numInString = Integer.toString(n);
            String GenerateId = text+numInString;

            return GenerateId;
        } else {
            return "D001";
        }
    }

    @Override
    public ArrayList<Orders> getAllOrdersAccordingToCustomerID(String id) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM orders WHERE CustID=?",id);
        ArrayList<Orders> allOrders =new ArrayList<>();
        while (result.next()) {
            allOrders.add(new Orders(result.getString(1),result.getDate(2).toLocalDate(),result.getString(3)));
        }
        return allOrders;
    }
}
