package com.project.codenames.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class WordFromFile {
    public static List<String> getWordFromFile(String filename){
        try{
            List<String> wordList = new ArrayList<>();
            String filePath = "words/"+filename+".txt";
            String fileContent = new String(Files.readAllBytes(Paths.get(new ClassPathResource(filePath).getURI())));

            String[] words = StringUtils.tokenizeToStringArray(fileContent, " \n");
            for (String word : words) {
                wordList.add(word.trim());
            }

            return wordList;
        }
        catch (IOException  ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }

    }
}
