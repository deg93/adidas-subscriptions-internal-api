package es.davidenjuan.subscriptions.internalapi.domain;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import es.davidenjuan.subscriptions.internalapi.domain.enumeration.EmailStatus;

@SuppressWarnings("serial")
@Document(collection = "email")
public class Email implements Serializable {

    @Id
    private String id;

    @NotNull
    @Field("status")
    private EmailStatus status;

    @NotEmpty
    @Field("from")
    private String from;

    @NotNull
    @javax.validation.constraints.Email
    @Field("to")
    private String to;

    @NotEmpty
    @Field("subject")
    private String subject;

    @NotEmpty
    @Field("body")
    private String body;

    @NotNull
    @Field("body_is_html")
    private Boolean bodyIsHtml;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public void setStatus(EmailStatus status) {
        this.status = status;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getBodyIsHtml() {
        return bodyIsHtml;
    }

    public void setBodyIsHtml(Boolean bodyIsHtml) {
        this.bodyIsHtml = bodyIsHtml;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Email)) {
            return false;
        }
        return id != null && id.equals(((Email) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Email [id=");
        builder.append(id);
        builder.append(", status=");
        builder.append(status);
        builder.append(", from=");
        builder.append(from);
        builder.append(", to=");
        builder.append(to);
        builder.append(", subject=");
        builder.append(subject);
        builder.append(", body=");
        builder.append(body);
        builder.append(", bodyIsHtml=");
        builder.append(bodyIsHtml);
        builder.append("]");
        return builder.toString();
    }
}
