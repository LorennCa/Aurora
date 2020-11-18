angular.module('aurora.userProfileService', [])
    //Service main


    .factory('userService', function (globalConstant, $mdMedia, $mdDialog, localStorageService, resourceAccessService) {

        console.log(" localStorage.length" + localStorage.length);

        var userUtil = {};
        var festivos = localStorageService.get('setFestivos');
        var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && true;

        //cargar datos de usuario para modificar
        userUtil.setUserData = function (data) {

            userUtil.userData = data;
        }
        userUtil.getUserData = function () {

            return userUtil.userData;
        }
        //id perfil modificar

        userUtil.setIdPerfil = function (data) {

            userUtil.IdPerfil = data;
        }
        userUtil.getIdPerfil = function () {

            return userUtil.IdPerfil;
        }

        // Datos Seleccionados
        userUtil.setSelected = function (data) {

            userUtil.selected = data;
        }
        userUtil.getSelected = function () {

            return userUtil.selected;
        }

        userUtil.DialogController = function ($scope, $mdDialog) {

            $scope.hide = function () {
                return $mdDialog.hide();
            };
            $scope.cancel = function () {
                return $mdDialog.cancel();
            };
            $scope.answer = function (answer) {
                return $mdDialog.hide(answer);
            };

        }




        /*****************************************
         * VALIDACIONES CONTRASEÑAS
         ******************************************/
        //valida caracteres consecutivos iguales
        userUtil.caracteresConsecutivos = function (cadena) {
            result = true;
            for (i = 0; i < cadena.length; i++) {
                var x = i + 1;
                var y = i + 2;
                if (cadena.charAt(i) == cadena.charAt(x) && cadena.charAt(i) == cadena.charAt(y)) {
                    return result = false;
                }
            }
            return result = true;

        }
        // valida que contraseña tenga al menos un caracter
        userUtil.validaunCaracter = function (cadena) {

            caracter = false;

            for (i = 0; i < cadena.length; i++) {
                if (!cadena.valueOf(cadena.charAt(i)).match(globalConstant.patternAlfanumerico)) {
                    caracter = true;
                    break;
                }
            }

            return caracter;
        }
        // caracteres consecutivos 
        userUtil.validarSecuen = function (password) {
            //console.log("password" + password)
            //letras ascedente
            result = true;
            var abecedario = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
            password = password.toUpperCase();
            //console.log("passwUpper" + password);
            for (j = 0; j < password.length - 2; j++) {
                var chrs = password.charAt(j);
                //console.log("chrs" + chrs)
                var valor = password.charAt(j + 1);
                //console.log("valor" +  valor)
                if (isDigit(chrs) && isDigit(valor) && isDigit(password.charAt(j + 2))) {
                    //console.log("es digito")
                    for (i = 0; i < abecedario.length; i++) {
                        //console.log("entro a for_ abecedario.length " + abecedario.length )
                        if (chrs == abecedario[i]) {
                            //console.log("entro por igual abecedario")
                            if (valor == abecedario[i + 1]) {
                                //console.log("entro por igual abecedario")
                                // console.log(password.charAt(j + 2) + "___" + abecedario[i + 2])
                                if (password.charAt(j + 2) == abecedario[i + 2]) {
                                    result = false;
                                    break;
                                }
                            }

                        }
                    }
                }
            }
            //letras descente
            for (j = 0; j < password.length - 2; j++) {
                var chrs = password.charAt(j);
                //console.log("chrs" + chrs)
                var valor = password.charAt(j + 1);
                //console.log("valor" +  valor)
                //console.log("password.charAt(j + 2)" + password.charAt(j + 2))
                if (isDigit(chrs) && isDigit(valor) && isDigit(password.charAt(j + 2))) {
                    //console.log("es digito")
                    for (i = 0; i < abecedario.length; i++) {
                        //console.log("entro a for_ abecedario.length " + abecedario[i] )
                        if (chrs == abecedario[i]) {
                            //console.log("entro por igual abecedario chrs == abecedario[i]" +  abecedario[i])
                            if (valor == abecedario[i - 1]) {
                                //console.log("entro por igual abecedario" + abecedario[i - 1])
                                // console.log(password.charAt(j + 2) + "___" + abecedario[i - 2])
                                if (password.charAt(j + 2) == abecedario[i - 2]) {
                                    result = false;
                                    break;
                                }
                            }

                        }
                    }
                }
            }


            //ascendente numerico
            for (j = 0; j < password.length - 2; j++) {
                var chrs = password.charAt(j);
                //console.log("chrs" + chrs)
                var valor = password.charAt(j + 1);
                //console.log("valor" +  valor)
                if (isDigit(chrs) && isDigit(valor) && isDigit(password.charAt(j + 2))) {
                    //console.log("es digito 2_" + (parseInt(chrs) + 1) + "*" + (parseInt(valor) + 1 ) + "*"+ password.charAt(j + 2) )
                    if (((parseInt(chrs) + 1) == valor && (parseInt(valor) + 1)) == password.charAt(j + 2)) {
                        result = false;
                    }
                }
            }

            //descente numerico
            for (j = 0; j < password.length - 2; j++) {
                var chrs = password.charAt(j);
                // console.log("chrs" + chrs)
                var valor = password.charAt(j + 1);
                // console.log("valor" +  valor)
                if (isDigit(chrs) && isDigit(valor) && isDigit(password.charAt(j + 2))) {
                    // console.log("es digito 2_" + (parseInt(chrs) - 1) + "*" + (parseInt(valor) - 1 ) + "*"+ password.charAt(j + 2) )
                    if (((parseInt(chrs) - 1) == valor && (parseInt(valor) - 1)) == password.charAt(j + 2)) {
                        result = false;
                    }
                }
            }


            return result;

        }


        function isDigit(aChar) {
            myCharCode = aChar.charCodeAt(0);

            if (((myCharCode > 47) && (myCharCode < 58)) || ((myCharCode > 64) && (myCharCode < 91))) {
                return true;
            }

            return false;
        }


        // validar longitud Contraseña
        userUtil.validaLongitudContra = function (cadena) {

            longitud = false;

            if (cadena.length < 8) {
                longitud = false;
            } else if (cadena.length <= 15) {
                longitud = true;
            } else {
                longitud = false;
            }

            return longitud;
        }

        // Fin validaciones contraseña  

        /***********************************
         * VALIDACION DV NIT
         ************************************/

        userUtil.validarRut = function (rut) {
            result = 0, sumatoria = 0;
            var nString = rut;
            if (rut != undefined) {
                while (nString.length < 15) {
                    nString = "0" + nString;
                }
                primos = [3, 7, 13, 17, 19, 23, 29, 37, 41, 43, 47, 53, 59, 67, 71];
                for (i = 0; i < primos.length; i++) {
                    sumatoria += parseInt(nString.substring(i, i + 1)) * primos[primos.length - (i + 1)];
                }
                result = sumatoria % 11;
                if (result > 1) {
                    result = 11 - result;
                }
                return result;
            }


        }

        /************************************
         * Validaciones UI-Tree
         ************************************/
        userUtil.selectChildren = function (children, val) {
            //console.log("selectChildren" + JSON.stringify(children) + val)
            //set as selected
            children.isSelected = val;
            if (children.children) {
                //recursve to set all children as selected
                children.children.forEach(function (el) {
                    userUtil.selectChildren(el, val);
                })
            }
        }

        userUtil.findParent = function (node, parent, targetNode, cb) {
            //  console.log("node_" + JSON.stringify(node) +  "parent" + JSON.stringify(parent) + "targetNode" + JSON.stringify(targetNode) + "cb" + cb)
            if (_.isEqual(node, targetNode)) {
                cb(parent);
                return;
            }
            //console.log("node.children" + JSON.stringify(node.children) + "node_" + node)
            if (node.children) {
                node.children.forEach(function (item) {
                    userUtil.findParent(item, node, targetNode, cb);
                });
            }
        }

        userUtil.getAllChildren = function (node, arr) {
            if (!node) return;
            arr.push(node);

            if (node.children) {
                //if the node has children call getSelected for each and concat to array
                node.children.forEach(function (childNode) {
                    arr = arr.concat(userUtil.getAllChildren(childNode, []))
                })
            }
            return arr;
        }


        /***************************************
         * VALIDACIONES DE FECHA: 
         * Festivos
         * Rango de fechas
         * Fines de semana
         ***************************************/


        userUtil.dateHourValid = function (fechaIni, fechaFin, hourIni, hourFin, flagFds, today) {

            if (userUtil.isDateValid(fechaIni, fechaFin, today)) {
                console.info("fuera de rango de fechas de ingreso de operacion");
                $mdDialog.show({
                    controller: userUtil.DialogController,
                    template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Esta intentando acceder en una fecha no permitida</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false,
                    fullscreen: useFullScreen,
                    multiple: true

                })
            } else {
                console.info("dentro de rango de fechas de ingreso de la operacion");
                if (userUtil.isHourValid(hourIni, hourFin, today)) {
                    console.info("fuera de rango de horas");
                    $mdDialog.show({
                        controller: userUtil.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Esta intentando acceder en un horario no permitido</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        fullscreen: useFullScreen,
                        multiple: true

                    })
                } else {
                    console.info("dentro del rango de hora");
                    if (userUtil.isFestivo(today)) {
                        $mdDialog.show({
                            controller: userUtil.DialogController,
                            template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Los días festivos no se encuentra habilitada la negociación</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                            parent: angular.element(document.body),
                            clickOutsideToClose: false,
                            fullscreen: useFullScreen,
                            multiple: true

                        })
                    } else {
                        if (flagFds) {
                            return true;
                        } else {
                            if (userUtil.fdsValid(today)) {
                                console.info("fds no puede acceder")
                                $mdDialog.show({
                                    controller: userUtil.DialogController,
                                    template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Los Fines de semana no se encuentra habilitada la negociación</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                                    parent: angular.element(document.body),
                                    clickOutsideToClose: false,
                                    fullscreen: useFullScreen,
                                    multiple: true

                                })
                            } else {
                                console.info("condiciones ok")
                                return true;
                            }

                        }
                    }

                }

            }


        }
        //metodo usado para validar acceso dentro de adjudicacion - derechos de preferencia
        /*
        *La adjudicación se puede realizar cuando:
        1.Despues de la hora de finalización de ingreso, eliminación y modificación de demandas
        2.Despues de la fecha de finalización de ingreso, eliminación y modificación de demandas
        */
        userUtil.dateHourInvalid = function (fechaIni, fechaFin, hourIni, hourFin, today) {

            console.log("Entro a validar fecha/hora");
            var day = moment(today).format('YYYY-MM-DD');
            var fecIni = moment(fechaIni).format('YYYY-MM-DD');
            var fecFin = moment(fechaFin).format('YYYY-MM-DD');
            console.log("fechas" + fecIni + " ** " + fecFin + "** " + day)
            var day1 = moment(today).format('1970-01-01THH:mm:SS.sss-05:00');
            var todayHour = moment(day1).unix()
            var horIn = moment(hourIni).unix();
            var horFin = moment(hourFin).unix();

            if ((moment(day).isBetween(fecIni, fecFin, null, '[]'))) {
                if (moment(day).isSame(fecFin)) {
                    if ((todayHour > horIn) && (todayHour < horFin)) {
                        console.log("No puede adjudicar, porque se encuentra horario abierto");
                        $mdDialog.show({
                            controller: userUtil.DialogController,
                            template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Adjudicación no permitida: horario de ingreso de demandas se encuentra abierto</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                            parent: angular.element(document.body),
                            clickOutsideToClose: false,
                            fullscreen: useFullScreen,
                            multiple: true

                        })

                    } else {
                        console.log("puede adjudicar, horario cerrado");
                        return true;
                    }
                } else {
                    console.log("No puede ajudicar,porque se encuentra dentro de las fechas de ingreso");
                    $mdDialog.show({
                        controller: userUtil.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Adjudicación no permitida: El periodo de ingreso de aceptaciones se encuentra abierto</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        fullscreen: useFullScreen,
                        multiple: true

                    })
                }

            } else {
                console.log("puede adjudicar, fuera de las fechas de ingreso");
                return true;


            }

        }


        userUtil.isFestivo = function (today) {
            //var today = moment().format('YYYY-MM-DD');  
            var day = moment(today).format('YYYY-MM-DD');
            console.log(day)
            for (i in festivos) {
                var idx = festivos[i].indexOf(day);
                if (idx > -1) {
                    console.log("festivo");
                    return true;
                    break;
                } else {
                    console.log("no festivo");
                }
            }
            return false;
        }




        userUtil.isDateValid = function (fechaIni, fechaFin, today) {
            console.log("fechaIni" + fechaIni + "**" + fechaFin + "**" + today);


            var day = moment(today).format('YYYY-MM-DD');

            var fecIni = moment(fechaIni).format('YYYY-MM-DD');
            var fecFin = moment(fechaFin).format('YYYY-MM-DD');
            if (moment(day).isBefore(fechaIni) || moment(day).isAfter(fechaFin)) {
                //if(day < (fecIni) || day > (fecFin)){

                return true;

            }

            return false;



        }


        userUtil.isHourValid = function (horaIni, horaFin, today) {
            console.log("HoraIni" + horaIni + "*horaFin" + horaFin);
            //var today = moment().format('1970-01-01THH:mm:SS.sss-05:00');
            var day = moment(today).format('1970-01-01THH:mm:SS.sss-05:00');
            var todayHour = moment(day).unix()
            var horIn = moment(horaIni).unix();
            var horFin = moment(horaFin).unix();

            //console.log("hours" + horIn + '*'+ horFin + '*'+ todayHour + '*' + day)
            if (todayHour < horIn || todayHour > horFin) {
                return true;
            }
            return false;
        }

        userUtil.fdsValid = function (today) {
            //var today = moment().format('dddd'); 
            var day = moment(today).format('dddd');
            console.log("hoy_" + day)
            if (day == 'Saturday' || day == 'Sunday' || day == 'Sábado' || day == 'Domingo') {
                return true;
            }
            return false;
        }

        /*********************************************************
         *Función para validar si la fecha enviada es igual a día
         *********************************************************/
        userUtil.todayisValid = function (fecha, texto, today) {
            //var today = moment().format('YYYY-MM-DD');
            var day = moment(today).format('YYYY-MM-DD');
            // console.log("fechas " + moment(fecha).format('YYYY-MM-DD') + "***" +  day)
            if (moment(fecha).format('YYYY-MM-DD') == day) {
                console.log("es del mismo dia")
                return true;
            } else {
                console.log(" no es del mismo dia")
                $mdDialog.show({
                    controller: userUtil.DialogController,
                    template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Esta operación solo permite la ' + texto + ' de las demandas ingresadas el día de hoy</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false,
                    fullscreen: useFullScreen,
                    multiple: true

                })
                return false;
            }


        }


        return userUtil;

    })

