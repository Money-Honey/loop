package com.loopme.webapp.runnable;

import com.github.fakemongo.Fongo;
import com.google.common.base.Preconditions;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.loopme.webapp.modules.BaseModule;
import com.loopme.webapp.modules.MongoDataBaseModule;
import com.loopme.webapp.modules.SqlDataBaseModule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.servlet.DispatcherType;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Properties;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class EntryPoint {
    private static final String CONFIGURATION_FILE_NAME = "cfg.properties";
    private static final String PROP_DATABASE_PASSWORD = "db.password";
    private static final String PROP_DATABASE_URL = "db.url";
    private static final String PROP_DATABASE_USERNAME = "db.username";
    private static final String SERVER_PORT = "port";

    public static void main(String[] args) throws Exception {
        Properties props = loadProps(CONFIGURATION_FILE_NAME);
        int port = Integer.valueOf(props.getProperty(SERVER_PORT));

        Module dataBaseModule = createDataBaseModule("sql", props);

        Server server = new Server(port);

        // tell jetty to ask Guice about what servlet should handle the requests
        ServletContextHandler context = new ServletContextHandler(
                server, "/", ServletContextHandler.SESSIONS);
        // attach listener who instantiate our injector
        context.addEventListener(new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return Guice.createInjector(new BaseModule(), dataBaseModule);
            }
        });
        context.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC));

        // DefaultServlet is at the end of the pipeline to handle what Guice didn't
        context.addServlet(DefaultServlet.class, "/*");

        try {
            server.start();
            server.join();
        } finally {
            if (server.isStopped()) {
                server.destroy();
            }
        }
    }

    private static Module createDataBaseModule(String sql,Properties props) {
        switch (sql) {
            case "sql":
                final DataSource dataSource = createDataSource(props);
                Preconditions.checkNotNull(dataSource, "DataSource is Null. App couldn't be run without it");

                return new SqlDataBaseModule(dataSource);

            case "nosql":
                Fongo fongo = new Fongo("mongo fake server");
                return new MongoDataBaseModule(fongo.getDB("fongoname"));
            default:
                throw new IllegalArgumentException("Module type not Supported");
        }
    }

    private static Properties loadProps(String path) throws Exception {
        Properties props = new Properties();
        InputStream inputStream = EntryPoint.class.getClassLoader().getResourceAsStream(path);

        if (inputStream != null) {
            props.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + path + "' not found in the classpath");
        }
        return props;
    }

    private static DataSource createDataSource(Properties env) {

        return JdbcConnectionPool.create(
                env.getProperty(PROP_DATABASE_URL), PROP_DATABASE_USERNAME, PROP_DATABASE_PASSWORD);
    }
}
