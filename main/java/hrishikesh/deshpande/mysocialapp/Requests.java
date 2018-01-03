package hrishikesh.deshpande.mysocialapp;

/**
 * Created by Hrishikesh Deshpande on 16-Nov-17.
 */

public class Requests {
    private String requestFrom ;
    private String requestTo ;

    public String getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(String requestFrom) {
        this.requestFrom = requestFrom;
    }

    public String getRequestTo() {
        return requestTo;
    }

    public void setRequestTo(String requestTo) {
        this.requestTo = requestTo;
    }

    @Override
    public String toString() {
        return "Requests{" +
                "requestFrom='" + requestFrom + '\'' +
                ", requestTo='" + requestTo + '\'' +
                '}';
    }
}
