package com.fixit.factories;

import android.content.Context;

import com.fixit.data.DataModelObject;
import com.fixit.data.JobReason;
import com.fixit.data.OrderData;
import com.fixit.data.Profession;
import com.fixit.database.CommonDAO;
import com.fixit.database.DatabaseManager;
import com.fixit.database.JobReasonDAO;
import com.fixit.database.OrderDataDAO;
import com.fixit.database.ProfessionDAO;

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
        addDmoToDaoMapping(OrderData.class, OrderDataDAO.class);
        addDmoToDaoMapping(JobReason.class, JobReasonDAO.class);
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

    public OrderDataDAO createOrderDataDao() { return new OrderDataDAO(mDbManager); }

    public JobReasonDAO createJobReasonDao() { return new JobReasonDAO(mDbManager); }

}
