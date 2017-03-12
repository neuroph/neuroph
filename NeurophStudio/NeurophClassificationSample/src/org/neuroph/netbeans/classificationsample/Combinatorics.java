package org.neuroph.netbeans.classificationsample;

import java.util.ArrayList;

/**
 *
 * @author Milos Randjic
 */
public class Combinatorics {

    public static class Variations {

        private static ArrayList<Double[]> variations;
        private static ArrayList<Boolean> mark;

        /**
         * Generise varijacije k klase za zadati niz vrednosti
         *
         * @param values niz vrednosti za varijacije
         * @param numberOfInputs broj pozicija na kojima se varijaju (koja je
         * klasa varijacija)
         * @param withRepetition da li su sa ponavljanjem ili bez ponavlajnja
         * @return
         */
        public static ArrayList<double[]> generateVariations(ArrayList<Double> values, int numberOfInputs, boolean withRepetition) {

            variations = new ArrayList<>(); // this list will hold all variations

            if (!withRepetition) {
                mark = new ArrayList<>(); // mark used elements 
                for (int i = 0; i < values.size(); i++) {
                    mark.add(false);
                }
            }

            Double[] initialVariation = new Double[numberOfInputs];
            for (int i = 0; i < initialVariation.length; i++) {
                initialVariation[i] = 0.0;
            }

            if (withRepetition) {
                calculateVariationsWithRepetition(0, initialVariation, values, numberOfInputs);
            } else {
                calculateVariationsWithoutRepetition(0, initialVariation, mark, values, numberOfInputs);
            }

            ArrayList<double[]> returnArray = new  ArrayList<>();
            
            // quick fix prebaci ovde sve u double[]
            for (int i = 0; i < variations.size(); i++) {
                double[] array = new double[numberOfInputs];
                Double[] input = variations.get(i);
                for(int j=0;j<numberOfInputs; j++) {
                    array[j] = input[j];
                }
                returnArray.add(array);
            }

            return returnArray;
        }

        private static void calculateVariationsWithRepetition(int position, Double[] variation, ArrayList<Double> array, int numberOfInputs) {

            if (position == numberOfInputs) {
                variations.add(variation.clone());
                return;
            }

            for (int i = 0; i < array.size(); i++) {
                variation[position] = array.get(i);
                calculateVariationsWithRepetition(position + 1, variation, array, numberOfInputs);
            }
        }

        private static void calculateVariationsWithoutRepetition(int position, Double[] variation, ArrayList<Boolean> mark, ArrayList<Double> array, int numberOfInputs) {

            if (position == numberOfInputs) {
                variations.add(variation.clone());
                return;
            }

            for (int i = 0; i < array.size(); i++) {

                if (!mark.get(i)) {
                    variation[position] = array.get(i);
                    mark.set(i, true);
                    calculateVariationsWithoutRepetition(position + 1, variation, mark, array, numberOfInputs);
                    mark.set(i, false);
                }

            }
        }
    }
}
