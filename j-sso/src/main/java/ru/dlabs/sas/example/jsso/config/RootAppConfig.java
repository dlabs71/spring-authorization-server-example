package ru.dlabs.sas.example.jsso.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "ru.dlabs.sas.example.jsso.dao.repository")
public class RootAppConfig {

    private final AppProperties.CorsProperties corsProperties;
    private final AppProperties.SwaggerProperties swaggerProperties;
    private final BuildProperties buildProperties;
    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        log.debug("CREATE CORS FILTER");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        corsProperties.getConfigs().forEach(configProps -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(configProps.allowCredentials());
            config.addAllowedOrigin(configProps.allowedOrigins());
            config.addAllowedOriginPattern(configProps.allowedOriginPatterns());
            config.addAllowedHeader(configProps.allowedHeaders());
            config.addExposedHeader(configProps.exposedHeaders());
            config.addAllowedMethod(configProps.allowedMethods());
            source.registerCorsConfiguration(configProps.pattern(), config);
        });
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public OpenAPI openAPI() {
        String buildDate = buildProperties.get("build-date");
        String buildInfo = "<h4>Build date: " + buildDate + "</h4>"
            + "<br>" + buildProperties.get("description");

        Components components = new Components();
        List<SecurityRequirement> securityRequirements = new ArrayList<>();

        // добавляем возможность указывать Authorization header
        if (swaggerProperties.getAuthTypes().authHeaderEnabled()) {
            String securitySchemeName = "Authorization header";
            components.addSecuritySchemes(
                securitySchemeName,
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
            );
            securityRequirements.add(new SecurityRequirement().addList(securitySchemeName));
        }

        // Добавим возможность OAuth2 Authorization code flow
        if (swaggerProperties.getAuthTypes().authorizationCodeEnabled()) {
            String securitySchemeName = "Authorization code flow";
            components.addSecuritySchemes(securitySchemeName, new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows().authorizationCode(
                    new OAuthFlow()
                        .tokenUrl(swaggerProperties.getAuthOauth().tokenUrl())
                        .authorizationUrl(swaggerProperties.getAuthOauth().authorizationUrl())
                        .refreshUrl(swaggerProperties.getAuthOauth().refreshUrl())
                )));
            securityRequirements.add(new SecurityRequirement().addList(securitySchemeName));
        }

        // Добавим возможность OAuth2 Client credentials flow
        if (swaggerProperties.getAuthTypes().clientCredentialsEnabled()) {
            String securitySchemeName = "Client credentials flow";
            components.addSecuritySchemes(securitySchemeName, new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows().clientCredentials(
                    new OAuthFlow().tokenUrl(swaggerProperties.getAuthOauth().tokenUrl())
                )));
            securityRequirements.add(new SecurityRequirement().addList(securitySchemeName));
        }

        return new OpenAPI()
            .components(components)
            .security(securityRequirements)
            .info(new Info()
                      .title(buildProperties.getName())
                      .description(buildInfo)
                      .version(buildProperties.getVersion())
            );
    }

    // Данный метод добавления глобального параметра запроса не работает, так как Swagger запрещает в явную указывать
    // следующие заголовки: Accept, Content-Type, Authorization
//    @Bean
//    @ConditionalOnProperty(value = "springdoc.auth.auth-header-enabled", havingValue = "true")
//    public OperationCustomizer customGlobalHeaders() {
//        return (Operation operation, HandlerMethod handlerMethod) -> {
//            Parameter authorizationHeader = new Parameter()
//                    .in(ParameterIn.HEADER.toString())
//                    .schema(new StringSchema())
//                    .name("Authorization")
//                    .description("Authorization Header (Bearer or Basic)")
//                    .required(false);
//            operation.addParametersItem(authorizationHeader);
//            return operation;
//        };
//    }

    /**
     * Бин необходимый для работы с сообщениями хранимыми в .properties файлах.
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
