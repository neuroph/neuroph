/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuroph.imgrec.image;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;

/**
 * This class creates image instances depending on the run-time platform:
 * ImageJ2SE - for J2SE
 * ImageAndroid - for Android
 * @author dmicic
 */
public class ImageFactory {

    private static Image image;
    private static final String IMAGE_ANDROID_CLASS_NAME = "org.neuroph.imgrec.image.ImageAndroid";
    private static final String IMAGE_J2SE_CLASS_NAME = "org.neuroph.imgrec.image.ImageJ2SE";
    private static Class imageClass;
    private static Constructor constructor;

    static {
        
        try {
            if (System.getProperty("java.vendor").indexOf("android") != -1) {
                imageClass = Class.forName(IMAGE_ANDROID_CLASS_NAME); 
            } else {
                imageClass = Class.forName(IMAGE_J2SE_CLASS_NAME);
            }

        } catch (ClassNotFoundException cnf) {
            System.err.println(cnf.getMessage());
        }

    }

    public static Image createImage(Integer width, Integer height, Integer imageType) {
        try {
            constructor = imageClass.getDeclaredConstructor(new Class[]{Integer.class, Integer.class, Integer.class});
            constructor.setAccessible(true);
            image = (Image) constructor.newInstance(width, height, imageType);
        } catch (Exception e) {
            handleException(e);
        }

        return image;
    }

    public static Image getImage(URL imageUrl) {
        try {
            constructor = imageClass.getDeclaredConstructor(new Class[]{URL.class});
            constructor.setAccessible(true);
            image = (Image) constructor.newInstance(imageUrl);
        } catch (Exception e) {
            handleException(e);
        }

        return image;
    }

    public static Image getImage(File imageFile) {
        try {
            constructor = imageClass.getDeclaredConstructor(new Class[]{File.class});
            constructor.setAccessible(true);
            image = (Image) constructor.newInstance(imageFile);
        } catch (Exception e) {
            handleException(e);
        }

        return image;
    }

    public static Image getImage(String filePath) {
        try {
            constructor = imageClass.getDeclaredConstructor(new Class[]{String.class});
            constructor.setAccessible(true);
            image = (Image) constructor.newInstance(filePath);
        } catch (Exception e) {
            handleException(e);
        }

        return image;
    }

    private static void handleException(Exception e) {
        System.err.println(e.getMessage());
    }

//    public static Image resizeImage(Image image, int width, int height) {
//        return image.resize(width, height);
//    }

//    public static Image cropImage(Image image, int x1, int y1, int x2, int y2) {
//        return image.cropImage(image, x1, y1, x2, y2);
//    }
}
