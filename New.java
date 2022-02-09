import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    Label l = new Label();
    TextField tf = new TextField();
    Stage stage;

    Tracker[] d;
    boolean b = true;
    int n = 0;
    int m = 0;

    public Main() { }

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

        Button b8 = new Button("Info");
        b8.setOnAction(q -> this.info());

        HBox hb = new HBox();
        hb.getChildren().addAll(b1, this.tf, b2, b3, b4, b5, b6, b7, b8);

        BorderPane bp = new BorderPane();
        bp.setTop(hb);
        bp.setLeft(this.c);
        bp.setRight(this.l);

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
            double a = 0, b = 0, xx = this.d[i].x.get(this.n), yy = this.d[i].y.get(this.n), o = 1, p = this.d.length;

            for (int j = 0; j < this.d.length; j++)
            {
                double v = this.d[j].x.get(this.n);
                double w = this.d[j].y.get(this.n);

                if (v - xx > 512) { v = -v - 512; }
                else if (xx - v > 512) { v += 1024; }
                
                if (w - yy > 288) { w = -v - 288; }
                else if (yy - w > 288) { w += 576; }

                a += v;
                b += w;
                
                o *= 1 / Math.sqrt((v - xx) * (v - xx) - (w - yy) * (w - yy));
            }
            
            a /= p;
            b /= p;
            
            double h = a - xx;
            double l = Math.atan((b - yy) / h);
            double q = h >= 0 ? Math.cos(l) * Math.pow(o, 1 / p) + xx : -Math.cos(l) * Math.pow(o, 1 / p) + xx;
            double s = h >= 0 ? Math.sin(l) * Math.pow(o, 1 / p) + yy : -Math.sin(l) * Math.pow(o, 1 / p) + yy;
            
            

            dd[i][0] = this.d[i].x.get(this.n) + (a > 0 ? Math.sqrt(a) : -Math.sqrt(-a));
            dd[i][1] = this.d[i].y.get(this.n) + (b > 0 ? Math.sqrt(b) : -Math.sqrt(-b));

            if (this.d[i].x.get(this.n) > 1024) { dd[i][0] = this.d[i].x.get(this.n) - 1024; }
            if (this.d[i].x.get(this.n) < 0) { dd[i][0] = 1024 + this.d[i].x.get(this.n); }
            if (this.d[i].y.get(this.n) > 576) { dd[i][1] = this.d[i].y.get(this.n) - 576; }
            if (this.d[i].y.get(this.n) < 0) { dd[i][1] = 576 + this.d[i].y.get(this.n); }
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

        try { Thread.sleep(33); }
        catch (Exception ignore) { }

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

    void info()
    {
        String s = this.d[this.m].xm + " - " + this.d[this.m].ym + "\n";

        for (int i = 0; i < this.n; i++) { s += this.d[this.m].x.get(i) + " - " + this.d[this.m].y.get(i) + "\n"; }

        this.l.setText(s);
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
