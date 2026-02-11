public class Cargo implements Comparable<Cargo> {
    String cargoName;
    int weightCargo;

    public Cargo() {
        this.cargoName = "No Name";
        this.weightCargo = 0;
    }

    public void setCargoName(String name) {
        this.cargoName = name;
    }

    public void setWeightCargo(int weight) {
        this.weightCargo = weight;
    }

    public String getCargoName() {
        return this.cargoName;
    }

    public int getWeightCargo() {
        return this.weightCargo;
    }

    @Override
    public int compareTo(Cargo other) {
        // String sudah memiliki method compareTo() sendiri, kita tinggal memakainya.
        return this.cargoName.compareTo(other.cargoName);
    }

    @Override
    public String toString() {
        return "(" + this.cargoName + ", " + this.weightCargo + "Kg)";
    }

}
