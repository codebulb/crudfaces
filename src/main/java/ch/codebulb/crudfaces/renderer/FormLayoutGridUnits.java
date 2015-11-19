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
package ch.codebulb.crudfaces.renderer;

import ch.codebulb.crudfaces.util.StringsHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple container to hold form grid units
 * for use within {@link FormLayoutRenderer}.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public abstract class FormLayoutGridUnits {
    protected final BootstrapFormLayoutProvider provider;
    
    protected double sm;
    protected double md;
    protected double lg;
    protected double xl;

    public FormLayoutGridUnits(BootstrapFormLayoutProvider provider) {
        this.provider = provider;
    }
    
    public String getStyleClasses() {
        List<String> styleClasses = new ArrayList<>();
        styleClasses.add(buildSmClass(max((int) (sm)), provider.getResolution()));
        if (md != sm) {
            styleClasses.add(buildMdClass(max((int) (md)), provider.getResolution()));
        }
        
        if (lg != md) {
            styleClasses.add(buildLgClass(max((int) (lg)), provider.getResolution()));
        }
        
        if (xl != lg) {
            styleClasses.add(buildXlClass(max((int) (xl)), provider.getResolution()));
        }
        
        return StringsHelper.join(styleClasses, " ");
    }

    protected abstract String buildLgClass(int md, int resolution);

    protected abstract String buildMdClass(int md, int resolution);

    protected abstract String buildSmClass(int sm, int resolution);

    protected abstract String buildXlClass(int md, int resolution);
    
    private int max(int original) {
        if (original > provider.getResolution()) {
            return provider.getResolution();
        }else {
            return original;
        }
    }
    
    public static class BootstrapFormGridUnits extends FormLayoutGridUnits {
        protected String offsetStyle = null;
        
        public BootstrapFormGridUnits(BootstrapFormLayoutProvider provider) {
            super(provider);
        }

        @Override
        protected String buildSmClass(int sm, int resolution) {
            StringBuilder sb = new StringBuilder();
            sb.append("col-xs-");
            if (offsetStyle != null) {
                sb.append(offsetStyle);
            }
            sb.append(sm);
            return sb.toString();
        }

        @Override
        protected String buildMdClass(int md, int resolution) {
            StringBuilder sb = new StringBuilder();
            sb.append("col-sm-");
            if (offsetStyle != null) {
                sb.append(offsetStyle);
            }
            sb.append(md);
            return sb.toString();
        }

        @Override
        protected String buildLgClass(int lg, int resolution) {
            StringBuilder sb = new StringBuilder();
            sb.append("col-md-");
            if (offsetStyle != null) {
                sb.append(offsetStyle);
            }
            sb.append(lg);
            return sb.toString();
        }

        @Override
        protected String buildXlClass(int xl, int resolution) {
            StringBuilder sb = new StringBuilder();
            sb.append("col-lg-");
            if (offsetStyle != null) {
                sb.append(offsetStyle);
            }
            sb.append(xl);
            return sb.toString();
        }
    }
    
    public static class BootstrapFormGridOffsetUnits extends BootstrapFormGridUnits {
        public BootstrapFormGridOffsetUnits(BootstrapFormLayoutProvider provider) {
            super(provider);
            super.offsetStyle = "offset-";
        }
        
    }
}
