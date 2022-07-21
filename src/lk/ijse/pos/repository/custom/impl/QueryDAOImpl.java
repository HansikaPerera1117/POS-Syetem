package lk.ijse.pos.repository.custom.impl;

import lk.ijse.pos.entity.CustomEntity;
import lk.ijse.pos.entity.Customer;
import lk.ijse.pos.entity.Item;
import lk.ijse.pos.repository.CrudUtil;
import lk.ijse.pos.repository.custom.CustomerDAO;
import lk.ijse.pos.repository.custom.QueryDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class QueryDAOImpl implements QueryDAO {
    @Override
    public ArrayList<CustomEntity> searchOrderByOrderID(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT  orderdetail.OrderID,OrderDate,CustID,i.ItemCode,Description,UnitPrice,orderdetail.Discount,OrderQTY from orderdetail inner join item i on orderdetail.ItemCode = i.ItemCode inner join orders o on orderdetail.OrderID = o.OrderID where o.OrderID=?",id);
        ArrayList<CustomEntity> orderRecords = new ArrayList();
        while (rst.next()) {
            orderRecords.add(new CustomEntity(rst.getString(1), LocalDate.parse(rst.getString(2)), rst.getString(3), rst.getString(4), rst.getString(5), rst.getBigDecimal(6),rst.getBigDecimal(7),rst.getInt(8)));
        }
        return orderRecords;
    }

    @Override
    public ArrayList<CustomEntity> mostMovableItems() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT item.ItemCode,Description,UnitPrice,QtyOnHand,SUM(OrderQTY) from item inner join orderdetail on item.ItemCode = orderdetail.ItemCode GROUP BY ItemCode ORDER BY SUM(OrderQTY) DESC");
        ArrayList<CustomEntity> mostMovable =new ArrayList<>();
        while (result.next()) {
            mostMovable.add(new CustomEntity(result.getString(1),result.getString(2),result.getBigDecimal(3),result.getInt(4),result.getInt(5)));
        }
        return mostMovable;
    }

    @Override
    public ArrayList<CustomEntity> lessMovableItems() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT item.ItemCode,Description,UnitPrice,QtyOnHand,SUM(OrderQTY) from item inner join orderdetail on item.ItemCode = orderdetail.ItemCode GROUP BY ItemCode ORDER BY SUM(OrderQTY)");
        ArrayList<CustomEntity> lessMovable =new ArrayList<>();
        while (result.next()) {
            lessMovable.add(new CustomEntity(result.getString(1),result.getString(2),result.getBigDecimal(3),result.getInt(4),result.getInt(5)));
        }
        return lessMovable;
    }

    @Override
    public ArrayList<CustomEntity> dailyIncomeReport() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT OrderDate,SUM(OrderQTY*UnitPrice-o.Discount)FROM orders INNER JOIN orderdetail o on orders.OrderID = o.OrderID INNER JOIN item i on o.ItemCode = i.ItemCode GROUP BY OrderDate ORDER BY OrderDate");
        ArrayList<CustomEntity> dailyIncome =new ArrayList<>();
        while (result.next()) {
            dailyIncome.add(new CustomEntity(result.getDate(1).toLocalDate(),result.getBigDecimal(2)));
        }
        return dailyIncome;
    }

}
