package ru.dlabs.sas.example.jsso.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityProperties {

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "spring.security.headers")
    public static class Headers {
        private List<String> csp;
        private HSTS hsts;
        private List<String> permissionPolicy;

        @Getter
        @Setter
        public static class HSTS {
            private Integer maxAge;
            private Boolean includeSubDomains;
            private Boolean preload;
        }

        public String getCSPLikeString() {
            if (this.csp != null) {
                return String.join("; ", this.csp);
            }
            return null;
        }

        public String getPermissionPolicyLikeString() {
            if (this.csp != null) {
                return String.join(", ", this.csp);
            }
            return null;
        }
    }
}