/***********************Fin validaciones fecha Ingreso al sistema********************************/

aurora.directive('validNumber', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attr, ngModelCtrl) {
            function fromUser(text) {
                if (text) {
                    var transformedInput = text.replace(/[^0-9\.]/g, '');

                    if (transformedInput !== text) {
                        ngModelCtrl.$setViewValue(transformedInput);
                        ngModelCtrl.$render();
                    }
                    return transformedInput;
                }
                return false;
            }
            ngModelCtrl.$parsers.push(fromUser);
        }
    };
});
// filtro  para eliminar duplicados
aurora.filter('unique', function () {
    // we will return a function which will take in a collection
    // and a keyname

    return function (collection, keyname) {
        // we define our output and keys array;
        var output = [],
            keys = [];

        // we utilize angular's foreach function
        // this takes in our original collection and an iterator function
        angular.forEach(collection, function (item) {
            // we check to see whether our object exists
            var key = item[keyname];
            //console.log("key" + key + item[keyname]);
            // if it's not already part of our keys array
            if (keys.indexOf(key) === -1) {
                keys.push(key);
                // push this item to our final output array
                // add it to our keys array
                output.push(item);
            }
        });
        // return our array which should be devoid of
        // any duplicates

        //console.log("collec" + JSON.stringify(collection) + "keyname" + keyname)
        //console.log("output" + JSON.stringify(output))
        return output;

    };
})

