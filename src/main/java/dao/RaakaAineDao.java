package dao;
 
import database.Database;
import domain.Annos;
import domain.RaakaAine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
public class RaakaAineDao implements Dao<RaakaAine, Integer> {
 
    private Database database;
 
    public RaakaAineDao(Database database) {
        this.database = database;
    }
 
    public RaakaAine findOne(Integer key) throws SQLException, Exception {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setObject(1, key);
 
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
 
        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
 
        RaakaAine raakaaine = new RaakaAine(id, nimi);
 
        rs.close();
        stmt.close();
        connection.close();
 
        return raakaaine;
    }
 
    public RaakaAine saveOrUpdate(RaakaAine raakaaine) throws SQLException, Exception {
        if (raakaaine.getId() == null) {
            return save(raakaaine);
        } else {
            return update(raakaaine);
        }
    }
 
    private RaakaAine save(RaakaAine raakaaine) throws SQLException, Exception {
 
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO RaakaAine"
                + " (id, nimi)"
                + " VALUES (?, ?)");
        stmt.setInt(1, raakaaine.getId());
        stmt.setString(2, raakaaine.getNimi());
 
        stmt.executeUpdate();
        stmt.close();
 
        stmt = conn.prepareStatement("SELECT * FROM RaakaAine"
                + " WHERE id = ? AND nimi = ?");
        stmt.setInt(1, raakaaine.getId());
        stmt.setString(2, raakaaine.getNimi());
 
        ResultSet rs = stmt.executeQuery();
        rs.next();
 
        RaakaAine a = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
 
        stmt.close();
        rs.close();
 
        conn.close();
 
        return a;
    }
 
    private RaakaAine update(RaakaAine raakaaine) throws SQLException, Exception {
 
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE RaakaAine SET"
                + "id = ?, nimi = ? WHERE id = ?");
        stmt.setInt(1, raakaaine.getId());
        stmt.setString(2, raakaaine.getNimi());
        stmt.setInt(3, raakaaine.getId());
 
        stmt.executeUpdate();
 
        stmt.close();
        conn.close();
 
        return raakaaine;
    }
 
    @Override
    public List<RaakaAine> findAll() throws SQLException, Exception {
 
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine");
 
        ResultSet rs = stmt.executeQuery();
        List<RaakaAine> raakaaineet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
 
            raakaaineet.add(new RaakaAine(id, nimi));
        }
 
        rs.close();
        stmt.close();
        connection.close();
 
        return raakaaineet;
    }
 
    public int countAnnokset(Integer key) throws SQLException, Exception {
 
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM annos INNER JOIN annosraakaaine ON annosraakaaine.annos_id = annos.id WHERE annosraakaaine.raaka_aine_id = ? ");
        stmt.setInt(1, key);
 
        ResultSet rs = stmt.executeQuery();
        int count;
 
        if (rs.next()) {
            count = rs.getInt(1);
 
        } else {
            count = 0;
        }
 
        rs.close();
        stmt.close();
        connection.close();
 
        return count;
 
    }
 
    @Override
    public void delete(Integer key) throws SQLException, Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
        stmt.setInt(1, key);
 
        stmt.executeUpdate();
 
        stmt.close();
        conn.close();
    }
}