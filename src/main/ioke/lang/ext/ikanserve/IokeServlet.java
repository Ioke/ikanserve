/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package ioke.lang.ext.ikanserve;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ioke.lang.Runtime;
import ioke.lang.exceptions.ControlFlow;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class IokeServlet extends HttpServlet {
    private Runtime r;

    @Override
    public void init() {
        try {
            System.err.println("*** IKanServe: creating runtime...");
            
            r = new Runtime();
            r.init();
            r.evaluateString("use(\"ikanserve.ik\")", r.message, r.ground);

            System.err.println("*** IKanServe: runtime ready...");
        } catch(Exception e) {
        } catch(ControlFlow cf) {}
    }

    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) 
        throws ServletException, IOException {

        try {
            // mimic IKanServe
            // set request and response
            // dispatch
            r.evaluateString("IKanServe dispatch", r.message, r.ground);
        } catch(ControlFlow cf) {}
    }
}// iokeservlet
