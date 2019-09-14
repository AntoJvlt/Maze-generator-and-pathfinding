package view;
import controller.MazeController;
import model.Cell;
import model.Direction;
import model.Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

class MazePanel extends JPanel implements Observer, MouseListener
{
    private final static int MAZE_PANEL_SIZE = 800;
    private int pathSize;
    private int wallSize;
    private int cellSize;
    private Maze maze;
    private MazeController mazeController;

    public MazePanel(Maze maze, MazeController mazeController)
    {
        this.maze = maze;
        this.mazeController = mazeController;
        maze.getObservable().addObserver(this);
        this.setPreferredSize(new Dimension(MAZE_PANEL_SIZE, MAZE_PANEL_SIZE));
        this.cellSize = MAZE_PANEL_SIZE / this.getMaze().getWidth();
        this.wallSize = (int)(this.getCellSize() * 0.3);
        this.pathSize = this.getCellSize() - this.getWallSize();
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        for(int x = 0; x < this.getMaze().getWidth(); x++)
        {
            for(int y = 0; y < this.getMaze().getHeight(); y++)
            {
                Cell currentCell = this.getMaze().getCell(x, y);
                boolean isStartOrEnd = currentCell == this.getMaze().getStartingCell() || currentCell == this.getMaze().getEndCell();

                    /* Drawing Cells paths */
                    if(currentCell.getOpenedDirection().isEmpty())
                        g.setColor(Color.RED);
                    else if (currentCell == this.getMaze().getStartingCell())
                        g.setColor(Color.GREEN);
                    else if(currentCell == this.getMaze().getEndCell())
                        g.setColor(Color.RED);
                    else if(this.getMaze().getPathFinder().pathContain(currentCell))
                        g.setColor(Color.MAGENTA);
                    else
                        g.setColor(Color.WHITE);

                    g.fillRect(x * this.getCellSize(), y * this.getCellSize(), this.getCellSize(), this.getCellSize());

                    /* Drawing Cells walls */
                    g.setColor(Color.BLACK);
                    if(!isStartOrEnd)
                    {
                        if(y == 0)
                            g.fillRect(x * this.getCellSize(), y * this.getCellSize(), this.getCellSize(), this.getWallSize());
                        if(x == 0)
                            g.fillRect(x * this.getCellSize(), y * this.getCellSize(), this.getWallSize(), this.getCellSize());
                    }
                    if(!currentCell.getOpenedDirection().contains(Direction.SOUTH) && !(isStartOrEnd && y == this.getMaze().getHeight() - 1))
                        g.fillRect(x * this.getCellSize() - this.getWallSize(), y * this.getCellSize() + this.getPathSize(), this.getCellSize() + this.getWallSize(), this.getWallSize());
                    if(!currentCell.getOpenedDirection().contains(Direction.EAST) && !(isStartOrEnd && x == this.getMaze().getWidth() - 1))
                        g.fillRect(x * this.getCellSize() + this.getPathSize(), y * this.getCellSize(), this.getWallSize(), this.getCellSize());

            }
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        this.repaint();
    }

    private Maze getMaze() {
        return this.maze;
    }

    private int getCellSize() {
        return this.cellSize;
    }

    private int getPathSize() {
        return this.pathSize;
    }

    private int getWallSize() {
        return this.wallSize;
    }

    public MazeController getMazeController() {
        return this.mazeController;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1 && !e.isShiftDown())
            this.getMazeController().setStartEndCell("start", new Point(e.getX() / this.getCellSize(), e.getY() / this.getCellSize()));
        else if(e.getButton() == MouseEvent.BUTTON3 && !e.isShiftDown())
            this.getMazeController().setStartEndCell("end", new Point(e.getX() / this.getCellSize(), e.getY() / this.getCellSize()));

        if(e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3)
        {
            if(e.isShiftDown())
                if(this.getMaze().isGenerated())
                    this.getMaze().getPathFinder().start();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
