package lk.ijse.pos.repository.custom;

import lk.ijse.pos.entity.CustomEntity;
import lk.ijse.pos.repository.SuperDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {
    ArrayList<CustomEntity> searchOrderByOrderID(String id)throws SQLException,ClassNotFoundException;

    ArrayList<CustomEntity> mostMovableItems()throws SQLException,ClassNotFoundException;

    ArrayList<CustomEntity> lessMovableItems()throws SQLException,ClassNotFoundException;

    ArrayList<CustomEntity> dailyIncomeReport()throws SQLException,ClassNotFoundException;


}
