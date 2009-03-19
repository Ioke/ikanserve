/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package ioke.lang.ext.ikanserve;

import ioke.lang.Runtime;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class DefaultIokeApplication implements IokeApplication {
    private Runtime runtime;
    public DefaultIokeApplication(Runtime runtime) {
        this.runtime = runtime;
    }

    public void init() {
    }

    public void destroy() {
    }

    public Runtime getRuntime() {
        return runtime;
    }
}// DefaultIokeApplication
