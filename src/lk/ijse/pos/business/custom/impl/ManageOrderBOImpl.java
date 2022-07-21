package lk.ijse.pos.business.custom.impl;

import lk.ijse.pos.business.custom.ManageOrderBO;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.dto.ItemDTO;
import lk.ijse.pos.dto.OrderDTO;
import lk.ijse.pos.dto.OrderDetailDTO;
import lk.ijse.pos.entity.*;
import lk.ijse.pos.repository.DAOFactory;
import lk.ijse.pos.repository.custom.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageOrderBOImpl implements ManageOrderBO {

    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private final OrderDetailDAO orderDetailsDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERYDAO);

    @Override
    public boolean removeOrder(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        //-------search and update item-------------------
        ArrayList<OrderDetailDTO> orderDetail = searchOrderDetail(id);
        for (OrderDetailDTO dto:orderDetail) {
            ItemDTO item = searchItem(dto.getItemCode());
            item.setQtyOnHand(item.getQtyOnHand() + dto.getOrderQTY());

            //-------------------update item----------------------------
            boolean update = itemDAO.update(new Item(item.getItemCode(), item.getDescription(), item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand(), item.getDiscount()));

            if (!update) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return orderDAO.delete(id);
    }

    @Override
    public boolean updateOrderDetails(OrderDetailDTO dto) throws SQLException, ClassNotFoundException {
       /* Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        //-------search and update item-------------------

        ArrayList<OrderDetailDTO> orderDetail = searchOrderDetail(dto.getOrderID());
        for (OrderDetailDTO detailDTO:orderDetail) {

            ItemDTO item = searchItem(detailDTO.getItemCode());

            if (detailDTO.getNonUpdatedQTY() > detailDTO.getOrderQTY()){

                int a = (detailDTO.getNonUpdatedQTY() - detailDTO.getOrderQTY());

                item.setQtyOnHand((item.getQtyOnHand()) + a);

                boolean update = itemDAO.update(new Item(item.getItemCode(), item.getDescription(), item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand(), item.getDiscount()));
                if (!update) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
                System.out.println( (a +" "+detailDTO.getItemCode()));

            }else {

                int b = (detailDTO.getOrderQTY() - detailDTO.getNonUpdatedQTY());
                item.setQtyOnHand((item.getQtyOnHand()) - b);

                System.out.println((b+" "+detailDTO.getItemCode()));

                boolean update = itemDAO.update(new Item(item.getItemCode(), item.getDescription(), item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand(), item.getDiscount()));
                if (!update) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }

        }
        connection.commit();
        connection.setAutoCommit(true);*/
        return   orderDetailsDAO.updateOrderDetails(new OrderDetail(dto.getOrderID(),dto.getItemCode(),dto.getOrderQTY(),dto.getDiscountPriceForOneItem()));
    }

    @Override
    public boolean removeOrderDetails(String oid,String itemCode) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        //-------search and update item-------------------
        OrderDetailDTO dto1 = searchRemovedOrderDetail(oid, itemCode);
        ItemDTO item = searchItem(itemCode);
        item.setQtyOnHand(item.getQtyOnHand() + dto1.getOrderQTY());

        //-------------------update item----------------------------
        boolean update = itemDAO.update(new Item(item.getItemCode(), item.getDescription(), item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand(), item.getDiscount()));

        if (!update) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }

        connection.commit();
        connection.setAutoCommit(true);

        return orderDetailsDAO.removeOrderDetails(oid,itemCode);
    }

    @Override
    public boolean orderExist(String id) throws SQLException, ClassNotFoundException {
            return orderDAO.exist(id);
    }

    @Override
    public ArrayList<OrderDetailDTO> searchOrderDetail(String id) throws SQLException, ClassNotFoundException {
        ArrayList<CustomEntity> all = queryDAO.searchOrderByOrderID(id);
        ArrayList<OrderDetailDTO> orderDetails= new ArrayList<>();
        for (CustomEntity entity : all) {
            orderDetails.add(new OrderDetailDTO(entity.getOrderID(),entity.getOrderDate(),entity.getCustID(),entity.getItemCode(),entity.getDescription(),entity.getUnitPrice(),entity.getDiscount(),entity.getOrderQTY()));
        }
        return orderDetails;

    }

    @Override
    public OrderDetailDTO searchRemovedOrderDetail(String oid, String ItemCode) throws SQLException, ClassNotFoundException {
        OrderDetail entity = orderDetailsDAO.searchRemovedOrderDetail(oid, ItemCode);
        return new OrderDetailDTO(entity.getOrderID(),entity.getItemCode(),entity.getOrderQTY(),entity.getDiscount());
    }

    @Override
    public ItemDTO searchItem(String code) throws SQLException, ClassNotFoundException {
        Item entity = itemDAO.search(code);
        return new ItemDTO(entity.getItemCode(),entity.getDescription(),entity.getPackSize(),entity.getUnitPrice(),entity.getQtyOnHand(),entity.getDiscount());
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> all = customerDAO.getAll();
        ArrayList<CustomerDTO> allCustomers= new ArrayList<>();
        for (Customer customer : all) {
            allCustomers.add(new CustomerDTO(customer.getCustID(),customer.getCustTitle(),customer.getCustName(),customer.getCustAddress(),customer.getCity(),customer.getProvince(),customer.getPostalCode()));
        }
        return allCustomers;
    }

    @Override
    public ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException {
        ArrayList<Orders> all = orderDAO.getAll();
        ArrayList<OrderDTO> allOrders= new ArrayList<>();
        for (Orders orders : all) {
            allOrders.add(new OrderDTO(orders.getOrderID(),orders.getOrderDate(),orders.getCustID()));
        }
        return allOrders;
    }

    @Override
    public ArrayList<OrderDTO> getAllOrdersAccordingToCustomerID(String id) throws SQLException, ClassNotFoundException {
        ArrayList<Orders> all = orderDAO.getAllOrdersAccordingToCustomerID(id);
        ArrayList<OrderDTO> allOrders= new ArrayList<>();
        for (Orders orders : all) {
            allOrders.add(new OrderDTO(orders.getOrderID(),orders.getOrderDate(),orders.getCustID()));
        }
        return allOrders;
    }
}