/***************************************************************************************************/
/**Directiva para restringir el input de caracteres especiales, y permitir solamente alfanuméricos**/
/***************************************************************************************************/
aurora.directive("regExInput", function () {
    "use strict";
    return {
        restrict: "A",
        require: "?regEx",
        scope: {},
        replace: false,
        link: function (scope, element, attrs, ctrl) {
            element.bind('keypress', function (event) {
                var regex = new RegExp(attrs.regEx);
                var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
                if (!regex.test(key)) {
                    event.preventDefault();
                    return false;
                }
            });
        }
    };
});

/*********************************************************************************/
/**Directiva para restringir el input de caracteres cuando son más que maxlength**/
/*********************************************************************************/
aurora.directive('inputMaxlength', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attrs, ngModelCtrl) {
            var maxlength = Number(attrs.inputMaxlength);

            function fromUser(text) {
                if (text.length > maxlength) {
                    var transformedInput = text.substring(0, maxlength);
                    ngModelCtrl.$setViewValue(transformedInput);
                    ngModelCtrl.$render();
                    return transformedInput;
                }
                return text;
            }
            ngModelCtrl.$parsers.push(fromUser);
        }
    };
});


// This filter makes the assumption that the input will be in decimal form (i.e. 17% is 0.17).
aurora.filter('percentage', ['$filter', function ($filter) {
    return function (input, decimals) {
        return $filter('number')(input * 100, decimals) + '%';
    };
}]);


