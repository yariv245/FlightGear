package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class MapDisplayer extends Canvas {

    public int[][] mapData;
    public int[][] mapPaintBase;
    double minElement = Double.MAX_VALUE;
    double maxElement = 0;
    public GraphicsContext gc;
    double height, width, WidthCanvas, HeightCanvas;
    double max_color = 255;
    double min_color = 0;
    HashMap<Integer, Image> images;

    public MapDisplayer() {
        images = new HashMap<>();
        images.put(0, new Image("./resources/plane0.png", 25, 25, false, false));
        images.put(45, new Image("./resources/plane45.png", 25, 25, false, false));
        images.put(90, new Image("./resources/plane90.png", 25, 25, false, false));
        images.put(135, new Image("./resources/plane135.png", 25, 25, false, false));
        images.put(180, new Image("./resources/plane180.png", 25, 25, false, false));
        images.put(225, new Image("./resources/plane225.png", 25, 25, false, false));
        images.put(270, new Image("./resources/plane270.png", 25, 25, false, false));
        images.put(315, new Image("./resources/plane315.png", 25, 25, false, false));
        gc = getGraphicsContext2D();
    }

    public void setMapData(int[][] mapData) {

        this.mapData = mapData;

        //Finding the maximum and minimum of the elements of the matrix
        for (int[] mapDatum : mapData)
            for (int i : mapDatum) {
                if (minElement > i) {
                    minElement = i;
                }
                if (maxElement < i) {
                    maxElement = i;
                }
            }

        this.mapPaintBase = new int[mapData.length][];

        //Filling the values by the CSV File values
        for (int i = 0; i < mapData.length; i++) {
            this.mapPaintBase[i] = new int[mapData[i].length];
            for (int j = 0; j < mapData[i].length; j++) {
                mapPaintBase[i][j] = (int) ((mapData[i][j] - minElement) / (maxElement - minElement) * (max_color - min_color) + min_color);
            }
        }

        //Ordering the matrix responsive
        WidthCanvas = getWidth();
        HeightCanvas = getHeight();

        width = WidthCanvas / mapData[0].length;
        height = HeightCanvas / mapData.length;


        reDraw();
        this.drawAirplane(new String[]{"21.308333", "-157.930417", "90.0"});
    }

    public void reDraw() {

        //Setting the colors for each element in the matrix by his value
        for (int i = 0; i < mapPaintBase.length; i++)
            for (int j = 0; j < mapPaintBase[i].length; j++) {
                gc.setFill(Color.rgb(255, 255, 255));
                gc.fillRect((j * width), (i * height), width, height);
            }
        for (int i = 0; i < mapPaintBase.length; i++)
            for (int j = 0; j < mapPaintBase[i].length; j++) {
                int tmp = mapPaintBase[i][j];
                gc.setFill(Color.rgb((255 - tmp), tmp, 0));
                gc.fillRect((j * width), (i * height), width, height);
            }
    }

    public void drawPath(String stringPositions) {
        ArrayList<Pair<Integer, Integer>> pairsPos = stringToPosition(stringPositions);

        for (Pair pair : pairsPos) {
            gc.setFill(Color.rgb(0, 0, 0));
            gc.fillRect(((int) pair.getValue() * width), ((int) pair.getKey() * height), width, height);
        }

    }

    private ArrayList<Pair<Integer, Integer>> stringToPosition(String stringPositions) {
        String[] stringPosList = stringPositions.split("-"); //Convert x1,y1-x2,y2 to array that holds string template:x1,y1
        ArrayList<Pair<Integer, Integer>> pairsPos = new ArrayList<>();

        for (String pos : stringPosList) //Convert the x1,y1 to Pair(int1 ,int2)
        {
            String[] nums = pos.split(","); // convert string: "x1,y1" to array = {"x1","y1"}
            pairsPos.add(new Pair(Integer.parseInt(nums[0]), Integer.parseInt(nums[1])));
        }
        return pairsPos;
    }

    public void drawAirplane(String[] data) {

        double heading = Double.parseDouble(data[2]);
        Pair<Double, Double> positions = calcPositions(Double.parseDouble(data[0]), Double.parseDouble(data[1]));
        double x = positions.getKey();
        double y = positions.getValue();

        //Draw image depend on the airplane heading value
        if (heading >= 0 && heading < 39)
            gc.drawImage(images.get(0), x, y, 25, 25);
        if (heading >= 39 && heading < 80)
            gc.drawImage(images.get(45), x, y, 25, 25);
        if (heading >= 80 && heading < 129)
            gc.drawImage(images.get(90), x, y, 25, 25);
        if (heading >= 129 && heading < 170)
            gc.drawImage(images.get(135), x, y, 25, 25);
        if (heading >= 170 && heading < 219)
            gc.drawImage(images.get(180), x, y, 25, 25);
        if (heading >= 219 && heading < 260)
            gc.drawImage(images.get(225), x, y, 25, 25);
        if (heading >= 260 && heading < 309)
            gc.drawImage(images.get(270), x, y, 25, 25);
        if (heading >= 309)
            gc.drawImage(images.get(315), x, y, 25, 25);

    }

    private Pair<Double, Double> calcPositions(double x, double y) {
        double lng, lat;
        lat = Math.abs((Math.abs(Math.abs(x) - 21.443738) * 0.01652));
        lng = Math.abs((Math.abs(Math.abs(y) - 158.020959) * 0.01652));
        return new Pair<>(lng, lat);
    }

}
