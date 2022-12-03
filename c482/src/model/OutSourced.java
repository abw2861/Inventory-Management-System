package model;

/** This is the Outsourced class. */
public class OutSourced extends Part{

    private String companyName;

    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /** @return The company name */
    public String getCompanyName() {
        return companyName;
    }

    /** @param companyName The company name to set */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
