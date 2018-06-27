package com.text.summarization;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class SynonymGetter {
    FeatureExtraction featureExtraction;
    String WORDNET_DICT = Utils.RESOURCE_DIRECTORY + "dict";

    public SynonymGetter(FeatureExtraction featureExtraction) {
        this.featureExtraction = featureExtraction;
    }

    public Set<String> replaceWordsWithSynonyms(Set<String[]> sentences, String[] strings) {
        System.setProperty("wordnet.database.dir", WORDNET_DICT);
        WordNet wordNet = new WordNet(featureExtraction);
        String synonym;
        Set<String> stringBuilder = new LinkedHashSet<>();
        int i = 0;
        for (String[] words : sentences) {
            String resultString = strings[i];
            System.out.println(i + "resultString:::" + resultString);
            String[] pos = featureExtraction.getPosTags(words);
            for (int k = 0; k < words.length; k++) {

                // get Synonym for only thoseWords whose length is greater than 5.
                if (pos[k].equals("NN") || (pos[k].equals("VBN")) && words[k].length() > 4) {

                    // Condition for first and second sentence.
                    if (i < 2) {
                        // if text has only one sentence.
                        if ((sentences.size() == 1) || (sentences.size() == 2)) {
                            synonym = wordNet.getSynonym(words[k], "", "", "", "");
                            // condition for current sentence being middle of
                            // (total) three sentences
                        } else if ((sentences.size() == 3) && (i == 1)) {
                            synonym = wordNet.getSynonym(words[k], "", "", strings[i + 1], strings[i - 1]);
                        } else {
                            synonym = wordNet.getSynonym(words[k], "", "", strings[i + 1], strings[i + 2]);
                        }
                        resultString = resultString.replaceAll(words[k], synonym);
                    }
                    if (i > sentences.size() - 2) {
                        if (sentences.size() == 1) {
                            synonym = wordNet.getSynonym(words[k], "", "", "", "");
                        } else {
                            synonym = wordNet.getSynonym(words[k], "", "", strings[i - 1], strings[i - 2]);
                        }
                        resultString = resultString.replaceAll(words[k], synonym);
                    }
                    if ((i > 1) && (i < sentences.size() - 2)) {
                        synonym = wordNet.getSynonym(words[k], strings[i - 1], strings[i - 2], strings[i + 1],
                                strings[i + 2]);
                        resultString = resultString.replace(words[k], synonym);
                    }
                }
            }
            stringBuilder.add(resultString);
            i++;
        }
        return stringBuilder;
    }
}