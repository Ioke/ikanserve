/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package ioke.lang.ext.ikanserve;

import javax.servlet.ServletContext;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface IokeApplicationFactory {
    void init(ServletContext servletContext);
    IokeApplication newApplication();
    IokeApplication getApplication();
    void finishedWithApplication(IokeApplication app);
    void destroy();
}// IokeApplicationFactory
