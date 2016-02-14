/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.compilatex;

import de.uniluebeck.compilatex.service.CORSFilter;
import de.uniluebeck.compilatex.service.JobFileService;
import de.uniluebeck.compilatex.service.JobService;
import de.uniluebeck.compilatex.service.LatexEnvironmentService;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
@ApplicationPath("/rest")
public class RestServices extends ResourceConfig {
    public RestServices() {
        packages("de.uniluebeck.compilatex.service");
        register(JobService.class);
        register(JobFileService.class);
        register(LatexEnvironmentService.class);
        register(CORSFilter.class);
        register(MultiPartFeature.class);
        register(MoxyJsonFeature.class);
    }
}
