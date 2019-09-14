package model;

import java.util.ArrayList;

public class Cell
{
    private int posX;
    private int posY;
    private ArrayList<Direction> openedDirection = new ArrayList<>();
    private boolean checked = false;
    private PathFinderStats pathFinderStats = new PathFinderStats();

    public Cell(int posX, int posY)
    {
        this.posX = posX;
        this.posY = posY;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return posY;
    }

    public ArrayList<Direction> getOpenedDirection() {
        return this.openedDirection;
    }

    public void addOpenedDirection(Direction openedDirection) {
        this.getOpenedDirection().add(openedDirection);
    }

    public boolean hasBeenChecked()
    {
        return this.checked;
    }

    public void setChecked()
    {
        this.checked = true;
    }

    public PathFinderStats getPathFinderStats() {
        return this.pathFinderStats;
    }
}
