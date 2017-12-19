package visitors;

import markdownSyntactic.IParser;
import markdownSyntactic.MarkdownTools;
import msi.gaml.compilation.ast.ISyntacticElement;
import msi.gaml.compilation.ast.ISyntacticElement.SyntacticVisitor;
/**
 * 
 * @author damienphilippon
 * Date : 19 Dec 2017
 * Class representing the visitor of ISyntacticElement representing a Reflex defined in GAML. This visitor
 * will generate the Markdown text for a visited IExpressionDescription for the Documentation
 */
public class VisitorReflexes implements SyntacticVisitor{
	/**
	 * Variable that will contain the markdown text generated by the visitor
	 */
	StringBuilder mDText;
	/**
	 * Variable that will tell whether or not it is the first reflex
	 */
	boolean first = true;
	
	/**
	 * Constructor of the visitor, does not expect anything
	 */
	public VisitorReflexes()
	{
	}
	/**
	 * Method to directly initialise the markdown text of the visitor, in order to let it add its generated text and return it to the model descriptor
	 * @param aBuilder {@code StringBuilder}, the StringBuilder of a model descriptor that will receive the generated text
	 */
	public void setText(StringBuilder aBuilder)
	{
		mDText=aBuilder;
	}
	/**
	 * Function that returns the StringBuilder of a VisitorReflexes once the visitor has done its job (adding text of a Reflex)
	 * @return {@code StringBuilder} the StringBuilder of a model descriptor 
	 */
	public StringBuilder getText()
	{
		return mDText;
	}

	/**
	 * Method to dispose all the objects that have been used by the VisitorReflexes and release memory
	 */
	public void dispose()
	{
		this.mDText=null;
	}
	/**
	 * Method used to visit a ISyntacticElement (expecting a Reflex here), generating the markdown Text of it
	 * @param element {@code ISyntacticElement}, the ISyntacticElement representing a Reflex that will be used to generate the markdown code
	 */
	public void visit(ISyntacticElement element) {
		if((element.getKeyword().equals(IParser.GAMA_KEYWORD_REFLEX))||(element.getKeyword().equals(IParser.GAMA_KEYWORD_INIT))||(element.getKeyword().equals(IParser.GAMA_KEYWORD_STATE)))
		{

			VisitorDebug.DEBUG("          doing reflex "+element.getName());
			//If it is the first reflex visited, we add the header of the table of reflexes
			if(first)
			{
				mDText.append(MarkdownTools.goBeginLine());
				mDText.append(MarkdownTools.goBeginLine());
				mDText.append("Type | Name ");
				mDText.append(MarkdownTools.goBeginLine());
				mDText.append("--- | --- ");
				first=false;
			}
			//We add ( reflex | name of the reflex ) in the table
			mDText.append(MarkdownTools.goBeginLine());
			mDText.append(element.getKeyword()+" | "+IParser.MARKDOWN_KEYWORD_SPACE+element.getName());
			mDText.append(MarkdownTools.addBr());
			//We add the commentaries provided by the comments in GAML
			mDText.append(MarkdownTools.addCode(MarkdownTools.getCommentsFromElement(element.getElement())));
		}
	}
}
