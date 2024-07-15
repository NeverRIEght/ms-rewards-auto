package dev.mkomarov.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static dev.mkomarov.random.RandomProvider.getRandomInt;

public class SearchControllerImpl implements SearchController {
    @Override
    public String getRandomWord() {
        List<String> words = getWordsList();
        return words.get(getRandomInt(words.size()));
    }

    @Override
    public List<String> getWordsList() {
        List<String> words = new ArrayList<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(WORDS_TXT_PATH));
            String line = reader.readLine();

            while (line != null) {
                words.add(line);
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + WORDS_TXT_PATH, e);
        }

        return words;
    }
}
