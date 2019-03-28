/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.nnet.comp;

import java.io.Serializable;

  /**
  * Dimensions (width and height) of the Layer2D
  */
public class Dimension2D implements Serializable {

        private static final long serialVersionUID = -4491706467345191108L;

        private int width;
        private int height;

        /**
         * Creates new dimensions with specified width and height
         *
         * @param width  total number  of columns
         * @param height total number of rows
         */
        public Dimension2D(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            String dimensions = "Width = " + width + "; Height = " + height;
            return dimensions;
        }
    }