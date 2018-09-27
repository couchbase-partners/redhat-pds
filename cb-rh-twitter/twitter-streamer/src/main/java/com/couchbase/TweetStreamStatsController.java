package com.couchbase;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TweetStreamStatsController {

    @CrossOrigin(origins = "*")
    @RequestMapping("/stream")
    public TweetStreamStats greeting() {
        return new TweetStreamStats();
    }
}
