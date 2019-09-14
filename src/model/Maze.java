package model;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.Stack;

public class Maze extends Observable implements Runnable
{
    private int width;
    private int height;
    private Cell[][] cells;
    private Cell startingCell;
    private Cell endCell;
    private Stack<Cell> cellsStack = new Stack<>();
    private int checkedCells = 0;
    private boolean isGenerated = false;
    private PathFinder pathFinder;


    public Maze(int cellsAmount)
    {
        this.width = (int) cellsAmount / 2;
        this.height = (int) cellsAmount / 2;

        this.cells = new Cell[this.getWidth()][this.getHeight()];
        this.initMaze();
        this.getCellsStack().push(this.getCell(0,0));
        this.pathFinder = new PathFinder(this);
        this.setStartingCell(this.getCell(0, 0));
        this.setEndCell(this.getCell(this.getWidth() - 1, this.getHeight() - 1));
    }

    @Override
    public void run()
    {
        this.generate();
    }

    public void generate()
    {
        while(this.getCheckedCells() < this.getWidth() * this.getHeight())
        {
            this.notifyChanges();
            try {
                Thread.sleep(1);
            }catch (InterruptedException e){}

            Cell underCheckCell = this.getCellsStack().peek();
            ArrayList<Cell> unCheckedSideCells = new ArrayList<>();

            /* Verify if top cell is unchecked */
            if(underCheckCell.getPosY() > 0 && !this.getSideCell(0, -1).hasBeenChecked())
                    unCheckedSideCells.add(this.getSideCell(0, -1));
            /* Verify if right cell is unchecked */
            if(underCheckCell.getPosX() < this.getWidth() - 1 && !this.getSideCell(1, 0).hasBeenChecked())
                    unCheckedSideCells.add(this.getSideCell(1, 0));
            /* Verify if bottom cell is unchecked */
            if(underCheckCell.getPosY() < this.getHeight() - 1 && !this.getSideCell(0, 1).hasBeenChecked())
                    unCheckedSideCells.add(this.getSideCell(0, 1));
            /* Verify if left cell is unchecked */
            if(underCheckCell.getPosX() > 0 && !this.getSideCell(-1, 0).hasBeenChecked())
                    unCheckedSideCells.add(this.getSideCell(- 1, 0));

            if(!unCheckedSideCells.isEmpty())
            {
                Random rand = new Random();
                int random = rand.nextInt((unCheckedSideCells.size()));
                Cell selectedCell = unCheckedSideCells.get(random);

                /* The selected cell is the top one */
                if(selectedCell.getPosY() == underCheckCell.getPosY() - 1)
                {
                    underCheckCell.addOpenedDirection(Direction.NORTH);
                    selectedCell.addOpenedDirection(Direction.SOUTH);
                }
                /* The selected cell is the right one */
                else if(selectedCell.getPosX() == underCheckCell.getPosX() + 1)
                {
                    underCheckCell.addOpenedDirection(Direction.EAST);
                    selectedCell.addOpenedDirection(Direction.WEST);
                }
                /* The selected cell is the bottom one */
                else if(selectedCell.getPosY() == underCheckCell.getPosY() + 1)
                {
                    underCheckCell.addOpenedDirection(Direction.SOUTH);
                    selectedCell.addOpenedDirection(Direction.NORTH);
                }
                /* The selected cell is the left one */
                else if(selectedCell.getPosX() == underCheckCell.getPosX() - 1)
                {
                    underCheckCell.addOpenedDirection(Direction.WEST);
                    selectedCell.addOpenedDirection(Direction.EAST);
                }

                this.getCellsStack().push(selectedCell);
                this.checkedCells++;
            }
            else
            {
                if(this.getCheckedCells() == this.getWidth() * this.getHeight() - 1)
                    this.checkedCells++;
                else
                    this.getCellsStack().pop();
            }

            underCheckCell.setChecked();
        }
        this.isGenerated = true;
    }

    private Cell getSideCell(int x, int y)
    {
        Cell topCell = this.getCellsStack().peek();

        return this.getCell(topCell.getPosX() + x, topCell.getPosY() + y);
    }

    private void initMaze()
    {
        for(int x = 0; x < this.getWidth(); x++)
        {
            for(int y = 0; y < this.getHeight(); y++)
            {
                this.cells[x][y] = new Cell(x, y);
               // System.out.println("x : " + x + " y  : " + y);
            }
        }
    }

    void notifyChanges()
    {
        this.setChanged();
        this.notifyObservers();
    }

    private int getCheckedCells() {
        return this.checkedCells;
    }

    public Cell getCell(int x, int y)
    {
        return this.cells[x][y];
    }

    public Stack<Cell> getCellsStack()
    {
        return this.cellsStack;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public Observable getObservable()
    {
        return this;
    }

    public void setStartingCell(Cell startingCell) {
        this.startingCell = startingCell;
        this.getPathFinder().stop();
        this.notifyChanges();
    }

    public Cell getStartingCell() {
        return this.startingCell;
    }

    public void setEndCell(Cell endCell) {
        this.endCell = endCell;
        this.getPathFinder().stop();
        this.notifyChanges();
    }

    public Cell getEndCell() {
        return this.endCell;
    }

    public boolean isGenerated() {
        return this.isGenerated;
    }

    public PathFinder getPathFinder() {
        return this.pathFinder;
    }
}
