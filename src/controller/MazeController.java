package controller;

import model.Maze;
import view.Frame;

import javax.swing.*;
import java.awt.*;

public class MazeController
{
    private Maze maze;
    private Frame frame;

    public MazeController()
    {
        this.maze = new Maze(100);
        this.frame = new Frame(this.maze, this);

        Thread thread = new Thread(maze);
        thread.start();
    }

    public void setStartEndCell(String order, Point pos)
    {
        if(this.getMaze().isGenerated())
        {
            int x = (int)pos.getX();
            int y = (int)pos.getY();

            if(x >= 0 && x < this.getMaze().getWidth() && y >= 0 && y < this.getMaze().getHeight())
            {
                if(order == "start")
                    this.getMaze().setStartingCell(this.getMaze().getCell(x, y));
                else if(order == "end")
                    this.getMaze().setEndCell(this.getMaze().getCell(x, y));

            }else
                System.out.println("out of the maze");
        }
    }

    public Maze getMaze() {
        return this.maze;
    }
}
