package main;
 
import dao.AnnosDao;
import dao.AnnosRaakaAineDao;
import dao.RaakaAineDao;
import database.Database;
import domain.Annos;
import domain.AnnosRaakaAine;
import domain.RaakaAine;
import java.util.HashMap;
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
            map.put("raaka_aineet", raaka_aine.findAll());
 
            return new ModelAndView(map, "raaka-aineet");
        }, new ThymeleafTemplateEngine());
 
        Spark.post("/raaka-aineet", (req, res) -> {
            RaakaAine raaka
                    = new RaakaAine(-1, req.queryParams("nimi"));
            raaka_aine.saveOrUpdate(raaka);
 
            res.redirect("/raaka-aineet");
            return "";
        });
 
        Spark.get("/raaka-aineet/:id", (req, res) -> {
            HashMap map = new HashMap();
            Integer id = Integer.parseInt(req.params(":id"));
            map.put("raaka_aine", raaka_aine.findOne(id));
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
        Spark.post("/annokset", (req, res) -> {
            //uusi annos
            String annosnimi = req.queryParams("nimi");
            Annos a = new Annos(-1, annosnimi);
            annokset.saveOrUpdate(a);
            //yhdista raaka_aineet
            int annos_id = Integer.valueOf(req.queryParams("annokseen"));
            int raakaaine_id = Integer.valueOf(req.queryParams("raaka-aine"));
           
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
            map.put("raaka-aineet", annokset.getRaakaAineet(id));
            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());
 
    }
 
}