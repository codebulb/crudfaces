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
import java.io.IOException;
import java.util.Collection;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


/**
 * A class which contains Bootstrap-specific style classes and grid-units to build a form grid
 * within {@link FormLayoutRenderer}.
 *
 * @author Nicolas Hofstetter (codebulb.ch)
 * @since 0.1
 */
public class BootstrapFormLayoutProvider {
    public int getGroupRatio(int ratio, int groups) {
        return ratio / (getResolution() * groups / getResolution());
    }

    public String getContainerStyleClass() {
        return "form-horizontal";
    }


    public String getRowStyleClass() {
        return "row";
    }


    public String getGroupStyleClass() {
        return "form-group";
    }


    public int getResolution() {
        return 12;
    }
    
    public FormLayoutGridUnits createUnits(double md, double xl) {
        FormLayoutGridUnits ret = new FormLayoutGridUnits.BootstrapFormGridUnits(this);
        ret.sm = getResolution();
        ret.md = md;
        ret.lg = md;
        ret.xl = xl;
        return ret;
    }

    public FormLayoutGridUnits createUnitsForXLarge(double xl) {
        FormLayoutGridUnits ret = new FormLayoutGridUnits.BootstrapFormGridUnits(this);
        ret.sm = getResolution();
        ret.md = xl * 2;
        ret.lg = xl * 2;
        ret.xl = xl;
        return ret;
    }


    public FormLayoutGridUnits createUnits(double sm, double md, double lg, double xl) {
        FormLayoutGridUnits ret = new FormLayoutGridUnits.BootstrapFormGridUnits(this);
        ret.sm = sm;
        ret.md = md;
        ret.lg = lg;
        ret.xl = xl;
        return ret;
    }
    
    private FormLayoutGridUnits createButtonBarOffsetUnits(double xl, int groups) {
        FormLayoutGridUnits ret = new FormLayoutGridUnits.BootstrapFormGridOffsetUnits(this);
        ret.sm = 0;
        ret.md = xl * groups;
        ret.lg = xl * groups;
        ret.xl = xl;
        return ret;
    }
    
    private FormLayoutGridUnits getLeftoverPlace(FormLayoutGridUnits other) {
        return createUnits(
            getResolution()-other.sm, getResolution()-other.md, getResolution()-other.lg, getResolution()-other.xl);
    }

    public void writeButtonBar(FacesContext context, boolean gapBeforeButtonBar, int firstColspan, int groups, Collection<UIComponent> buttons) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        FormLayoutGridUnits offsetUnits = createButtonBarOffsetUnits(firstColspan, groups);
        FormLayoutGridUnits buttonBarUnits = getLeftoverPlace(offsetUnits);

        writer.startElement("div", null);
        writer.writeAttribute("class", 
                StringsHelper.join(" ").add(offsetUnits.getStyleClasses()).add(buttonBarUnits.getStyleClasses()).add("cf-formlayout-buttonbar").toString(), 
                null);
        for (UIComponent button : buttons) {
            button.encodeAll(context);
        }
        writer.endElement("div");
    }
    
    public String getMessageComponentSubCellStyleClass() {
        return "col-sm-8";
    }
    
    public String getMessageSubCellStyleClass() {
        return "col-sm-4";
    }
    
    public String getComponentClass(UIComponent component) {
        if (component instanceof HtmlOutputLabel) {
            return "control-label";
        }
        else if (component instanceof HtmlOutputText) {
            return "cf-formlayout-outputtext";
        }
        else {
            return null;
        }
    }
}
