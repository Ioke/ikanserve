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
    private Runtime r;
    private ServletContext sc;

    @Override
    public void init() {
        sc = getServletConfig().getServletContext();
        try {
            System.err.println("*** IKanServe: creating runtime...");
            
            r = new Runtime();
            r.init();
            r.evaluateString("use(\"ikanserve.ik\")", r.message, r.ground);
            Object server = r.evaluateString("IKanServe", r.message, r.ground);
            IokeObject.setCell(server, null, null, "servletConfig", r.registry.wrap(getServletConfig()));
            IokeObject.setCell(server, null, null, "servletContext", r.registry.wrap(sc));

            System.err.println("*** IKanServe: runtime ready...");
        } catch(Exception e) {
            sc.log("error while loading application", e);
        } catch(ControlFlow cf) {
            sc.log("controlflow while loading application: " + cf);
        }
    }

    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) 
        throws ServletException, IOException {

        try {
            Object server = r.evaluateString("IKanServe mimic", r.message, r.ground);
            IokeObject.setCell(server, null, null, "request", r.registry.wrap(request));
            IokeObject.setCell(server, null, null, "response", r.registry.wrap(response));
            r.newMessage("dispatch").sendTo(r.ground, server);
        } catch(ControlFlow cf) {
            sc.log("controlflow while dispatching application: " + cf);
        }
    }
}// iokeservlet
