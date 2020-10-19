package lemonade.challenge.wordcount.repositories;

import java.util.Map;

public interface WordCountRepository {
    Long get(String key);

    void reduce(Map<String, Integer> map);
}
