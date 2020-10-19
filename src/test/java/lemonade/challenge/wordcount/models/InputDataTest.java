package lemonade.challenge.wordcount.models;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class InputDataTest {

    @Test
    public void simpleText() {
        InputData inputData = new InputData("simple", "This tale grew in the telling");
        StepVerifier.create(inputData.getData())
                .expectNext("This tale grew in the telling")
                .verifyComplete();
    }

    @Test
    public void smallFile() {
        InputData inputData = new InputData("path", "src/test/resources/data/small");
        StepVerifier.create(inputData.getData())
                .expectNext("This tale grew in the telling,")
                .expectNext("until it became a history of the Great War of the Ring and included many glimpses of the yet more ancient history that preceded it.")
                .thenCancel()
                .verify();
    }

    @Test
    public void biggerFile() {
        InputData inputData = new InputData("path", "src/test/resources/data/2mb.txt");
        StepVerifier.create(inputData.getData())
                .expectNextMatches(s -> s.contains("Lorem ipsum dolor sit amet"))
                .expectNextMatches(s -> s.contains("In nisl nisi"))
                .thenCancel()
                .verify();
    }

    @Test
    public void url() {
        InputData inputData = new InputData("url", "https://www.learningcontainer.com/wp-content/uploads/2020/04/sample-text-file.txt");
        StepVerifier.create(inputData.getData())
                .expectNext("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, ")
                .expectNext("quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ")
                .thenCancel()
                .verify();
    }

    @Test
    public void urlTheHobbit() {
        InputData inputData = new InputData("url", "http://conquent.com/bissellator/blogimg/The%20Hobbit.txt");
        StepVerifier.create(inputData.getData())
                .expectNextMatches(s -> s.contains("SPECIAL NOTE:"))
                .expectNextMatches(s -> s.contains("Bilbo"))
                .thenCancel()
                .verify();
    }
}