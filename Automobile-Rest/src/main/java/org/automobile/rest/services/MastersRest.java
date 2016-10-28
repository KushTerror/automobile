package org.automobile.rest.services;

import org.automobile.bal.MastersBal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Kushal
 * @Date 27-Oct-16
 */
@Component
@Path("/masters")
public class MastersRest {

    private final MastersBal mastersBal;
    @Context
    HttpHeaders headers;


    @Autowired
    public MastersRest(MastersBal mastersBal) {
        this.mastersBal = mastersBal;
    }

    @GET
    @Path("/years")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdjustmentReasons(@Context HttpServletRequest request) {
        try {
            return Response.status(200).entity(mastersBal.getYearMasters()).build();
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage());
        }
    }
}
