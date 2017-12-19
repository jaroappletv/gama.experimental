package visitors;

import markdownSyntactic.IParser;
import markdownSyntactic.MarkdownTools;
import msi.gaml.compilation.ast.ISyntacticElement;
import msi.gaml.compilation.ast.ISyntacticElement.SyntacticVisitor;

/**
 * 
 * @author damienphilippon
 * Date : 19 Dec 2017
 * Class representing the visitor of ISyntacticElement representing a micro species defined in GAML. This visitor
 * will generate the Markdown text for a visited IExpressionDescription for the Documentation
 */
public class VisitorMicroSpecies implements SyntacticVisitor{
	/**
	 * Variable that will contain the markdown text generated by the visitor
	 */
	StringBuilder mDText;
	
	/**
	 * Constructor of the visitor, does not expect anything
	 */
	public VisitorMicroSpecies()
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
	 * Function that returns the StringBuilder of a MicroSpeciesVisitor once the visitor has done its job (adding text of a micro species)
	 * @return {@code StringBuilder} the StringBuilder of a model descriptor 
	 */
	public StringBuilder getText()
	{
		return mDText;
	}
	/**
	 * Method to dispose all the objects that have been used by the VisitorMicroSpecies and release memory
	 */
	public void dispose()
	{
		this.mDText=null;
	}
	/**
	 * Method used to visit a ISyntacticElement (expecting micro species here), generating the markdown Text of it
	 * @param element {@code ISyntacticElement}, the ISyntacticElement representing a Micro species that will be used to generate the markdown code
	 */
	public void visit(ISyntacticElement element) {
		//Check that the element is a species to generate its text
		if(element.isSpecies())
		{

			VisitorDebug.DEBUG("          micro species : "+element.getName());
			mDText.append(MarkdownTools.goBeginLine());
			mDText.append(IParser.MARKDOWN_KEYWORD_LIST+IParser.MARKDOWN_KEYWORD_SPACE+MarkdownTools.addLink(element.getName(),"#"+element.getKeyword()+"-"+element.getName()));
		}
	}
	
		
}
