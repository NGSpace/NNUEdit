package NNU.Editor.Tokenizers;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.fife.ui.rsyntaxtextarea.TokenTypes;

public class BrainFTokenizer extends ATokenizer {

	/**
	 * Returns a list of tokens representing the given text.
	 *
	 * @param text The text to break into tokens.
	 * @param startTokenType The token with which to start tokenizing.
	 * @param startOffset The offset at which the line of tokens begins.
	 * @return A linked list of tokens representing <code>text</code>.
	 */
	public Token getTokenList(Segment text, int startTokenType, int startOffset) {
		resetTokenList();
		
	    char[] array = text.array;
	    int offset = text.offset;
	    int count = text.count;
	    int end = offset + count;
	    int newStartOffset = startOffset - offset;

		for (int i=offset; i<end; i++) {
			char c = array[i];
			//System.out.print("p" + count + " " + c + "   " + i);

			int currentTokenType = TokenTypes.COMMENT_EOL;
            
			switch (c) {
				case '+':
				case '-':
					currentTokenType = TokenTypes.OPERATOR;
					break;
				default:
					currentTokenType = TokenTypes.COMMENT_EOL;
			}
			System.out.print(currentTokenType + " ");
		}
		System.out.println();
		addToken(text,0,end, TokenTypes.OPERATOR, newStartOffset);
		addNullToken();
		return firstToken;
	}

	@Override
	public TokenMap getWordsToHighlight() {
		return new TokenMap();
	}
}
