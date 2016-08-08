package com.loopme.webapp.runnable;

import com.google.common.base.Throwables;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.loopme.webapp.dao.AppDao;
import com.loopme.webapp.dao.JdbcAppDao;
import com.loopme.webapp.restfull.EndPoint;
import com.loopme.webapp.services.AppService;
import com.loopme.webapp.services.AppServiceImpl;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.sql.DataSource;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class AppInjector extends ServletModule {
    private final DataSource dataSource;

    AppInjector(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    @Override
    protected void configureServlets() {
        bind(EndPoint.class).in(Scopes.SINGLETON);

        bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
        bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

        bind(AppDao.class).toInstance(new JdbcAppDao());
        bind(AppService.class).toInstance(new AppServiceImpl());

        bind(Connection.class).toProvider(() -> {
            try {
                return dataSource.getConnection();
            } catch (Exception e) {
                Throwables.propagate(e);
                return null;
            }
        });

        Map<String, String> options = new HashMap<>();
        options.put("com.sun.jersey.api.json.POJOMappingFeature", "true");

        serve("/*").with(GuiceContainer.class, options);
    }
}
