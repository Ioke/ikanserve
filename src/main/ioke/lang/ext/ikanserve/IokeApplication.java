/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package ioke.lang.ext.ikanserve;

import ioke.lang.Runtime;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface IokeApplication {
    void init();
    void destroy();
    Runtime getRuntime();
}// IokeApplication
