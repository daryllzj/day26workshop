package paf.day26_workshop_r1.repo;

import java.time.LocalDate;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import paf.day26_workshop_r1.exception.GameNotFoundException;
import paf.day26_workshop_r1.model.Game;

@Slf4j
@Repository
public class GameRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String C_GAMES = "game";

    public JsonObject findGamesViaLimitAndOffset(Integer limit, Integer offset) {
        
        log.info("limitRepo>>> " + limit);
        log.info("offsetRepo>>> " + offset);

        Criteria criteria = Criteria.where("gid").exists(true);

        Query query = Query.query(criteria).with(Sort.by(Sort.Direction.ASC,"gid")).skip(offset).limit(limit);

        List<Game> listOfGames = mongoTemplate.find(query, Game.class, C_GAMES);

        log.info(listOfGames.toString());


        JsonObjectBuilder gameInJson = Json.createObjectBuilder();

        JsonArrayBuilder gameArray = Json.createArrayBuilder();

        for (Game game : listOfGames) {
            Integer game_id = game.getGid();
            String name = game.getName();
            gameInJson.add("game_id",game_id);
            gameInJson.add("name",name);
            gameArray.add(gameInJson);
        }

        JsonObjectBuilder jsonResponse = Json.createObjectBuilder();

        jsonResponse.add("games", gameArray);
        jsonResponse.add("offset", offset);
        jsonResponse.add("limit", limit);
        jsonResponse.add("total", listOfGames.size());
        jsonResponse.add("timestamp", LocalDate.now().toString());

        JsonObject response = jsonResponse.build();

        log.info(response.toString());

        return response;

    }
    

    public JsonObject rankGames(Integer limit, Integer offset) {
        

        Criteria criteria = Criteria.where("ranking").exists(true);

        Query query = Query.query(criteria).with(Sort.by(Sort.Direction.ASC,"ranking")).skip(offset).limit(limit);

        List<Game> listOfGames = mongoTemplate.find(query, Game.class, C_GAMES);


        JsonObjectBuilder gameInJson = Json.createObjectBuilder();

        JsonArrayBuilder gameArray = Json.createArrayBuilder();

        for (Game game : listOfGames) {
            Integer game_id = game.getGid();
            String name = game.getName();
            gameInJson.add("game_id",game_id);
            gameInJson.add("name",name);
            gameArray.add(gameInJson);
        }

        JsonObjectBuilder jsonResponse = Json.createObjectBuilder();

        jsonResponse.add("games", gameArray);
        jsonResponse.add("offset", offset);
        jsonResponse.add("limit", limit);
        jsonResponse.add("total", listOfGames.size());
        jsonResponse.add("timestamp", LocalDate.now().toString());

        JsonObject response = jsonResponse.build();

        log.info(response.toString());

        return response;
    }

    public JsonObject getGame(String id) {

        try {
            ObjectId docId = new ObjectId(id);

            Document game = mongoTemplate.findById(docId, Document.class, C_GAMES);

            JsonObjectBuilder gameInJson = Json.createObjectBuilder();

            gameInJson.add("game_id", id);
            gameInJson.add("name", game.getString("name"));
            gameInJson.add("year", game.getInteger("year"));
            gameInJson.add("ranking", game.getInteger("ranking"));
            gameInJson.add("users_rated", game.getInteger("users_rated"));
            gameInJson.add("url", game.getString("url"));
            gameInJson.add("thumbnail", game.getString("image"));
            gameInJson.add("timestamp", LocalDate.now().toString());
            JsonObject finalGame = gameInJson.build();

            log.info(finalGame.toString());
            return finalGame;

        } catch (Exception e) {
            throw new GameNotFoundException("game id is not valid");
        }
        
    }

}
