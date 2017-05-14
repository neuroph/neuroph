package org.neuroph.contrib.graphml;

/**
 * XML Schema key element.
 * 
 *  Holds 4 XML elements: 
 *  
 *  1. The id value for this key (referred to in data fields). 
 *  2. The for value, specifying for which XML element this key is valid
 *  3. The name of the attribute this key referes to. 
 *  4. The data type.   
 * 
 * @author fernando carrillo (fernando@carrillo.at)
 *
 */
public class Key extends XMLElement 
{
	public Key( final String idValue, final String forValue, 
			final String attrNameValue, final String attrTypeValue )
	{
		addAttribute( new XMLAttribute( "id", idValue ) );
		addAttribute( new XMLAttribute( "for", forValue ) );
		addAttribute( new XMLAttribute( "attr.name", attrNameValue ) );
		addAttribute( new XMLAttribute( "attr.type", attrTypeValue ) );
	}
	
	public String getTag() { return "key"; }
}
