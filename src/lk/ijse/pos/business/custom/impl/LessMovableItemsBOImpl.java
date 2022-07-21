package lk.ijse.pos.business.custom.impl;

import lk.ijse.pos.business.BOFactory;
import lk.ijse.pos.business.custom.LessMovableItemsBO;
import lk.ijse.pos.business.custom.MostMovableItemsBO;
import lk.ijse.pos.dto.CustomDTO;
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.entity.CustomEntity;
import lk.ijse.pos.entity.Customer;
import lk.ijse.pos.repository.DAOFactory;
import lk.ijse.pos.repository.custom.QueryDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class LessMovableItemsBOImpl implements LessMovableItemsBO {

    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERYDAO);
    
    @Override
    public ArrayList<CustomDTO> getAllLessMovableItems() throws SQLException, ClassNotFoundException {
        ArrayList<CustomEntity> all = queryDAO.lessMovableItems();
        ArrayList<CustomDTO> lessMovable= new ArrayList<>();
        for (CustomEntity entity: all) {
            lessMovable.add(new CustomDTO(entity.getItemCode(),entity.getDescription(),entity.getUnitPrice(),entity.getQtyOnHand(),entity.getOrderQTY()));
        }
        return lessMovable;
    }
}
