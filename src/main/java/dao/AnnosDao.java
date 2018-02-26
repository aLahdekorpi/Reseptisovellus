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
 
public class AnnosDao implements Dao<Annos, Integer> {
 
    private Database database;
 
    public AnnosDao(Database database) {
        this.database = database;
    }
 
    public Annos findOne(Integer key) throws SQLException, Exception {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setObject(1, key);
 
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
 
        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
 
        Annos annos = new Annos(id, nimi);
 
        rs.close();
        stmt.close();
        connection.close();
 
        return annos;
    }
 
    public Annos saveOrUpdate(Annos annos) throws SQLException, Exception {
        if (annos.getId() == null) {
            return save(annos);
        } else {
            return update(annos);
        }
    }
 
    private Annos save(Annos annos) throws SQLException, Exception {
 
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Annos"
                + " (id, nimi)"
                + " VALUES (?, ?)");
        stmt.setInt(1, annos.getId());
        stmt.setString(2, annos.getNimi());
 
        stmt.executeUpdate();
        stmt.close();
 
        stmt = conn.prepareStatement("SELECT * FROM Annos"
                + " WHERE id = ? AND nimi = ?");
        stmt.setInt(1, annos.getId());
        stmt.setString(2, annos.getNimi());
 
        ResultSet rs = stmt.executeQuery();
        rs.next();
 
        Annos a = new Annos(rs.getInt("id"), rs.getString("nimi"));
 
        stmt.close();
        rs.close();
 
        conn.close();
 
        return a;
    }
 
    private Annos update(Annos annos) throws SQLException, Exception {
 
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Annos SET"
                + "id = ?, nimi = ? WHERE id = ?");
        stmt.setInt(1, annos.getId());
        stmt.setString(2, annos.getNimi());
        stmt.setInt(3, annos.getId());
 
        stmt.executeUpdate();
 
        stmt.close();
        conn.close();
 
        return annos;
    }
 
    @Override
    public List<Annos> findAll() throws SQLException, Exception {
 
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos");
 
        ResultSet rs = stmt.executeQuery();
        List<Annos> annokset = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
 
            annokset.add(new Annos(id, nimi));
        }
 
        rs.close();
        stmt.close();
        connection.close();
 
        return annokset;
    }
   
     public List<RaakaAine> getdRaakaAineet(Integer key) throws SQLException, Exception {
     
         Connection connection = database.getConnection();
         PreparedStatement stmt = connection.prepareStatement("SELECT raakaine FROM raakaaine  "
                 + "INNER JOIN annosraakaaine ON annosraakaaine.raaka_aine_id = raakaaine.id "
                 + "WHERE annosraakaaine.annos_id = ? ORDER BY annosraakaaine.jarjestys ");
         stmt.setInt(1, key);
         
         ResultSet rs = stmt.executeQuery();
         List<RaakaAine> raakaaineet = new ArrayList<>();
         while(rs.next()) {
             Integer id = rs.getInt("id");
             String nimi = rs.getString("nimi");
             
             raakaaineet.add(new RaakaAine(id, nimi));
         }
         
         rs.close();
         stmt.close();
         connection.close();
         
         return raakaaineet;
         
         
     }
     
     
 
    @Override
    public void delete(Integer key) throws SQLException, Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Annos WHERE id = ?");
        stmt.setInt(1, key);
 
        stmt.executeUpdate();
 
        stmt.close();
        conn.close();
    }
}