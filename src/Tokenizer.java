import java.util.HashSet;

/**
 * Created by po917265 on 6/30/17.
 */
public class Tokenizer {

    private String source;
    private HashSet<Terminal> ignore;

    public Tokenizer(String source, HashSet<Terminal> ignore) {
        this.source = source;
        this.ignore = ignore;
        ignore();
    }

    public Token next(HashSet<Terminal> possibleParses, boolean use) {
        Token best = null;
        for(Terminal t : possibleParses) {
            Token hypothetical = t.parse(source);
            if(hypothetical != null && (best == null || hypothetical.value().length() > best.value().length())) {
                best = hypothetical;
            }
        }
        if(use && best != null) {
            source = source.substring(best.value().length()); //skip to the next begin spot.
            ignore();
        }
        return best;
    }

    public Token next(Terminal t, boolean use) {
        HashSet<Terminal> arg = new HashSet<Terminal>();
        arg.add(t);
        return next(arg, use);
    }

    public void ignore() {
        Token best = null;
        for(Terminal t : ignore) {
            Token hypothetical = t.parse(source);
            if(hypothetical != null && (best == null || hypothetical.value().length() > best.value().length())) {
                best = hypothetical;
            }
        }
        if(best != null) {
            source = source.substring(best.value().length()); //skip to the next begin spot.
        }
    }

    public String source() {
        return source;
    }
}
