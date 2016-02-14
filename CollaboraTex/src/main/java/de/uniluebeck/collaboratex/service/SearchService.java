package de.uniluebeck.collaboratex.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
@Path("/search")
public class SearchService {

    private final RequestHandler requestHandler = new RequestHandler();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@FormParam("query") String query) {
        Logger.getLogger(SearchService.class.getName()).log(Level.WARNING, "search service called, searching for {0}", query);
        return requestHandler.handleSearch(query);
    }
}
