package com.text.summarization;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

import java.util.ArrayList;

class WordNet {
    CosineSimilarity cosineSimilarity;
    WordNetDatabase wd = WordNetDatabase.getFileInstance();
    ArrayList<String> synonymList = new ArrayList<String>();
    ArrayList<String> definitionList = new ArrayList<String>();
    String replacement = "";
    double cosineValue = 0;

    WordNet(FeatureExtraction featureExtraction) {
        cosineSimilarity = new CosineSimilarity(featureExtraction);
    }

    /**
     * implementation of Lesk Algorithm.
     * This method returns synonym of a word .
     * when more than one synonyms are found in wordNet (as most is the case) .
     * Then cosine Similarity is found between definition of each synonym and
     * surrounding sentences od word . The one with high value of cosine similarity is selected.
     *
     * @param inputWord             :word whose synonym is to be found.
     * @param currentSentenceMinus2 : neighbouring sentence of word
     * @param currentSentenceMinus1 : neighbouring sentence of word
     * @param currentSentencePlus1  :neighbouring sentence of word
     * @param currentSentencePlus2  : neighbouring sentence of word
     * @return synonym
     */
    public String getSynonym(String inputWord, String currentSentenceMinus2, String currentSentenceMinus1,
                             String currentSentencePlus1, String currentSentencePlus2) {


        String firstDef;
        Synset[] wordNetResult = wd.getSynsets(inputWord);

        //get wordNet result of word and parse it, and add synonyms to synonymList ,
        // and its corresponding definition to definitionList
        if (wordNetResult.length > 0) {
            firstDef = wordNetResult[0].toString();

            String[] definition = firstDef.split("\\[")[1].split("\\]");


            String[] wordSynonyms = definition[0].split(",");
            if (wordSynonyms.length > 1) {
                for (int i = 0; i < wordSynonyms.length; i++)
                    synonymList.add(wordSynonyms[1]);

                definition[1] = definition[1].replaceFirst("-", " ").trim();
                definitionList.add(definition[1]);

            } else {
                synonymList.add(wordSynonyms[0]);
                definition[1] = definition[1].replaceFirst("-", " ").trim();
                definitionList.add(definition[1]);
            }

        }
        if (wordNetResult.length > 1) {
            firstDef = wordNetResult[1].toString();
            String[] definition = firstDef.split("\\[")[1].split("\\]");
            String[] wordSynonyms = definition[0].split(",");
            if (wordSynonyms.length > 1) {
                synonymList.add(wordSynonyms[1]);
                definition[1] = definition[1].replaceFirst("-", " ").trim();
                definitionList.add(definition[1]);

            } else {
                synonymList.add(wordSynonyms[0]);
                definition[1] = definition[1].replaceFirst("-", " ").trim();
                definitionList.add(definition[1]);
            }

        }

        if (wordNetResult.length > 2) {
            firstDef = wordNetResult[2].toString();
            String[] definition = firstDef.split("\\[")[1].split("\\]");
            String[] wordSynonyms = definition[0].split(",");
            if (wordSynonyms.length > 1) {
                synonymList.add(wordSynonyms[1]);

                definition[1] = definition[1].replaceFirst("-", " ").trim();
                definitionList.add(definition[1]);

            } else {
                synonymList.add(wordSynonyms[0]);
                definition[1] = definition[1].replaceFirst("-", " ").trim();
                definitionList.add(definition[1]);
            }
        }

        // calculate Cosine Similarity of Synonym Definition and neighbouring sentences of word.
        double cosineSimilarities[] = new double[definitionList.size()];
        for (int n = 0; n < definitionList.size(); n++) {
            String def = definitionList.get(n);
            cosineValue = cosineSimilarity.getSimilarityScore(def, currentSentenceMinus2);
            cosineValue = cosineValue + cosineSimilarity.getSimilarityScore(def, currentSentenceMinus1);
            cosineValue = cosineValue + cosineSimilarity.getSimilarityScore(def, currentSentencePlus1);
            cosineValue = cosineValue + cosineSimilarity.getSimilarityScore(def, currentSentencePlus2);
            cosineSimilarities[n] = cosineValue;
        }
        double finalCosineValueOfSynonym = 0;
        replacement = inputWord;
        for (int m = 0; m < definitionList.size(); m++) {
            if (finalCosineValueOfSynonym < cosineSimilarities[m]) {
                replacement = synonymList.get(m);
                finalCosineValueOfSynonym = cosineSimilarities[m];
            }
        }

        synonymList.clear();
        definitionList.clear();
        return replacement;
    }
}