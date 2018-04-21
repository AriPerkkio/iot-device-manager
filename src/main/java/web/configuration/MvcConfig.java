package web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");

        // TODO better SPA solution should be studied
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/devices").setViewName("index");
        registry.addViewController("/groups").setViewName("index");
        registry.addViewController("/types").setViewName("index");
        registry.addViewController("/configurations").setViewName("index");
        registry.addViewController("/location-updates").setViewName("index");
        registry.addViewController("/measurements").setViewName("index");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/templates/**")
                .addResourceLocations("/resources/templates/");
    }
}