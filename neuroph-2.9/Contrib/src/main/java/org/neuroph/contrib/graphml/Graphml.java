package org.neuroph.contrib.graphml;

/**
 * The parent XML element for a graphml document. 
 * 
 * Holds header containing namespace and schema links. 
 * 
 * Holds key definitions.
 * 
 * 
 * @author fernando carrillo (fernando@carrillo.at)
 *
 */
public class Graphml extends XMLElement {

	public Graphml() { 
		addAttribute( new XMLAttribute( "xmlns", getNameSpace() ) );
		addAttribute( new XMLAttribute( "xmlns:xsi", getXsiNameSpace() ) );
		addAttribute( new XMLAttribute( "xsi:schemaLocation", getXsiSchemaLocation() ) );
		
		appendChild( new Key( "d1", "edge", "weight", "double" ) );
	}
	
	private static String getNameSpace() { return new String("http://graphml.graphdrawing.org/xmlns"); }
	private static String getXsiNameSpace() { return new String( "http://www.w3.org/2001/XMLSchema-instance" ); }
	private static String getXsiSchemaLocation() { return new String( "http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd" ); }
	
	@Override
	public String getTag() { return "graphml"; }
}
