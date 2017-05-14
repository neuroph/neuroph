package org.neuroph.core.input;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.*;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

/**
 * 
 * @author Shivanth
 */
public class ProductTest {

	private Product product;
        private List<Connection> inputConnections;
        private InputNeuron[] inputNeurons;        

	@Before
	public void setUp() {
            product = new Product();
            inputNeurons = new InputNeuron[4];
            for (int i = 0; i < 4; i++) {
                inputNeurons[i] = new InputNeuron();
            }

            Neuron toNeuron = new Neuron();

            inputConnections = new ArrayList<Connection>(4);
            for (int i = 0; i < 4; i++) {
                Connection conn = new Connection(inputNeurons[i], toNeuron, 1);
                inputConnections.add(conn);
                toNeuron.addInputConnection(conn);
            }            
	}

	@Test
	public void testProductPositives() {
            double[] inputs = new double[]{.5, .10, .5, .3};
            for (int i = 0; i < inputNeurons.length; i++) {
                inputNeurons[i].setInput(inputs[i]);
                inputNeurons[i].calculate();
            }

            double output = product.getOutput(inputConnections);
            assertEquals(0.0075, output, .0001);
	}

	@Test
	public void testProductMixed() {
            double[] inputs = new double[]{-.1, .10, 2, 3};

            for (int i = 0; i < inputNeurons.length; i++) {
                inputNeurons[i].setInput(inputs[i]);
                inputNeurons[i].calculate();
            }

            double output = product.getOutput(inputConnections);
            assertEquals(-.060, output, .0001);
	}

	@Test
	public void testProductNegativess() {
            double[] inputs = new double[]{-1, -.5, -.10, .1};

            for (int i = 0; i < inputNeurons.length; i++) {
                inputNeurons[i].setInput(inputs[i]);
                inputNeurons[i].calculate();
            }

            double output = product.getOutput(inputConnections);
            assertEquals(-.0050, output, .0001);
	}

	@Test
	public void testProductMultiple() {
            double[] inputs = new double[]{1, .5, .10, .9};
            for (int i = 0; i < inputNeurons.length; i++) {
                inputNeurons[i].setInput(inputs[i]);
                inputNeurons[i].calculate();
            }

            double output = product.getOutput(inputConnections);
            assertEquals(.045, output, .0001);
	}

	@Test
	public void testNZeroes() {
            double[] inputs = new double[]{1, 2, 4, 0};

            for (int i = 0; i < inputNeurons.length; i++) {
                inputNeurons[i].setInput(inputs[i]);
                inputNeurons[i].calculate();
            }

            double output = product.getOutput(inputConnections);
            assertEquals(0, output, .00001);
	}

	@Test
	public void testEmptyInput() {
		 double output = product.getOutput(new ArrayList<Connection>());
		assertEquals(0, output, .00001);
	}
}
