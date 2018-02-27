 
package domain;
 
public class AnnosRaakaAine {
 
    private Integer id;
    private Integer annosId;
    private Integer raakaAineId;
    private Integer jarjestys;
    private String maara;
    private String ohje;
 
    public AnnosRaakaAine(Integer id, Integer annosId, Integer raakaAineId, int jarjestys, String maara, String ohje) {
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
 
    public int getJarjestys() {
        return jarjestys;
    }
 
    public String getMaara() {
        return maara;
    }
 
    public String getOhje() {
        return ohje;
    }
}