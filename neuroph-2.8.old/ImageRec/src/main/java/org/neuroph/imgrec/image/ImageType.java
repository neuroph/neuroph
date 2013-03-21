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

/**
 * This class holds image type codes
 * @author dmicic
 */
public class ImageType {

    public static final int J2SE_TYPE_CUSTOM = 0;
    public static final int J2SE_TYPE_INT_RGB = 1;
    public static final int J2SE_TYPE_INT_ARGB = 2;
    public static final int J2SE_TYPE_INT_ARGB_PRE = 3;
    public static final int J2SE_TYPE_INT_BGR = 4;
    public static final int J2SE_TYPE_3BYTE_BGR = 5;
    public static final int J2SE_TYPE_4BYTE_ABGR = 6;
    public static final int J2SE_TYPE_4BYTE_ABGR_PRE = 7;
    public static final int J2SE_TYPE_USHORT_565_RGB = 8;
    public static final int J2SE_TYPE_USHORT_555_RGB = 9;
    public static final int J2SE_TYPE_BYTE_GRAY = 10;
    public static final int J2SE_TYPE_USHORT_GRAY = 11;
    public static final int J2SE_TYPE_BYTE_BINARY = 12;
    public static final int J2SE_TYPE_BYTE_INDEXED = 13;
    
    public static final int ANDROID_TYPE_ALPHA_8 = 14;
    public static final int ANDROID_TYPE_ARGB_8888 = 15;
    public static final int ANDROID_TYPE_RGB_565 = 16;
}
