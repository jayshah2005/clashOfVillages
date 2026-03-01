package src.Utility;

public class Region { // Everything in this class can be handled by map. this feels redundant
    private int top; // top of the map
    private int left; // left of the map
    private int bottom; // bottom of the map
    private int right; // right of the map

    public Region(int top, int left, int bottom, int right){
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    /**
     * Determines if the coordinates x and y are within the game Region. if the point at (x,y) if outside
     * of the boundaries top,left,bottom,right then it should return true if it is within the boundaries
     * and false if not
     * @param p
     * @return
     */
    public boolean boundaries(Position p) {
        int x = p.getX();
        int y = p.getY();

        boolean boundaryX = (x >= left) && (x <= right);
        boolean boundaryY = (y >= top) && (y <= bottom);

        if (boundaryX && boundaryY) {
            return true;
        } else {
            return false;
        }
    }
}
