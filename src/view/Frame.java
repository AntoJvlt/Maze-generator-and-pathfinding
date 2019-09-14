package view;

import controller.MazeController;
import model.Maze;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Frame extends JFrame implements Runnable
{
    public Frame(Maze maze, MazeController mazeController)
    {
        this.setTitle("Maze generator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        MazePanel mazePanel = new MazePanel(maze, mazeController);
        this.add(mazePanel);

        SwingUtilities.invokeLater(this);
    }

    @Override
    public void run() {
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
    }
}
