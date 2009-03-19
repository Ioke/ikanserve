/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package ioke.lang.ext.ikanserve;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ioke.lang.Runtime;
import ioke.lang.IokeObject;
import ioke.lang.exceptions.ControlFlow;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class IokeServlet extends HttpServlet {
    private ServletContext sc;

    @Override
    public void init() {
        sc = getServletConfig().getServletContext();
    }

    private IokeApplicationFactory getIokeFactory() {
        return (IokeApplicationFactory)sc.getAttribute(IokeServletContextListener.FACTORY_KEY);
    }

    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) 
        throws ServletException, IOException {

        final IokeApplicationFactory factory = getIokeFactory();
        IokeApplication app = null;
        try {
            app = factory.getApplication();
            Runtime r = app.getRuntime();
            Object server = r.evaluateString("IKanServe mimic", r.message, r.ground);
            IokeObject.setCell(server, null, null, "request", r.registry.wrap(request));
            IokeObject.setCell(server, null, null, "response", r.registry.wrap(response));
            r.newMessage("dispatch").sendTo(r.ground, server);
        } catch(ControlFlow cf) {
            sc.log("controlflow while dispatching application: " + cf);
        } finally {
            if (app != null) {
                factory.finishedWithApplication(app);
            }
        }
    }
}// iokeservlet
