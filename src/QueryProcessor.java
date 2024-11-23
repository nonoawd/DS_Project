public class QueryProcessor {

    static InvertedIndex inverted;

    public QueryProcessor(InvertedIndex inverted) {
        this.inverted = inverted;
    }

    public static LinkedList<Integer> BooleanQuery(String Query) {
        if (!Query.contains("AND") && !Query.contains("OR"))
            return AndQuery(Query);
        else if (Query.contains("AND") && !Query.contains("OR"))
            return AndQuery(Query);
        else if (!Query.contains("AND") && Query.contains("OR"))
            return ORQuery(Query);
        else
            return MixedQuery(Query);
    }

    public static LinkedList<Integer> MixedQuery(String Query) {
        LinkedList<Integer> A = new LinkedList<Integer>();
        LinkedList<Integer> B = new LinkedList<Integer>();
        if (Query.length() == 0)
            return A;
        String ors[] = Query.split("OR");

        A = AndQuery(ors[0]);
        for (int i = 1; i < ors.length; i++) {
            B = AndQuery(ors[i]);
            A = ORQuery(A, B);
        }
        return A;
    }

    public static LinkedList<Integer> AndQuery(String Query) {
        LinkedList<Integer> A = new LinkedList<Integer>();
        LinkedList<Integer> B = new LinkedList<Integer>();
        String terms[] = Query.split("AND");
        if (terms.length == 0)
            return A;

        boolean found = inverted.search_Word_inverted(terms[0].trim().toLowerCase());
        if (found)
            A = inverted.Inverted_index.retrieve().doc_IDS;
        for (int i = 1; i < terms.length; i++) {
            found = inverted.search_Word_inverted(terms[i].trim().toLowerCase());
            if (found)
                B = inverted.Inverted_index.retrieve().doc_IDS;
            A = AndQuery(A, B);
        }
        return A;
    }

    public static LinkedList<Integer> AndQuery(LinkedList<Integer> A, LinkedList<Integer> B) {

        LinkedList<Integer> result = new LinkedList<Integer>();
        if (A.empty() || B.empty())
            return result;
        A.findfirst();
        while (true) {
            boolean found = existsIn_result(result, A.retrieve());
            if (!found) {
                B.findfirst();
                while (true) {
                    if (B.retrieve().equals(A.retrieve())) {
                        result.insert(A.retrieve());
                        break;
                    }
                    if (!B.last())
                        B.findnext();
                    else
                        break;
                }//end inner while for B
            }//end if
            if (!A.last())
                A.findnext();
            else
                break;
        }
        return result;
    }

    public static LinkedList<Integer> ORQuery(String Query) {
        LinkedList<Integer> A = new LinkedList<Integer>();
        LinkedList<Integer> B = new LinkedList<Integer>();
        String terms[] = Query.split("OR");
        if (terms.length == 0)
            return A;

        boolean found = inverted.search_Word_inverted(terms[0].trim().toLowerCase());
        if (found)
            A = inverted.Inverted_index.retrieve().doc_IDS;
        for (int i = 1; i < terms.length; i++) {
            found = inverted.search_Word_inverted(terms[i].trim().toLowerCase());
            if (found)
                B = inverted.Inverted_index.retrieve().doc_IDS;
            A = ORQuery(A, B);
        }
        return A;
    }

    public static LinkedList<Integer> ORQuery(LinkedList<Integer> A, LinkedList<Integer> B) {

        LinkedList<Integer> result = new LinkedList<Integer>();
        if (A.empty() && B.empty())
            return result;
        A.findfirst();
        while (!A.empty()) {
            boolean found = existsIn_result(result, A.retrieve());
            if (!found)
                result.insert(A.retrieve());

            if (!A.last())
                A.findnext();
            else
                break;
        }
        B.findfirst();
        while (!B.empty()) {
            boolean found = existsIn_result(result, A.retrieve());
            if (!found)
                result.insert(B.retrieve());
            if (!B.last())
                B.findnext();
            else
                break;
        }
        return result;
    }

    public static boolean existsIn_result(LinkedList<Integer>result, Integer id){
        if (result.empty ())
            return false;
        result.findfirst();
        while (!result.last()) {
            if (result.retrieve().equals(id)) {
                return true;
            }
            result.findnext();
        }
        if (result.retrieve().equals(id))
            return true;

        return false;

    }
}