package com.loopme.webapp.dao.impl.sql;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.loopme.webapp.dao.AppDao;
import com.loopme.webapp.dto.Advertise;
import com.loopme.webapp.dto.AdvertiseRequestEvent;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class JdbcAppDao implements AppDao {

    static Logger Log = Logger.getLogger(JdbcAppDao.class.getName());

    @Inject
    private Provider<Connection> connectionProvider;

    @Override
    public List<Advertise> retrieveAdvertises(AdvertiseRequestEvent event) {

        List<Advertise> ads = Lists.newArrayList();

        try (Connection connection = connectionProvider.get();
             PreparedStatement ps = prepareStatement(connection, event);
             ResultSet rs = ps.executeQuery()) {

            Log.info(String.format("Got connection: %s", connection));

            while (rs.next()) {
                ads.add(new Advertise(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }

        } catch (SQLException e) {
            Log.error("Got exception while trying to fetch ads from db", e);
            Throwables.propagate(e);
        }

        return ads;

    }

    @SuppressWarnings("JpaQueryApiInspection")
    private PreparedStatement prepareStatement(Connection connection, AdvertiseRequestEvent event)
            throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "Select * " +
                "From ( " +
                     "Select id " +
                     "From ADs a " +
                     "Where a.OS = ? And a.COUNTRY = ? " +
                     "Order By RAND() LIMIT ? " +
                ") " +
                "As ids Join ads On ads.id = ids.id"
        );

        ps.setString(1, event.getOs());
        ps.setString(2, event.getCountry());
        ps.setInt(3, event.getLimit());

        return ps;
    }

}
