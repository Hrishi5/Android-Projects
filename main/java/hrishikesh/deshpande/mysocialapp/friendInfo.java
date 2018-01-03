package hrishikesh.deshpande.mysocialapp;

public class friendInfo {
    String email,firstName;

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
        return "friendInfo{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
