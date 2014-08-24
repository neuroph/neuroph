package org.neuroph.contrib.graphml;

import java.util.ArrayList;

/**
 * XML element consisting of 
 * i) a tag
 * ii) a list of attributes
 * iii) a list of child elements. 
 * 
 * @author fernando carrillo (fernando@carrillo.at)
 *
 */
public abstract class XMLElement 
{ 
	private ArrayList<XMLAttribute> attributes = new ArrayList<XMLAttribute>();
	private ArrayList<XMLElement> childElements = new ArrayList<XMLElement>();
	
	
	/**
	 * Append child element. 
	 * @param child
	 */
	public void appendChild( final XMLElement child ) {
		this.childElements.add( child ); 
	}
	
	/**
	 * Add attribute. 
	 * @param attribute
	 */
	public void addAttribute( final XMLAttribute attribute ) {
		this.attributes.add( attribute ); 
	}
	
	@Override
	public String toString() {
		
		String out = getStartTag() + ">";
		if( getChildElements().size() != 0 )
		{
			out += "\n";
			out += getChildElementsString(); 		
		}
		
		out += getEndTag(); 
		
		return out; 
	}
	
	/**
	 * Returns XML start tag including all attributes. 
	 * @return
	 */
	private String getStartTag() { 
		return new String( "<" + getTag() + getAttributesString() ); 
	}
	
	/**
	 * Returns XML end tag. 
	 * @return
	 */
	private String getEndTag() { 
		return new String( "</" + getTag()  + ">" ); 
	}
	
	/**
	 * Returns all attribute fields separated by a single whitespace. 
	 * @return
	 */
	private String getAttributesString() {
		String out = ""; 
		for( XMLAttribute attribute : getAttributes() ) {
			out += " " + attribute.toString(); 
		}
		
		return out; 
	}
	
	/**
	 * Gets XML representation of child elements indented by 4 whitespace characters. 
	 * @return
	 */
	private String getChildElementsString() {
		String out = ""; 
		for( XMLElement child : getChildElements() ) {
			out += child.toString().replace("\n", "\n    ") + "\n"; 
		}
		
		return out; 
	}
		
	//Getter methods.
	public abstract String getTag();  
	public ArrayList<XMLAttribute> getAttributes() { return this.attributes; } 
	public ArrayList<XMLElement> getChildElements() { return this.childElements; } 
}
