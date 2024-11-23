import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class SearchEngine {

 private LinkedList<String> stopWords;
 private static index index1;
 private InvertedIndex inv;
 private InvertedIndexBST invBST;
 private int numToken = 0;
 private LinkedList<String> uniqueWord = new LinkedList<>();

 public SearchEngine() {
  stopWords = new LinkedList<>();
  index1 = new index();
  inv = new InvertedIndex();
  invBST = new InvertedIndexBST();
 }

 public void loadStopWords(String fileName) {
  try {
   File file = new File(fileName);
   Scanner scanner = new Scanner(file);
   while (scanner.hasNextLine()) {
    String line = scanner.nextLine();
    if (!line.trim().isEmpty()) {
     stopWords.insert(line);
    }
   }
   scanner.close();
  } catch (IOException e) {
   e.printStackTrace();
   System.out.println("Error loading stop words.");
  }
 }

 public void loadDocuments(String fileName) {
  try {
   File file = new File(fileName);
   Scanner scanner = new Scanner(file);
   scanner.nextLine(); // skip the first line
   while (scanner.hasNextLine()) {
    String line = scanner.nextLine();
    if (line.trim().isEmpty() || !line.contains(",")) {
     System.out.println("Empty or invalid line -> skipping this line = " + line);
     continue;
    }
    String[] parts = line.split(",", 2);
    if (parts.length < 2 || parts[0].trim().isEmpty() || !isNumeric(parts[0].trim())) {
     System.out.println("Invalid format -> skipping this line = " + line);
     continue;
    }

    try {
     int id = Integer.parseInt(parts[0].trim());
     String content = parts[1].trim();
     LinkedList<String> wordsInDoc = extractWordsFromContent(content, id);
     index1.add_Document(new Document(id, wordsInDoc, content));
    } catch (NumberFormatException e) {
     System.out.println("Invalid ID -> skipping this line = " + line);
    }
   }
   scanner.close();
  } catch (Exception e) {
   e.printStackTrace();
   System.out.println("Error loading documents.");
  }
 }

 private boolean isNumeric(String str) {
  try {
   Integer.parseInt(str);
   return true;
  } catch (NumberFormatException e) {
   return false;
  }
 }


 private LinkedList<String> extractWordsFromContent(String content, int id) {
  LinkedList<String> wordsInDoc = new LinkedList<>();
  processContentAndUpdateIndices(content, wordsInDoc, id);
  return wordsInDoc;
 }

 private void processContentAndUpdateIndices(String content, LinkedList<String> wordsInDoc, int id) {
  content = content.replaceAll("'", " ")
          .replaceAll("-", " ")
          .toLowerCase()
          .replaceAll("[^a-zA-Z0-9]", " ");
  String[] tokens = content.split("\\s+");
  numToken += tokens.length;

  for (String word : tokens) {
   if (!uniqueWord.exist(word)) {
    uniqueWord.insert(word);
   }
   if (!isStopWord(word)) {
    wordsInDoc.insert(word);
    inv.add(word, id);
    invBST.add(word, id);
   }
  }
 }

 private boolean isStopWord(String word) {
  if (stopWords.empty()) return false;
  stopWords.findfirst();
  while (!stopWords.last()) {
   if (stopWords.retrieve().equals(word)) return true;
   stopWords.findnext();
  }
  return stopWords.retrieve().equals(word);
 }

 public void loadAllFiles(String stopWordsFile, String documentsFile) {
  loadStopWords(stopWordsFile);
  loadDocuments(documentsFile);
 }

 public void displayDocumentsWithIDs(LinkedList<Integer> ids) {
  if (ids.empty()) {
   System.out.println("There exists no document");
   return;
  }
  ids.findfirst();
  while (!ids.last()) {
   Document doc = index1.get_document_from_id(ids.retrieve());
   if (doc != null) {
    System.out.println("Document " + doc.id + ": " + doc.content);
   }
   ids.findnext();
  }
  Document doc = index1.get_document_from_id(ids.retrieve());
  if (doc != null) {
   System.out.println("Document " + doc.id + ": " + doc.content);
  }
  System.out.println("");
 }

 public static void main(String[] args) {
  Scanner scanner = new Scanner(System.in);
  SearchEngine searchEngine = new SearchEngine();
  searchEngine.loadAllFiles("/Users/noufalawad/Desktop/DS/DS_PROJECT/stop.txt", "/Users/noufalawad/Desktop/DS/DS_PROJECT/dataset.csv");

  int choice;
  do {
   displayMenu();
   System.out.print("Enter your choice: ");
   while (!scanner.hasNextInt()) {
    System.out.println("Invalid input. Please enter a number.");
    scanner.next(); // clear invalid input
   }
   choice = scanner.nextInt();
   handleUserChoice(scanner, searchEngine, choice);
  } while (choice != 10);
 }

 private static void displayMenu() {
  System.out.println("\nMenu:");
  System.out.println("1) Retrieve a term:");
  System.out.println("   - Index with list");
  System.out.println("   - Inverted index with list");
  System.out.println("   - Inverted index with BST");
  System.out.println("2) Boolean retrieval (using AND – OR)");
  System.out.println("3) Ranked retrieval");
  System.out.println("4) Display indexed documents with number of words");
  System.out.println("5) Display number of documents in index");
  System.out.println("6) Display number of unique words in index");
  System.out.println("7) Display inverted index with list of lists");
  System.out.println("8) Display inverted index with BST");
  System.out.println("9) Display indexed tokens");
  System.out.println("10) Exit");
 }

 private static void handleUserChoice(Scanner scanner, SearchEngine searchEngine, int choice) {
  switch (choice) {
   case 1:
    retrieveTerm(scanner, searchEngine);
    break;
   case 2:
    booleanRetrieval(scanner, searchEngine);
    break;
   case 3:
    rankedRetrieval(scanner, searchEngine);
    break;
   case 4:
    searchEngine.index1.displayDoc();
    break;
   case 5:
    System.out.println("Number of documents: " + index1.all_document.n);
    break;
   case 6:
    System.out.println("Number of unique words: " + searchEngine.inv.Inverted_index.n);
    break;
   case 7:
    searchEngine.inv.display_InvertedIndex();
    break;
   case 8:
    searchEngine.invBST.display_Inverted_index();
    break;
   case 9:
    System.out.println("Number of tokens: " + searchEngine.numToken);
    System.out.println("Number of unique words: " + searchEngine.uniqueWord.n);
    break;
   case 10:
    System.out.println("Exiting Search Engine...");
    break;
   default:
    System.out.println("Invalid choice, try again.");
  }
 }

 public static void retrieveTerm(Scanner scanner, SearchEngine searchEngine) {
  System.out.print("Enter a term to retrieve: ");
  String term = scanner.next().toLowerCase().trim();
  System.out.println("Using index with list: ");
  LinkedList<Integer> res = index1.GetDocWithTerm(term);
  if (res != null && !res.empty()) {
   System.out.print("Word: " + term + " [");
   res.displayLinked();
   System.out.println("]");
  } else {
   System.out.println("Term not found in index with list.");
  }

  System.out.println("======================");
  System.out.println("Using inverted index with list: ");
  boolean found = searchEngine.inv.search_Word_inverted(term);
  if (found)
   searchEngine.inv.Inverted_index.retrieve().display();
  else
   System.out.println("Term not found");

  System.out.println("======================");
  System.out.println("Using inverted index with BST: ");
  boolean found1 = searchEngine.invBST.search_word_invertedBST(term);
  if (found1)
   searchEngine.invBST.Inverted_index.retrieve().display();
  else
   System.out.println("Term not found");
 }

 public static void booleanRetrieval(Scanner scanner, SearchEngine searchEngine) {
  scanner.nextLine(); // consume newline
  System.out.print("Enter query to retrieve (e.g., market AND sports): ");
  String query = scanner.nextLine().toLowerCase().replaceAll("and", "AND").replaceAll("or", "OR");

  System.out.println("Using which method?");
  System.out.println("1) Inverted index with list");
  System.out.println("2) Inverted index with BST");
  int choice = scanner.nextInt();

  switch (choice) {
   case 1:
    QueryProcessor queryProcessor = new QueryProcessor(searchEngine.inv);
    LinkedList<Integer> res1 = QueryProcessor.MixedQuery(query);
    if (res1 != null && !res1.empty()) {
     searchEngine.displayDocumentsWithIDs(res1);
    } else {
     System.out.println("No documents match the query.");
    }
    break;
   case 2:
    QueryProcessorBST queryProcessorBST = new QueryProcessorBST(searchEngine.invBST);
    LinkedList<Integer> res2 = QueryProcessorBST.MixedQuery(query);
    if (res2 != null && !res2.empty()) {
     searchEngine.displayDocumentsWithIDs(res2);
    } else {
     System.out.println("No documents match the query.");
    }
    break;
   default:
    System.out.println("Wrong choice.");
  }
 }


 private static void rankedRetrieval(Scanner scanner, SearchEngine searchEngine) {
  scanner.nextLine(); // consume newline
  System.out.print("Enter a query to rank: ");
  String query = scanner.nextLine().toLowerCase();
  Ranking ranking = new Ranking(searchEngine.invBST, index1, query);
  ranking.InsertInList();
  ranking.displayDocScore();
 }
}