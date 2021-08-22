package kotoale.parking.lot.service;

import kotoale.parking.lot.processors.Processor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommandHandlerTest {

    @Autowired
    private CommandHandler handler;

    @Test
    void getProcessors() {
        List<String> commands = handler.getProcessors()
                .stream()
                .map(Processor::getCommandName)
                .collect(Collectors.toList());
        assertThat(commands).isEqualTo(List.of("create", "leave", "park", "status"));
    }

    @ParameterizedTest
    @CsvSource({
            "0-input.txt, 0-output.txt",
            "1-input.txt, 1-output.txt",
            "2-input.txt, 2-output.txt",
            "3-input.txt, 3-output.txt",
            "4-input.txt, 4-output.txt"
    })
    void handle(String inputFile, String outputFile) throws Exception {
        var writer = new StringWriter();
        Stream<String> commands = Files.lines(Paths.get(new ClassPathResource(inputFile).getURI()));
        handler.handle(commands, new PrintWriter(writer, true));
        String expected = Files.readString(Paths.get(new ClassPathResource(outputFile).getURI()));
        assertThat(writer.toString()).isEqualTo(expected);
    }
}