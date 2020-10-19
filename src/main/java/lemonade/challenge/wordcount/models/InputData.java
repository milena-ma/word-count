package lemonade.challenge.wordcount.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.TypeMismatchException;
import org.springframework.data.util.StreamUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.BaseStream;

@Data
@AllArgsConstructor
public class InputData {
    protected String type;
    protected String text;

    public Flux<String> getData() {
        Flux<String> stringFlux;

        switch (type) {
            case "simple":
                stringFlux = Flux.using(() -> StreamUtils.fromNullable(text),
                        Flux::fromStream,
                        BaseStream::close
                );
                break;
            case "path":
                Path ipPath = Paths.get(text);
                stringFlux = Flux.using(() -> Files.lines(ipPath),
                        Flux::fromStream,
                        BaseStream::close
                );
                break;
            case "url":
                WebClient client = WebClient.create(decode(text));
                stringFlux = client.get()
                        .retrieve()
                        .bodyToFlux(String.class);
                break;
            default:
                throw new TypeMismatchException(type, String.class);
        }

        return stringFlux.filter(s -> !s.isEmpty());
    }

    private String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            throw new TypeMismatchException(text, URL.class);
        }
    }
}
