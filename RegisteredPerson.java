
// Guy-robert Bogning
// March 18, 2026
// Implements a registered person, extending Person

public class RegisteredPerson extends Person {
    private String govID;

    // Constructor
    RegisteredPerson(String firstName, String lastName, String govID) {
        super(firstName, lastName);
        this.govID = govID;
    }
    
    RegisteredPerson(Person other, String govID) {
        super(other);
        this.govID = govID;
    }
    
    RegisteredPerson(RegisteredPerson other) {
        super(other);
        this.govID = other.govID;
    }
    
    public String getGovernmentID() {
        return govID;
    }
    
    public boolean equals(RegisteredPerson other) {
        return super.equals(other) && govID.equals(other.govID);
    }
    
    public boolean equals(Person other) {
        return super.equals(other);
    }
    
    public String toString() {
        return super.toString() + " Government ID: " + govID;
    }
}