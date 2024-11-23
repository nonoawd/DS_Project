public class Ranking {

    static String Query;
    static InvertedIndexBST inverted;
    static index index1;
    static LinkedList<Integer> query_doc;
    static LinkedList<Rank> ranked_doc;

    public Ranking (InvertedIndexBST inverted, index index1, String Query){
        this.inverted = inverted;
        this.index1 = index1;
        this.Query = Query;
        query_doc = new LinkedList<Integer>();
        ranked_doc = new LinkedList<Rank>();
    }

    public static void displayDocScore(){
        if (ranked_doc.empty()){
            System.out.println("Empty");
            return;
        }
        System.out.printf("%-8s%-8s\n","DocID","Score");
        ranked_doc.findfirst();
        while (!ranked_doc.last()){
            ranked_doc.retrieve().displayRank();
            ranked_doc.findnext();
        }
        ranked_doc.retrieve().displayRank();
    }

    public static Document DocByID (int id) {
        return index1.get_document_from_id(id);
    }

    public static int TermFrequency(Document d,String term){
        int freq = 0;
        LinkedList<String> words = d.words;
        if (words.empty())
            return 0;
        words.findfirst();
        while (!words.last()) {
            if (words.retrieve().equalsIgnoreCase(term))
                freq++;
            words.findnext();
        }
        if (words.retrieve().equalsIgnoreCase(term))
            freq++;
        return freq;
    }

    public static int RankScore(Document d,String Query) {
        if (Query.length()==0)
            return 0;
        String terms[] = Query.split(" ");
        int sum = 0;
        for(int i =0;i<terms.length;i++)
            sum += TermFrequency(d,terms[i].trim().toLowerCase());
        return sum;
    }

    public static void RankQuery(String Query) {
        LinkedList<Integer> A = new LinkedList<Integer>();
        if (Query.length()==0)
            return;
        String terms[] = Query.split("\\s+");
        boolean found = false;
        for (int i = 0;i<terms.length;i++){
            found = inverted.search_word_invertedBST(terms[i].trim().toLowerCase());
            if (found)
                A = inverted.Inverted_index.retrieve().doc_IDS;
            AddInList(A);
        }
    }

    public static void AddInList(LinkedList<Integer> A){
        if (A.empty())
            return;
        A.findfirst();
        while (!A.empty()){
            boolean found = existResult(query_doc,A.retrieve());
            if(!found)
                InsertIdList(A.retrieve());
            if (!A.last())
                A.findnext();
            else
                break;
        }
    }

    public static boolean existResult(LinkedList<Integer> result, Integer id) {
        if (result.empty())
            return false;
        result.findfirst();
        while (!result.last()) {
            if (result.retrieve().equals(id))
                return true;
            result.findnext();
        }
        if (result.retrieve().equals(id))
            return true;
        return false;
    }

    public static void InsertIdList(Integer id) {
        if (query_doc.empty()) {
            query_doc.insert(id);
            return;
        }
        query_doc.findfirst();
        while (!query_doc.last()) {
            if (id<query_doc.retrieve()) {
                Integer id1 = query_doc.retrieve();
                query_doc.update(id);
                query_doc.insert(id1);
                return;
            }
            else
                query_doc.findnext();
        }
        if (id<query_doc.retrieve()) {
            Integer id1 = query_doc.retrieve();
            query_doc.update(id);
            query_doc.insert(id1);
            return;
        }
        else
            query_doc.insert(id);
    }

    public static void InsertInList () {
        RankQuery(Query);
        if (query_doc.empty()){
            System.out.println("Empty Query");
            return;
        }
        query_doc.findfirst();
        while (!query_doc.last()) {
            Document d = DocByID(query_doc.retrieve());
            int rank = RankScore(d,Query);
            InsertSortedList(new Rank(query_doc.retrieve(),rank));
            query_doc.findnext();
        }
        Document d = DocByID(query_doc.retrieve());
        int rank = RankScore(d,Query);
        InsertSortedList(new Rank(query_doc.retrieve(),rank));
    }

    public static void InsertSortedList(Rank dr) {
        if (ranked_doc.empty()){
            ranked_doc.insert(dr);
            return;
        }
        ranked_doc.findfirst();
        while (!ranked_doc.last()) {
            if (dr.rank>ranked_doc.retrieve().rank) {
                Rank dr1 = ranked_doc.retrieve();
                ranked_doc.update(dr);
                ranked_doc.insert(dr1);
                return;
            }
            else
                ranked_doc.findnext();
        }
        if (dr.rank>ranked_doc.retrieve().rank) {
            Rank dr1 = ranked_doc.retrieve();
            ranked_doc.update(dr);
            ranked_doc.insert(dr1);
            return;
        }
        else
            ranked_doc.insert(dr);
    }

}
