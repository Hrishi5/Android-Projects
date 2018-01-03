package hrishikesh.deshpande.mysocialapp ;

/**
 * Created by sanket on 11/19/2017.
 */

public class userInfo {
    String firstName,lastName,email,dataOfBirth;

    public String getLastName() {
        return lastName;
    }

    public String getDataOfBirth() {
        return dataOfBirth;
    }

    public void setDataOfBirth(String dataOfBirth) {
        this.dataOfBirth = dataOfBirth;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "userInfo{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", dataOfBirth='" + dataOfBirth + '\'' +
                '}';
    }
}
