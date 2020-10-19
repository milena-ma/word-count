package lemonade.challenge.wordcount.services;

import com.github.sonus21.rqueue.annotation.RqueueListener;
import com.github.sonus21.rqueue.core.RqueueMessageEnqueuer;
import lemonade.challenge.wordcount.models.InputData;
import lemonade.challenge.wordcount.repositories.WordCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class WordCountServiceImpl implements WordCountService {

    private WordCountRepository wordCountRepository;
    private RqueueMessageEnqueuer rqueueMessageEnqueuer;

    @Value("${text.queue.name}")
    private String textQueueName;

    @Autowired
    public WordCountServiceImpl(WordCountRepository wordCountRepository, RqueueMessageEnqueuer rqueueMessageEnqueuer) {
        this.wordCountRepository = wordCountRepository;
        this.rqueueMessageEnqueuer = rqueueMessageEnqueuer;
    }

    @Override
    public long getWordStatistics(String word) {
        return wordCountRepository.get(word);
    }

    @Override
    public void countWords(InputData inputData) {
        inputData.getData()
                .filter(data -> !data.isEmpty())
                .flatMap(data -> Flux.just(data.split("\\W+")))
                .collect(toMap(
                        s -> s.toLowerCase(),
                        s -> 1,
                        Integer::sum)).subscribe(map -> rqueueMessageEnqueuer.enqueue(textQueueName, map));
    }

    @RqueueListener(value = "${text.queue.name}")
    private void reduceData(Map<String, Integer> counterMap) {
        wordCountRepository.reduce(counterMap);
    }
}