/******************************************************************
 *Directiva para validar que fecha fin no es menor que fecha inicio
 ******************************************************************/
aurora.directive('dateValidate', function () {
    return {
        require: 'ngModel',

        link: function (scope, element, attrs, ngModel) {

            var checker = function () {

                //get the value of the first date
                var e1 = scope.$eval(attrs.ngModel);

                //get the value of the other date  
                var e2 = scope.$eval(attrs.dateValidate);
                //console.log(moment(e2).format('L') + moment(e1).format('L') + (moment(e2).format('L') > moment(e1).format('L')));
                return moment(e2).isAfter(e1);

            };
            scope.$watch(checker, function (n) {

                //set the form control to valid if  
                //date Ini is  greather than date fin , else invalid
                ngModel.$setValidity("dateValidate", !n);
            });

        }
    }

});



/******************************************************************
 *Directiva para validar que hora fin no es menor que hora inicio
 ******************************************************************/

aurora.directive('hourValidate', function () {
    return {
        require: 'ngModel',

        link: function (scope, element, attrs, ngModel) {

            var checker = function () {

                //get the value of the first date
                var e1 = scope.$eval(attrs.ngModel);

                //get the value of the other date  
                var e2 = scope.$eval(attrs.hourValidate);
                //console.log("hour" + moment(e1) +"**"+moment(e2))
                //console.log(moment(e2).format('LT') + moment(e1).format('LT') + (moment(e2) > moment(e1)));
                return (moment(e2) > moment(e1));
            };
            scope.$watch(checker, function (n) {

                //set the form control to valid if  
                //date Ini is  greather than date fin , else invalid
                ngModel.$setValidity("hourValidate", !n);
            });

        }
    }

});

