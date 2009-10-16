/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package ioke.lang.ext.ikanserve;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PooledIokeApplicationFactory implements IokeApplicationFactory {
    static final int DEFAULT_TIMEOUT = 30;

    private ServletContext servletContext;
    private IokeApplicationFactory factory;
    private Queue<IokeApplication> applicationPool = new LinkedList<IokeApplication>();
    private Integer initial, maximum;
    private long timeout = DEFAULT_TIMEOUT;
    private Semaphore permits;

    public PooledIokeApplicationFactory(IokeApplicationFactory factory) {
        this.factory = factory;
    }
    
    public void init(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.factory.init(servletContext);
        
        initial = getInitial();
        maximum = getMaximum();

        if (maximum != null) {
            if (initial != null && initial > maximum) {
                maximum = initial;
            }
            permits = new Semaphore(maximum, true);
        }

        if (initial != null) {
            fillInitialPool();
        }
    }

    public IokeApplication newApplication() {
        return getApplication();
    }

    public IokeApplication getApplication() {
        if (permits != null) {
            boolean acquired = false;
            try {
                acquired = permits.tryAcquire(timeout, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            if (!acquired) {
                throw new RuntimeException("timeout: all listeners busy", new InterruptedException());
            }
        }
        synchronized (applicationPool) {
            if (!applicationPool.isEmpty()) {
                return applicationPool.remove();
            }
        }

        return factory.getApplication();
    }

    public void finishedWithApplication(IokeApplication app) {
        synchronized (applicationPool) {
            if (maximum != null && applicationPool.size() >= maximum) {
                return;
            }
            applicationPool.add(app);
            if (permits != null) {
                permits.release();
            }
        }
    }

    public void destroy() {
        synchronized (applicationPool) {
            for(IokeApplication app : applicationPool) {
                app.destroy();
            }
        }
    }

    private void fillInitialPool() {
        createApplications();
    }

    private void createApplications() {
        for (int i = 0; i < initial; i++) {
            IokeApplication app = factory.newApplication();
            app.init();
            applicationPool.add(app);
        }
    }

    private Integer getInitial() {
        return getRangeValue("min", "minIdle");
    }

    private Integer getMaximum() {
        return getRangeValue("max", "maxActive");
    }

    private Integer getRangeValue(String end, String gsValue) {
        Integer v = getPositiveInteger("ioke." + end + ".runtimes");
        if (v == null) {
            v = getPositiveInteger("ioke.pool." + gsValue);
        }
        if (v == null) {
            servletContext.log("Warning: no " + end + " runtimes specified.");
        } else {
            servletContext.log("Info: received " + end + " runtimes = " + v);
        }
        return v;
    }

    private Integer getPositiveInteger(String string) {
        try {
            int i = Integer.parseInt(servletContext.getInitParameter(string));
            if (i > 0) {
                return new Integer(i);
            }
        } catch (Exception e) {
        }
        return null;
    }
}// PooledIokeApplicationFactory
