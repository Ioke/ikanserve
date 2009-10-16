/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package ioke.lang.ext.ikanserve;

import javax.servlet.ServletContext;

import ioke.lang.Runtime;
import ioke.lang.IokeObject;
import ioke.lang.exceptions.ControlFlow;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class DefaultIokeApplicationFactory implements IokeApplicationFactory {
    private ServletContext servletContext;

    public void init(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public IokeApplication newApplication() {
        final Runtime runtime = newRuntime();
        return new DefaultIokeApplication(runtime);
    }

    public IokeApplication getApplication() {
        IokeApplication app = newApplication();
        app.init();
        return app;
    }

    public void finishedWithApplication(IokeApplication app) {
        app.destroy();
    }

    public void destroy() {
    }

    public Runtime newRuntime() {
        try {
            System.err.println("*** IKanServe: creating runtime...");
            Runtime r = new Runtime();
            r.init();
            r.evaluateString("use(\"ikanserve.ik\")", r.message, r.ground);
            Object server = r.evaluateString("IKanServe", r.message, r.ground);
            IokeObject.setCell(server, null, null, "servletContext", r.registry.wrap(servletContext));
            System.err.println("*** IKanServe: runtime ready...");
            return r;
        } catch(Exception e) {
            servletContext.log("error while loading application", e);
        } catch(ControlFlow cf) {
            servletContext.log("controlflow while loading application: " + cf);
        }
        return null;
    }
}// DefaultIokeApplicationFactory
