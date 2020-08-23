package sample;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        new Main();
    }


    public Main() {
        this.setSize(835, 860);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new Controller.DrawStuff(), BorderLayout.CENTER);
        this.setVisible(true);
    }


               }













