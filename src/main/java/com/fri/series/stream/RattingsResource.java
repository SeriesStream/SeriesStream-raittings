package com.fri.series.stream;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.kumuluz.ee.fault.tolerance.annotations.CommandKey;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import javax.inject.Inject;
import java.util.Optional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.inject.Provider;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

@Produces(MediaType.APPLICATION_JSON)
@Path("rattings")
@Log
@ApplicationScoped
@CircuitBreaker
public class RattingsResource {

    private Logger log = LogManager.getLogger(RattingsResource.class.getName());

    @Inject
    @DiscoverService("series-stream-catalog")
    private Provider<Optional<String>> baseProvider;
    //private String baseUrl;

    private Client httpClient = ClientBuilder.newClient();

    @GET
    public Response getAllRattings() {
        List<Rattings> rattings = RattingsDatabase.getRattings();
        return Response.ok(rattings).build();
    }

    @GET
    @Timed(name = "get_episodes_long_lasting")
    @Path("{id}/episode")
    public Response getRattingdEpisode(@PathParam("id") int id) {
        Rattings ratting = RattingsDatabase.getRatting(id);
        if(ratting != null){
            return processParchesdEpisode(ratting.getEpisodeId());
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Timed(name = "get_episodes_long_lasting")
    @Path("user/{id}")
    public Response getRattings(@PathParam("id") int id) {
        List<Rattings> rattings = RattingsDatabase.getRattingsFromUser(id);
        if(rattings != null){
            return Response.ok(rattings).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @CommandKey("find-episode")
    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getRattingdEpisodeFallback")
    private Response processParchesdEpisode(int id) {
        Optional<String> baseUrl = baseProvider.get();
        if (baseUrl.isPresent()) {
            try {
                String link = baseUrl.get();
                System.out.println(link);
                return httpClient
                        .target(link + "/v1/episodes/" + id)
                        .request().get();
            } catch (WebApplicationException | ProcessingException e) {
                System.out.println("Error se je zgodil");
                log.error(e);
                System.out.println("Error se je zgodil");
                throw new InternalServerErrorException(e);
            }
        }
        log.error("baseUrl ni prisoten");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public Response getRattingdEpisodeFallback(int id){
        System.out.println("Napaka pri poizvedbi za epizodo z idijem" + id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id}")
    @Counted(name = "ratting_counter")
    public Response getRatting(@PathParam("id") int id) {
        Rattings ratting = RattingsDatabase.getRatting(id);
        if(ratting != null){
            return Response.ok(ratting).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response addRatting(Rattings ratting) {
        RattingsDatabase.addRatting(ratting);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteRatting(@PathParam("id") int id) {
        RattingsDatabase.deleteRatting(id);
        return Response.noContent().build();
    }
}
