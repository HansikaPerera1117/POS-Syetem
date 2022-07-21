package lk.ijse.pos.business.custom;

import lk.ijse.pos.business.SuperBO;
import lk.ijse.pos.dto.CustomDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface LessMovableItemsBO extends SuperBO {
    ArrayList<CustomDTO> getAllLessMovableItems() throws SQLException, ClassNotFoundException;
}
