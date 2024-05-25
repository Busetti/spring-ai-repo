package com.guru.sampleai.output;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/output")
@RestController
@Slf4j
public class OutputParserController {


    @Autowired
    ChatClient chatClient;

    @GetMapping("/country")
    public Country getCountryCapital(@RequestParam("country") String country){
        var outputParser = new BeanOutputParser<>(Country.class);
        var format = outputParser.getFormat();
        log.info("format: {}", format);

        String message = """
                Generate country capital for given {country} in {format}
                """;
        PromptTemplate  promptTemplate = new PromptTemplate(message, Map.of("country", country, "format", format));
        Prompt prompt = promptTemplate.create();
        Generation result = chatClient.call(prompt).getResult();
        return outputParser.parse(result.getOutput().getContent());

    }
}
