
package dao;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    T findOne(K key) throws SQLException, Exception;

    List<T> findAll() throws SQLException, Exception;

    T saveOrUpdate(T object) throws SQLException, Exception;

    void delete(K key) throws SQLException, Exception;
}
