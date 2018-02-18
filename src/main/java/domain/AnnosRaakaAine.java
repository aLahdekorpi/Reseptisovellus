
package domain;

public class AnnosRaakaAine {

    private Integer id;
    private Integer annosId;
    private Integer raakaAineId;
    private String jarjestys;
    private String maara;
    private String ohje;

    public AnnosRaakaAine(Integer id, Integer annosId, Integer raakaAineId, String jarjestys, String maara, String ohje) {
        this.id = id;
        this.annosId = annosId;
        this.raakaAineId = raakaAineId;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }

    public Integer getId() {
        return id;
    }

    public Integer getAnnosId() {
        return annosId;
    }

    public Integer getRaakaAineId() {
        return raakaAineId;
    }

    public String getJarjestys() {
        return jarjestys;
    }

    public String getMaara() {
        return maara;
    }

    public String getOhje() {
        return ohje;
    }
}
