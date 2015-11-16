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
    
    public FormLayoutGridUnits minus(FormLayoutGridUnits other) {
        return provider.createUnits(
                sm - other.sm,
                md - other.md,
                lg - other.lg,
                xl - other.xl
        );
    }
    
    public String getStyleClasses() {
        StringBuilder sb = new StringBuilder();
        appendSmClass(sb, (int) (sm), provider.getResolution());
        sb.append(" ");
        if (md != sm) {
            appendMdClass(sb, (int) (md), provider.getResolution());
            sb.append(" ");
        }
        
        if (lg != md) {
            appendLgClass(sb, (int) (lg), provider.getResolution());
            sb.append(" ");
        }
        
        if (xl != lg) {
            appendXlClass(sb, (int) (xl), provider.getResolution());
            sb.append(" ");
        }
        
        return sb.toString();
    }

    protected abstract void appendLgClass(StringBuilder sb, int md, int resolution);

    protected abstract void appendMdClass(StringBuilder sb, int md, int resolution);

    protected abstract void appendSmClass(StringBuilder sb, int sm, int resolution);

    protected abstract void appendXlClass(StringBuilder sb, int md, int resolution);
    
    public static class BootstrapFormGridUnits extends FormLayoutGridUnits {
        protected String offsetStyle = null;
        
        public BootstrapFormGridUnits(BootstrapFormLayoutProvider provider) {
            super(provider);
        }

        @Override
        protected void appendSmClass(StringBuilder sb, int sm, int resolution) {
            sb.append("col-xs-");
            if (offsetStyle != null) {
                sb.append(offsetStyle);
            }
            sb.append(sm);
        }

        @Override
        protected void appendMdClass(StringBuilder sb, int md, int resolution) {
            sb.append("col-sm-");
            if (offsetStyle != null) {
                sb.append(offsetStyle);
            }
            sb.append(md);
        }

        @Override
        protected void appendLgClass(StringBuilder sb, int lg, int resolution) {
            sb.append("col-md-");
            if (offsetStyle != null) {
                sb.append(offsetStyle);
            }
            sb.append(lg);
        }

        @Override
        protected void appendXlClass(StringBuilder sb, int xl, int resolution) {
            sb.append("col-lg-");
            if (offsetStyle != null) {
                sb.append(offsetStyle);
            }
            sb.append(xl);
        }
    }
    
    public static class BootstrapFormGridOffsetUnits extends BootstrapFormGridUnits {
        public BootstrapFormGridOffsetUnits(BootstrapFormLayoutProvider provider) {
            super(provider);
            super.offsetStyle = "offset-";
        }
        
    }
}
