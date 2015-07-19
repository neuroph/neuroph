 List<BufferedImage> lista = new ArrayList<BufferedImage>();
for (int i = 0; i <= 7; i++) {
File f = new File("data / char_" + i + ".png");
BufferedImage bi = ImageIO.read(f);
lista.add(bi);
});
 NeuralNetwork nnet = NeuralNetwork.createFromFile(m)
nnet.addPlugin(new OcrPlugin(new Dimension(10, 10), ColorMode.BLACK_AND_WHITE));
   OcrPlugin ocrPlugin = (OcrPlugin) nnet.getPlugin(OcrPlugin.class);
for (int i = 0; i < lista.size(); i++) {
recognizedCharacters+=ocrPlugin.recognizeCharacter(new ImageJ2SE(lista.get(i))) + " ";
System.out.print(ocrPlugin.recognizeCharacter(new ImageJ2SE(lista.get(i))) + " ");
