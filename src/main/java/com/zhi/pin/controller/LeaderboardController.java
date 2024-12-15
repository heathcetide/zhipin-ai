package com.zhi.pin.controller;

import com.zhi.pin.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @PostMapping("/add")
    public void addScore(@RequestParam String user, @RequestParam double score) {
        leaderboardService.addScore(user, score);
    }

    @PostMapping("/increment")
    public void incrementScore(@RequestParam String user, @RequestParam double increment) {
        leaderboardService.incrementScore(user, increment);
    }

    @GetMapping("/top/{n}")
    public Set<String> getTopN(@PathVariable int n) {
        return leaderboardService.getTopN(n);
    }

    @GetMapping("/rank/{user}")
    public Long getUserRank(@PathVariable String user) {
        return leaderboardService.getUserRank(user);
    }

    @GetMapping("/score/{user}")
    public Double getUserScore(@PathVariable String user) {
        return leaderboardService.getUserScore(user);
    }
}
