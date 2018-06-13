package com.text.summarization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class WeightCalculator {
    private int TOTAL_NO_OF_CORPUS = 823;
    private int totalNoOfCorpusDocuments = 0;
    private OpenNlpTools openNlpProcessing;
    private HashMap<String, Integer> documentFrequency;

    public WeightCalculator(OpenNlpTools openNlpProcessing) throws IOException {
        this.openNlpProcessing = openNlpProcessing;
        //HashMap<String, Integer> documentfreq = calculateDf();
        //write(documentfreq);
        loadDfFile("dict.txt");
    }

    private void loadDfFile(String file) throws FileNotFoundException {
        documentFrequency = new HashMap<>();
        Scanner scanner = new Scanner(new File(file));
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] split = s.split("\t");
            if (split.length == 2) {
                documentFrequency.put(split[0].trim(), Integer.valueOf(split[1].trim()));
            } else {
                System.out.println(s);
            }
        }
        scanner.close();
        System.out.println(documentFrequency);
        System.out.println(totalNoOfCorpusDocuments);
    }

    private void write(HashMap<String, Integer> documentfreq) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter("dict.txt");
        Set<String> keys = documentfreq.keySet();
        for (String key : keys) {
            printWriter.write(key + "\t" + documentfreq.get(key) + "\n");
        }
        printWriter.close();
    }

    /**
     * Calculates Df using background corpus.
     *
     * @throws IOException
     */
    public HashMap<String, Integer> calculateDf() throws IOException {
        HashMap<String, Integer> documentFrequency = new HashMap<>();
        HashMap<Integer, Set<String>> documentss = new HashMap<>();
        File dir = new File("/home/shabir/Documents/project/hybrid-text-summarizer/resources/Documents/");
        File[] allfiles = dir.listFiles();
        for (File f : allfiles) {
            if (f.getName().endsWith(".txt")) {
                String s = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));
                String document = s.toLowerCase();
                Set<String> tokenSet = new HashSet<>();
                String[] tokenArray = openNlpProcessing.getTokens(document);
                String[] stems = openNlpProcessing.getStems(tokenArray);

                for (int i = 0; i < tokenArray.length; i++) {
                    String tok = tokenArray[i].trim().toLowerCase();
                    if (!tokenSet.contains(tok)) {
                        tokenSet.add(tok);
                    }
                    documentFrequency.put(tok, documentFrequency.getOrDefault(tok, 0) + 1);
                }
                for (int i = 0; i < stems.length; i++) {
                    String stm = stems[i].trim().toLowerCase();
                    if (!tokenSet.contains(stm)) {
                        tokenSet.add(stm);
                    }
                    documentFrequency.put(stm, documentFrequency.getOrDefault(stm, 0) + 1);
                }
                documentss.put(this.totalNoOfCorpusDocuments++, tokenSet);
            }
        }
        return documentFrequency;
    }

    public static void main(String[] args) throws IOException {
        OpenNlpTools openNlpTools = new OpenNlpTools();
        WeightCalculator weightCalculator = new WeightCalculator(openNlpTools);
    }
}
