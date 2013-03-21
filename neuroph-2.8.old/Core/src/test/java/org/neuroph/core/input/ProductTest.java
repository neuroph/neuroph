package org.neuroph.core.input;

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
        private Connection[] inputConnections;
        private InputNeuron[] inputNeurons;        

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
            product = new Product();
            inputNeurons = new InputNeuron[4];
            for (int i = 0; i < inputNeurons.length; i++) {
                inputNeurons[i] = new InputNeuron();
            }

            Neuron toNeuron = new Neuron();

            inputConnections = new Connection[4];
            for (int i = 0; i < inputConnections.length; i++) {
                inputConnections[i] = new Connection(inputNeurons[i], toNeuron, 1);
                toNeuron.addInputConnection(inputConnections[i]);
            }            
	}

	@After
	public void tearDown() {
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
		 double output = product.getOutput(new Connection[0]);
		assertEquals(0, output, .00001);
	}
}
