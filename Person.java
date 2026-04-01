import java.io.Serializable;

// Guy-robert Bogning
// March 18, 2026
// Implements a person class

public class Person implements Serializable {

    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(Person p) {
        firstName = p.firstName;
        lastName = p.lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean equals(Person p) {
        return firstName.equals(p.firstName) && lastName.equals(p.lastName);
    }

    public void eat() {
        System.out.println(
            getClass().getName() + " " + toString() + " is eating.!!!"
        );
    }

    public void sleep() {
        System.out.println(
            getClass().getName() + " " + toString() + " is sleeping.!!!"
        );
    }

    public void play() {
        System.out.println(
            getClass().getName() + " " + toString() + " is playing.!!!"
        );
    }

    public void run() {
        System.out.println(
            getClass().getName() + " " + toString() + " is running.!!!"
        );
    }

    @Override
    public String toString() {
        return lastName + ", " + firstName;
    }
}
