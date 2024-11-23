public class index {
    LinkedList<Document> all_document;

    public index() {
        all_document = new LinkedList<Document>();
    }

    public void add_Document(Document doc) {
        all_document.insert(doc);
    }

    public Document get_document_from_id(int id) {
        if(all_document.empty()){
            System.out.println("There exist no document");
            return null;
        }
        all_document.findfirst();
        while(!all_document.last()) {
            if(all_document.retrieve().id==id)
                return all_document.retrieve();
            all_document.findnext();
        }
        if(all_document.retrieve().id==id)
            return all_document.retrieve();
        return null;
    }

public void displayDoc() {
        if (all_document == null){
            System.out.println("null document");
            return;
        }
        else if (all_document.empty()) {
            System.out.println("Empty document");
            return;
        }
        all_document.findfirst();
        while(!all_document.last()) {
            Document d = all_document.retrieve();
            System.out.println("\n---------------------------");
            System.out.println("ID: " + d.id);
            d.words.displayLinked();
            all_document.findnext();
        }
        Document d = all_document.retrieve();
        System.out.println("\n---------------------------");
        System.out.println("ID: " + d.id);
        d.words.displayLinked();
    }

    public LinkedList<Integer> GetDocWithTerm (String term) {
        LinkedList<Integer> result = new LinkedList<>();
        if (all_document.empty()){
            System.out.println("There exists no document");
            return null;
        }
        all_document.findfirst();
        while (!all_document.last()) {
            if (all_document.retrieve().words.exist(term.toLowerCase().trim()))
                result.insert(all_document.retrieve().id);
            all_document.findnext();
        }
        if (all_document.retrieve().words.exist(term.toLowerCase().trim()))
            result.insert(all_document.retrieve().id);
        return result;
    }

}