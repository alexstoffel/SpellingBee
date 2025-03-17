// Alex Stoffel
// This is my spelling bee code and given a string it will output all the words in that string
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Alex Stoffel
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        // These string will be helpful for recursive method
        makeWords("", letters);
    }

    // Generate Helper
    public void makeWords(String usedWords, String rest){
        // If the word is longer than empty you can add it
        if (usedWords.length() >= 1){
            words.add(usedWords);
        }

        // Splitting the rest up
        String word1;
        String word2;
        // Kind of acts as base case (if rest is empty, no more recursive calls)
        for (int i = 0; i < rest.length(); i++){
            // Recursive step
            word1 = usedWords + rest.substring(i, i + 1);
            word2 = rest.substring(0,i) + rest.substring(i + 1);
            makeWords(word1,word2);
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.

    // Sort
    public void sort(){
        // Calling on the helper method
        words = sortHelper(words, 0, words.size());
    }

    // Sort Helper
    public ArrayList<String> sortHelper(ArrayList<String> arr, int low, int high){
        // Base case
        if (high - low <= 1){
            ArrayList<String> newArr = new ArrayList<String>(1);
            if (high > low){
                // Add the smallest element
                newArr.add(arr.get(low));
            }
            return newArr;
        }
        // Splitting into two halves
        int med = (high + low) / 2;
        ArrayList<String> arr1 = sortHelper(arr, low, med);
        ArrayList<String> arr2 = sortHelper(arr, med, high);
        return merge(arr1, arr2);
    }

    // Merge method
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2){
        // Create the arraylist of sorted things
        ArrayList<String> merged = new ArrayList<String>();
        int a = 0, b = 0;

        // While loops to go through arrays
        while (a < arr1.size() && b < arr2.size()){
            int compareNum = arr1.get(a).compareTo(arr2.get(b));
            // Sort alphabetically
            if (compareNum <= 0){
                merged.add(arr1.get(a++));
            }else{
                merged.add(arr2.get(b++));
            }
        }

        // Based on which array is empty
        while (a < arr1.size()){
            merged.add(arr1.get(a++));
        }
        while (b < arr2.size()){
            merged.add(arr2.get(b++));
        }

        // Return sorted array
        return merged;
    }


    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        int num = 0;
        // Go through each string in words
        while (num < words.size()){
            // If the string is in the dictionary have, if else remove it
            if (found(words.get(num), DICTIONARY, 0, DICTIONARY_SIZE)){
                num++;
            }else{
                words.remove(num);
            }
        }
    }

    // Found function
    public boolean found(String s, String[] dict, int start, int finish){
        // Base case
        if(start >= finish){
            return false;
        }

        // Storing the middle in a variable
        int mid = (start + finish) / 2;

        // Comparing it to
        if (dict[mid].equals(s)){
            return true;
        }else if(dict[mid].compareTo(s) > 0){
            return found(s, dict, start, mid);
        }else{
            return found(s, dict, mid + 1, finish);
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
