/*
 * Copyright 2015 CrudFaces / Nicolas Hofstetter (codebulb.ch).
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
package ch.codebulb.crudfaces.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple JSF navigation outcome URL builder with support for setting parameters and the "redirect" flag.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class Url {
    private final String outcome;
    private boolean redirect;
    private final List<Parameter> parameters = new ArrayList<>();

    public Url(String outcome) {
        this(outcome, false);
    }

    public Url(String outcome, boolean redirect) {
        this.outcome = outcome;
        this.redirect = redirect;
    }

    /**
     * Activates redirect.
     */
    public Url redirect() {
        this.redirect = true;
        return this;
    }

    /**
     * Appends the parameter provided.
     */
    public Url param(String name, Object value) {
        parameters.add(new Parameter(name, value.toString()));
        return this;
    }

    /**
     * Builds and returns the navigation outcome String.
     */
    public String navigateTo() {
        if (!redirect && parameters.size() == 0) {
            // allows to return null
            return outcome;
        }
        if (outcome == null) {
            // ignore redirect and parameters
            return outcome;
        }

        StringBuilder ret = new StringBuilder(outcome);
        char operator = '?';
        if (redirect) {
            ret.append(operator);
            ret.append("faces-redirect=true");
            operator = '&';
        }
        for (Parameter parameter : parameters) {
            ret.append(operator);
            ret.append(parameter.name);
            ret.append('=');
            ret.append(parameter.value);
            operator = '&';
        }

        return ret.toString();
    }
    
    protected static class Parameter {
        private final String name, value;

        public Parameter(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}