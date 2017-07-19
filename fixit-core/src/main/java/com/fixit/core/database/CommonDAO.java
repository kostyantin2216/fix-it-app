package com.fixit.core.database;

import com.fixit.core.data.DataModelObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kostyantin on 1/8/2016.
 */
public interface CommonDAO<DMO extends DataModelObject> {

    long insert(DMO obj);

    /**
     * @param objs Objects to insert into database
     * @return amount of objects unsuccessfully inserted
     */
    int insert(List<DMO> objs);
    DMO findById(long id);
    DMO[] findByQuery(String query, String[] values);
    DMO findOneByProperty(String property, String value);
    DMO[] findByProperty(String property, String value);

    /**
     * @return List&lt;T&gt; All objects of current type or an empty immutable List
     *         if no objects can be found.
     */
    DMO[] findAll();
    DMO[] findAll(String orderBy);
    boolean update(DMO obj);

    /**
     * @param objs Objects to update in database.
     * @return Amount of objects failed to update.
     */
    int updateAll(List<DMO> objs);

    boolean delete(String id);
    int truncate();

    boolean contains(Serializable id);
    int count();
    int countQuery(String query, String[] selectionArgs);

    String getTableName();
    Class<DMO> getEntityType();
}
