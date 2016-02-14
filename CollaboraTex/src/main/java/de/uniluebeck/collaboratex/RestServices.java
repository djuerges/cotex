/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.collaboratex;

import de.uniluebeck.collaboratex.service.SessionService;
import de.uniluebeck.collaboratex.service.ProjectFileService;
import de.uniluebeck.collaboratex.service.ProjectService;
import de.uniluebeck.collaboratex.service.SearchService;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
@ApplicationPath("/rest")
public class RestServices extends ResourceConfig {
    public RestServices() {
        packages("de.uniluebeck.collaboratex.service");
        
        /* register services */
        register(ProjectService.class);
        register(ProjectFileService.class);
        register(SearchService.class);
        register(SessionService.class);
        
        /* register Jersey MultiPart support */
        register(MultiPartFeature.class);
        
//        unknown internal server error when using MOxy Serialisation
//        register(MoxyJsonFeature.class);

        /* register Jackson for JSON De-/Serialisation */
        register(JacksonFeature.class);
    }
}
