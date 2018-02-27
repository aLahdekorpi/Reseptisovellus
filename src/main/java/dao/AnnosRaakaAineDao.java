package dao;

import database.Database;
import domain.AnnosRaakaAine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {

    private Database database;

    public AnnosRaakaAineDao(Database database) {
        this.database = database;
    }

    public AnnosRaakaAine findOne(Integer key) throws SQLException, Exception {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        Integer annosId = rs.getInt("annos_id");
        Integer raakaAineId = rs.getInt("raaka_aine_id");
        Integer jarjestys = rs.getInt("jarjestys");
        String maara = rs.getString("maara");
        String ohje = rs.getString("ohje");

        AnnosRaakaAine annosraakaaine = new AnnosRaakaAine(id, annosId, raakaAineId, jarjestys, maara, ohje);

        rs.close();
        stmt.close();
        connection.close();

        return annosraakaaine;
    }

    public AnnosRaakaAine saveOrUpdate(AnnosRaakaAine annosraakaaine) throws SQLException, Exception {

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, annos_id, raaka_aine_id, jarjestys, maara, ohje FROM Annos WHERE annos_id = ? AND raaka_aine_id = ?");
            stmt.setInt(1, annosraakaaine.getAnnosId());
            stmt.setInt(2, annosraakaaine.getRaakaAineId());

            
            ResultSet result = stmt.executeQuery();

            if (!result.next()) {
                return save(annosraakaaine);
            } else {
                return update(annosraakaaine);
            }
        }
    }

    private AnnosRaakaAine save(AnnosRaakaAine annosraakaaine) throws SQLException, Exception {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine"
                + " (annos_id, raaka_aine_id, jarjestys, maara, ohje)"
                + " VALUES (?,?,?,?,?)");
        stmt.setInt(1, annosraakaaine.getAnnosId());
        stmt.setInt(2, annosraakaaine.getRaakaAineId());
        stmt.setInt(3, annosraakaaine.getJarjestys());
        stmt.setString(4, annosraakaaine.getMaara());
        stmt.setString(5, annosraakaaine.getOhje());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine"
                + " WHERE annos_id = ? AND raaka_aine_id = ?");
        stmt.setInt(1, annosraakaaine.getAnnosId());
        stmt.setInt(2, annosraakaaine.getRaakaAineId());

        ResultSet rs = stmt.executeQuery();
        rs.next();

        Integer id = rs.getInt("id");
        Integer annosId = rs.getInt("annos_id");
        Integer raakaAineId = rs.getInt("raaka_aine_id");
        int jarjestys = rs.getInt("jarjestys");
        String maara = rs.getString("maara");
        String ohje = rs.getString("ohje");

        AnnosRaakaAine a = new AnnosRaakaAine(id, annosId, raakaAineId, jarjestys, maara, ohje);

        stmt.close();
        rs.close();

        conn.close();

        return a;
    }

    private AnnosRaakaAine update(AnnosRaakaAine annosraakaaine) throws SQLException, Exception {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE AnnosRaakaAine SET"
                + " (annos_id, raaka_aine_id, jarjestys, maara, ohje) WHERE id = ?");
        stmt.setInt(1, annosraakaaine.getAnnosId());
        stmt.setInt(2, annosraakaaine.getRaakaAineId());
        stmt.setInt(3, annosraakaaine.getJarjestys());
        stmt.setString(4, annosraakaaine.getMaara());
        stmt.setString(5, annosraakaaine.getOhje());
        stmt.setInt(6, annosraakaaine.getId());

        stmt.executeUpdate();

        stmt.close();
        conn.close();

        return annosraakaaine;
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException, Exception {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<AnnosRaakaAine> annosraakaaineet = new ArrayList<>();

        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer annosId = rs.getInt("annos_id");
            Integer raakaAineId = rs.getInt("raaka_aine_id");
            Integer jarjestys = rs.getInt("jarjestys");
            String maara = rs.getString("maara");
            String ohje = rs.getString("ohje");

            annosraakaaineet.add(new AnnosRaakaAine(id, annosId, raakaAineId, jarjestys, maara, ohje));
        }

        rs.close();
        stmt.close();
        connection.close();

        return annosraakaaineet;
    }

    @Override
    public void delete(Integer key) throws SQLException, Exception {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE id = ?");
        stmt.setInt(1, key);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
}
