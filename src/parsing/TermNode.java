package parsing;

import grammar.Symbol;

/**
 * Created by po917265 on 7/31/17.
 */
public interface TermNode {

    /**
     * Returns the string value stored at this TermNode.
     * If the TermNode is a Token, returns the value of the token.
     * If the TermNode is a Rule, returns the non-terminal associated with the rule.
     * @return the value stored at this TermNode.
     */
    String value();

    /**
     * Returns the child at the specific index.
     * Will throw a {@code IndexOutOfBoundsException} if this is a Token.
     * @param index the index to lookup.
     * @return the child at the specific index.
     */
    TermNode getChild(int index);

    /**
     * Returns the type of the child at the specified index.
     * @param index an valid index.
     * @return the child at the specific index.
     */
    Symbol getType(int index);

    /**
     * Returns the number of children of this TermNode.
     * Tokens always return 0.
     * @return the number of children of this TermNode.
     */
    int numberOfChildren();
}
