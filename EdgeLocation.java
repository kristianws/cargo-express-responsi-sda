public class EdgeLocation {
    String source;
    String destination;
    int distance;

    public EdgeLocation(String destination, int distance) {
        this.destination = destination;
        this.distance = distance;
        this.source = null;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return this.source;
    }

    public String getDestination(){
        return this.destination;
    }

    public void setDestination(String newDestination) {
        this.destination = newDestination;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int newDistance) {
        this.distance = newDistance;
    }

    @Override
    public String toString() {
        return "( " + destination + ", " + distance + " )";
    }

}
