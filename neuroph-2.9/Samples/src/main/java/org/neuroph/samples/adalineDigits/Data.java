package org.neuroph.samples.adalineDigits;


import org.neuroph.core.data.DataSetRow;

public class Data {

    public final static int CHAR_WIDTH = 5;
    public final static int CHAR_HEIGHT = 7;

    public static String[][] DIGITS = {
           {" OOO ",
            "O   O",
            "O   O",
            "O   O",
            "O   O",
            "O   O",
            " OOO "},
           {"  O  ",
            " OO  ",
            "O O  ",
            "  O  ",
            "  O  ",
            "  O  ",
            "  O  "},
           {" OOO ",
            "O   O",
            "    O",
            "   O ",
            "  O  ",
            " O   ",
            "OOOOO"},
           {" OOO ",
            "O   O",
            "    O",
            " OOO ",
            "    O",
            "O   O",
            " OOO "},
           {"   O ",
            "  OO ",
            " O O ",
            "O  O ",
            "OOOOO",
            "   O ",
            "   O "},
           {"OOOOO",
            "O    ",
            "O    ",
            "OOOO ",
            "    O",
            "O   O",
            " OOO "},
           {" OOO ",
            "O   O",
            "O    ",
            "OOOO ",
            "O   O",
            "O   O",
            " OOO "},
           {"OOOOO",
            "    O",
            "    O",
            "   O ",
            "  O  ",
            " O   ",
            "O    "},
           {" OOO ",
            "O   O",
            "O   O",
            " OOO ",
            "O   O",
            "O   O",
            " OOO "},
           {" OOO ",
            "O   O",
            "O   O",
            " OOOO",
            "    O",
            "O   O",
            " OOO "}};
    
    public static DataSetRow convertImageIntoData(String[] image) {

        DataSetRow dataSetRow = new DataSetRow(Data.CHAR_HEIGHT * Data.CHAR_WIDTH);

        double[] array = new double[Data.CHAR_WIDTH * Data.CHAR_HEIGHT];

        for (int row = 0; row < Data.CHAR_HEIGHT; row++) {
            for (int column = 0; column < Data.CHAR_WIDTH; column++) {
                int index = (row * Data.CHAR_WIDTH) + column;
                char ch = image[row].charAt(column);
                array[index] = (ch == 'O' ? 1 : -1);
            }
        }
        dataSetRow.setInput(array);
        return dataSetRow;
    }

    public static String[] convertDataIntoImage(double[] data) {

        String[] image = new String[data.length / Data.CHAR_WIDTH];
        String row = "";

        for (int i = 0; i < data.length; i++) {
            if (data[i] == 1) {
                row += "O";
            } else {
                row += " ";
            }
            if (row.length() % 5 == 0 && row.length() != 0) {
                image[i / 5] = row;
                row = "";
            }
        }
        return image;
    }
}
