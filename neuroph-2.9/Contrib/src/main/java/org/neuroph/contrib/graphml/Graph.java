package org.neuroph.contrib.graphml;

import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;

/**
 * The parent XML element holding nodes and edges of a (sub)graph.  
 * 
 * Characterized by two attributes: 
 * 1. The Id of the graph. 
 * 2. The edgetype. For neural network representation directed is chosen. 
 * 
 * Contains all edges and nodes of the network as child elements. 
 * 
 * @author fernando carrillo (fernando@carrillo.at)
 *
 */
public class Graph extends XMLElement 
{
	public Graph( final String id )
	{
		addAttribute( new XMLAttribute( "id", id ) );
		addAttribute( new XMLAttribute( "edgedefault", "directed" ) );
	}
	
	/**
	 * Adds a neuroph neural network to the graphml reprentation. 
	 * @param ann
	 */
	public void addNetwork( final NeuralNetwork ann ) {
	
		for( int layer = 0; layer < ann.getLayersCount(); layer++ ) {
			for( Neuron neuron : ann.getLayerAt( layer ).getNeurons()  ) {
				addNode( neuron );
				addEdges( neuron );
			}
		}
	}
	
	/**
	 * Adds a child element of type node for the given neuron 
	 * @param neuron
	 */
	private void addNode( final Neuron neuron ) {
		appendChild( new Node( neuron.getLabel() ) );
	}
	
	/**
	 * Adds a child element of type edge for each connection in the given neuron. 
	 * @param neuron
	 */
	private void addEdges( final Neuron neuron ) {
		final String source = neuron.getLabel();
		
		String target; 
		String weight; 
		String weightKeyId = "d1"; 
		
		for( Connection con : neuron.getOutConnections() ) {
			target = con.getToNeuron().getLabel();
			weight = String.valueOf( con.getWeight() ); 
			
			appendChild( new Edge( source, target, weightKeyId, weight ) );
		}
	}
	
	
	public static void main(String[] args) 
	{
		Graph graph = new Graph( "graph1" );  
		System.out.println( graph ); 
	}
	
	public String getTag() { return "graph"; }
	
}
