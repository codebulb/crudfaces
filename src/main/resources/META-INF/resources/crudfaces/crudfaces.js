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
            $('table:not(.ui-datepicker-calendar)').addClass('table').addClass('table-striped').addClass('table-bordered');
            
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
            $('.ui-chkbox-box').addClass('fa').addClass('form-control');
            $('.ui-chkbox-icon').removeClass('ui-icon').removeClass('ui-c')
            
            // radiobuttons
            $('.ui-radiobutton-box:not(ui-state-active)').removeClass('fa').removeClass('fa-circle');
            $('.ui-radiobutton-box.ui-state-active').addClass('fa').addClass('fa-circle');
            
            // selectOneMenu
            $('.ui-selectonemenu-list').addClass('dropdown-menu');
            $('label.ui-selectonemenu-label').removeClass('ui-corner-all').removeClass('form-control');
            $('.ui-selectonemenu').addClass('btn').addClass('btn-default').addClass('dropdown-toggle').addClass('form-control');
            
            // calendar
            // Note: unfortunately, all calendar "datepicker" style classes get overridden dynamically at runtime again
            $('button.ui-datepicker-trigger > .ui-icon').addClass('fa').addClass('fa-calendar').removeClass('ui-icon').removeClass('ui-button-icon-left').removeClass('ui-icon-calendar');
            $('button.ui-datepicker-trigger > .ui-button-text').addClass('hidden');
            
            // table selection column header
            $('.ui-selection-column').addClass('text-center');
            
            // form: form-control-static
            $(".cf-formlayout-componentcell:has(span:not('.ui-icon'))").addClass('form-control-static');
            
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
            
            // calendar
            /*
             * Execute once (with every Ajax call) only, not on every document click.
             * Otherwise, buggy behavior is introduced where the calendar pops up on every click.
             */
            $('#ui-datepicker-div').css('display', 'table');
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

/*
 * Install PrimeFaces components localization
 * 
 * copied from https://code.google.com/p/primefaces/wiki/PrimeFacesLocales
 * (Warning: Don't use https://github.com/primefaces/primefaces/wiki/Locales - these are erroneous!)
 */
