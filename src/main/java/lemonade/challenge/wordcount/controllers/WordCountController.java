package lemonade.challenge.wordcount.controllers;

import lemonade.challenge.wordcount.models.InputData;
import lemonade.challenge.wordcount.services.WordCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/word/")
public class WordCountController {

    private WordCountService wordCountService;

    @Autowired
    public WordCountController(WordCountService wordCountService) {
        this.wordCountService = wordCountService;
    }

    @PostMapping("/counter")
    public void wordCounter(@Valid @RequestBody InputData input) {
        wordCountService.countWords(input);
    }

    @GetMapping("/statistics/{word}")
    public long wordStatistics(@PathVariable String word) {
        return wordCountService.getWordStatistics(word);
    }
}
