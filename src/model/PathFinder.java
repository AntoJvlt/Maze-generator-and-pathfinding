package model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PathFinder implements Runnable
{
    private Maze maze;
    private Cell underCheckCell;
    private boolean isSearching = false;
    private Thread thread;

    public PathFinder(Maze maze)
    {
        this.maze = maze;
    }

    @Override
    public void run() {
        this.findPath();
    }

    public void start()
    {
        this.stop();

        this.isSearching = true;

        this.thread = new Thread(this);
        this.getThread().start();
    }

    public void stop()
    {
        if(this.getThread() != null && this.getThread().isAlive())
            this.getThread().stop();

        this.reset();
        this.isSearching = false;
    }

    public void reset()
    {
        for(int x = 0; x < this.getMaze().getWidth(); x++)
        {
            for(int y = 0; y < this.getMaze().getHeight(); y++)
            {
                this.setUnderCheckCell(null);
                this.getMaze().getCell(x, y).getPathFinderStats().setUnchecked();
                this.getMaze().getCell(x, y).getPathFinderStats().setGlobalScore(Double.POSITIVE_INFINITY);
                this.getMaze().getCell(x, y).getPathFinderStats().setLocalScore(Double.POSITIVE_INFINITY);
                this.getMaze().getCell(x, y).getPathFinderStats().setParent(null);
            }
        }
    }

    public void findPath()
    {

        this.reset();

        this.getMaze().getStartingCell().getPathFinderStats().setLocalScore(0);
        this.getMaze().getStartingCell().getPathFinderStats().setGlobalScore(this.getHeuristic(this.getMaze().getStartingCell(), this.getMaze().getEndCell()));

        ArrayList<Cell> toBeCheck = new ArrayList<>();
        toBeCheck.add(this.getMaze().getStartingCell());

        while(!toBeCheck.isEmpty() && this.getUnderCheckCell() != this.getMaze().getEndCell())
        {
            Collections.sort(toBeCheck, new SortByGlobalScore());

            if(toBeCheck.get(0).getPathFinderStats().hasBeenChecked())
                toBeCheck.remove(toBeCheck.get(0));

            if(toBeCheck.isEmpty())
                break;

            this.setUnderCheckCell(toBeCheck.get(0));

            ArrayList<Cell> neighbours = this.getNeighbours(this.getUnderCheckCell());

            for(Cell neighbour : neighbours)
            {
                if(!neighbour.getPathFinderStats().hasBeenChecked())
                    toBeCheck.add(neighbour);

                Double possiblyLocalScore = this.getUnderCheckCell().getPathFinderStats().getLocalScore() + this.getDistance(this.getUnderCheckCell(), neighbour);

                if(possiblyLocalScore < neighbour.getPathFinderStats().getLocalScore())
                {
                    neighbour.getPathFinderStats().setParent(this.getUnderCheckCell());
                    neighbour.getPathFinderStats().setLocalScore(possiblyLocalScore);
                    neighbour.getPathFinderStats().setGlobalScore(neighbour.getPathFinderStats().getLocalScore() + this.getHeuristic(neighbour, this.getMaze().getEndCell()));
                }
            }


            this.getUnderCheckCell().getPathFinderStats().setChecked();
            this.getMaze().notifyChanges();

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Cell> getNeighbours(Cell cell)
    {
        ArrayList<Cell> neighbours = new ArrayList<>();

        if(cell.getOpenedDirection().contains(Direction.NORTH) && cell.getPosY() > 0)
            neighbours.add(this.getMaze().getCell(cell.getPosX(), cell.getPosY() - 1));

        if(cell.getOpenedDirection().contains(Direction.EAST) && cell.getPosX() < this.getMaze().getWidth())
            neighbours.add(this.getMaze().getCell(cell.getPosX() + 1, cell.getPosY()));

        if(cell.getOpenedDirection().contains(Direction.SOUTH) && cell.getPosY() < this.getMaze().getHeight())
            neighbours.add(this.getMaze().getCell(cell.getPosX(), cell.getPosY() + 1));

        if(cell.getOpenedDirection().contains(Direction.WEST) && cell.getPosX() > 0)
            neighbours.add(this.getMaze().getCell(cell.getPosX() - 1, cell.getPosY()));

        return neighbours;
    }

    private double getDistance(Cell cellA, Cell cellB)
    {
        return Math.sqrt(Math.pow((cellA.getPosX() - cellB.getPosX()), 2) + Math.pow((cellA.getPosY() - cellB.getPosY()), 2));
    }

    private double getHeuristic(Cell cellA, Cell cellB)
    {
        return this.getDistance(cellA, cellB);
    }

    public boolean pathContain(Cell cell)
    {
        Cell parent = this.getHigherGlobalScoreCell().getPathFinderStats().getParent();

        if(this.getMaze().getEndCell().getPathFinderStats().hasBeenChecked())
            parent = this.getMaze().getEndCell();

        while(parent != null)
        {
            if(parent == cell)
                return true;
            else
                parent = parent.getPathFinderStats().getParent();
        }

        return false;
    }

    private Cell getHigherGlobalScoreCell()
    {
        Cell higherGlobalScore = this.getMaze().getStartingCell();

        for(int x = 0; x < this.getMaze().getWidth(); x++)
        {
            for(int y = 0; y < this.getMaze().getHeight(); y++)
            {
                if(this.getMaze().getCell(x, y).getPathFinderStats().getGlobalScore() != Double.POSITIVE_INFINITY)
                    if(this.getMaze().getCell(x, y).getPathFinderStats().getGlobalScore() > higherGlobalScore.getPathFinderStats().getGlobalScore())
                        higherGlobalScore = this.getMaze().getCell(x, y);
            }
        }

        return higherGlobalScore;
    }

    public Maze getMaze() {
        return this.maze;
    }

    public Cell getUnderCheckCell() {
        return this.underCheckCell;
    }

    public void setUnderCheckCell(Cell underCheckCell) {
        this.underCheckCell = underCheckCell;
    }

    public boolean isSearching() {
        return this.isSearching;
    }

    class SortByGlobalScore implements Comparator<Cell>
    {
        public int compare(Cell a, Cell b)
        {
            return (int)(a.getPathFinderStats().getGlobalScore() - b.getPathFinderStats().getGlobalScore());
        }
    }

    public Thread getThread() {
        return this.thread;
    }
}
