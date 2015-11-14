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

/* jQuery plugins */
$.fn.removePrimeFaces = function() {this.attr('class', function(i, c){return c.replace(/ui-.*?\S+/g, '').trim();});};

var CrudFaces = {
    /*
     * Retrieves the PrimeFaces widget for the component with the clientId provided.
     * This works for components with explicit widgetVar attribute as well as with components
     * without this implicit attribute.
     */
    // based on http://stackoverflow.com/a/22955176
    getWidgetVarById : function(id) {
        id = id.replace("_input", "");
        for (var propertyName in PrimeFaces.widgets) {
          if (PrimeFaces.widgets[propertyName] != null && PrimeFaces.widgets[propertyName].id === id) {
            return PrimeFaces.widgets[propertyName];
          }
        }
     },
     
     styleClassChanges : {
        changePrimeFacesToBootstrap : function(data) {
            // buttons
            $('.ui-button').filter(function() {return !this.className.match(/btn-.*/);}).addClass('btn-default');
            $('.ui-button').addClass('btn').removeClass('ui-button');
            $('.ui-button-text-only').removeClass('ui-button-text-only');
            
            // input texts
            $('.ui-inputfield').addClass('form-control').removeClass('ui-inputfield').removeClass('ui-inputtext');
            
            // error on divs
            $('.has-error').removeClass('has-error').removeClass('cf-state-error-parent'); // reset "error";
            $('.ui-state-error').closest('.form-group').addClass('has-error');
            
            // dataTable
            $('table').addClass('table').addClass('table-striped').addClass('table-bordered');
            
            // dataTable selectable
            $('tr.ui-datatable-selectable').closest('table').addClass('table-hover'); // keep ui-datatable-selectable
            
            // dataTable sort icons
            $('.ui-icon-carat-2-n-s').removeClass('ui-icon').addClass('fa').addClass('fa-fw').addClass('fa-sort').removeClass('fa-sort-asc').removeClass('fa-sort-desc'); // reset
            $('.ui-icon-triangle-1-n').addClass('fa').addClass('fa-fw').addClass('fa-sort-asc').removeClass('fa-sort').removeClass('fa-sort-desc');
            $('.ui-icon-triangle-1-s').addClass('fa').addClass('fa-fw').addClass('fa-sort-desc').removeClass('fa-sort').removeClass('fa-sort-asc');
            
            // dataTable active row
            $('tr.ui-datatable-selectable').removeClass('active'); // reset "active"
            $('tr.ui-state-highlight').addClass('active'); // keep ui-state-highlight
            
            // button disable
            // cannot use .prop because of old jQuery shipped with PrimeFaces
            $(':not(ui-state-disabled)').removeClass('disabled'); // reset "active"
            $('.ui-state-disabled').addClass('disabled'); // keep ui-state-disabled
            
            // checkboxes
            $('.ui-chkbox-box:not(ui-state-active)').removeClass('fa').removeClass('fa-check');
            $('.ui-chkbox-box.ui-state-active').addClass('fa').addClass('fa-check');
            
            // radiobuttons
            $('.ui-radiobutton-box:not(ui-state-active)').removeClass('fa').removeClass('fa-circle');
            $('.ui-radiobutton-box.ui-state-active').addClass('fa').addClass('fa-circle');
            
            // table selection column header
            $('.ui-selection-column').addClass('text-center');
            
            // form: form-control-static
            $(".cf-formlayout-componentcell:has(span)").addClass('form-control-static');
            
            // dataTable paginator
            $('.ui-paginator > .ui-paginator-first > .ui-icon-seek-first').addClass('fa').addClass('fa-angle-double-left')
                    .removeClass('ui-icon').removeClass('ui-icon-seek-first').empty();
            $('.ui-paginator > .ui-paginator-prev > .ui-icon-seek-prev').addClass('fa').addClass('fa-angle-left')
                    .removeClass('ui-icon').removeClass('ui-icon-seek-prev').empty();
            $('.ui-paginator > .ui-paginator-next > .ui-icon-seek-next').addClass('fa').addClass('fa-angle-right')
                    .removeClass('ui-icon').removeClass('ui-icon-seek-next').empty();
            $('.ui-paginator > .ui-paginator-last > .ui-icon-seek-end').addClass('fa').addClass('fa-angle-double-right')
                    .removeClass('ui-icon').removeClass('ui-icon-seek-end').empty();
            $('.ui-paginator > .ui-paginator-first').removeClass('ui-state-default');
            $('.ui-paginator > .ui-paginator-prev').removeClass('ui-state-default');
            $('.ui-paginator > .ui-paginator-next').removeClass('ui-state-default');
            $('.ui-paginator > .ui-paginator-last').removeClass('ui-state-default');
            $('.ui-paginator-bottom').addClass('cf-pagination-bottom');
            $('.ui-paginator').addClass('pagination').removePrimeFaces();
            $('.pagination > .ui-paginator-pages > .ui-state-active').addClass('active');
            
            // dataTable row-editor
            $('.ui-row-editor.ui-helper-clearfix > .ui-icon.ui-icon-pencil').addClass('fa').addClass('fa-pencil');
            $('.ui-row-editor.ui-helper-clearfix > .ui-icon.ui-icon-check').addClass('fa').addClass('fa-check');
            $('.ui-row-editor.ui-helper-clearfix > .ui-icon.ui-icon-close').addClass('fa').addClass('fa-close');
            $('.ui-row-editor.ui-helper-clearfix').addClass('btn').addClass('btn-default').removeClass('ui-helper-clearfix');
            
            // tab panel
            $('ul.ui-tabs-nav').addClass('nav').addClass('nav-tabs').removeClass('ui-tabs-nav').removeClass('ui-helper-reset ');
            $('li:not(ui-state-active)').removeClass('active'); // reset "active"
            $('li.ui-state-active').addClass('active');
            
            // messages
            $('div').filter(function() {return this.className.match(/ui-messages?-.*/);}).addClass('alert');
            $('div').filter(function() {return this.className.match(/ui-messages?-warn/);}).addClass('alert-warning');
            $('div').filter(function() {return this.className.match(/ui-messages?-error/);}).addClass('alert-danger');
            $('div').filter(function() {return this.className.match(/ui-messages?.*/);}).removePrimeFaces();
            $('span').filter(function() {return this.className.match(/ui-messages?.*/);}).removePrimeFaces();
        },
        
        initChangePrimeFacesToBootstrap : function() {
            $(document).click(function(e) {CrudFaces.styleClassChanges.changePrimeFacesToBootstrap();});
        },
    },
    
    focus: function(id) {
        $('#' + id).focus().select();
    },
    
    /*
     * Fixes a bug in PrimeFaces Extensions' inputNumber component:
     * Recognizing emptyValue to hide the symbol doesn't work if the component
     * is backed by a primitive int value as it is not rendered as "empty String" but as "0".
     * 
     * The bugfix resolves this problem by removing the value "0" onblur.
     */
    primeFacesExtensionsInputNumberZeroToEmpty : function() {
        $(document).ready(function() {
            $("span.bugfix-pe-inputNumber-zeroToEmpty > input:first").each(function() {
                var match = /[0-9]+/.exec(this.value);
                if (match !== null && match[0] === "0") {
                    this.value = "";
                }
            });
        });
        $("span.bugfix-pe-inputNumber-zeroToEmpty > input:first").each(function() {
            var onblur = this.onblur;
            this.onblur = function(){
                var match = /[0-9]+/.exec(this.value);
                if (match !== null && match[0] === "0") {
                    this.value = "";
                }
                // keep original function
                if (onblur !== null) {
                    return onblur();
                }
            };
        });
    },
    
    /*
     * Applies all bugfixes at ones.
     * 
     * It is recommeded to place this inside
     * <o:onloadScript>Bugfixes.apply();</o:onloadScript>
     */
    apply : function() {
        Bugfixes.primeFacesExtensionsInputNumberZeroToEmpty();
    }
};