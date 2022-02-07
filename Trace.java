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
    int m = 0;

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

        Button b4 = new Button("Step");
        b4.setOnAction(q ->
        {
           this.b = false;
           this.execute();
        });

        Button b5 = new Button("Trace");
        b5.setOnAction(q -> this.trace());

        Button b6 = new Button("Next");
        b6.setOnAction(q -> this.m = this.m == this.d.length - 1 ? 0 : this.m + 1);

        Button b7 = new Button("Prev");
        b7.setOnAction(q -> this.m = this.m == 0 ? this.d.length - 1 : this.m - 1);

        HBox hb = new HBox();
        hb.getChildren().addAll(b1, this.tf, b2, b3, b4, b5, b6, b7);

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
        if (this.d[0] == null) { return; }

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

            double _a = a * (this.d[i].xm == 0 ? 1 : this.d[i].xm);
            double _b = b * (this.d[i].ym == 0 ? 1 : this.d[i].xm);

            double aa = _a > 0 ? Math.sqrt(_a) : -Math.sqrt(-_a);
            double bb = _b > 0 ? Math.sqrt(_b) : -Math.sqrt(-_b);

            double a_ = aa > 0 ? Math.sqrt(aa) : -Math.sqrt(-aa);
            double b_ = bb > 0 ? Math.sqrt(bb) : -Math.sqrt(-bb);

            dd[i][0] = this.d[i].x.get(this.n) + a_; // + 1 / -(1024 - this.d[i][0]) + 1 / this.d[i][0];
            dd[i][1] = this.d[i].y.get(this.n) + b_; // + 1 / -(576 - this.d[i][1]) + 1 / this.d[i][1];

            if (dd[i][0] > 1024)
            {
                dd[i][0] = this.d[i].x.get(this.n) - 1024;
                a_ = 0;
            }
            if (dd[i][0] < 0)
            {
                dd[i][0] = 1024 + this.d[i].x.get(this.n);
                a_ = 0;
            }
            if (dd[i][1] > 576)
            {
                dd[i][1] = this.d[i].y.get(this.n) - 576;
                b_ = 0;
            }
            if (dd[i][1] < 0)
            {
                dd[i][1] = 576 + this.d[i].y.get(this.n);
                b_ = 0;
            }

            this.d[i].xm = a_;
            this.d[i].ym = b_;
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

        this.n++;

        if (this.b) { Platform.runLater(this::execute); }
    }

    void trace()
    {
//        this.gc.setFill(Color.BLACK);
//        this.gc.fillRect(0, 0, 1024, 576);
        this.gc.setStroke(Color.ORANGERED);
        this.gc.setLineWidth(2);

        for (int i = 0; i < this.n - 1; i++)
        {
            this.gc.strokeLine(
                this.d[this.m].x.get(i), this.d[this.m].y.get(i), this.d[this.m].x.get(i + 1),
                this.d[this.m].y.get(i + 1)
            );
        }
    }
}

class Tracker
{
    public ArrayList<Double> x = new ArrayList<>();
    public ArrayList<Double> y = new ArrayList<>();
    double xm = 0;
    double ym = 0;

    Tracker() { }
}
