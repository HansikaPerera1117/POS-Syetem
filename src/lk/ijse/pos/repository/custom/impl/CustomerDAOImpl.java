package lk.ijse.pos.repository.custom.impl;

import lk.ijse.pos.entity.Customer;
import lk.ijse.pos.repository.CrudUtil;
import lk.ijse.pos.repository.custom.CustomerDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public ArrayList<Customer> getAll() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer");
        ArrayList<Customer> allCustomers =new ArrayList<>();
        while (result.next()) {
            allCustomers.add(new Customer(result.getString(1),result.getString(2),result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7)));
        }
        return allCustomers;
    }

    @Override
    public boolean save(Customer entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Customer (CustID, CustTitle, CustName, CustAddress, City, Province, PostalCode) VALUES (?,?,?,?,?,?,?)", entity.getCustID(),entity.getCustTitle(),entity.getCustName(),entity.getCustAddress(),entity.getCity(),entity.getProvince(),entity.getPostalCode());
    }

    @Override
    public boolean update(Customer entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Customer SET CustTitle=?, CustName=?, CustAddress=?, City=?, Province=?, PostalCode=? WHERE CustID=?", entity.getCustTitle(), entity.getCustName(), entity.getCustAddress(), entity.getCity(), entity.getProvince(), entity.getPostalCode(), entity.getCustID());
    }

    @Override
    public Customer search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Customer WHERE CustID=?", id);
        if (rst.next()) {
            return new Customer(rst.getString(1), rst.getString(2), rst.getString(3),rst.getString(4),rst.getString(5),rst.getString(6),rst.getString(7));
        }
        return null;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT CustID FROM Customer WHERE CustID=?", id);
        return result.next();
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Customer WHERE CustID=?", id);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT CustID FROM Customer ORDER BY CustID DESC LIMIT 1;");
        if (rst.next()) {
            String id = rst.getString("CustID");
            int co=id.length();
            String text = id.substring(0,2);
            String num= id.substring(2,co);
            int n=Integer.parseInt(num);
            n++;
            String numInString = Integer.toString(n);
            String GenerateId = text+numInString;

             return GenerateId;

        } else {
            return "C001";
        }
    }
}
