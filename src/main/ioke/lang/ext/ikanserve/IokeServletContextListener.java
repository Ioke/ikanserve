/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package ioke.lang.ext.ikanserve;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class IokeServletContextListener implements ServletContextListener {
    public static final String FACTORY_KEY = "ikanserve.factory";

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        IokeApplicationFactory factory = newApplicationFactory(ctx);
        ctx.setAttribute(FACTORY_KEY, factory);
        try {
            factory.init(ctx);
        } catch (Exception ex) {
            ctx.log("Error: application initialization failed", ex);
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        IokeApplicationFactory factory = (IokeApplicationFactory)ctx.getAttribute(FACTORY_KEY);
        if (factory != null) {
            factory.destroy();
            ctx.removeAttribute(FACTORY_KEY);
        }
    }

    protected IokeApplicationFactory newApplicationFactory(ServletContext context) {
        return new PooledIokeApplicationFactory(new DefaultIokeApplicationFactory());
    }
}// IokeServletContextListener
