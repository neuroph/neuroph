/*
 * This class is based on code from PlugableRendererDemo from JUNG framework
 *
 * Copyright by Neuroph Project (c) 2004, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 *
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 *
 * Authors: Danyel Fisher, Joshua O'Madadhain, Tom Nelson
 * Created on Nov 7, 2004
 *
 * Zoran Sevarac
 */

package org.neuroph.netbeans.main.easyneurons;

import org.neuroph.core.*;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.netbeans.main.easyneurons.view.RowLayersLayout;
import org.neuroph.netbeans.main.easyneurons.view.SquareLayersLayout;
import org.neuroph.netbeans.main.easyneurons.view.KohonenLayout;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.functors.MapTransformer;

import edu.uci.ics.jung.algorithms.generators.random.MixedRandomGraphGenerator;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;


import edu.uci.ics.jung.algorithms.scoring.VoltageScorer;
import edu.uci.ics.jung.algorithms.scoring.util.VertexScoreTransformer;
import edu.uci.ics.jung.algorithms.util.SelfLoopEdgePredicate;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.GradientEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.NumberFormattingTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Tree;


import java.text.NumberFormat;
import java.text.DecimalFormat;
import org.neuroph.netbeans.main.easyneurons.view.NetworkLayout;


/**
 * @author Zoran Sevarac
 */
public class NeuralGraphRenderer implements ActionListener {
	protected JCheckBox v_color;
	protected JCheckBox e_color;
	protected JCheckBox v_stroke;
	protected JCheckBox e_uarrow_pred;
	protected JCheckBox e_darrow_pred;
	protected JCheckBox v_shape;
	protected JCheckBox v_size;
	protected JCheckBox v_aspect;
	protected JCheckBox v_labels;
	protected JRadioButton e_line;
	protected JRadioButton e_bent;
	protected JRadioButton e_wedge;
	protected JRadioButton e_quad;
	protected JRadioButton e_cubic;
	protected JCheckBox e_labels;
	protected JCheckBox font;
	protected JCheckBox e_show_d;
	protected JCheckBox e_show_u;
	protected JCheckBox v_small;
	protected JCheckBox zoom_at_mouse;
	protected JCheckBox fill_edges;

	protected JRadioButton layout_FR;
	protected JRadioButton layout_KK;
	protected JRadioButton layout_Circle;
	protected JComboBox layoutCombo;

	protected JRadioButton no_gradient;
	// protected JRadioButton gradient_absolute;
	protected JRadioButton gradient_relative;

	protected static final int GRADIENT_NONE = 0;
	protected static final int GRADIENT_RELATIVE = 1;
	// protected static final int GRADIENT_ABSOLUTE = 2;
	protected static int gradient_level = GRADIENT_NONE;

        protected SeedFillColor<Neuron> seedFillColor;
        protected SeedDrawColor<Neuron> seedDrawColor;
        protected EdgeWeightStrokeFunction<Connection> ewcs;
        protected VertexStrokeHighlight<Neuron,Connection> vsh;
        protected Transformer<Neuron,String> vs;
        protected Transformer<Neuron,String> vs_none;
        protected Transformer<Connection,String> es;
        protected Transformer<Connection,String> es_none;
        protected VertexFontTransformer<Neuron> vff;
        protected EdgeFontTransformer<Connection> eff;
        protected VertexShapeSizeAspect<Neuron,Connection> vssa;
        protected DirectionDisplayPredicate<Neuron,Connection> show_edge;
        protected DirectionDisplayPredicate<Neuron,Connection> show_arrow;
        protected VertexDisplayPredicate<Neuron,Connection> show_vertex;
        protected Predicate<Context<Graph<Neuron,Connection>,Connection>> self_loop;
        protected GradientPickedEdgePaintFunction<Neuron,Connection> edgeDrawPaint;
        protected GradientPickedEdgePaintFunction<Neuron,Connection> edgeFillPaint;


	protected final static Object ACTIVATION_KEY = "neuron_activation";
	protected final static Object WEIGHT_KEY = "connection_weight";

	protected final static Object TRANSPARENCY = "transparency";

	protected VisualizationViewer vv;
	protected DefaultModalGraphMouse<Neuron, Connection> graphMouse;
        protected Set<Neuron> seedVertices = new HashSet<Neuron>();
	//protected EditingModalGraphMouse<Neuron, Connection> graphMouse;

	protected Transformer affineTransformer;
        protected Transformer<Neuron, Double> voltages;

        protected Map<Neuron,Connection> transparency = new HashMap<Neuron,Connection>();
        protected Map<Connection,Number> edge_weight = new HashMap<Connection,Number>();

	protected Graph<Neuron, Connection> graph;
	protected NeuralNetwork neuralNet;
	protected JPanel graphPanel;

	protected Layout<Neuron, Connection> layout;
        protected Transformer<Neuron, Point2D> staticTranformer;

