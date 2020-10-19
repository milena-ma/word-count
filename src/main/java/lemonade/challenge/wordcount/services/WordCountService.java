package lemonade.challenge.wordcount.services;

import lemonade.challenge.wordcount.models.InputData;

public interface WordCountService {
    void countWords(InputData inputData);

    long getWordStatistics(String word);
}
