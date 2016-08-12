package com.loopme.webapp.modules;

import com.google.inject.Scopes;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.loopme.webapp.restfull.EndPoint;
import com.loopme.webapp.services.AppService;
import com.loopme.webapp.services.AppServiceImpl;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class BaseModule extends ServletModule {

    public BaseModule() {
        super();
    }

    @Override
    protected void configureServlets() {
        bind(EndPoint.class).in(Scopes.SINGLETON);

        bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
        bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

        bind(AppService.class).toInstance(new AppServiceImpl());

        Map<String, String> options = new HashMap<>();
        options.put("com.sun.jersey.api.json.POJOMappingFeature", "true");

        serve("/*").with(GuiceContainer.class, options);
    }
}
