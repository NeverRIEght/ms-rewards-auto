package dev.mkomarov.search;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public interface SearchController {
    String WORDS_TXT_PATH = Paths.get("").toAbsolutePath()
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "resources"
            + File.separator + "words_alpha.txt";
    String getRandomWord();
    List<String> getWordsList();
}