/********************************************************
 *Directiva para validar que una fecha se encuentre dentro 
 * un rango de fechas
 *********************************************************/
aurora.directive('dateRangeValidate', function () {
    return {
        require: 'ngModel',
        multiElement: true,
        link: function (scope, element, attrs, ngModel) {

            var checker = function () {

                //get the value of the first date
                var e1 = scope.$eval(attrs.ngModel);

                //get the value of the other date(start, end)  
                var e2 = scope.$eval(attrs.dateRangeValidate);
                //console.log("e2" + e2[0] + "**" + e2[1])  
                if (e1 !== undefined) {
                    //return (moment(e1).format('L') < moment(e2[0]).format('L') || moment(e1).format('L') > moment(e2[1]).format('L') );        
                    return moment(e1).isBefore(e2[0]) || moment(e1).isAfter(e2[1]);
                }
                return false;

            };
            scope.$watch(checker, function (n) {

                //set the form control to valid if  
                //date Ini is  greather than date fin , else invalid
                ngModel.$setValidity("dateRangeValidate", !n);
            });

        }
    }

});


/******************************************************************
 *Directiva para validar que una fecha no sea mayor a otra
 ******************************************************************/
aurora.directive('dateGreater', function () {
    return {
        require: 'ngModel',

        link: function (scope, element, attrs, ngModel) {

            var checker = function () {

                //get the value of the first date
                var e1 = scope.$eval(attrs.ngModel);

                //get the value of the other date  
                var e2 = scope.$eval(attrs.dateGreater);
                //console.log(moment(e2).format('L') + moment(e1).format('L') + ( moment(e1).isBefore(e2)));
                if (e1 !== undefined) {
                    return (moment(e1).isBefore(e2));
                }
                return false;

            };
            scope.$watch(checker, function (n) {

                //set the form control to valid if  
                //date Ini is  greather than date fin , else invalid
                ngModel.$setValidity("dateGreater", !n);
            });

        }
    }

});





