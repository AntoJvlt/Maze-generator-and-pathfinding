package model;

public class PathFinderStats
{
    private boolean hasBeenChecked = false;
    private double localScore = Double.POSITIVE_INFINITY;
    private double globalScore = Double.POSITIVE_INFINITY;
    private Cell parent = null;

    public double getLocalScore() {
        return this.localScore;
    }

    public void setLocalScore(double localScore) {
        this.localScore = localScore;
    }

    public double getGlobalScore() {
        return this.globalScore;
    }

    public void setGlobalScore(double globalScore) {
        this.globalScore = globalScore;
    }

    public Cell getParent() {
        return this.parent;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public boolean hasBeenChecked() {
        return this.hasBeenChecked;
    }

    public void setChecked()
    {
        this.hasBeenChecked = true;
    }

    public void setUnchecked()
    {
        this.hasBeenChecked = false;
    }
}