        private NetworkLayout netLayout;


	public NeuralGraphRenderer(NeuralNetwork neuralNet) {
		this.neuralNet = neuralNet;

		v_labels = new JCheckBox("show activation levels");
		v_labels.addActionListener(this);

		e_labels = new JCheckBox("show weights");
		e_labels.addActionListener(this);

                staticTranformer = new Transformer<Neuron, Point2D>() {
                    int layerY = 70;

                    @Override
                    public Point2D transform(Neuron n) {
                        int x, y;

                        Dimension size = layout.getSize();
                        x = size.width / 2;
                        y = layerY;

                        Layer parentLayer = n.getParentLayer();
                        int parentLayerSize = parentLayer.getNeuronsCount();
                        int neuronIdx = parentLayer.indexOf(n);
                        int layerIdx = parentLayer.getParentNetwork().indexOf(parentLayer);
                        int layerLayout = netLayout.getLayerLayout(layerIdx);

                        y  = (layerIdx+1)*70;

                        if (layerLayout == NetworkLayout.ROW_LAYOUT) {
                            if (parentLayerSize%2 !=0 ) { // neparni broj neurona
                                if (neuronIdx <= (parentLayerSize/2) ) {
                                   x = size.width / 2 - 70 * ((parentLayerSize/2) - neuronIdx) - 35;
                                } else {
                                   x = size.width / 2 + 70 * (neuronIdx - (parentLayerSize/2)-1) + 35;
                                }
                            } else { // parni broj neurona
                                if (neuronIdx < (parentLayerSize/2) ) {
                                   x = size.width / 2 - 70 * ((parentLayerSize/2) - neuronIdx);
                                } else {
                                   x = size.width / 2 + 70 * (neuronIdx - (parentLayerSize/2));
                                }
                            }

                           if (y>layerY) layerY=y;

                        } else { // NetworkLayout.SQUARE_LAYOUT
                            int rowNeurons = (int)Math.sqrt(parentLayerSize);
                            y = layerY + ((neuronIdx/rowNeurons)+1)* 70;

                            if (rowNeurons%2 !=0 ) { // neparni broj neurona u redu
                                if (((neuronIdx%rowNeurons)%2 ) <= (rowNeurons/2) ) {
                                   x = size.width / 2 - 70 * ((rowNeurons/2) - (neuronIdx%rowNeurons)) -35;
                                } else {
                                   x = size.width / 2 + 70 * ((rowNeurons/2) - (neuronIdx%rowNeurons)) -35;
                                }
                            } else { // parni broj neurona
                                if (((neuronIdx%rowNeurons)%2 ) <= (rowNeurons/2) ) {
                                   x = size.width / 2 - 70 * ((rowNeurons/2) - (neuronIdx%rowNeurons));
                                } else {
                                   x = size.width / 2 + 70 * ((rowNeurons/2) - (neuronIdx%rowNeurons));
                                }
                            }
                        }

                        Point2D neuronLocation = new Point(x, y);
                        return neuronLocation;
                    }
                };

		graph = createGraph();
                layout = new StaticLayout<Neuron, Connection>(graph, staticTranformer);

                try {
		vv = new VisualizationViewer<Neuron, Connection>(layout);
                } catch(Exception ex) {
                  ex.printStackTrace();
                }
		createGraphPanel();
	}

	public JPanel getGraphPanel() {
		return this.graphPanel;
	}

	public void createGraphPanel() {
		graph = createGraph();

                setNetLayout(neuralNet.getNetworkType());

               //layout = new FRLayout<Neuron, Connection>(graph);
                layout = new StaticLayout<Neuron, Connection>(graph, staticTranformer);
		vv = new VisualizationViewer<Neuron, Connection>(layout);
		// add Shape based pick support
//		vv.setPickSupport(new ShapePickSupport());
//		PickedState picked_state = vv.getPickedState();
                PickedState<Neuron> picked_state = vv.getPickedVertexState();

//		affineTransformer = vv.getLayoutTransformer();

		// create decorators
                seedFillColor = new SeedFillColor<Neuron>(picked_state);
                seedDrawColor = new SeedDrawColor<Neuron>(picked_state);
                ewcs =
                    new EdgeWeightStrokeFunction<Connection>(edge_weight);
                vsh = new VertexStrokeHighlight<Neuron,Connection>(graph, picked_state);
                vff = new VertexFontTransformer<Neuron>();
                eff = new EdgeFontTransformer<Connection>();
                vs_none = new ConstantTransformer(null);
                es_none = new ConstantTransformer(null);
                vssa = new VertexShapeSizeAspect<Neuron,Connection>(graph, voltages);
                show_edge = new DirectionDisplayPredicate<Neuron,Connection>(true, true);
                show_arrow = new DirectionDisplayPredicate<Neuron,Connection>(true, false);
                show_vertex = new VertexDisplayPredicate<Neuron,Connection>(false);

                vv.getRenderContext().setVertexFontTransformer(vff);
                vv.getRenderContext().setVertexShapeTransformer(vssa);
                vv.getRenderContext().setVertexStrokeTransformer(vsh);

                vv.getRenderContext().setEdgeFontTransformer(eff);
                vv.getRenderContext().setEdgeStrokeTransformer(ewcs);

                vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Neuron,Connection>());

