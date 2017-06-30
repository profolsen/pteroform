import java.util.ArrayList;

/**
 * Created by po917265 on 6/29/17.
 */
public class Term {
    private ArrayList<Term> children;
    private String head;

    public Term(String head) {
        this.head = head;
        children = new ArrayList<Term>();
    }

    public String toString() {
        String kids = "";
        for(int i = 0; i < children.size() - 1; i++) {
            kids += children.get(i) + ",";
        }
        if(children.size() > 0) {
            kids += children.get(children.size() - 1);
        }
        return "(" + head + "," + kids + ")";
    }

    public void addChild(String value) {

    }
}
