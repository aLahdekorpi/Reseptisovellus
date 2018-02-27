package dao;

import database.Database;
import domain.Annos;
import domain.AnnosRaakaAine;
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

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Annos WHERE nimi = ?");
            stmt.setString(1, annos.getNimi());

            ResultSet result = stmt.executeQuery();

            if (!result.next()) {
                return save(annos);
            } else {
                return update(annos);
            }
        }
    }

    private Annos save(Annos annos) throws SQLException, Exception {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Annos"
                + " (nimi)"
                + " VALUES (?)");
        stmt.setString(1, annos.getNimi());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM Annos"
                + " WHERE nimi = ?");
        stmt.setString(1, annos.getNimi());

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
                + " nimi = ? WHERE id = ?");
        stmt.setString(1, annos.getNimi());
        stmt.setInt(2, annos.getId());

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

    public List<RaakaAine> getRaakaAineet(Integer key) throws SQLException, Exception {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM raakaaine  "
                + "INNER JOIN annosraakaaine ON annosraakaaine.raaka_aine_id = raakaaine.id "
                + "WHERE annosraakaaine.annos_id = ? ORDER BY annosraakaaine.jarjestys ");
        stmt.setInt(1, key);

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

    public List<AnnosRaakaAine> getOhjeet(Integer key) throws SQLException, Exception {
        Connection connection = database.getConnection();
        PreparedStatement smt = connection.prepareStatement("SELECT * FROM  annosraakaaine "
                + "WHERE annosraakaaine.annos_id = ? ORDER BY jarjestys");
        smt.setInt(1, key);
        ResultSet rs = smt.executeQuery();

        ArrayList<AnnosRaakaAine> ohjeet = new ArrayList<>();
        while (rs.next()) {
            AnnosRaakaAine a = new AnnosRaakaAine(rs.getInt("id"),
                    rs.getInt("annos_id"), rs.getInt("raaka_aine_id"),
                    rs.getInt("jarjestys"), rs.getString("maara"), rs.getString("ohje"));
            ohjeet.add(a);
        }
        
        return ohjeet;

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
