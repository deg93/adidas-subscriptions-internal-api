package es.davidenjuan.subscriptions.internalapi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private Jwt jwt;

    private EmailApi emailApi;

    private String frontEndUri;

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public EmailApi getEmailApi() {
        return emailApi;
    }

    public void setEmailApi(EmailApi emailApi) {
        this.emailApi = emailApi;
    }

    public String getFrontEndUri() {
        return frontEndUri;
    }

    public void setFrontEndUri(String frontEndUri) {
        this.frontEndUri = frontEndUri;
    }

    public static class Jwt {

        private String base64Secret;

        private int tokenValiditySeconds;

        public String getBase64Secret() {
            return base64Secret;
        }

        public void setBase64Secret(String base64Secret) {
            this.base64Secret = base64Secret;
        }

        public int getTokenValiditySeconds() {
            return tokenValiditySeconds;
        }

        public void setTokenValiditySeconds(int tokenValiditySeconds) {
            this.tokenValiditySeconds = tokenValiditySeconds;
        }
    }

    public static class EmailApi {

        private String uri;

        private String user;

        private String password;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
