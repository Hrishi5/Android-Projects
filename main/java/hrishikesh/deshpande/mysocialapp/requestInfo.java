package hrishikesh.deshpande.mysocialapp;

/**
 * Created by Hrishikesh Deshpande on 21-Nov-17.
 */

public class requestInfo {
    String to,from;
    String toName,requestId,fromName;

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "requestInfo{" +
                "to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", toName='" + toName + '\'' +
                ", requestId='" + requestId + '\'' +
                ", fromName='" + fromName + '\'' +
                '}';
    }
}