/******************************************************************
 *Directiva para validar Digito de verificación para nit
 ******************************************************************/
aurora.directive('dvValidate', function (userService) {
    return {
        require: 'ngModel',

        link: function (scope, element, attrs, ngModel) {

            var checker = function () {

                //get the value modelo nit 
                var e1 = scope.$eval(attrs.ngModel);
                //console.log("e1" + e1)

                //get the value of the numero documento
                var e2 = scope.$eval(attrs.dvValidate);
                //console.log(moment(e2).format('L') + moment(e1).format('L') + (moment(e2).format('L') > moment(e1).format('L')));

                if (e2 !== undefined && e2[0] == 2) {
                    //console.log("e2[0]" + e2[0] +  e2[1]+ (userService.validarRut(e2[1]) != e1))
                    if (e1 !== undefined) {
                        return (userService.validarRut(e2[1]) != e1);
                    }

                }
                return false;

            };
            scope.$watch(checker, function (n) {

                //set the form control to valid if  
                //dv is valid , else invalid

                ngModel.$setValidity("dvValidate", !n);
            });

        }
    }

});


/******************************************************************
 *Directiva para validar estructura de correo electronico
 ******************************************************************/
aurora.directive('regexmail', function (globalConstant) {
    return {
        require: 'ngModel',

        link: function (scope, element, attrs, ngModel) {
            var checker = function () {
                // crear the regex obj.
                var regex = new RegExp(globalConstant.patternEmail);
                var e1 = scope.$eval(attrs.ngModel);
                //console.log("regex" + regex + e1 +regex.test(e1));
                return regex.test(e1);

            }

            scope.$watch(checker, function (n) {
                ngModel.$setValidity("regexmail", n);
            });
        }

    }
});


/******************************************************************
 *Directiva para validar que tipo de dato acepta un campo de acuerdo
 *al numero de documento:
 * 1,2,3: Numerico
 * 4,5,6: Alfanumerico
 ******************************************************************/
aurora.directive('regexDoc', function (globalConstant) {
    return {
        require: 'ngModel',

        link: function (scope, element, attrs, ngModel) {

            var checker = function () {

                // crear the regex obj alfanumerico.
                var regexAlf = new RegExp(globalConstant.patternAlfanumerico);
                // crear the regex obj numerico .
                var regexNum = new RegExp(globalConstant.patternNumerico);
                //get the value of the ducument value
                var e1 = scope.$eval(attrs.ngModel);
                //get the value of the document type
                var e2 = scope.$eval(attrs.regexDoc);

                //console.log(e2 + e1 + regexAlf + regexNum);
                if (e2 !== "" && e1 !== "") {
                    if (e2 == 1 || e2 == 2 || e2 == 3) {
                        return regexNum.test(e1);
                    } else if (e2 == 4 || e2 == 5 || e2 == 6) {
                        return regexAlf.test(e1);
                    }
                }

                return true;

            };
            scope.$watch(checker, function (n) {

                //set the form control to valid if  
                ngModel.$setValidity("regexDoc", n);
            });

        }
    }

});


/******************************************************************
 *Directiva validar que un campo sea mayor a cero
 ******************************************************************/
aurora.directive('noCero', function (globalConstant) {
    return {
        require: 'ngModel',

        link: function (scope, element, attrs, ngModel) {
            var checker = function () {
                var e1 = scope.$eval(attrs.ngModel);
                //console.log("regex" + e1 );
                if (e1 !== "") {
                    return e1 != 0;
                } else return true;

            }

            scope.$watch(checker, function (n) {
                ngModel.$setValidity("noCero", n);
            });
        }

    }
});
