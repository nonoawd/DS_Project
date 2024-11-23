public class Rank {

    int id;
    int rank;

    public Rank(int i, int r) {
        id = i;
        rank = r;
    }

    public void displayRank(){
        System.out.printf("%-8d%-8d\n",id,rank);
    }

}
