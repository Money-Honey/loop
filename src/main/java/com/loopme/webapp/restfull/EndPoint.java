package com.loopme.webapp.restfull;

import com.google.inject.Inject;
import com.loopme.webapp.model.dto.Advertise;
import com.loopme.webapp.model.dto.AdvertiseBunch;
import com.loopme.webapp.model.dto.AdvertiseRequestEvent;
import com.loopme.webapp.services.AppService;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
@Path("/ads")
public class EndPoint {

    static Logger Log = Logger.getLogger(EndPoint.class.getName());

    @Inject
    private AppService service;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public AdvertiseBunch echo(AdvertiseRequestEvent request) {
        Log.debug(String.format("Got request: %s", request));

        delayInMiliseconds(20);

        List<Advertise> advertises = service.proposeAdvertises(request);

        return new AdvertiseBunch(advertises);
    }

    private void delayInMiliseconds(int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }
}
