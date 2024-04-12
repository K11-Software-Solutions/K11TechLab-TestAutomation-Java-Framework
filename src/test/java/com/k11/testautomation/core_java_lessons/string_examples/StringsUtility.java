package com.k11.testautomation.core_java_lessons.string_examples;

import org.testng.annotations.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringsUtility {

    //Checking for palindromes in Java 7 or earlier
   // String problem #  1)
    public boolean isPalindrome(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                sb.append(c);
            }
        }
        String forward = sb.toString().toLowerCase();
        String backward = sb.reverse().toString().toLowerCase();
        return forward.equals(backward);
    }

     //Compare 2 strings and tell if they are anagram
     //Cinema and Iceman: Can you write a java program that compares 2 strings and tell if they are anagram?
     //An anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once. To determine if two strings are anagrams in Java, you can compare their sorted characters or count the characters in each string and then compare these counts. Here's a simple and efficient approach using character count comparison:
     /*Time Complexity:
      O(n log n), where n is the length of the strings. The most time-consuming operation in this method is the sorting of the character arrays. Java's Arrays.sort() for primitive types uses a dual-pivot Quicksort algorithm that has a time complexity of O(n log n).
      Space Complexity:
      O(n), where n is the length of the strings. This space is used to store the character arrays created from the input strings.
      */
     // String problem #  2)
    public static boolean anagramsCheckerUsingArraysSort(String str1, String str2) {
        // If length of strings is not same, then they cannot be anagram
        if (str1.length() != str2.length()) {
            return false;
        }
        // Convert strings to char array
        char[] charArray1 = str1.toCharArray();
        char[] charArray2 = str2.toCharArray();
        // Sort the char array
        Arrays.sort(charArray1);
        Arrays.sort(charArray2);
        // Check if sorted char arrays are equal
        return Arrays.equals(charArray1, charArray2);
    }


    /*
    We first check if the two strings have the same length. If not, they cannot be anagrams.
    We use a count array of size 256 (to cover the entire ASCII range) to keep track of the count of each character.
    We iterate over the first string, increasing the count for each character.
    We then iterate over the second string, decreasing the count for each character.
    Finally, we check the count array. If all counts are 0, the strings are anagrams; otherwise, they are not.

    Time Complexity:
      O(n), where n is the length of the strings. We iterate through each string once, and the final loop through the count array is constant time with respect to the string length.
      Space Complexity:
      O(1), as the count array size is fixed and does not grow with the input string length.
    */
    // String problem #  3)
    public static boolean areAnagrams(String str1, String str2) {
            // If length of strings is not same, then they cannot be anagram
            if (str1.length() != str2.length()) {
                return false;
            }
            // Create count arrays of size 256 for all possible characters, initialized to 0
            int[] count = new int[256];// Assuming ASCII character set
            // Increment count for each character in the first string
            for (int i = 0; i < str1.length(); i++) {
                count[str1.charAt(i)]++;
            }
            // Decrement count for each character in the second string
            for (int i = 0; i < str2.length(); i++) {
                count[str2.charAt(i)]--;
            }
            // If count of any character is not 0, then strings are not anagram
            for (int i = 0; i < 256; i++) {
                if (count[i] != 0) {
                    return false;
                }
            }
       return true;
    }

    // String problem #  4)
    public static boolean isUniqueChars(String str) {
        if(str.length()>128) return false;
        boolean[] char_set=new boolean[128]; //Assuming the string is an ASCII string
        for(int i=0; i<=str.length()-1; ++i) {
            int val = str.charAt(i);
            if (char_set[val]) {
                return false;
            }
            char_set[val]=true;
        }
        return true;
    }

    // String problem #  5)
    public static void findFirstNonRepeatingCharacter(String input) {
        for (char i : input.toCharArray()) {
            if (input.indexOf(i) == input.lastIndexOf(i)) {
                System.out.println(i);
                break;
            }
        }
    }

    // String problem #  6)
    void findOccurance(String str, String substringToFind) {
        int start = 0, count=0;
        while (true) {
            int found = str.indexOf(substringToFind, start);
            if (found != -1) {
                count++;
            }
            if (found == -1) break;
            start = found + 2; // move start up for next iteration
        }
    }

    //Java Program : Find the occurrence of characters from string
    // String problem #  7)
    public static Map<Character, Integer> countCharacterOccurrences(String str) {
       Map<Character, Integer> occurrenceMap = new HashMap<>();
       // Iterate through the string and count occurrences of each character
        for (char c : str.toCharArray()) {
            occurrenceMap.put(c, occurrenceMap.getOrDefault(c, 0) + 1);
        }
        return occurrenceMap;
   }


    // String problem #  8)
    public static boolean canConstruct(String str1, String str2) {
        if (str1.length() > str2.length())
            return false;

        Map<Character, Integer> charFrequency = new HashMap<>();

        // Count the frequency of characters in str2
        for (char c : str2.toCharArray()) {
            charFrequency.put(c, charFrequency.getOrDefault(c, 0) + 1);
        }

        // Check if str1 can be constructed from str2
        for (char c : str1.toCharArray()) {
            if (!charFrequency.containsKey(c) || charFrequency.get(c) == 0)
                return false;
            charFrequency.put(c, charFrequency.get(c) - 1);
        }

        return true;
    }

    // String problem #  9)
    static void removeDuplicates(char[] str) {
        Set<Character> hashset = new LinkedHashSet<Character>();

        int writeIndex = 0;
        int readIndex = 0;

        while (readIndex != str.length) {

            if (!hashset.contains(str[readIndex])) {

                hashset.add(str[readIndex]);
                str[writeIndex] = str[readIndex];
                ++writeIndex;
            }

            ++readIndex;
        }

        str[writeIndex] = '\0';
    }

    // String problem #  10)
    public static void removeDuplicateLetters(String s) {
        //To remove special char: punctuations and symbols
        String s1=s.replaceAll("[^\\p{L}\\p{Z}]","");
        char a[] = s1.toCharArray();
        StringBuffer sb = new StringBuffer();
        //Using LinkedHashSet to preserve the order so that the set will keep only the first occurrence of a letter
        LinkedHashSet<Character> hs = new LinkedHashSet<>();
        for (int i = 0; i < a.length; i++) {
            if((!hs.contains(Character.toLowerCase(a[i])) && (!hs.contains(Character.toUpperCase(a[i]))))) {
                hs.add(a[i]);
            }
        }
        Iterator<Character> itr = hs.iterator();
        while (itr.hasNext()) {
            char o = itr.next();
            if (o != ' ')
            {
                sb.append(o);
            }
        }
        System.out.println(sb);
      }

     // String problem #  11)
       public static int countVowels(String input) {
            int count = 0;
            String vowels = "aeiouAEIOU";
            for (int i = 0; i < input.length(); i++) {
                if (vowels.indexOf(input.charAt(i)) != -1) {
                    count++;
                }
            }
            return count;
        }

    //Write java program to find the output:
    // Input a1b2c4O2,
    // Output a-bb-cccc-OO
    // String problem #  12)
        public static String generateOutput(String input) {
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < input.length(); i++) {
                char currentChar = input.charAt(i);
                if (Character.isAlphabetic(currentChar)) {
                    if (i + 1 < input.length() && Character.isDigit(input.charAt(i + 1))) {
                        int repeatCount = Character.getNumericValue(input.charAt(i + 1));
                        for (int j = 0; j < repeatCount; j++) {
                            output.append(currentChar);
                        }
                        i++; // Skip the next character as it has already been processed
                    } else {
                        output.append(currentChar);
                    }
                } else if (!Character.isDigit(currentChar)) {
                    output.append("-");
                }
            }

            return output.toString();
        }

       //Java program to find the occurrence of duplicate values in a sequence string with the help of map
       // String problem #  13)
        public static void findDuplicateWordsInString(String str) {
            Map<String, Integer> wordCountMap = new HashMap<>();

            // Split the string into words
            String[] words = str.split("\\s+");

            // Iterate through each word
            for (String word : words) {
                // Remove punctuation marks from the word
                word = word.replaceAll("[^a-zA-Z0-9]", "");

                // Convert word to lowercase for case-insensitive comparison
                word = word.toLowerCase();

                // If word already exists in the map, increment its count
                if (wordCountMap.containsKey(word)) {
                    int count = wordCountMap.get(word);
                    wordCountMap.put(word, count + 1);
                } else {
                    // If word doesn't exist, add it to the map with count 1
                    wordCountMap.put(word, 1);
                }
            }
            // Print duplicate words and their counts
            System.out.println("Duplicate words and their counts:");
            for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
                if (entry.getValue() > 1) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
        }


    //Find the second repeating word and second largest occurrence
   /* To find the second repeating word and the second largest occurrence of a word in a string, you can follow these steps:
    Split the string into words based on spaces or punctuation.
    Use a HashMap to keep track of the count of each word.
    Iterate through the HashMap to find the first and second largest occurrences.
    To find the second repeating word, keep track of the order in which words are repeated for the second time.
            Here's how you can implement it in Java:
            import java.util.*; */

    // String problem #  14)
    public static void findSecondRepeatingWordAndOccurrence(String input) {
            String[] words = input.toLowerCase().split("\\W+"); // Split on non-word characters
            Map<String, Integer> wordCounts = new HashMap<>();
            LinkedHashMap<String, Integer> orderOfRepetition = new LinkedHashMap<>();

            for (String word : words) {
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                if (wordCounts.get(word) == 2) { // Check for second occurrence
                    orderOfRepetition.putIfAbsent(word, wordCounts.get(word));
                }
            }

            if (orderOfRepetition.size() < 2) {
                System.out.println("There are not enough repeating words.");
                return;
            }

            // Finding the second largest occurrence
            int firstLargestCount = 0, secondLargestCount = 0;
            String secondRepeatingWord = null;
            for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                int count = entry.getValue();
                if (count > firstLargestCount) {
                    secondLargestCount = firstLargestCount;
                    firstLargestCount = count;
                } else if (count > secondLargestCount && count <  firstLargestCount) {
                    secondLargestCount = count;
                }
            }

            // Finding the second repeating word
            Iterator<String> iterator = orderOfRepetition.keySet().iterator();
            iterator.next(); // Skip the first repeating word
            secondRepeatingWord = iterator.next(); // Get the second repeating word

            System.out.println("Second repeating word: " + secondRepeatingWord);
            System.out.println("Second largest occurrence: " + secondLargestCount);
    }


        // Given a number (eg 25) write a function to return the string 'Twenty Five'
        /* To convert a number into its corresponding English words representation, you can approach this by breaking down the number into its place values (e.g., hundreds, tens, and ones) and mapping each segment to its corresponding word. For numbers up to 99, you'll specifically need to handle the units (1-9), the special cases for ten to nineteen, and the tens (twenty, thirty, etc.).
          Here's a simple Java function that handles numbers up to 99, as per the example given: */
        // String problem #  15)
        public static String numberToWords(int num) {
            if (num < 0 || num > 99) {
                return "Number out of range";
            }

            String[] units = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
            String[] teens = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
            String[] tens = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

            if (num == 0) return "Zero";

            if (num < 10) {
                return units[num];
            } else if (num < 20) {
                return teens[num - 10];
            } else {
                int tenVal = num / 10;
                int unitVal = num % 10;
                return tens[tenVal] + (unitVal > 0 ? " " + units[unitVal] : "");
            }
    }


    // Java Program : Find sum of all the numbers from String

    //To find the sum of all the numbers present in a string in Java, you can use regular expressions to extract the numbers and then sum them up. Here's a Java program that accomplishes this:

    /* The program design involves:
    A method to extract and sum numbers from a given string.
    Using regular expressions to find matches that represent numbers.
    Iterating over the found matches to sum them up.
         * This method extracts all numbers from the given string and sums them up.
         * @param input The input string containing numbers.
         * @return The sum of all extracted numbers.
     */
    // String problem #  16)
     public static int sumOfNumbers(String input) {
            // Pattern to match numbers in the string
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(input);

            int sum = 0;
            while (matcher.find()) {
                // Convert the matched number to integer and add it to sum
                sum += Integer.parseInt(matcher.group());
            }
            return sum;
     }


    public static void main(String[] args) {
            String str1 = "hello";
            String str2 = "worldhello";
            System.out.println("Can \"" + str1 + "\" be constructed from \"" + str2 + "\"? " + canConstruct(str1, str2)); // Output: true

        String input = "hello";
        Map<Character, Integer> occurrenceMap = countCharacterOccurrences(input);
        System.out.println("Occurrences of characters in the string:");
        for (Map.Entry<Character, Integer> entry : occurrenceMap.entrySet()) {
            System.out.println("'" + entry.getKey() + "' : " + entry.getValue());
        }

        input = "a1b2c4O2";
            String output = generateOutput(input);
            System.out.println("Input: " + input);
            System.out.println("Output: " + output);

            String str = "This is a program to find duplicate words in a string, again! a program";
            findDuplicateWordsInString(str);

            String s1 = "This is a test. This test is just a test.";
            findSecondRepeatingWordAndOccurrence(input);

            int num = 25;
            System.out.println(numberToWords(num)); // Should print "Twenty Five"

            str1 = "Cinema";
            str2 = "Iceman";
            if (areAnagrams(str1.toLowerCase(), str2.toLowerCase())) {
            System.out.println(str1 + " and " + str2 + " are Anagrams");
            } else {
            System.out.println(str1 + " and " + str2 + " are not Anagrams");
            }
    }

}
