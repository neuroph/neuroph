/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

        public static ArrayList<Double[]> generateVariations(ArrayList<Double> array, int numberOfInputs, boolean withRepetition) {
           
            variations = new ArrayList<>();

            if (!withRepetition) {
                mark = new ArrayList<>();

                for (int i = 0; i < array.size(); i++) {
                    mark.add(false);
                }
            }

            Double[] initialVariation = new Double[numberOfInputs];

            for (int i = 0; i < initialVariation.length; i++) {
                initialVariation[i] = 0.0;
            }

            if (withRepetition) {
                calculateVariationsWithRepetition(0, initialVariation, array, numberOfInputs);
            } else {
                calculateVariationsWithoutRepetition(0, initialVariation, mark, array, numberOfInputs);
            }

            return variations;
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