// American English
PrimeFaces.locales ['en_US'] = {
    closeText: 'Close',
    prevText: 'Previous',
    nextText: 'Next',
    monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December' ],
    monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ],
    dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
    dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Tue', 'Fri', 'Sat'],
    dayNamesMin: ['S', 'M', 'T', 'W ', 'T', 'F ', 'S'],
    weekHeader: 'Week',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix:'',
    timeOnlyTitle: 'Only Time',
    timeText: 'Time',
    hourText: 'Time',
    minuteText: 'Minute',
    secondText: 'Second',
    currentText: 'Current Date',
    ampm: false,
    month: 'Month',
    week: 'week',
    day: 'Day',
    allDayText: 'All Day',
    messages: {
        'javax.faces.component.UIInput.REQUIRED': '{0}: Validation Error: Value is required.',
        'javax.faces.converter.IntegerConverter.INTEGER': '{2}: \'{0}\' must be a number consisting of one or more digits.',
        'javax.faces.converter.IntegerConverter.INTEGER_detail': '{2}: \'{0}\' must be a number between -2147483648 and 2147483647 Example: {1}',
        'javax.faces.converter.DoubleConverter.DOUBLE': '{2}: \'{0}\' must be a number consisting of one or more digits.',
        'javax.faces.converter.DoubleConverter.DOUBLE_detail': '{2}: \'{0}\' must be a number between 4.9E-324 and 1.7976931348623157E308  Example: {1}',
        'javax.faces.converter.BigDecimalConverter.DECIMAL': '{2}: \'{0}\' must be a signed decimal number.',
        'javax.faces.converter.BigDecimalConverter.DECIMAL_detail': '{2}: \'{0}\' must be a signed decimal number consisting of zero or more digits, that may be followed by a decimal point and fraction.  Example: {1}',
        'javax.faces.converter.BigIntegerConverter.BIGINTEGER': '{2}: \'{0}\' must be a number consisting of one or more digits.',
        'javax.faces.converter.BigIntegerConverter.BIGINTEGER_detail': '{2}: \'{0}\' must be a number consisting of one or more digits. Example: {1}',
        'javax.faces.converter.ByteConverter.BYTE': '{2}: \'{0}\' must be a number between 0 and 255.',
        'javax.faces.converter.ByteConverter.BYTE_detail': '{2}: \'{0}\' must be a number between 0 and 255.  Example: {1}',
        'javax.faces.converter.CharacterConverter.CHARACTER': '{1}: \'{0}\' must be a valid character.',
        'javax.faces.converter.CharacterConverter.CHARACTER_detail': '{1}: \'{0}\' must be a valid ASCII character.',
        'javax.faces.converter.ShortConverter.SHORT': '{2}: \'{0}\' must be a number consisting of one or more digits.',
        'javax.faces.converter.ShortConverter.SHORT_detail': '{2}: \'{0}\' must be a number between -32768 and 32767 Example: {1}',
        'javax.faces.converter.BooleanConverter.BOOLEAN': '{1}: \'{0}\' must be \'true\' or \'false\'',
        'javax.faces.converter.BooleanConverter.BOOLEAN_detail': '{1}: \'{0}\' must be \'true\' or \'false\'.  Any value other than \'true\' will evaluate to \'false\'.',
        'javax.faces.validator.LongRangeValidator.MAXIMUM': '{1}: Validation Error: Value is greater than allowable maximum of \'{0}\'',
        'javax.faces.validator.LongRangeValidator.MINIMUM': '{1}: Validation Error: Value is less than allowable minimum of \'{0}\'',
        'javax.faces.validator.LongRangeValidator.NOT_IN_RANGE': '{2}: Validation Error: Specified attribute is not between the expected values of {0} and {1}.',
        'javax.faces.validator.LongRangeValidator.TYPE={0}': 'Validation Error: Value is not of the correct type.',
        'javax.faces.validator.DoubleRangeValidator.MAXIMUM': '{1}: Validation Error: Value is greater than allowable maximum of \'{0}\'',
        'javax.faces.validator.DoubleRangeValidator.MINIMUM': '{1}: Validation Error: Value is less than allowable minimum of \'{0}\'',
        'javax.faces.validator.DoubleRangeValidator.NOT_IN_RANGE': '{2}: Validation Error: Specified attribute is not between the expected values of {0} and {1}',
        'javax.faces.validator.DoubleRangeValidator.TYPE={0}': 'Validation Error: Value is not of the correct type',
        'javax.faces.converter.FloatConverter.FLOAT': '{2}: \'{0}\' must be a number consisting of one or more digits.',
        'javax.faces.converter.FloatConverter.FLOAT_detail': '{2}: \'{0}\' must be a number between 1.4E-45 and 3.4028235E38  Example: {1}',
        'javax.faces.converter.DateTimeConverter.DATE': '{2}: \'{0}\' could not be understood as a date.',
        'javax.faces.converter.DateTimeConverter.DATE_detail': '{2}: \'{0}\' could not be understood as a date. Example: {1}',
        'javax.faces.converter.DateTimeConverter.TIME': '{2}: \'{0}\' could not be understood as a time.',
        'javax.faces.converter.DateTimeConverter.TIME_detail': '{2}: \'{0}\' could not be understood as a time. Example: {1}',
        'javax.faces.converter.DateTimeConverter.DATETIME': '{2}: \'{0}\' could not be understood as a date and time.',
        'javax.faces.converter.DateTimeConverter.DATETIME_detail': '{2}: \'{0}\' could not be understood as a date and time. Example: {1}',
        'javax.faces.converter.DateTimeConverter.PATTERN_TYPE': '{1}: A \'pattern\' or \'type\' attribute must be specified to convert the value \'{0}\'', 
        'javax.faces.converter.NumberConverter.CURRENCY': '{2}: \'{0}\' could not be understood as a currency value.',
        'javax.faces.converter.NumberConverter.CURRENCY_detail': '{2}: \'{0}\' could not be understood as a currency value. Example: {1}',
        'javax.faces.converter.NumberConverter.PERCENT': '{2}: \'{0}\' could not be understood as a percentage.',
        'javax.faces.converter.NumberConverter.PERCENT_detail': '{2}: \'{0}\' could not be understood as a percentage. Example: {1}',
        'javax.faces.converter.NumberConverter.NUMBER': '{2}: \'{0}\' could not be understood as a date.',
        'javax.faces.converter.NumberConverter.NUMBER_detail': '{2}: \'{0}\' is not a number. Example: {1}',
        'javax.faces.converter.NumberConverter.PATTERN': '{2}: \'{0}\' is not a number pattern.',
        'javax.faces.converter.NumberConverter.PATTERN_detail': '{2}: \'{0}\' is not a number pattern. Example: {1}',
        'javax.faces.validator.LengthValidator.MINIMUM': '{1}: Validation Error: Length is less than allowable minimum of \'{0}\'',
        'javax.faces.validator.LengthValidator.MAXIMUM': '{1}: Validation Error: Length is greater than allowable maximum of \'{0}\'',
        'javax.faces.validator.RegexValidator.PATTERN_NOT_SET': 'Regex pattern must be set.',
        'javax.faces.validator.RegexValidator.PATTERN_NOT_SET_detail': 'Regex pattern must be set to non-empty value.',
        'javax.faces.validator.RegexValidator.NOT_MATCHED': 'Regex Pattern not matched',
        'javax.faces.validator.RegexValidator.NOT_MATCHED_detail': 'Regex pattern of \'{0}\' not matched',
        'javax.faces.validator.RegexValidator.MATCH_EXCEPTION': 'Error in regular expression.',
        'javax.faces.validator.RegexValidator.MATCH_EXCEPTION_detail': 'Error in regular expression, \'{0}\''
    }
};
// French
PrimeFaces.locales ['fr'] = {
    closeText: 'Fermer',
    prevText: 'Précédent',
    nextText: 'Suivant',
    monthNames: ['Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre' ],
    monthNamesShort: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Jun', 'Jul', 'Aoû', 'Sep', 'Oct', 'Nov', 'Déc' ],
    dayNames: ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'],
    dayNamesShort: ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'],
    dayNamesMin: ['D', 'L', 'M', 'M', 'J', 'V', 'S'],
    weekHeader: 'Semaine',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix:'',
    timeOnlyTitle: 'Choisir l\'heure',
    timeText: 'Heure',
    hourText: 'Heures',
    minuteText: 'Minutes',
    secondText: 'Secondes',
    currentText: 'Maintenant',
    ampm: false,
    month: 'Mois',
    week: 'Semaine',
    day: 'Jour',
    allDayText: 'Toute la journée'
};
// German
PrimeFaces.locales['de'] = {
    closeText: 'Schließen',
    prevText: 'Zurück',
    nextText: 'Weiter',
    monthNames: ['Januar', 'Februar', 'März', 'April', 'Mai', 'Juni', 'Juli', 'August', 'September', 'Oktober', 'November', 'Dezember'],
    monthNamesShort: ['Jan', 'Feb', 'MÃ¤r', 'Apr', 'Mai', 'Jun', 'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dez'],
    dayNames: ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'],
    dayNamesShort: ['Son', 'Mon', 'Die', 'Mit', 'Don', 'Fre', 'Sam'],
    dayNamesMin: ['S', 'M', 'D', 'M ', 'D', 'F ', 'S'],
    weekHeader: 'Woche',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'Nur Zeit',
    timeText: 'Zeit',
    hourText: 'Stunde',
    minuteText: 'Minute',
    secondText: 'Sekunde',
    currentText: 'Aktuelles Datum',
    ampm: false,
    month: 'Monat',
    week: 'Woche',
    day: 'Tag',
    allDayText: 'Ganzer Tag'
};
// Italian
PrimeFaces.locales ['it'] = {
     closeText: 'Chiudi',
     prevText: 'Precedente',
     nextText: 'Prossimo',
     monthNames: ['Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno', 'Luglio', 'Agosto', 'Settembre', 'Ottobre', 'Novembre', 'Dicembre' ],
     monthNamesShort: ['Gen', 'Feb', 'Mar', 'Apr', 'Mag', 'Giu', 'Lug', 'Ago', 'Set', 'Ott', 'Nov', 'Dic' ],
     dayNames: ['Domenica', 'Lunedì', 'Martedì', 'Mercoledì', 'Giovedì', 'Venerdì', 'Sabato'],
     dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab'],
     dayNamesMin: ['D', 'L', 'M', 'M ', 'G', 'V ', 'S'],
     weekHeader: 'Sett',
     firstDay: 1,
     isRTL: false,
     showMonthAfterYear: false,
     yearSuffix:'',
     timeOnlyTitle: 'Solo Tempo',
     timeText: 'Ora',
     hourText: 'Ore',
     minuteText: 'Minuto',
     secondText: 'Secondo',
     currentText: 'Data Odierna',
     ampm: false,
     month: 'Mese',
     week: 'Settimana',
     day: 'Giorno',
     allDayText: 'Tutto il Giorno'
 };
// Spanish
PrimeFaces.locales['es'] = {
    closeText: 'Cerrar',
    prevText: 'Anterior',
    nextText: 'Siguiente',
    monthNames: ['Enero','Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
    monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun','Jul','Ago','Sep','Oct','Nov','Dic'],
    dayNames: ['Domingo','Lunes','Martes','Miércoles','Jueves','Viernes','Sábado'],
    dayNamesShort: ['Dom','Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'],
    dayNamesMin: ['D','L','M','X','J','V','S'],
    weekHeader: 'Semana',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'Sólo hora',
    timeText: 'Tiempo',
    hourText: 'Hora',
    minuteText: 'Minuto',
    secondText: 'Segundo',
    currentText: 'Fecha actual',
    ampm: false,
    month: 'Mes',
    week: 'Semana',
    day: 'Día',
    allDayText : 'Todo el día'
};
// TODO Add more PrimeFaces translations