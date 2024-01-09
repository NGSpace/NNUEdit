package NNU.Editor.Tokenizers;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.fife.ui.rsyntaxtextarea.TokenTypes;

public class TestTokenizer extends AbstractTokenMaker {

	public TestTokenizer() {}

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

	   // Token starting offsets are always of the form:
	   // 'startOffset + (currentTokenStart-offset)', but since startOffset and
	   // offset are constant, tokens' starting positions become:
	   // 'newStartOffset+currentTokenStart'.
	   int newStartOffset = startOffset - offset;

	   int currentTokenStart = offset;
	   int currentTokenType  = startTokenType;

	   for (int i=offset; i<end; i++) {

	      char c = array[i];

	      switch (currentTokenType) {

	         case TokenTypes.NULL:

	            currentTokenStart = i;   // Starting a new token here.

	            switch (c) {

	               case ' ':
	               case '\t':
	                  currentTokenType = TokenTypes.WHITESPACE;
	                  break;

	               case '"':
	                  currentTokenType = TokenTypes.LITERAL_STRING_DOUBLE_QUOTE;
	                  break;

	               case '#':
	                  currentTokenType = TokenTypes.COMMENT_EOL;
	                  break;

	               default:
	                  if (RSyntaxUtilities.isDigit(c)) {
	                     currentTokenType = TokenTypes.LITERAL_NUMBER_DECIMAL_INT;
	                     break;
	                  }
	                  else if (RSyntaxUtilities.isLetter(c) || c=='/' || c=='_') {
	                     currentTokenType = TokenTypes.IDENTIFIER;
	                     break;
	                  }
	                  
	                  // Anything not currently handled - mark as an identifier
	                  currentTokenType = TokenTypes.IDENTIFIER;
	                  break;

	            } // End of switch (c).

	            break;

	         case TokenTypes.WHITESPACE:

	            switch (c) {

	               case ' ':
	               case '\t':
	                  break;   // Still whitespace.

	               case '"':
	                  addToken(text, currentTokenStart,i-1, TokenTypes.WHITESPACE, newStartOffset+currentTokenStart);
	                  currentTokenStart = i;
	                  currentTokenType = TokenTypes.LITERAL_STRING_DOUBLE_QUOTE;
	                  break;

	               case '#':
	                  addToken(text, currentTokenStart,i-1, TokenTypes.WHITESPACE, newStartOffset+currentTokenStart);
	                  currentTokenStart = i;
	                  currentTokenType = TokenTypes.COMMENT_EOL;
	                  break;

	               default:   // Add the whitespace token and start anew.

	                  addToken(text, currentTokenStart,i-1, TokenTypes.WHITESPACE, newStartOffset+currentTokenStart);
	                  currentTokenStart = i;

	                  if (RSyntaxUtilities.isDigit(c)) {
	                     currentTokenType = TokenTypes.LITERAL_NUMBER_DECIMAL_INT;
	                     break;
	                  }
	                  else if (RSyntaxUtilities.isLetter(c) || c=='/' || c=='_') {
	                     currentTokenType = TokenTypes.IDENTIFIER;
	                     break;
	                  }

	                  // Anything not currently handled - mark as identifier
	                  currentTokenType = TokenTypes.IDENTIFIER;

	            } // End of switch (c).

	            break;

	         default: // Should never happen
	         case TokenTypes.IDENTIFIER:

	            switch (c) {

	               case ' ':
	               case '\t':
	                  addToken(text, currentTokenStart,i-1, TokenTypes.IDENTIFIER, newStartOffset+currentTokenStart);
	                  currentTokenStart = i;
	                  currentTokenType = TokenTypes.WHITESPACE;
	                  break;

	               case '"':
	                  addToken(text, currentTokenStart,i-1, TokenTypes.IDENTIFIER, newStartOffset+currentTokenStart);
	                  currentTokenStart = i;
	                  currentTokenType = TokenTypes.LITERAL_STRING_DOUBLE_QUOTE;
	                  break;

	               default:
	                  if (RSyntaxUtilities.isLetterOrDigit(c) || c=='/' || c=='_') {
	                     break;   // Still an identifier of some type.
	                  } // Otherwise, we're still an identifier (?).
	            } // End of switch (c).

	            break;

	         case TokenTypes.LITERAL_NUMBER_DECIMAL_INT:

	            switch (c) {

	               case ' ':
	               case '\t':
	                  addToken(text, currentTokenStart,i-1, TokenTypes.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
	                  currentTokenStart = i;
	                  currentTokenType = TokenTypes.WHITESPACE;
	                  break;

	               case '"':
	                  addToken(text, currentTokenStart,i-1, TokenTypes.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
	                  currentTokenStart = i;
	                  currentTokenType = TokenTypes.LITERAL_STRING_DOUBLE_QUOTE;
	                  break;

	               default:

	                  if (RSyntaxUtilities.isDigit(c)) {
	                     break;   // Still a literal number.
	                  }

	                  // Otherwise, remember this was a number and start over.
	                  addToken(text, currentTokenStart,i-1, TokenTypes.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
	                  i--;
	                  currentTokenType = TokenTypes.NULL;

	            } // End of switch (c).

	            break;

	         case TokenTypes.COMMENT_EOL:
	            i = end - 1;
	            addToken(text, currentTokenStart,i, currentTokenType, newStartOffset+currentTokenStart);
	            // We need to set token type to null so at the bottom we don't add one more TokenTypes.
	            currentTokenType = TokenTypes.NULL;
	            break;

	         case TokenTypes.LITERAL_STRING_DOUBLE_QUOTE:
	            if (c=='"') {
	               addToken(text, currentTokenStart,i, TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset+currentTokenStart);
	               currentTokenType = TokenTypes.NULL;
	            }
	            break;

	      } // End of switch (currentTokenType).

	   } // End of for (int i=offset; i<end; i++).

	   switch (currentTokenType) {

	      // Remember what token type to begin the next line with.
	      case TokenTypes.LITERAL_STRING_DOUBLE_QUOTE:
	         addToken(text, currentTokenStart,end-1, currentTokenType, newStartOffset+currentTokenStart);
	         break;

	      // Do nothing if everything was okay.
	      case TokenTypes.NULL:
	         addNullToken();
	         break;

	      // All other token types don't continue to the next line...
	      default:
	         addToken(text, currentTokenStart,end-1, currentTokenType, newStartOffset+currentTokenStart);
	         addNullToken();

	   }

	   // Return the first token in our linked list.
	   return firstToken;

	}
	
	@Override
	public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
	   // This assumes all keywords, etc. were parsed as "identifiers."
	   if (tokenType==TokenTypes.IDENTIFIER) {
	      int value = wordsToHighlight.get(segment, start, end);
	      if (value != -1) {
	         tokenType = value;
	      }
	   }
	   super.addToken(segment, start, end, tokenType, startOffset);
	}	

	@Override
	public TokenMap getWordsToHighlight() {
		TokenMap tokenMap = new TokenMap();
		   
		tokenMap.put("case",  TokenTypes.RESERVED_WORD);
		tokenMap.put("for",   TokenTypes.RESERVED_WORD);
		tokenMap.put("if",    TokenTypes.RESERVED_WORD);
		tokenMap.put("while", TokenTypes.RESERVED_WORD);
		
		
		tokenMap.put("printf", TokenTypes.FUNCTION);
		tokenMap.put("scanf",  40);//TokenTypes.FUNCTION);
		tokenMap.put("fopen",  TokenTypes.FUNCTION);
		   
		return tokenMap;
	}

}
