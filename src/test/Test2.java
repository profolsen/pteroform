package test;

import parsing.Parser;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by po917265 on 7/10/17.
 */
public class Test2 {
    public static void main(String[] args) throws IOException
    {
        System.out.println(Bootstrap.grammar);
        Scanner scan = new Scanner(new File("bootstrap/bootstrap.ptero"));
        String input = "";
        Parser p = new Parser(Bootstrap.grammar);
        while(scan.hasNextLine()) {
            input += scan.nextLine() + "\n";
        }
        System.out.println(p.parse(input));
    }
}
