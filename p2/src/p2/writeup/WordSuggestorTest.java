package p2.writeup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import cse332.datastructures.containers.Item;
import cse332.types.NGram;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;
import p2.clients.NGramTester;
import p2.sorts.TopKSort;
import p2.wordsuggestor.NGramToNextChoicesMap;
import p2.wordsuggestor.WordSuggestor;

/**
 * A little user interface which offers to operations:
 * 
 * suggestions stringi use suggestions to find words which follow stringi. A
 * search with N = 3 would look like: suggestions the tester suggestion
 * 
 * resetNK allows the N and K to be manipulated without starting program over,
 * calls look like: reset 3 -1
 * 
 * @author newdr
 *
 */
public class WordSuggestorTest {

    private WordSuggestor ws;
    private final PrintWriter output;
    private final BufferedReader input;
    String file;

    public static void main(String[] args) {
        String corpus = "spoken.corpus";
        String ourText = "historytechnology.txt";

        WordSuggestorTest wst;
        try {
            // wst = new WordSuggestorTest(new InputStreamReader(System.in),
            // new OutputStreamWriter(System.out), corpus, 3, 4);

            wst = new WordSuggestorTest(new InputStreamReader(System.in),
                    new OutputStreamWriter(System.out), ourText, 1, 4);
            System.out.println("welcome to the user interface \n"
                    + "type 'suggestions' followed by the string you would like to search for");
            wst.runTests();

        } catch (IOException e) {
            System.out.println("error");
        }

    }

    public WordSuggestorTest(Reader r, Writer w, String file, int n, int k)
            throws FileNotFoundException, IOException {
        ws = new WordSuggestor(file, n, k, NGramTester.binarySearchTreeConstructor(),
                NGramTester.binarySearchTreeConstructor());
        input = new BufferedReader(r);
        output = new PrintWriter(w);
        this.file = file;

    }

    public void runTests() {
        String inputLine;

        try {
            while ((inputLine = input.readLine()) != null) {
                System.out.println("welcome to the user interface \n"
                        + "type 'suggestions' followed by the string you would like to search for");
                if ((inputLine.trim().length() == 0) || (inputLine.charAt(0) == '#')) {
                    // echo blank and comment lines
                    output.println(inputLine);
                }
                else {
                    // separate the input line on white space
                    StringTokenizer st = new StringTokenizer(inputLine);
                    if (st.hasMoreTokens()) {
                        String command = st.nextToken();

                        List<String> arguments = new ArrayList<String>();
                        while (st.hasMoreTokens()) {
                            arguments.add(st.nextToken());
                        }

                        executeCommand(command, arguments);
                    }
                }
                output.flush();
            }
        } catch (IndexOutOfBoundsException | IOException e) {
            System.out.println("missed an argument");

        }
    }

    private void executeCommand(String command, List<String> arguments)
            throws FileNotFoundException, IOException, IndexOutOfBoundsException {
        try {
            if (command.equals("suggestions")) {
                displaySuggestions(arguments);

            }
            else if (command.equals("resetNK")) {
                int n = Integer.parseInt(arguments.get(0));
                int k = Integer.parseInt(arguments.get(1));

                this.ws = new WordSuggestor(this.file, n, k,
                        NGramTester.binarySearchTreeConstructor(),
                        NGramTester.binarySearchTreeConstructor());
                System.out.println("N and K have been changed");
            }
            else {

                output.println(
                        "This class only returns the results of WordSuggestor's suggestions \n"
                                + " type 'suggestions' followed by the desired search string for results");
            }
        } catch (IndexOutOfBoundsException | IOException e) {
            System.out.println("missed an argument");

        }

    }

    private void displaySuggestions(List<String> arguments) {
        String s = "";
        for (String str : arguments) {
            s += str + " ";
        }
        String[] results = ws.getSuggestions(s);
        System.out.println("Suggestions for " + s + "were :");
        for (String str : results) {
            System.out.println(str);
        }
    }

}
