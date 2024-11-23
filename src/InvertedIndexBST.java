public class InvertedIndexBST {
 BST<Word>Inverted_index;

 public InvertedIndexBST() {
  Inverted_index = new BST<Word>();
 }

 public void add_from_inverted_list(InvertedIndex inv) {
  if(inv.Inverted_index.empty())
   return;

  inv.Inverted_index.findfirst();
  while(!inv.Inverted_index.last()) {
   Inverted_index.insert(inv.Inverted_index.retrieve().text, inv.Inverted_index.retrieve());
   inv.Inverted_index.findnext();
  }
  Inverted_index.insert(inv.Inverted_index.retrieve().text, inv.Inverted_index.retrieve());
 }

 public void add(String text , int id) {
  if(!search_word_invertedBST(text)){
   Word w = new Word(text);
   w.doc_IDS.insert(id);
   Inverted_index.insert(text ,w);
  }
  else {
   Word existing_word = Inverted_index.retrieve();
   existing_word.add_id(id);
  }
 }

 public boolean search_word_invertedBST(String w) {
  return Inverted_index.findKey(w);
 }

public void display_Inverted_index(){
  if (Inverted_index == null){
   System.out.println("Null Inverted Index");
   return;
  }
  else if (Inverted_index.empty()) {
   System.out.println("empty Inverted Index");
   return;
  }
  Inverted_index.inOrder();
 }
}