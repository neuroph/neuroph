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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * This class represents image on Android platform, and the Image interface is used to provide compatibility with J2SE images.
 * It is a wrapper around Bitmap and implementation of Image interface.
 * @author dmicic
 */
public class ImageAndroid implements Image {

    private Bitmap bitmap;
 
    public ImageAndroid(Bitmap bitmap) {
       this.bitmap = bitmap; 
    }
    
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private ImageAndroid(int width, int height, int imageType) {
        bitmap = Bitmap.createBitmap(width, height, imageTypeToBitmapConfig(imageType));
    }

    private ImageAndroid(File imageFile) {
        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
    }

    private ImageAndroid(String filePath) {
        bitmap = BitmapFactory.decodeFile(filePath);
    }

    private ImageAndroid(URL imageUrl) throws IOException {
        bitmap = BitmapFactory.decodeStream((InputStream) imageUrl.getContent());
    }

    @Override
    public int getPixel(int x, int y) {
        return bitmap.getPixel(x, y);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        bitmap.setPixel(x, y, color);
    }

    @Override
    public int[] getPixels(int offset, int stride, int x, int y, int width, int height) {
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, offset, stride, x, y, width, height);
        return pixels;
    }

    @Override
    public void setPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        bitmap.setPixels(pixels, offset, stride, x, y, width, height);
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Image resize(int width, int height) {
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return new ImageAndroid( bitmap);
    }

    @Override
    public Image crop(int x1, int y1, int x2, int y2) {
        return new ImageAndroid(Bitmap.createBitmap(bitmap, x1, y1, x2 - x1, y2 - y1));
    }

    private Bitmap.Config imageTypeToBitmapConfig(int imageType) throws IllegalArgumentException {
        Bitmap.Config bitmapConfig = null;
        switch (imageType) {
            case ImageType.ANDROID_TYPE_ALPHA_8:
                bitmapConfig = Bitmap.Config.ALPHA_8;
                break;
            case ImageType.ANDROID_TYPE_ARGB_8888:
                bitmapConfig = Bitmap.Config.ARGB_8888;
                break;
            case ImageType.ANDROID_TYPE_RGB_565:
                bitmapConfig = Bitmap.Config.RGB_565;
                break;
            default:
                throw new IllegalArgumentException("Illegal image type, image type: " + imageType);
        }

        return bitmapConfig;
    }

    public int getType() {
        return ImageType.ANDROID_TYPE_ALPHA_8; // FIX how to get bitmap type?
    }
}
