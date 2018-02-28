package main;
 
import dao.AnnosDao;
import dao.AnnosRaakaAineDao;
import dao.RaakaAineDao;
import database.Database;
import domain.Annos;
import domain.AnnosRaakaAine;
import domain.RaakaAine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
 
public class Main {
 
    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        //Database pitää muokata Herokuun sopivaksi
        Database database = new Database("jdbc:sqlite:reseptit.db");
        AnnosDao annokset = new AnnosDao(database);
        RaakaAineDao raaka_aine = new RaakaAineDao(database);
        AnnosRaakaAineDao annosraakaaine = new AnnosRaakaAineDao(database);
 
        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap();
            map.put("annokset", annokset.findAll());
 
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
 
        Spark.get("/raaka-aineet", (req, res) -> {
            HashMap map = new HashMap();
            map.put("raakaAineet", raaka_aine.findAll());
 
            return new ModelAndView(map, "raaka-aineet");
        }, new ThymeleafTemplateEngine());
 
        Spark.post("/raaka-aineet", (req, res) -> {
         String raakaAineNimi = req.queryParams("nimi");
         
         if (raakaAineNimi.equals("")) {
                res.status(418);
                return "Väärä tai tyhjä syöte";
 
            }
         RaakaAine ran = new RaakaAine(-1, raakaAineNimi);
         raaka_aine.saveOrUpdate(ran);
         res.redirect("/raaka-aineet");
            return "";
        });
 
        Spark.get("/raaka-aineet/:id", (req, res) -> {
            HashMap map = new HashMap();
            Integer id = Integer.parseInt(req.params(":id"));
            map.put("raakaAine", raaka_aine.findOne(id));
            map.put("annoksessa", raaka_aine.countAnnokset(id));
 
            return new ModelAndView(map, "raaka-aine");
        }, new ThymeleafTemplateEngine());
 
        Spark.get("/annokset", (req, res) -> {
            HashMap map = new HashMap();
            map.put("annokset", annokset.findAll());
            map.put("raakaAineet", raaka_aine.findAll());
            return new ModelAndView(map, "annokset");
        }, new ThymeleafTemplateEngine());
 
        // Annosten luominen ja raaka-aineiden yhdistäminen
        Spark.post("/annokseenLisaaminen", (req, res) -> {
            
            //yhdista raaka_aineet
          int annos_id = Integer.valueOf(req.queryParams("annokseen"));
            int raakaaine_id = Integer.valueOf(req.queryParams("raakaAine"));
           
            String maara = req.queryParams("maara");
            String ohje = req.queryParams("ohje");
           
            int jarjestys;
            try {
                jarjestys = Integer.valueOf(req.queryParams("jarjestys"));
            } catch (NumberFormatException e) {
                res.status(418);
                return "Väärä tai tyhjä syöte";
            }
 
            if (maara.equals("") || ohje.equals("")) {
                res.status(418);
                return "Väärä tai tyhjä syöte";
 
            }
 
            AnnosRaakaAine ara
                    = new AnnosRaakaAine(-1, annos_id, raakaaine_id, jarjestys, maara, ohje);
            annosraakaaine.saveOrUpdate(ara);
 
            res.redirect("/annokset");
            return "";
        });
 
        Spark.get("/annokset/:id", (req, res) -> {
            HashMap map = new HashMap();
            Integer id = Integer.parseInt(req.params(":id"));
            map.put("annos", raaka_aine.findOne(id));
            map.put("raakaAineet", annokset.getRaakaAineet(id));
            map.put("AnnosRaakaAine", annokset.getOhjeet(id));
            
            List<String> kaikki = new ArrayList<>();
            for (int i = 0; i<annokset.getRaakaAineet(id).size(); i++){

                kaikki.add(annokset.getRaakaAineet(id).get(i).getNimi() + ", " + annokset.getOhjeet(id).get(i).getMaara() + ", ohje:" + kaikki.add(annokset.getOhjeet(id).get(i).getOhje()));
                kaikki.add("\n");
            }
            
            map.put("kaikki", kaikki);
            
            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());
 
        Spark.post("/uusiAnnos", (req, res) -> {
            //uusi annos
            String annosnimi = req.queryParams("nimi");
           
            if (annosnimi.equals("")) {
                res.status(418);
                return "Väärä tai tyhjä syöte";
 
            }
            Annos a = new Annos(-1, annosnimi);
            annokset.saveOrUpdate(a);
            res.redirect("/annokset");
            return "";
            
        });
    }
 
}