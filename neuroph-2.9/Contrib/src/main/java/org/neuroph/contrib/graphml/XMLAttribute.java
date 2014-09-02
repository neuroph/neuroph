package org.neuroph.contrib.graphml;

/**
 * XML Attribute. Contains id value pairs to characterize XML elements. 
 * 
 * @author fernando carrillo (fernando@carrillo.at)
 *
 */
public class XMLAttribute 
{
	private String id; 
	private String value; 
	
	public XMLAttribute( final String id, final String value ) {
		this.id = id; 
		this.value = value; 
	}
	
	public String toString() {
		return getId() + "=\"" + getValue() + "\"";  
	}
	
	//Getter 
	public String getId() { return id; } 
	public String getValue() { return value; } 
	
	public static void main(String[] args) {
		XMLAttribute attribute = new XMLAttribute( "id" , "testValue" ); 
		System.out.println( attribute ); 
	}
}
