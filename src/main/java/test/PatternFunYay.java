package test;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by po917265 on 6/29/17.
 *//*
MIT License

Copyright (c) 2017 Paul Olsen

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
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
        //matches two '/' in a row.
        //"^(?!.*?(/){2}).*"
        //matches a single '/'.
        //"(?<!/)/(?!/)"
        //combination of the previous two:
        //"^(?!.*?((?<!/)/(?!/))).*"
        //matches / any string as long as it doesn't have a / or a newline in it /.
        //"^(?<!/)/(?!/).*(?<!/)/(?!/)"

        Pattern p = Pattern.compile("^(?<!/)/(?!/).*(?<!/)/(?!/)");
        Matcher test1 = p.matcher("/basic test/");
        Matcher test2 = p.matcher("/test with escaped // in it/ and then more crap.");
        Matcher test3 = p.matcher("/no \n// at all in this one./");
        System.out.println("------");
        if(test1.find()) {
            System.out.println(test1.start() + ", " + test1.end() + ", " + test1.group());
        } else {
            System.out.println("NO!");
        }
        if(test2.find()) {
            System.out.println(test2.start() + ", " + test2.end() + ", " + test2.group());
        } else {
            System.out.println("NO!");
        }
        if(test3.find()) {
            System.out.println(test3.start() + ", " + test3.end() + ", " + test3.group());
        } else {
            System.out.println("NO!");
        }
    }
}
