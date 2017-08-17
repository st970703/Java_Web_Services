package nz.ac.auckland.concert.services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by st970 on 3/08/2017.
 */
@ApplicationPath("/services")
public class ConcertApplication extends Application {
    private Set<Object> _singletons = new HashSet<Object>();

    private Set<Class<?>> _classes = new HashSet<Class<?>>();

    @Override
    public Set<Class<?>> getClasses() {
        return _classes;
    }

    public ConcertApplication()
    {
        _classes.add(ConcertResource.class);
    }

    @Override
    public Set<Object> getSingletons()
    {
        _singletons.add(PersistenceManager.instance());

        // Return a Set containing an instance of ParoleeResource that will be
        // used to process all incoming requests on Parolee resources.
        return _singletons;
    }
}
