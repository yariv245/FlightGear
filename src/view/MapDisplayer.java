package view;

import javafx.beans.property.IntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class MapDisplayer extends Canvas {

    public int[][] mapData;
    double minElement = Double.MAX_VALUE;
    double maxElement = 0;
    GraphicsContext gc;
    double height, width, WidthCanvas, HeightCanvas;
    double max_color = 255;
    double min_color = 0;


    public MapDisplayer() {

        gc = getGraphicsContext2D();
        this.setOnMouseClicked(arg0 -> {
            System.out.println("The X on the matrix is : " + arg0.getX() / 2);
            System.out.println("The Y on the matrix is : " + arg0.getY() / 2);
            gc.strokeText("X", arg0.getX(), arg0.getY());
            // TODO: אני מניח תפילין 10 דקות פה
        });

    }

    public void setMapData(int[][] mapData) {
        this.mapData = mapData;

        //Finding the maxiumum and minimum of the elements of the matrix
        for (int i = 0; i < mapData.length; i++)
            for (int j = 0; j < mapData[i].length; j++) {
                if (minElement > mapData[i][j]) {
                    minElement = mapData[i][j];
                }
                if (maxElement < mapData[i][j]) {
                    maxElement = mapData[i][j];
                }
            }

        //Filling the values by the CSV File values
        for (int i = 0; i < mapData.length; i++)
            for (int j = 0; j < mapData[i].length; j++) {
                mapData[i][j] = (int)((mapData[i][j] - minElement) / (maxElement - minElement) * (max_color - min_color) + min_color);
            }

        reDraw();
    }


    public void reDraw() {

        //First - Ordering the matrix responsivly
        WidthCanvas = getWidth();
        HeightCanvas = getHeight();

        width = WidthCanvas / mapData[0].length;
        height = HeightCanvas / mapData.length;


        //Setting the colors for each element in the matrix by his value
        for (int i = 0; i < mapData.length; i++)
            for (int j = 0; j < mapData[i].length; j++) {
                int tmp = mapData[i][j];
                gc.setFill(Color.rgb((255 - tmp), (0 + tmp), 0));
                gc.fillRect((j * width), (i * height), width, height);
            }
    }

}
