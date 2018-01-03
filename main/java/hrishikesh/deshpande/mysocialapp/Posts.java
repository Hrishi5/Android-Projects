package hrishikesh.deshpande.mysocialapp;

/**
 * Created by Hrishikesh Deshpande on 16-Nov-17.
 */

public class Posts {
    private String post ;
    private String from ;
    private String emailId;
    private String date ;
    private String postId ;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "post='" + post + '\'' +
                ", from='" + from + '\'' +
                ", emailId='" + emailId + '\'' +
                ", date='" + date + '\'' +
                ", postId='" + postId + '\'' +
                '}';
    }
}
