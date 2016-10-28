/*
 * Copyright (c) 2015 FC-ServiceStack Compulynx LTD.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are NOT permitted.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL COMPULYNX LTD BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.automobile.rest.utils;

import org.automobile.dal.log.AutomobileLogger;
import org.automobile.dal.log.impl.AutomobileLoggerImpl;
import org.automobile.models.ExPayload;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Kushal
 * @Date 27-Oct-16
 */
class CustomExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Context
    private HttpHeaders headers;

    public Response toResponse(WebApplicationException exception) {
        AutomobileLogger log = new AutomobileLoggerImpl(this.getClass());
        int status;
        ExPayload payload;
        if (null != exception.getMessage() && exception.getMessage().equalsIgnoreCase("Timeout Expired")) {
            status = 401;
            payload = new ExPayload(401, exception.getMessage());
            payload.setServerMessage("Oops!!Seems like your session has expired.");
        } else {
            status = exception.getResponse().getStatus();
            payload = new ExPayload(exception.getResponse().getStatus(), exception.getMessage());
            switch (exception.getResponse().getStatus()) {
                case 400: {
                    payload.setServerMessage("Oh Snap!!" + exception.getMessage());
                    break;
                }
                case 403: {
                    payload.setServerMessage("Oops!!Seems like you dont have permissions to perform this action");
                    break;
                }
                case 404: {
                    payload.setServerMessage("Oops!!This is not the resource you are looking for");
                    break;
                }
                case 500: {
                    payload.setServerMessage("Oops!!Looks like something went wrong");
                    break;
                }
                case 409: {
                    payload.setServerMessage("Oh Snap!!" + exception.getMessage());
                    break;
                }
                default: {
                    payload.setServerMessage("Oops!!Seems like we ran into some trouble");
                    break;
                }
            }
        }
        log.error(payload.getServerMessage(), exception);
        return Response.status(status)
                .entity(payload)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();

    }
}

