public class Word {

    String text;
    LinkedList<Integer> doc_IDS;

    public Word(String w) {
        text = w;
        doc_IDS = new LinkedList<Integer>();
    }

    public void add_id(int id) {
        if(!existIn_doc_IDS(id))
            doc_IDS.insert(id);
    }

    public void display() {
        System.out.println("\n---------------------------");
        System.out.println("Word: "+text);
        System.out.println("[");
        doc_IDS.displayLinked();
        System.out.println("]");
    }

    public boolean existIn_doc_IDS(Integer id) {
        if (doc_IDS.empty())
            return false;
        doc_IDS.findfirst();
        while (!doc_IDS.last()) {
            if (doc_IDS.retrieve().equals(id))
                return true;
            doc_IDS.findnext();
        }
        if (doc_IDS.retrieve().equals(id))
            return true;
        return false;
    }

}