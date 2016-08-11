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

/**
 * Created by Volodymyr Dema. Will see.
 */
@Path("/hole")
public class EndPoint {

    static Logger Log = Logger.getLogger(EndPoint.class.getName());

    @Inject
    private AppService service;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public AdvertiseBunch echo(AdvertiseRequestEvent request) {
        Log.info(String.format("Got request: %s", request));

        List<Advertise> advertises = service.proposeAdvertises(request);

        return new AdvertiseBunch(advertises);
    }
}
