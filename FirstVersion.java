package com.example.gravitynew;

import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    Canvas c = new Canvas();
    GraphicsContext gc = this.c.getGraphicsContext2D();

    TextField tf = new TextField();
    Stage stage;

    Tracker[] d;
    boolean b = true;
    int n = 0;

    public static void main(String[] q) { Application.launch(); }

    public void start(Stage stage)
    {
        Button b1 = new Button("Randomize");
        b1.setOnAction(q -> this.randomize());

        Button b2 = new Button("Start");
        b2.setOnAction(q ->
        {
            this.b = true;
            this.execute();
        });

        Button b3 = new Button("Pause");
        b3.setOnAction(q -> this.b = false);

        HBox hb = new HBox();
        hb.getChildren().addAll(b1, this.tf, b2, b3);

        BorderPane bp = new BorderPane();
        bp.setTop(hb);
        bp.setLeft(this.c);

        this.c.setHeight(576);
        this.c.setWidth(1024);

        this.gc.setFill(Color.BLACK);
        this.gc.fillRect(0, 0, 1024, 576);

        Scene scene = new Scene(bp);

        this.stage = stage;
        this.stage.setScene(scene);
        this.stage.setOnCloseRequest(q -> System.exit(0));
        this.stage.show();
    }

    void randomize()
    {
        int i;

        try { i = Integer.parseInt(this.tf.getText()); }
        catch (Exception ignore) { return; }

        this.d = new Tracker[i];

        Random r = new Random();

        this.gc.setFill(Color.BLACK);
        this.gc.fillRect(0, 0, 1024, 576);
        this.gc.setFill(Color.RED);

        for (int j = 0; j < i; j++)
        {
            this.d[j] = new Tracker();
            this.d[j].x.add((double) r.nextInt(1025));
            this.d[j].y.add((double) r.nextInt(577));

            this.gc.fillOval(this.d[j].x.get(0) - 5, this.d[j].y.get(0) - 5, 10, 10);
        }

        this.n = 0;
    }

    void execute()
    {
        this.stage.setTitle(String.valueOf(this.n));

        double[][] dd = new double[this.d.length][2];

        for (int i = 0; i < this.d.length; i++)
        {
            double a = 0, b = 0, xx = this.d[i].x.get(this.n), yy = this.d[i].y.get(this.n);

            for (int j = 0; j < this.d.length; j++)
            {
                if (i != j)
                {
                    a += 1 / (this.d[j].x.get(this.n) - xx);
                    b += 1 / (this.d[j].y.get(this.n) - yy);
                }
            }

            dd[i][0] = this.d[i].x.get(this.n) + (a > 0 ? Math.sqrt(a) : -Math.sqrt(-a)); // + 1 / -(1024 - this.d[i][0]) + 1 / this.d[i][0];
            dd[i][1] = this.d[i].y.get(this.n) + (b > 0 ? Math.sqrt(b) : -Math.sqrt(-b)); // + 1 / -(576 - this.d[i][1]) + 1 / this.d[i][1];

            if (dd[i][0] > 1024) { dd[i][0] = this.d[i].x.get(this.n) - 1024; }
            if (dd[i][0] < 0) { dd[i][0] = 1024 + this.d[i].x.get(this.n); }
            if (dd[i][1] > 576) { dd[i][1] = this.d[i].y.get(this.n) - 576; }
            if (dd[i][1] < 0) { dd[i][1] = 576 + this.d[i].y.get(this.n); }
        }

        this.gc.setFill(Color.BLACK);
        this.gc.fillRect(0, 0, 1024, 576);
        this.gc.setFill(Color.RED);

        for (int i = 0; i < this.d.length; i++) { this.gc.fillOval(dd[i][0] - 5, dd[i][1] - 5, 10, 10); }

        for (int i = 0; i < this.d.length; i++)
        {
            this.d[i].x.add(dd[i][0]);
            this.d[i].y.add(dd[i][1]);
        }

        long l = System.currentTimeMillis();
        while (System.currentTimeMillis() - l < 33) { System.out.print(""); }

        n++;

        if (this.b) { Platform.runLater(this::execute); }
    }
}

class Tracker
{
    public ArrayList<Double> x = new ArrayList<>();
    public ArrayList<Double> y = new ArrayList<>();

    Tracker() { }
}
