public class InvertedIndex {
 LinkedList<Word> Inverted_index;

 public InvertedIndex() {
  Inverted_index = new LinkedList<Word>();
 }

 public void add (String text , int id) {
  if(!search_Word_inverted(text)) {
   Word w = new Word(text);
   w.doc_IDS.insert(id);
   Inverted_index.insert(w);
  }
  else {
   Word existing_word = Inverted_index.retrieve();
   existing_word.add_id(id);
  }
 }

 public boolean search_Word_inverted(String w) {
  if (Inverted_index == null || Inverted_index.empty())
   return false;

  Inverted_index.findfirst();
  while (!Inverted_index.last()){
   if (Inverted_index.retrieve().text.equals(w)) {
    return true; }
   Inverted_index.findnext();
  }
  if (Inverted_index.retrieve().equals(w)) {
   return true;
  }
  return false;
 }

 public void display_InvertedIndex(){
  if (Inverted_index == null){
   System.out.println("Null Inverted Index");
   return;
  }
  else if (Inverted_index.empty()) {
   System.out.println("empty Inverted Index");
   return;
  }
  Inverted_index.findfirst();
  while (!Inverted_index.last()) {
   Inverted_index.retrieve().display();
   Inverted_index.findnext();
  }
  Inverted_index.retrieve().display();
 }
}