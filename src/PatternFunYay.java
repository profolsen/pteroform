import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by po917265 on 6/29/17.
 */
public class PatternFunYay {
    public static void main(String[] args) {
        String pattern = "^([0-9]*)";
        System.out.println(Pattern.matches(pattern, "45ggg"));
        Matcher m = Pattern.compile(pattern).matcher("45ggg");
        if(m.find()) {
            System.out.println(m.group(1));
        } else {
            System.out.println("Everyone should hate patterns");
        }
        MatchResult r = m.toMatchResult();
        System.out.println(r.start());
    }
}
