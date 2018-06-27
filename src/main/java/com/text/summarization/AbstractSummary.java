package com.text.summarization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.text.summarization.Utils.RESOURCE_DIRECTORY;

public class AbstractSummary {
    private Set<String> connectingWords = new HashSet<>();
    private SynonymGetter synonymGetter;

    public AbstractSummary(FeatureExtraction featureExtraction) throws IOException {
        loadConnectingSet();
        synonymGetter = new SynonymGetter(featureExtraction);
    }

    private void loadConnectingSet() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(RESOURCE_DIRECTORY + "connectingWords.txt"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().toLowerCase().trim();
            if (line.length() > 0) {
                connectingWords.add(line);
            }
        }
        scanner.close();
    }

    public Set<String> getAbstractSummary(Map<Integer, Double> sentencesWeightMap, HashMap<Integer, String[]> sentenceTokenMap,
                                          HashMap<Integer, String> sentenceMap) {
        Map<Integer, Double> sentenceDoubleMap = useConnectingWordFeature(sentencesWeightMap, sentenceTokenMap);

        Set<Integer> positions = sentenceDoubleMap.keySet();
        Set<String[]> summary = new HashSet<>();
        String[] strings = new String[positions.size()];
        int i = 0;
        for (Integer position : positions) {
            int p = position;
            strings[i++] = sentenceMap.get(p);
            summary.add(sentenceTokenMap.get(p));
        }
        if (summary.size() < 3) {
            return new LinkedHashSet<>(Arrays.asList(strings));
        }
        Set<String> finalSummary = synonymGetter.replaceWordsWithSynonyms(summary, strings);
        return finalSummary;
    }

    /**
     * Use Connecting Word feature is any selected sentence is begin with connecting word,
     * then include its previous sentence in summary.
     *
     * @param sentencesMap
     * @param sentenceTokenMap
     * @return
     */
    private Map<Integer, Double> useConnectingWordFeature(Map<Integer, Double> sentencesMap, HashMap<Integer, String[]> sentenceTokenMap) {
        Set<Integer> positions = sentencesMap.keySet();
        for (int position : positions) {
            String word = sentenceTokenMap.get(position)[0].toLowerCase().trim();
            if (position > 0 && connectingWords.contains(word) && !sentencesMap.containsKey(position - 1)) {
                sentencesMap.put(position - 1, 1.0);
            }
        }
        return sortByKey(sentencesMap);
    }

    private Map sortByKey(Map<Integer, Double> weightMap) {
        List list = new LinkedList(weightMap.entrySet());

        // Defined Custom Comparator here
        Collections.sort(list, (Comparator) (o1, o2) -> ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey()));
        Map sortedHashMap = new LinkedHashMap();
        for (Iterator it = (list).iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}
