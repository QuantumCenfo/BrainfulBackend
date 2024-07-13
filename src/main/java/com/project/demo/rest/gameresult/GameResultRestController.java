package com.project.demo.rest.gameresult;
import com.project.demo.logic.entity.gameresult.GameResult;
import com.project.demo.logic.entity.gameresult.GameResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/gameResults")
public class GameResultRestController {
    @Autowired
    private GameResultRepository gameResultRepository;
    @GetMapping
    public List<GameResult> GetAllResults() {
        return gameResultRepository.findAll();
    }
    @PostMapping
    public GameResult addResult(@RequestBody GameResult gameResult) {
        return gameResultRepository.save(gameResult);
    }
    @GetMapping("/{id}")
    public GameResult getResultbyId(@PathVariable Long id) {
        return gameResultRepository.findById(id).orElseThrow(RuntimeException::new);
    }
    @DeleteMapping("/{id}")
    public void deleteResults(@PathVariable Long id) {
        gameResultRepository.deleteById(id);
    }
}