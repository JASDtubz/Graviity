package com.example.gravitynew;

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

    double[][] d;

    public static void main(String[] q) { Application.launch(); }

    public void start(Stage stage)
    {
        Button b1 = new Button("Randomize");
        b1.setOnAction(q -> this.randomize());

        Button b2 = new Button("Start");
        b2.setOnAction(q -> this.execute());

        HBox hb = new HBox();
        hb.getChildren().addAll(b1, this.tf, b2);

        BorderPane bp = new BorderPane();
        bp.setTop(hb);
        bp.setLeft(this.c);

        this.c.setHeight(576);
        this.c.setWidth(1024);

        this.gc.setFill(Color.BLACK);
        this.gc.fillRect(0, 0, 1024, 576);

        Scene scene = new Scene(bp);

        stage.setScene(scene);
        stage.show();
    }

    void randomize()
    {
        if (this.tf.getText().equals("")) { return; }
        else
        {
            int i;

            try { i = Integer.parseInt(this.tf.getText()); }
            catch (Exception ignore) { return; }

            this.d = new double[i][2];

            Random r = new Random();

            this.gc.setFill(Color.BLACK);
            this.gc.fillRect(0, 0, 1024, 576);
            this.gc.setFill(Color.RED);

            for (int j = 0; j < i; j++)
            {
                this.d[j][0] = r.nextInt(1025);
                this.d[j][1] = r.nextInt(577);

                this.gc.fillOval(this.d[j][0] - 5, this.d[j][1] - 5, 10, 10);
            }
        }
    }

    void execute()
    {
        double[][] dd = new double[this.d.length][2];

        for (int i = 0; i < this.d.length; i++)
        {
            double a = 0, b = 0, xx = this.d[i][0], yy = this.d[i][0];

            for (int j = 0; j < this.d.length; j++)
            {
                if (i != j)
                {
                    a += 1 / (this.d[j][0] - xx);
                    b += 1 / (this.d[j][1] - yy);
                }
            }

            dd[i][0] = this.d[i][0] + a + 1 / -(1024 - this.d[i][0]) + 1 / this.d[i][0];
            dd[i][1] = this.d[i][1] + b + 1 / -(576 - this.d[i][1]) + 1 / this.d[i][1];

            if (dd[i][0] > 1024) { dd[i][0] = d[i][0] % 1024; }
            if (dd[i][0] < 0) { dd[i][0] = 1024 + d[i][0]; }
            if (dd[i][1] > 576) { dd[i][1] = d[i][1] % 576; }
            if (dd[i][1] < 0) { dd[i][1] = 576 + d[i][1]; }
        }

        this.gc.setFill(Color.BLACK);
        this.gc.fillRect(0, 0, 1024, 576);
        this.gc.setFill(Color.RED);

        for (int i = 0; i < this.d.length; i++) { this.gc.fillOval(dd[i][0] - 5, dd[i][1] - 5, 10, 10); }

        this.d = dd;

//        long l = System.currentTimeMillis();
//        while (System.currentTimeMillis() - l < 10) { System.out.print(""); }

        Platform.runLater(this::execute);
    }
}
