package NNU.Editor.Tokenizers;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;

public abstract class ATokenizer extends AbstractTokenMaker {
	 
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
}