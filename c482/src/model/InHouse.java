package model;

/** This is the InHouse Class. */
public class InHouse extends Part{

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /** @return The machine id */
    public int getMachineId() {
        return machineId;
    }

    /** @param machineId The machine id to set */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}


