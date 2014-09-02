package org.neuroph.contrib.graphml;

/**
 * 
 * XML Data. 
 * 
 * Holds two attributes:
 *  
 * 1. The id of the key this data is referring to. 
 * 2. The value for the referrenced attribute.  
 *    
 * 
 * @author fernando carrillo (fernando@carrillo.at)
 */
public class Data extends XMLElement {

	public Data( final String keyId, final String value ) {
		addAttribute( new XMLAttribute( "key", keyId) );
		addAttribute( new XMLAttribute( "value", value ) );
	}
	
	
	public String toString() {
		
		String out = getStartTag(); 
		out += getValue(); 
		out += getEndTag(); 
		
		return out; 
	}
	
	private String getStartTag() { return new String("<" + getTag() + " " + getAttributes().get( 0 ).toString() + ">"); }
	private String getValue() { return new String(getAttributes().get( 1 ).getValue()); } 
	private String getEndTag() { return new String("</" + getTag() + ">"); }
	
	@Override
	public String getTag() { return new String( "data" ); }
	
	public static void main(String[] args) {
		System.out.println( new Data( "d1", "0.0" ) ); 
	}

}
