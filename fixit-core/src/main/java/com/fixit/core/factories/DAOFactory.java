package com.fixit.core.factories;

import android.content.Context;

import com.fixit.core.data.DataModelObject;
import com.fixit.core.data.Order;
import com.fixit.core.data.Profession;
import com.fixit.core.database.CommonDAO;
import com.fixit.core.database.DatabaseManager;
import com.fixit.core.database.OrderDAO;
import com.fixit.core.database.ProfessionDAO;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kostyantin on 3/28/2017.
 */

public class DAOFactory {

    private final Map<String, String> mDmoToDaoMappings = new HashMap<>();

    private final DatabaseManager mDbManager;

    public DAOFactory(Context context) {
        mDbManager = new DatabaseManager(context);

        addDmoToDaoMapping(Profession.class, ProfessionDAO.class);
        addDmoToDaoMapping(Order.class, OrderDAO.class);
    }

    private <DMO extends DataModelObject, DAO extends CommonDAO<DMO>> void addDmoToDaoMapping(Class<DMO> dmoClass, Class<DAO> daoClass) {
        mDmoToDaoMappings.put(dmoClass.getSimpleName().toLowerCase(), daoClass.getName());
    }

    public CommonDAO<DataModelObject> createDaoForDmoName(String dmoName) {
        return createDao(mDmoToDaoMappings.get(dmoName));
    }

    public CommonDAO<DataModelObject> createDao(String daoClassName) {
        if(daoClassName != null) {
            try {
                Constructor c = Class.forName(daoClassName).getConstructor(DatabaseManager.class);
                return (CommonDAO<DataModelObject>) c.newInstance(mDbManager);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ProfessionDAO createProfessionDao() {
        return new ProfessionDAO(mDbManager);
    }

    public OrderDAO createOrderDao() {
        return new OrderDAO(mDbManager);
    }

}
