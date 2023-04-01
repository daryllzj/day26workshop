package paf.day26_workshop_r1.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import paf.Utils;
import paf.day26_workshop_r1.model.Limitoffset;
import paf.day26_workshop_r1.repo.GameRepo;

@RestController
@RequestMapping
public class GameRestController {

    @Autowired
    GameRepo gameRepo;

    /*
    db.game.find({
    gid :{$exists:true}
    }).limit(10).skip(10).sort({ gid: 1 })
     */
    @GetMapping(path = "/game")
    public ResponseEntity<?> getGamesViaOffsetAndLimit(@RequestBody String payload) {

        Limitoffset limitoffset = Utils.convertToLimitOffset(payload);

        Integer limit = limitoffset.getLimit();
        Integer offset = limitoffset.getOffset();
    
        JsonObject response = gameRepo.findGamesViaLimitAndOffset(limit, offset);

        return ResponseEntity.ok(response.toString());
    }
    
    @GetMapping(path = "/game/rank")
    public ResponseEntity<?> rankGames(@RequestBody String payload) {

        Limitoffset limitoffset = Utils.convertToLimitOffset(payload);

        Integer limit = limitoffset.getLimit();
        Integer offset = limitoffset.getOffset();
    
        JsonObject response = gameRepo.rankGames(limit, offset);

        return ResponseEntity.ok(response.toString());
    }

    @GetMapping(path = "/game/{game_id}")
    public ResponseEntity<?> getGame(@PathVariable("game_id") String id) {

        try {
            JsonObject response =  gameRepo.getGame(id);

            return ResponseEntity.ok(response.toString());
            
        } catch (Exception e) {
            JsonObject err = Json.createObjectBuilder()
				.add("message", e.getMessage())
				.build();
			return ResponseEntity.status(400).body(err.toString());
        }
        
    }


}
