/*
 * Copyright 2015 CrudFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ch.codebulb.crudfaces;

import ch.codebulb.crudfaces.util.FacesHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import org.omnifaces.eventlistener.DefaultServletContextListener;
import org.omnifaces.util.Messages;

/**
 * An implementation of a {@link DefaultServletContextListener} which configures CrudFaces startup.
 * 
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
@WebListener
public class ApplicationListener extends DefaultServletContextListener {
    
    private static final Logger LOGGER = Logger.getLogger(ApplicationListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logVersion();
        initOmniFacesMessagesResolver();
    }
    
    // TODO Don't use hard-coded version
    private void logVersion() {
        LOGGER.log(Level.INFO, "Using CrudFaces version {0}", "0.1");
    }
    
    // as explained in http://showcase.omnifaces.org/utils/Messages
    private void initOmniFacesMessagesResolver() {
        Messages.setResolver(new Messages.Resolver() {
            @Override
            public String getMessage(String message, Object... params) {
                return FacesHelper.i18n(message, params);
            }
        });
    }
}