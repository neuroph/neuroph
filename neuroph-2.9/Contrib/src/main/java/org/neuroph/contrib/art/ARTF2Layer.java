package org.neuroph.contrib.art;

import java.util.List;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.util.NeuronProperties;

/**
 *
 * @author ja
 */
public class ARTF2Layer extends Layer {

    /**
     * Max iterations for neurons to compete This is necessary to limit,
     * otherwise if there is no winner there will be endless loop.
     */
    private int maxIterations = 100;

    /**
     * The competition winner for this layer
     */
    private ART1CompetitiveNeuron winner;

    /**
     * Create an instance of CompetitiveLayer with the specified number of
     * neurons with neuron properties
     *
     * @param neuronNum neuron number in this layer
     * @param neuronProperties properties for the neurons in this layer
     */
    public ARTF2Layer(int neuronNum, NeuronProperties neuronProperties) {
        super(neuronNum, neuronProperties);
    }

    /*
        * Performs calculation for all neurons in this layer
     */

    @Override
    public void calculate() {

        double normOfF1b = 0;
        double normOfF1a = 0;
        int iterationsCount = 0;

        /*
                * Perform calculation for every neuron
                * that is not inhibited
         */
        for (Neuron neuron : this.getNeurons()) {
            ART1CompetitiveNeuron artNeuron = (ART1CompetitiveNeuron) neuron;
            if (!artNeuron.isInhibited()) {
                neuron.calculate();
            }
        }

        winner = null;

        while (true) {

            /**
             * Make the first uninhibited neuron in F2 layer a winning neuron
             * Step 6. For each node that is not inhibited, compute the output
             * of the competitive neuron
             *
             */
            for (Neuron neuron : this.getNeurons()) {
                ART1CompetitiveNeuron artNeuron = (ART1CompetitiveNeuron) neuron;
                if (!artNeuron.isInhibited()) {
                    winner = artNeuron;
                    break;
                }
            }

            /**
             * Step 8. User specified Find the winning neuron, such that is has
             * bigger output than any other neuron. If all nodes have -1 output,
             * than all nodes are inhibited, and pattern cannot be clustered.
             */
            for (Neuron neuron : this.getNeurons()) {
                ART1CompetitiveNeuron artNeuron = (ART1CompetitiveNeuron) neuron;
                if (artNeuron.getOutput() >= winner.getOutput()) {
                    winner = artNeuron;
                }
            }

            if (winner != null) {

                // Collection of input connections for the winning neuron
                List<Connection> winnerConnections = winner.getInputConnections();

                for (int i = 0; i < winnerConnections.size(); i++) {

                    Connection winnerConnection = winnerConnections.get(i);

                    // Neurons in layers F1a and F1b, which are connected to the winning neuron
                    Neuron f1bNeuron = winnerConnection.getFromNeuron();
                    Neuron f1aNeuron = f1bNeuron.getInputConnections().get(i).getFromNeuron();

                    /**
                     * Step 9. Calculating the activation for the neuron in F1a
                     * layer x(i) = s(i)*t(J,i)
                     */
                    double f1bActivation = f1aNeuron.getOutput() * winner.getConnectionFrom(f1bNeuron).getWeight().value; //winnerConnection.getWeight().value;
                    f1bNeuron.setOutput(f1bActivation);

                }

                /**
                 * Step 10. Computing the norm vector x, layer F1b needed for
                 * testing the reset module
                 */
                for (Neuron neuron : this.getParentNetwork().getLayerAt(1).getNeurons()) {
                    normOfF1b += neuron.getOutput();

                }
                /**
                 * Step 4. Computing the norm of vector s
                 */
                for (Neuron neuron : this.getParentNetwork().getLayerAt(0).getNeurons()) {
                    normOfF1a += neuron.getOutput();

                }

                ART1Network nnet = (ART1Network) this.getParentNetwork();

                /**
                 * Step 11. Test for reset If the expression is smaller than the
                 * vigilance parameter the neuron gets excluded from competing
                 */
                if (normOfF1b / normOfF1a < nnet.getVigilance()) {
                    winner.setInhibited(true);
                } /**
                 * Step 12. For neuron that passed reset test, update the
                 * weights for the winning node
                 */
                else {
                    /**
                     * Updating bottom-up weights L*x(i)/(L-1 +||x||)
                     */
                    for (int i = 0; i < winnerConnections.size(); i++) {
                        Connection winnerConnection = winnerConnections.get(i);
                        Weight newWeight = new Weight();
                        newWeight.setValue((nnet.getL() * winnerConnection.getFromNeuron().getOutput()) / (nnet.getL() - 1 + normOfF1b));
                        winnerConnection.setWeight(newWeight);
                    }
                    /**
                     * Updating top-down weights
                     */
                    List<Connection>topDownConnections = winner.getOutConnections();

                    for (int i = 0; i < topDownConnections.size(); i++) {
                        Connection topDownConnection = topDownConnections.get(i);
                        Weight newWeight = new Weight();
                        newWeight.setValue(topDownConnection.getToNeuron().getOutput());
                    }
                }
            }
            /**
             * Step 13. Test for stopping condition
             */
            iterationsCount++;
            if (iterationsCount > 500) {
                break;
            }

        }

        /**
         * If there is no winner, add a new neuron to the F2 layer and connected
         * it to other layers
         */
        if (winner == null) {
            ART1CompetitiveNeuron newNeuron = new ART1CompetitiveNeuron();
            this.addNeuron(newNeuron);
            List<Neuron> f1BNeurons = this.getParentNetwork().getLayerAt(1).getNeurons();

            // Forming new connections
            for (int i = 0; i < f1BNeurons.size(); i++) {
                Neuron f1BNeuron = f1BNeurons.get(i);

                Connection bottomUp = new Connection(f1BNeuron, newNeuron);

                // Setting new bottom-up weights
                Weight weightB = new Weight();
                weightB.setValue(bottomUp.getFromNeuron().getOutput() / (0.5 + normOfF1b));
                bottomUp.setWeight(weightB);

                Connection topDown = new Connection(newNeuron, f1BNeuron);

                //Setting new top-down weights
                Weight weightT = new Weight();
                weightT.setValue(f1BNeuron.getOutput());
                topDown.setWeight(weightT);
            }
        }
    }

    /**
     * Returns the winning neuron for this layer
     *
     * @return winning neuron for this layer
     */
    public ART1CompetitiveNeuron getWinner() {
        return this.winner;
    }

    /**
     * Returns the maxIterations setting for this layer
     *
     * @return maxIterations setting for this layer
     */
    public int getMaxIterations() {
        return maxIterations;
    }

    /**
     * Sets max iterations for neurons to compete in this layer
     *
     * @param maxIterations max iterations for neurons to compete in this layer
     */
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

}
