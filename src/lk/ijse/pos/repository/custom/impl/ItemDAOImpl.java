package lk.ijse.pos.repository.custom.impl;

import lk.ijse.pos.entity.Item;
import lk.ijse.pos.repository.CrudUtil;
import lk.ijse.pos.repository.custom.ItemDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public ArrayList<Item> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Item");
        ArrayList<Item> allItems = new ArrayList<>();
        while (rst.next()) {
            allItems.add(new Item(rst.getString(1), rst.getString(2), rst.getString(3),rst.getBigDecimal(4),rst.getInt(5),rst.getBigDecimal(6)));
        }
        return allItems;
    }

    @Override
    public boolean save(Item entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Item (ItemCode, Description, PackSize, UnitPrice, QtyOnHand, Discount) VALUES (?,?,?,?,?,?)", entity.getItemCode(),entity.getDescription(),entity.getPackSize(),entity.getUnitPrice(),entity.getQtyOnHand(),entity.getDiscount());
    }

    @Override
    public boolean update(Item entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Item SET Description=?, PackSize=?, UnitPrice=?,QtyOnHand=?,Discount=? WHERE ItemCode=?", entity.getDescription(),entity.getPackSize(),entity.getUnitPrice(),entity.getQtyOnHand(),entity.getDiscount(),entity.getItemCode());
    }

    @Override
    public Item search(String code) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Item WHERE ItemCode=?", code);
        if (rst.next()) {
            return new Item(rst.getString(1), rst.getString(2), rst.getString(3),rst.getBigDecimal(4),rst.getInt(5),rst.getBigDecimal(6));
        }
        return null;
    }

    @Override
    public boolean exist(String code) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT ItemCode FROM Item WHERE ItemCode=?", code);
        return result.next();
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Item WHERE ItemCode=?", code);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT ItemCode FROM Item ORDER BY ItemCode DESC LIMIT 1;");
        if (rst.next()) {
            String id = rst.getString("ItemCode");
            int co=id.length();
            String text = id.substring(0,2);
            String num= id.substring(2,co);
            int n=Integer.parseInt(num);
            n++;
            String numInString = Integer.toString(n);
            String GenerateId = text+numInString;

            return GenerateId;
        } else {
            return "P001";
        }
    }
}
