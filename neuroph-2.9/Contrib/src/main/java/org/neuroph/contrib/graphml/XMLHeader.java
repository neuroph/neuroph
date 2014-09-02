package org.neuroph.contrib.graphml;

/**
 * XML header, specifying the XML version and character encoding standard. 
 * 
 * Here XML version is set to 1.0 and encoding to UTF-8. 
 * 
 * @author fernando carrillo (fernando@carrillo.at)
 *
 */
public class XMLHeader 
{
	public static String getHeader() { 
		return getHeader("1.0", "UTF-8"); 
	}
	
	public static String getHeader( final String version, final String encoding ) { 
		String out = "<?xml"; 
		out += " " + new XMLAttribute("version", version );
		out += " " + new XMLAttribute("encoding", encoding ); 
		out += " ?>"; 
		return out;  
	}
}