                this.graphPanel = new JPanel();
		graphPanel.setLayout(new BorderLayout());

		vv.setBackground(Color.white);

                GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(vv);
		scrollPane.setPreferredSize(new Dimension(500, 400));
		graphPanel.add(scrollPane);

//        Factory<Neuron> vertexFactory = new VertexFactory();
//        Factory<Connection> edgeFactory = new EdgeFactory();
//
//        graphMouse =  new EditingModalGraphMouse(vv.getRenderContext(),
//                                                 vertexFactory, edgeFactory);

        graphMouse = new DefaultModalGraphMouse<Neuron, Connection>();
        vv.setGraphMouse(graphMouse);
        graphMouse.add(new PopupGraphMousePlugin());

        vssa.setScaling(false);

        vv.setVertexToolTipTransformer(new VoltageTips<Number>());
        addControls(graphPanel);
	}

	/**
	 * Generates a mixed-mode random graph, runs VoltageRanker on it, and
	 * returns the resultant graph.
	 */
	public Graph createGraph() {

		Graph<Neuron, Connection> g = new DirectedSparseGraph<Neuron, Connection>();

//		Iterator<Layer> li = this.neuralNet.getLayersIterator();
//		while (li.hasNext()) {
                for(Layer layer : this.neuralNet.getLayers()) {
//			Layer layer = li.next();
//			Iterator<Neuron> ni = layer.getNeuronsIterator();
//			while (ni.hasNext()) {
//				Neuron neuron = ni.next();
                        for(Neuron neuron : layer.getNeurons()) {
				g.addVertex(neuron);

//				Iterator<Connection> ci = neuron.getInputConnections().iterator();
//				while (ci.hasNext()) {
//					Connection connection = ci.next();
                                for(Connection connection : neuron.getInputConnections()) {
					Neuron fromNeuron = connection.getFromNeuron(); // .getConnectedNeuron();
					g.addEdge(connection, fromNeuron, neuron);

                                        // used for highlighting weights
                                        edge_weight.put(connection, connection.getWeight().getValue());
				}
			}
		}

                voltages = new Transformer<Neuron, Double>() {
                              @Override
                              public Double transform(Neuron n) {
                                return n.getOutput();
                            }
                };

                vs = new Transformer<Neuron, String>() {
                            @Override
                            public String transform(Neuron n) {
                                NumberFormat numberFormat = DecimalFormat.getNumberInstance();
                                numberFormat.setMaximumFractionDigits(4);

                                return numberFormat.format(n.getOutput());
                            } };

                es = new Transformer<Connection, String>() {
                            @Override
                            public String transform(Connection c) {
                                NumberFormat numberFormat = DecimalFormat.getNumberInstance();
                                numberFormat.setMaximumFractionDigits(5);

                                return numberFormat.format(c.getWeight().getValue());
                            } };

		return g;
	}

	/**
	 * @param jp
	 *            panel to which controls will be added
	 */
	protected void addControls(final JPanel jp) {
		final JPanel control_panel = new JPanel();
		jp.add(control_panel, BorderLayout.EAST);

		control_panel.setLayout(new GridBagLayout());

		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

		// control_panel.setBackground(new Color(00, 255, 00));

		final Box vertex_panel = Box.createVerticalBox();
		vertex_panel.setBorder(BorderFactory.createTitledBorder("Neurons"));
		final Box edge_panel = Box.createVerticalBox();
		edge_panel.setBorder(BorderFactory.createTitledBorder("Connections"));

		final Box edges_and_vertex_panel = Box.createVerticalBox();
		// Box zoom_and_mode_panel = Box.createVerticalBox();
		// zoom_and_mode_panel.setBorder(BorderFactory.createTitledBorder("Other controls"));
//		final Box layout_panel = Box.createVerticalBox();
//		layout_panel.setBorder(BorderFactory.createTitledBorder("Layout"));
		// JPanel zoom_and_mode_panel = new JPanel();
		// zoom_and_mode_panel.setLayout(new GridBagLayout() );
		// //GridLayout(3,1)
		// zoom_and_mode_panel.setBackground(new Color(0,255,0));

		edges_and_vertex_panel.add(vertex_panel);
		edges_and_vertex_panel.add(edge_panel);
		// edges_and_vertex_panel.add(layout_panel);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		control_panel.add(edges_and_vertex_panel, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
//		control_panel.add(layout_panel, gridBagConstraints);

		// originalni layout
		// control_panel.add(vertex_panel, BorderLayout.WEST);
		// control_panel.add(edge_panel, BorderLayout.EAST);
		// control_panel.add(edges_and_vertex_panel, BorderLayout.CENTER);

		// set up vertex controls
		v_color = new JCheckBox("vertex seed coloring");
		v_color.addActionListener(this);
//		v_stroke = new JCheckBox("<html>stroke highlight selected</html>");
//		v_stroke.addActionListener(this);

		v_shape = new JCheckBox("vertex degree shapes");
		v_shape.addActionListener(this);
		v_size = new JCheckBox("activation size");
		v_size.addActionListener(this);
		v_size.setSelected(false);
		v_aspect = new JCheckBox("vertex degree ratio stretch");
		v_aspect.addActionListener(this);
		v_small = new JCheckBox("filter vertices of degree < "
				+ VertexDisplayPredicate.MIN_DEGREE);
		v_small.addActionListener(this);

		// vertex_panel.add(v_color);

		vertex_panel.add(v_labels);
		// vertex_panel.add(v_shape);
		vertex_panel.add(v_size);
		// vertex_panel.add(v_aspect);
		// vertex_panel.add(v_small);
		// vertex_panel.add(v_stroke);

		// set up edge controls
		// JPanel gradient_panel = new JPanel(new GridLayout(1, 0));
		// gradient_panel.setBorder(BorderFactory.createTitledBorder("Edge paint"));
		// no_gradient = new JRadioButton("Solid color");
		// no_gradient.addActionListener(this);
		// no_gradient.setSelected(true);
		// // gradient_absolute = new JRadioButton("Absolute gradient");
		// // gradient_absolute.addActionListener(this);
		// gradient_relative = new JRadioButton("Gradient");
		// gradient_relative.addActionListener(this);
		// ButtonGroup bg_grad = new ButtonGroup();
		// bg_grad.add(no_gradient);
		// bg_grad.add(gradient_relative);
		// //bg_grad.add(gradient_absolute);
		// gradient_panel.add(no_gradient);
		// //gradientGrid.add(gradient_absolute);
		// gradient_panel.add(gradient_relative);

//		JPanel shape_panel = new JPanel(new GridLayout(3, 2));
//		shape_panel.setBorder(BorderFactory
//				.createTitledBorder("Connection shape"));
//		e_line = new JRadioButton("line");
//		e_line.addActionListener(this);
//		e_line.setSelected(true);
//		// e_bent = new JRadioButton("bent line");
//		// e_bent.addActionListener(this);
//		e_wedge = new JRadioButton("wedge");
//		e_wedge.addActionListener(this);
//		e_quad = new JRadioButton("quad curve");
//		e_quad.addActionListener(this);
//		e_cubic = new JRadioButton("cubic curve");
//		e_cubic.addActionListener(this);
//		ButtonGroup bg_shape = new ButtonGroup();
//		bg_shape.add(e_line);
//		// bg.add(e_bent);
//		bg_shape.add(e_wedge);
//		bg_shape.add(e_quad);
//		bg_shape.add(e_cubic);
//		shape_panel.add(e_line);
//		// shape_panel.add(e_bent);
//		// shape_panel.add(e_wedge);
//		shape_panel.add(e_quad);
//		shape_panel.add(e_cubic);
//		// fill_edges = new JCheckBox("fill edge shapes");
//		// fill_edges.setSelected(false);
//		// fill_edges.addActionListener(this);
//		// shape_panel.add(fill_edges);
//		shape_panel.setOpaque(true);
		e_color = new JCheckBox("weight highlighting");
		e_color.addActionListener(this);
		e_uarrow_pred = new JCheckBox("undirected");
		e_uarrow_pred.addActionListener(this);
		e_darrow_pred = new JCheckBox("directed");
		e_darrow_pred.addActionListener(this);
		e_darrow_pred.setSelected(true);
		// JPanel arrow_panel = new JPanel(new GridLayout(1,0));
		// arrow_panel.setBorder(BorderFactory.createTitledBorder("Show arrows"));
		// arrow_panel.add(e_uarrow_pred);
		// arrow_panel.add(e_darrow_pred);

		e_show_d = new JCheckBox("directed");
		e_show_d.addActionListener(this);
		e_show_d.setSelected(true);
		e_show_u = new JCheckBox("undirected");
		e_show_u.addActionListener(this);
		e_show_u.setSelected(true);
		JPanel show_edge_panel = new JPanel(new GridLayout(1, 0));
		show_edge_panel.setBorder(BorderFactory
				.createTitledBorder("Show edges"));
		show_edge_panel.add(e_show_u);
		show_edge_panel.add(e_show_d);

//		shape_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
//		edge_panel.add(shape_panel);
		// gradient_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		// edge_panel.add(gradient_panel);
		// show_edge_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		// edge_panel.add(show_edge_panel);
		// arrow_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		// edge_panel.add(arrow_panel);

		e_color.setAlignmentX(Component.LEFT_ALIGNMENT);
		edge_panel.add(e_color);
		e_labels.setAlignmentX(Component.LEFT_ALIGNMENT);
		edge_panel.add(e_labels);

//		Class<?>[] combos = getLayoutOptions();
//		final JComboBox layoutCombo = new JComboBox(combos);
//		// use a renderer to shorten the layout name presentation
//		layoutCombo.setRenderer(new DefaultListCellRenderer() {
//			public Component getListCellRendererComponent(JList list,
//					Object value, int index, boolean isSelected,
//					boolean cellHasFocus) {
//				String valueString = value.toString();
//				valueString = valueString.substring(valueString
//						.lastIndexOf('.') + 1);
//				return super.getListCellRendererComponent(list, valueString,
//						index, isSelected, cellHasFocus);
//			}
//		});
//		layoutCombo.addActionListener(new LayoutChooser(layoutCombo, vv));
//// UNDO:		layoutCombo.setSelectedItem(FRLayout.class);
//		layout_panel.add(layoutCombo);

		// set up zoom controls
		zoom_at_mouse = new JCheckBox(
				"<html><center>zoom at mouse<p>(wheel only)</center></html>");
		zoom_at_mouse.addActionListener(this);

		final ScalingControl scaler = new CrossoverScalingControl();

		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1.1f, vv.getCenter());
			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1 / 1.1f, vv.getCenter());
			}
		});

		JPanel zoomPanel = new JPanel();
		zoomPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		zoomPanel.setBorder(BorderFactory.createTitledBorder("Zoom"));
		zoomPanel.add(plus);
		zoomPanel.add(minus);

		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;

		JComboBox modeBox = graphMouse.getModeComboBox();
		modeBox.setAlignmentX(Component.RIGHT_ALIGNMENT);

		JPanel modePanel = new JPanel(new BorderLayout()) {
			@Override
			public Dimension getMaximumSize() {
				return getPreferredSize();
			}
		};
		modePanel.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));
		modePanel.add(modeBox);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		control_panel.add(modePanel, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		control_panel.add(zoomPanel, gridBagConstraints);

		// add font control to center panel
		font = new JCheckBox("bold text");
		font.addActionListener(this);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		control_panel.add(font, gridBagConstraints);
	}

	private void setNetLayout(NeuralNetworkType type) {

                if (type == null){
                    this.netLayout = new RowLayersLayout();
                    return;
                }

		switch (type) {
		case ADALINE:
			this.netLayout = new RowLayersLayout();
			break;
		case PERCEPTRON:
			this.netLayout = new RowLayersLayout();
			break;
		case MULTI_LAYER_PERCEPTRON:
			this.netLayout = new RowLayersLayout();
			break;
		case HOPFIELD:
			this.netLayout = new SquareLayersLayout();
			break;
		case KOHONEN:
			this.netLayout = new KohonenLayout();
			break;
		case NEURO_FUZZY_REASONER:
			this.netLayout = new RowLayersLayout();
			break;

		default:
			this.netLayout = new RowLayersLayout();
			break;
		}
	}

	private static Class<?>[] getLayoutOptions() {
		List<Class<?>> layouts = new ArrayList<Class<?>>();
		layouts.add(DAGLayout.class);
                layouts.add(KKLayout.class);
		layouts.add(FRLayout.class);
		layouts.add(CircleLayout.class);
                layouts.add(TreeLayout.class);
		return layouts.toArray(new Class[0]);
	}

        private final static class VertexFontTransformer<V>
            implements Transformer<V,Font>
        {
            protected boolean bold = false;
            Font f = new Font("Helvetica", Font.PLAIN, 12);
            Font b = new Font("Helvetica", Font.BOLD, 12);

            public void setBold(boolean bold)
            {
                this.bold = bold;
            }

            @Override
            public Font transform(V v)
            {
                if (bold)
                    return b;
                else
                    return f;
            }
        }

        private final static class EdgeFontTransformer<E>
            implements Transformer<E,Font>
        {
        protected boolean bold = false;
        Font f = new Font("Helvetica", Font.PLAIN, 12);
        Font b = new Font("Helvetica", Font.BOLD, 12);

        public void setBold(boolean bold)
        {
            this.bold = bold;
        }

        @Override
        public Font transform(E e)
        {
            if (bold)
                return b;
            else
                return f;
        }
    }


        @Override
	public void actionPerformed(ActionEvent e) {
            AbstractButton source = (AbstractButton)e.getSource();

            if (source == e_line)
            {
                if(source.isSelected())
                {
                    vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Neuron,Connection>());
                }
            }
            else if (source == e_quad)
            {
                if(source.isSelected())
                {
                    vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<Neuron,Connection>());
                }
            }
            else if (source == e_cubic)
            {
                if(source.isSelected())
                {
                    vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.CubicCurve<Neuron,Connection>());
                }
            }
            else if (source == v_labels)
            {
                if (source.isSelected())
                    vv.getRenderContext().setVertexLabelTransformer(vs);
                else
                    vv.getRenderContext().setVertexLabelTransformer(vs_none);
            }
            else if (source == e_labels)
            {
                if (source.isSelected())
                    vv.getRenderContext().setEdgeLabelTransformer(es);
                else
                    vv.getRenderContext().setEdgeLabelTransformer(es_none);
            }
            else if (source == font)
            {
                vff.setBold(source.isSelected());
                eff.setBold(source.isSelected());
            }
            else if (source == e_color)
            {
                ewcs.setWeighted(source.isSelected());
            }
            else if (source == v_size)
            {
                vssa.setScaling(source.isSelected());
            }
            else if (source == v_stroke)
            {
                vsh.setHighlight(source.isSelected());
            }
            vv.repaint();
        }

   /**
     * a GraphMousePlugin that offers popup
     * menu support
     */
    protected class PopupGraphMousePlugin extends AbstractPopupGraphMousePlugin
    	implements MouseListener {

        public PopupGraphMousePlugin() {
            this(MouseEvent.BUTTON3_MASK);
        }
        public PopupGraphMousePlugin(int modifiers) {
            super(modifiers);
        }

        /**
         * If this event is over a Vertex, pop up a menu to
         * allow the user to increase/decrease the voltage
         * attribute of this Vertex
         * @param e
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void handlePopup(MouseEvent e) {
            final VisualizationViewer<Neuron,Connection> vv =
                (VisualizationViewer<Neuron,Connection>)e.getSource();
            Point2D p = e.getPoint();//vv.getRenderContext().getBasicTransformer().inverseViewTransform(e.getPoint());

            GraphElementAccessor<Neuron,Connection> pickSupport = vv.getPickSupport();
//            if(pickSupport != null) {
//                final Neuron v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
//                if(v != null) {
//                    JPopupMenu popup = new JPopupMenu();
//                    popup.add(new AbstractAction("Delete Neuron") {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
////                        	Double value = Math.min(1, transparency.get(v).doubleValue()+0.1);
////                        	transparency.put(v, value);
////                        	transparency.put(v, )transparency.get(v);
////                            MutableDouble value = (MutableDouble)transparency.getNumber(v);
////                            value.setDoubleValue(Math.min(1, value.doubleValue() + 0.1));
//                            vv.repaint();
//                        }
//                    });
//                    popup.add(new AbstractAction("Neuron Properties"){
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
////                        	Double value = Math.max(0,
////                            		transparency.get(v).doubleValue()-0.1);
////                            	transparency.put(v, value);
////                            MutableDouble value = (MutableDouble)transparency.getNumber(v);
////                            value.setDoubleValue(Math.max(0, value.doubleValue() - 0.1));
//                            vv.repaint();
//                        }
//                    });
//                    popup.show(vv, e.getX(), e.getY());
//                } else {
//                    final Connection edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
//                    if(edge != null) {
//                        JPopupMenu popup = new JPopupMenu();
//                        popup.add(new AbstractAction(edge.toString()) {
//                            @Override
//                            public void actionPerformed(ActionEvent e) {
//                                System.err.println("got "+edge);
//                            }
//                        });
//                        popup.show(vv, e.getX(), e.getY());
//
//                    }
//                }
//            }
        }
    }

    /**
     * Controls the shape, size, and aspect ratio for each vertex.
     *
     * @author Joshua O'Madadhain
     */
    private final static class VertexShapeSizeAspect<V,E>
    extends AbstractVertexShapeTransformer <V>
    implements Transformer<V,Shape>  {

        protected boolean stretch = false;
        protected boolean scale = false;
        protected boolean funny_shapes = false;
        protected Transformer<V,Double> voltages;
        protected Graph<V,E> graph;
//        protected AffineTransform scaleTransform = new AffineTransform();

        public VertexShapeSizeAspect(Graph<V,E> graphIn, Transformer<V,Double> voltagesIn)
        {
        	this.graph = graphIn;
            this.voltages = voltagesIn;
            setSizeTransformer(new Transformer<V,Integer>() {
                                @Override
				public Integer transform(V v) {
                                    if (scale)
                                        return (int)(voltages.transform(v) * 30) + 20;
                                    else
                                        return 20;
				}});

            setAspectRatioTransformer(new Transformer<V,Float>() {
                                @Override
				public Float transform(V v) {
                                    if (stretch) {
                                        return (float)(graph.inDegree(v) + 1) /
                                                (graph.outDegree(v) + 1);
                                    } else {
                                        return 1.0f;
                                    }
				}});
               }

		public void setStretching(boolean stretch)
                {
                    this.stretch = stretch;
                }

        public void setScaling(boolean scale)
        {
            this.scale = scale;
        }

        public void useFunnyShapes(boolean use)
        {
            this.funny_shapes = use;
        }

        @Override
        public Shape transform(V v)
        {
            if (funny_shapes)
            {
                if (graph.degree(v) < 5)
                {
                    int sides = Math.max(graph.degree(v), 3);
                    return factory.getRegularPolygon(v, sides);
                }
                else
                    return factory.getRegularStar(v, graph.degree(v));
            }
            else
                return factory.getEllipse(v);
        }
    }

    private final class SeedFillColor<V> implements Transformer<V,Paint>
    {
        protected PickedInfo<V> pi;
        protected final static float dark_value = 0.8f;
        protected final static float light_value = 0.2f;
        protected boolean seed_coloring;

        public SeedFillColor(PickedInfo<V> pi)
        {
            this.pi = pi;
            seed_coloring = false;
        }

        public void setSeedColoring(boolean b)
        {
            this.seed_coloring = b;
        }

//        public Paint getDrawPaint(V v)
//        {
//            return Color.BLACK;
//        }

        @Override
        public Paint transform(V v)
        {
            float alpha = (float)transparency.get(v).getWeight().getValue();
            if (pi.isPicked(v))
            {
                return new Color(1f, 1f, 0, alpha);
            }
            else
            {
                if (seed_coloring && seedVertices.contains(v))
                {
                    Color dark = new Color(0, 0, dark_value, alpha);
                    Color light = new Color(0, 0, light_value, alpha);
                    return new GradientPaint( 0, 0, dark, 10, 0, light, true);
                }
                else
                    return new Color(1f, 0, 0, alpha);
            }

        }
    }

    private final static class EdgeWeightStrokeFunction<E>
    implements Transformer<E,Stroke>
    {
        protected static final Stroke basic = new BasicStroke(1);
        protected static final Stroke heavy = new BasicStroke(2);
        protected static final Stroke dotted = RenderContext.DOTTED;

        protected boolean weighted = false;
        protected Map<E,Number> edge_weight;

        public EdgeWeightStrokeFunction(Map<E,Number> edge_weight)
        {
            this.edge_weight = edge_weight;
        }

        public void setWeighted(boolean weighted)
        {
            this.weighted = weighted;
        }

        @Override
        public Stroke transform(E e)
        {
            if (weighted)
            {
                if (drawHeavy(e))
                    return heavy;
                else
                    return dotted;
            }
            else
                return basic;
        }

        protected boolean drawHeavy(E e)
        {
            double value = edge_weight.get(e).doubleValue();
            if (value > 0.7)
                return true;
            else
                return false;
        }

    }

    private final static class VertexStrokeHighlight<V,E> implements
    Transformer<V,Stroke>
    {
        protected boolean highlight = false;
        protected Stroke heavy = new BasicStroke(5);
        protected Stroke medium = new BasicStroke(3);
        protected Stroke light = new BasicStroke(1);
        protected PickedInfo<V> pi;
        protected Graph<V,E> graph;

        public VertexStrokeHighlight(Graph<V,E> graph, PickedInfo<V> pi)
        {
        	this.graph = graph;
            this.pi = pi;
        }

        public void setHighlight(boolean highlight)
        {
            this.highlight = highlight;
        }

        @Override
        public Stroke transform(V v)
        {
            if (highlight)
            {
                if (pi.isPicked(v))
                    return heavy;
                else
                {
                	for(V w : graph.getNeighbors(v)) {
//                    for (Iterator iter = graph.getNeighbors(v)v.getNeighbors().iterator(); iter.hasNext(); )
//                    {
//                        Vertex w = (Vertex)iter.next();
                        if (pi.isPicked(w))
                            return medium;
                    }
                    return light;
                }
            }
            else
                return light;
        }

    }



    private final static class DirectionDisplayPredicate<V,E>
    	implements Predicate<Context<Graph<V,E>,E>>
    	//extends AbstractGraphPredicate<V,E>
    {
        protected boolean show_d;
        protected boolean show_u;

        public DirectionDisplayPredicate(boolean show_d, boolean show_u)
        {
            this.show_d = show_d;
            this.show_u = show_u;
        }

        public void showDirected(boolean b)
        {
            show_d = b;
        }

        public void showUndirected(boolean b)
        {
            show_u = b;
        }

        @Override
        public boolean evaluate(Context<Graph<V,E>,E> context)
        {
        	Graph<V,E> graph = context.graph;
        	E e = context.element;
            if (graph.getEdgeType(e) == EdgeType.DIRECTED && show_d) {
                return true;
            }
            if (graph.getEdgeType(e) == EdgeType.UNDIRECTED && show_u) {
                return true;
            }
            return false;
        }
    }

    private final static class VertexDisplayPredicate<V,E>
    	implements Predicate<Context<Graph<V,E>,V>>
//    	extends  AbstractGraphPredicate<V,E>
    {
        protected boolean filter_small;
        protected final static int MIN_DEGREE = 4;

        public VertexDisplayPredicate(boolean filter)
        {
            this.filter_small = filter;
        }

        public void filterSmall(boolean b)
        {
            filter_small = b;
        }

        @Override
        public boolean evaluate(Context<Graph<V,E>,V> context) {
        	Graph<V,E> graph = context.graph;
        	V v = context.element;
//            Vertex v = (Vertex)arg0;
            if (filter_small)
                return (graph.degree(v) >= MIN_DEGREE);
            else
                return true;
        }
    }

    private final class SeedDrawColor<V> implements Transformer<V,Paint>
    {
        protected PickedInfo<V> pi;
        protected final static float dark_value = 0.8f;
        protected final static float light_value = 0.2f;
        protected boolean seed_coloring;

        public SeedDrawColor(PickedInfo<V> pi)
        {
            this.pi = pi;
            seed_coloring = false;
        }

        public void setSeedColoring(boolean b)
        {
            this.seed_coloring = b;
        }

        @Override
        public Paint transform(V v)
        {
            return Color.BLACK;
        }

//        public Paint getFillPaint(V v)
//        {
//            float alpha = transparency.get(v).floatValue();
//            if (pi.isPicked(v))
//            {
//                return new Color(1f, 1f, 0, alpha);
//            }
//            else
//            {
//                if (seed_coloring && seedVertices.contains(v))
//                {
//                    Color dark = new Color(0, 0, dark_value, alpha);
//                    Color light = new Color(0, 0, light_value, alpha);
//                    return new GradientPaint( 0, 0, dark, 10, 0, light, true);
//                }
//                else
//                    return new Color(1f, 0, 0, alpha);
//            }
//
//        }
    }

    public class GradientPickedEdgePaintFunction<V,E> extends GradientEdgePaintTransformer<V,E>
    {
        private Transformer<E,Paint> defaultFunc;
        protected boolean fill_edge = false;
        Predicate<Context<Graph<V,E>,E>> selfLoop = new SelfLoopEdgePredicate<V,E>();

        public GradientPickedEdgePaintFunction(Transformer<E,Paint> defaultEdgePaintFunction,
                VisualizationViewer<V,E> vv)
        {
            super(Color.WHITE, Color.BLACK, vv);
            this.defaultFunc = defaultEdgePaintFunction;
        }

        public void useFill(boolean b)
        {
            fill_edge = b;
        }

        @Override
        public Paint transform(E e) {
            if (gradient_level == GRADIENT_NONE) {
                return defaultFunc.transform(e);
            } else {
            	return super.transform(e);
            }
        }

        @Override
        protected Color getColor2(E e)
        {
            return vv.getPickedEdgeState().isPicked(e)? Color.CYAN : c2;
        }

//        public Paint getFillPaint(E e)
//        {
//            if (selfLoop.evaluateEdge(vv.getGraphLayout().getGraph(), e) || !fill_edge)
//                return null;
//            else
//                return getDrawPaint(e);
//        }

    }

    public class VoltageTips<E>
    	implements Transformer<Neuron, String> {

        @Override
        public String transform(Neuron vertex) {
           return "Voltage:"+voltages.transform(vertex);
        }
    }

//    class VertexFactory implements Factory<Neuron> {
//
//		public Neuron create() {
//			return new Neuron();
//		}
//    }

//    class EdgeFactory implements Factory<Connection> {
//
//		public Connection create() {
//			return new Connection();
//		}
//    }


        /**
	 *
	 * @author danyelf
	 */

	private final class LayoutChooser implements ActionListener
        {
            private final JComboBox jcb;
            private final VisualizationViewer<Neuron,Connection> vv;

            private LayoutChooser(JComboBox jcb, VisualizationViewer<Neuron,Connection> vv)
            {
                super();
                this.jcb = jcb;
                this.vv = vv;
            }

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                Object[] constructorArgs = { graph };

                Class<? extends Layout<Neuron,Connection>> layoutC =
                    (Class<? extends Layout<Neuron,Connection>>) jcb.getSelectedItem();
    //            Class lay = layoutC;
                try
                {
                    Constructor<? extends Layout<Neuron, Connection>> constructor = layoutC
                            .getConstructor(new Class[] {Graph.class});
                    Object o = constructor.newInstance(constructorArgs);
                    Layout<Neuron, Connection> l = (Layout<Neuron, Connection>) o;
                    l.setInitializer(vv.getGraphLayout());
                    l.setSize(vv.getSize());

                                    LayoutTransition<Neuron, Connection> lt =
                                            new LayoutTransition<Neuron, Connection>(vv, vv.getGraphLayout(), l);
                                    Animator animator = new Animator(lt);
                                    animator.start();
                                    vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
                                    vv.repaint();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }



}
