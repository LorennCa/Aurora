angular.module('aurora.userProfileController', ['ngIdle', 'ngMaterial'])

    .controller('userProfileController', function ($scope, $state, $mdMedia, $mdToast, $location, $mdSidenav, $mdDialog, mainService, loginService, resourceAccessService, userService, globalConstant, localStorageService) {


        // evita que vaya atras con el boton de atras
        history.pushState(null, null, location.href);
        window.onpopstate = function (event) {
            history.go(1);
        };

        loginService.validateSession();

        $scope.progressBar = false;
        /************************************
        parametros constantes en la peticion
        *************************************/
        $scope.userToken = localStorageService.get('setDataToken');
        $scope.userIp = localStorageService.get('setDataIp');
        $scope.userScope = localStorageService.get('setDataScope');
        $scope.validaToken = localStorageService.get('setUriToken');
        //perfil que inicia sesión
        $scope.IdProfile = localStorageService.get('setIdPerfil');


        $scope.requerido = globalConstant.requiredMessage;
        $scope.invalidCh = globalConstant.chInvalidMessage;
        $scope.validaAlfaNumericoEsp = globalConstant.patternAlfanumericoEsp;

        //obtener todas las opciones para el módulo de perfiles
        $scope.generalOpc = localStorageService.get('setGeneralOpc');
        //console.log("$scope.generalOpc_" + JSON.stringify(resourceAccessService.getGeneralOpc()))
        JSON.stringify($scope.generalOpc);

        //lista de perfiles
        $scope.items = $scope.generalOpc.listaPerfil;
        //console.log("items_" + JSON.stringify($scope.items))

        //opciones de menu
        $scope.opcmenu = $scope.generalOpc.menuOpciones;

        //console.log("$scope.opcmenu" + JSON.stringify($scope.generalOpc.menuOpciones))
        // recurso
        $scope.resource = localStorageService.get('setResource');
        //obtener url para validar si perfil se puede eliminar    
        $scope.perfilEliminar = localStorageService.get('setUriPerfilEliminar');
        //recursos especificos
        $scope.permisosurl = [];
        for (i = 0; i < $scope.generalOpc.permisos.length; i++) {
            $scope.permisosurl.push($scope.generalOpc.permisos[i].resource)
            //console.log("array_" + $scope.generalOpc.permisos[i].isSelected )
        }
        //console.log("array_" + $scope.permisosurl )

        $scope.selNueva = false;
        //formularios
        $scope.form = {};
        $scope.formC = {};
        $scope.perfilSuperior = {};
        $scope.item = [];
        $scope.selected = [];

        $scope.selectedTab = 0;
        $scope.fieldsDisabled = true;
        $scope.itemSel = "";

        $scope.nombreC = "";
        $scope.opcmenuC = [];
        $scope.perfilSuperiorC = {};

        $scope.formProfile = {};
        $scope.formProfileC = {};
        $scope.dataProfile = {};
        $scope.profileName;
        $scope.superiorProfile;
        $scope.menus = [];
        $scope.numSelected = 0;
        $scope.itemsToAdd = [];
        $scope.opcselect = false;

        $scope.options = {};
        $scope.hideButton = false;

        /*********************************
         *Inicializar Botones
         *habilitar o deshabilitar botones
         *********************************/
        $scope.opcHabNew = false;
        $scope.opcHabMod = false;
        $scope.opcHabDel = false;
        $scope.opcHabRep = false;
        $scope.selectAll = false;
        $scope.opcHabCon = true;

        /********************************************
        mostrar u ocultar opciones tabs / permisos 
        *********************************************/
        //Consultar

        //Reportes
        $scope.tabRep = function () {
            for (var i = 0; i < 1; i++) {
                return $scope.generalOpc.permisos[i].isSelected;
            }
        };

        //crear
        $scope.tabNew = function () {
            for (var i = 1; i < 2; i++) {
                // console.log("entro1" +  JSON.stringify($scope.generalOpc.permisosPerfil[i].estado))
                return $scope.generalOpc.permisos[i].isSelected;
            }
        };
        //modificar
        $scope.tabMod = function () {
            for (var i = 2; i < 3; i++) {
                return $scope.generalOpc.permisos[i].isSelected;
            }
        };
        //Eliminar
        $scope.tabDel = function () {
            for (var i = 3; i < 4; i++) {

                return $scope.generalOpc.permisos[i].isSelected;
            }
        };
        //validar cambio de tab

        $scope.onTabChanges = function (tab) {
            switch (tab) {
                case 0:
                    //console.log("entro a 0")
                    $state.go('menu.perfiles.new')

                    break;
                case 1:
                    //console.log("entro a 1")
                    $state.go('menu.perfiles.detail')
                    break;
            }
        }


        //validar si deshabilitar o habilitar lista


        $scope.validarList = function () {

            if (($scope.tabMod() === false && $scope.tabDel() === false && $scope.tabRep() === false) && $scope.tabNew() === true) {

                return true


            } else if (($scope.tabMod() === false && $scope.tabDel() === false && $scope.tabRep() === false) && $scope.tabNew() === false) {

                return true
            } else {

                return false
            }


        }
        //select all menus
        $scope.list = [];
        $scope.modMenu = false;
        /********************************************
         *Inicia logica para seleccionar menus
         *********************************************/
        $scope.expandNode = function (n, $event) {
            $event.stopPropagation();
            n.toggle();
        }

        $scope.numSelected = 0;

        //console.log("$scope.list" + JSON.stringify($scope.list) + "***$scope.list[0]" + JSON.stringify($scope.list))

        $scope.itemSelect = function (item, tipo) {
            $scope.modMenu = true;
            if (tipo == 'C') {
                $scope.list = $scope.opcmenu;
            } else {
                $scope.list = $scope.opcmenuC;
            }
            //console.log("item_" + JSON.stringify(item));
            var rootVal = !item.isSelected;
            userService.selectChildren(item, rootVal)

            userService.findParent($scope.list, null, item, selectParent);
            var s = _.compact(userService.getAllChildren($scope.list, []).map(function (c) {
                return c.isSelected && !c.children;
            }));
            $scope.numSelected = s.length;
            itemid($scope.list);

            //console.log("$scope.numSelected" + $scope.numSelected);
            //console.log("$scope.selected" + JSON.stringify($scope.list));
        }

        function selectParent(parent) {
            var item = userService.getAllChildren(parent, []);

            if (!item) return;
            item = item.slice(1).map(function (c) {
                return c.isSelected;
            });

            parent.isSelected = item.length === _.compact(item).length;
            userService.findParent($scope.list, null, parent, selectParent)
        }

        $scope.nodeStatus = function (node) {
            var flattenedTree = getAllChildren(node, []);
            flattenedTree = flattenedTree.map(function (n) {
                return n.isSelected
            });

            return flattenedTree.length === _.compact(flattenedTree);
        }


        //armar array para guardar
        function itemid(item) {
            //escenario dos niveles
            //console.log("menus_cargar" + $scope.menus.length)
            //console.log("item****" + JSON.stringify(item))
            if (item.children != null && item.children.length > 0) {
                //console.log("item.children != null _1" + JSON.stringify(item.children))
                //Grandparent
                for (i = 0; i < item.children.length; i++) {
                    //console.log("tem.children.isSelected" + item.children[i].isSelected)
                    //PRIMER NIVEL  GrandParent
                    if (item.children[i].isSelected) {
                        var p = ($scope.menus.indexOf(item.children[i].id))
                        if (p == -1) {
                            $scope.menus.push(item.children[i].id)
                        }
                        //SEGUNDO NIVEL parent
                        if (item.children[i].children.length > 0 && item.children[i].children != null) {
                            for (j = 0; j < item.children[i].children.length; j++) {
                                if (item.children[i].children[j].isSelected) {
                                    var q = (($scope.menus.indexOf(item.children[i].children[j].id)))
                                    if (q == -1) {
                                        $scope.menus.push(item.children[i].children[j].id)
                                    }
                                    //TERCER NIVEL son
                                    if (item.children[i].children[j].children.length > 0 && item.children[i].children[j].children != null) {
                                        for (k = 0; k < item.children[i].children[j].children.length; k++) {
                                            if (item.children[i].children[j].children[k].isSelected) {
                                                var x = (($scope.menus.indexOf(item.children[i].children[j].children[k].id)))
                                                if (x == -1) {
                                                    $scope.menus.push(item.children[i].children[j].children[k].id)
                                                }
                                            }
                                        }
                                    } // fin else tercer nivel
                                }
                            }
                        } // fin else segundo Nivel 
                    } else {
                        //eliminar grandParent si ya existe y viene en false
                        var y = (($scope.menus.indexOf(item.children[i].id)))
                        if (y > -1) {
                            $scope.menus.splice(y, 1);
                        }
                        //SEGUNDO NIVEL parent 
                        if (item.children[i].children.length > 0 && item.children[i].children.length != null) {
                            for (p = 0; p < item.children[i].children.length; p++) {
                                //si parent en true
                                if (item.children[i].children[p].isSelected) {
                                    var a = (($scope.menus.indexOf(item.children[i].children[p].id)))
                                    if (a == -1) {
                                        $scope.menus.push(item.children[i].children[p].id)
                                    }
                                    //TERCER NIVEL son 
                                    if (item.children[i].children[p].children.length > 0 && item.children[i].children[p].children != null) {
                                        for (e = 0; e < item.children[i].children[p].children.length; e++) {
                                            // si son en true
                                            if (item.children[i].children[p].children[e].isSelected) {
                                                var b = (($scope.menus.indexOf(item.children[i].children[p].children[e].id)))
                                                if (b == -1) {
                                                    $scope.menus.push(item.children[i].children[p].children[e].id)
                                                }
                                            } else {
                                                //false eliminar
                                                var u = (($scope.menus.indexOf(item.children[i].children[p].children[e].id)))
                                                if (u > -1) {
                                                    $scope.menus.splice(u, 1);
                                                }
                                            }
                                        }
                                    }
                                    //else parent viene en false 
                                } else {
                                    var c = (($scope.menus.indexOf(item.children[i].children[p].id)))
                                    if (c > -1) {
                                        $scope.menus.splice(c, 1);
                                    }
                                    if (item.children[i].children[p].children.length > 0 && item.children[i].children[p].children != null) {
                                        for (g = 0; g < item.children[i].children[p].children.length; g++) {
                                            // si son en true
                                            if (item.children[i].children[p].children[g].isSelected) {
                                                var o = (($scope.menus.indexOf(item.children[i].children[p].children[g].id)))
                                                if (o == -1) {
                                                    $scope.menus.push(item.children[i].children[p].children[g].id)
                                                }
                                            } else {
                                                //false eliminar
                                                var h = (($scope.menus.indexOf(item.children[i].children[p].children[g].id)))
                                                if (h > -1) {
                                                    $scope.menus.splice(h, 1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }


                    } //FIN ELSE GRANDPARENT
                    console.info("MENU____" + JSON.stringify($scope.menus));
                } //fin For GrandParent
            }
        } //fin funcion para armar array


        /*Finaliza la logica para seleccionar los menus*/

        $scope.selectedAll = function () {

            $scope.select = true;

        }

        /************************************
         * Respuesta exitosa, actualizar campos
         *************************************/
        function responseOk(data) {
            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor                      
            localStorageService.set('setDataToken', data.data.accessToken);
            $scope.userToken = localStorageService.get('setDataToken');
            //console.log("token" 
            //+ localStorageService.get('setDataToken') + $scope.userToken)

        }

        //Armar el array para enviar las opciones de menu con el formato json   
        function arraySubmit() {
            // itemid($scope.list);
            //console.log("scope.menus"+ $scope.menus.length)
            $scope.itemsToAdd = [];
            if ($scope.menus.length > 0) {
                for (i = 0; i < $scope.menus.length; i++) {
                    $scope.itemsToAdd.push({
                        id: $scope.menus[i]
                    });
                    //console.log("id menu: " + $scope.menus[i]);
                }
            }
        }
        /**************************
         *Submit Form Crear
         ***************************/
        //Agregar label id a el array de opciones de menu 
        $scope.submitForm = function (ev) {

            if ($scope.menus.length >= 0) {

                arraySubmit();
                //json formulario
                $scope.dataProfile = {
                    nombre: $scope.formProfile.nombre,
                    descripcion: $scope.formProfile.nombre,
                    perfilSuperior: {
                        id: $scope.formProfile.perfilSuperior.id

                    },
                    opcionesMenu:

                        $scope.itemsToAdd

                }
                console.info("dataProfile_" + JSON.stringify($scope.dataProfile))
                var confirm = $mdDialog.confirm()
                    .title('Está seguro que desea incluir el nuevo perfil?')
                    //.textContent('All of the banks have agreed to forgive you your debts.')
                    .ariaLabel('Lucky day')
                    .targetEvent(ev)
                    .ok('OK')
                    .cancel('CANCELAR');

                $mdDialog.show(confirm).then(function () {
                    $scope.status =

                        /*********peticion submit******
                         *******************************/
                        $scope.progressBar = true;
                    resourceAccessService.submitServer($scope.userScope, $scope.userToken, $scope.resource, $scope.permisosurl[1], $scope.dataProfile).then(function onSuccess(data) {
                        console.log("data_" +
                            data.data)
                        if (data.status == 200) {

                            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor
                            responseOk(data);

                            $scope.items.push(data.data.otrasOpciones.listaPerfil[0]);

                            //console.log("$scope.items_" + JSON.stringify($scope.items))                
                            $scope.progressBar = false;
                            $mdDialog.show(
                                $mdDialog.alert()
                                .parent(angular.element(document.querySelector('#popupContainer')))
                                .clickOutsideToClose(true)
                                .title('El perfil se ha creado con éxito')
                                .ariaLabel('Alert Dialog Demo')
                                .ok('Aceptar!')
                            )
                            console.log("respondio ok")
                            var datos = localStorageService.get('setGeneralOpc');
                            //console.log("data***" + JSON.stringify(datos));
                            datos.listaPerfil.push(data.data.otrasOpciones.listaPerfil[0]);
                            //console.log(" data.listaUsuarios" +  JSON.stringify(datos.listaPerfil))
                            localStorageService.set('setGeneralOpc', datos);
                            //console.log("localStorageService.get('setGeneralOpc'" + localStorageService.get('setGeneralOpc'));
                            $state.go('menu.perfiles', {}, {
                                reload: 'menu.perfiles'
                            });

                        }
                        //else{
                        //$state.go('home'); 
                        //console.error("Error al guardar formulario" );


                        //}

                    }).catch(function onError(data) {
                        if (data.status == 412) {
                            $scope.progressBar = false;
                            //validar errores de negocio
                            console.log("Error 412" + data.data.message)
                            $mdDialog.show({
                                controller: userService.DialogController,
                                template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>' + data.data.message + '</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                                parent: angular.element(document.body),
                                clickOutsideToClose: true,
                                fullscreen: useFullScreen,
                                multiple: true

                            });
                        } else {
                            $scope.progressBar = false;
                            //$state.go('home');
                            console.error("Error en acceso al servidor para obtener recurso" + data);
                            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                            localStorageService.set('setDataToken', data.data.accessToken);
                            loginService.validateSession();
                            localStorageService.clearAll();
                            $state.go('login');
                        }

                    });
                }, function () {
                    $scope.status = '';
                });

            } else {
                $mdDialog.show({
                    controller: userService.DialogController,
                    template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Debe asignar al perfil al menos un permiso</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                    parent: angular.element(document.body),
                    clickOutsideToClose: true,
                    fullscreen: useFullScreen,
                    multiple: true

                });
            }
        }

        /***************************************
         * Submit formulario modificado
         ***************************************/

        $scope.saveMod = function (ev) {

            if ($scope.menus.length > 0) {
                arraySubmit();
                //json formulario

                console.info("$scope.formProfile.nombreC" +
                    $scope.formProfileC.nombreC + "$scope.formProfile.perfilSuperiorC" +
                    $scope.formProfileC.perfilSuperiorC.id + "MENU" + JSON.stringify($scope.itemsToAdd) + "ID" + JSON.stringify(userService.getIdPerfil()))
                $scope.dataProfile = {
                    nombre: $scope.formProfileC.nombreC,
                    descripcion: $scope.formProfileC.nombreC,
                    perfilSuperior: {
                        id: $scope.formProfileC.perfilSuperiorC.id

                    },
                    opcionesMenu:

                        $scope.itemsToAdd

                }
                var confirm = $mdDialog.confirm()
                    .title('Desea guardar los cambios del perfil?')
                    //.textContent('All of the banks have agreed to forgive you your debts.')
                    .ariaLabel('Lucky day')
                    .targetEvent(ev)
                    .ok('OK')
                    .cancel('CANCELAR');

                $mdDialog.show(confirm).then(function () {
                    $scope.progressBar = true;
                    $scope.status =
                        /*********peticion submit******
                         *******************************/

                        resourceAccessService.submitMod($scope.userScope, $scope.userToken, $scope.resource, $scope.permisosurl[2], $scope.dataProfile, userService.getIdPerfil()).then(function onSuccess(data) {
                            if (data.status == 200) {
                                //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor
                                responseOk(data);
                                $scope.progressBar = false;
                                $mdDialog.show(
                                    $mdDialog.alert()
                                    .parent(angular.element(document.querySelector('#popupContainer')))
                                    .clickOutsideToClose(true)
                                    .title('El perfil se ha guardado con éxito')
                                    .ariaLabel('Alert Dialog Demo')
                                    .ok('Aceptar!')
                                )
                                console.log("respondio ok")
                                userService.setIdPerfil("");
                                var datos = localStorageService.get('setGeneralOpc');
                                //console.log("data***" + JSON.stringify(datos));
                                //Actualizar listaPerfil
                                for (i = 0; i < datos.listaPerfil.length; i++) {
                                    if (datos.listaPerfil[i].id == data.data.otrasOpciones.listaPerfil[0].id) {
                                        datos.listaPerfil.splice(i, 1);
                                        datos.listaPerfil.push(data.data.otrasOpciones.listaPerfil[0]);
                                        break;
                                    }
                                }
                                //console.log(" data.listaUsuarios" +  JSON.stringify(datos.listaPerfil))
                                localStorageService.set('setGeneralOpc', datos);
                                //console.log("localStorageService.get('setGeneralOpc'" + localStorageService.get('setGeneralOpc'));
                                $scope.selNueva = false;
                                resourceAccessService.setSpecificOpc(undefined)
                                $scope.hideFields = true;


                                $state.go('menu.perfiles', {}, {
                                    reload: 'menu.perfiles'
                                });

                            }
                            //else{
                            //$state.go('home'); 
                            // console.error("Error al guardar formulario" );
                            //}

                        }).catch(function onError(data) {
                            $scope.progressBar = false;
                            //$state.go('home');
                            console.error("Error en acceso al servidor para obtener recurso" + data);
                            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                            localStorageService.set('setDataToken', data.data.accessToken);
                            loginService.validateSession();
                            localStorageService.clearAll();
                            $state.go('login');
                        });
                }, function () {
                    $scope.status = '';
                });
            } else {
                $mdDialog.show({
                    controller: userService.DialogController,
                    template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Debe asignar al perfil al menos un permiso</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                    parent: angular.element(document.body),
                    clickOutsideToClose: true,
                    fullscreen: useFullScreen,
                    multiple: true

                });
            }
        }


        /************************************************************
         * función para validar que no nombre de perfil no exista
         *************************************************************/
        $scope.validarNombre = function (nombre, tipo) {

            console.log("nombre" + nombre + "/" + $scope.permisosurl[0]);
            if (nombre !== '' && nombre !== undefined) {
                $scope.progressBar = true;
                //actualizar token para que en caso de respuesta 204, que no tiene response, el token ya este actualizado
                resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, $scope.validaToken).then(function onSuccess(data) {
                    if (data.status == 200) {
                        //actualizar token 
                        responseOk(data);
                        resourceAccessService.nameValidate($scope.userScope, $scope.userToken, $scope.permisosurl[0], nombre, $scope.item).then(function onSuccess(data) {
                            if (data.status == 200) {
                                console.warn("Perfil ya existe en el Sistema");
                                //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor
                                responseOk(data);
                                $scope.progressBar = false;
                                if (tipo == 'C') {
                                    $scope.exits = true;
                                    $scope.form.profileForm.profileName.$invalid = true;
                                } else {
                                    $scope.formC.profileFormC.nombreC.$invalid = false;
                                }
                                $scope.exitsPro = "Perfil ya existe en el Sistema";

                            } else {
                                $scope.progressBar = false;
                                //if (data.status== 204){
                                console.info("Perfil no existe en el Sistema");

                                $scope.exits = false;
                                if (tipo == 'C') {
                                    $scope.form.profileForm.profileName.$invalid = false;
                                } else {
                                    $scope.formC.profileFormC.nombreC.$invalid = false;
                                }

                                $scope.exitsPro = "";

                            }

                        }).catch(function onError(data) {
                            $scope.progressBar = false;
                            //$state.go('home');
                            console.error("Error en acceso al servidor para obtener recurso" + data);
                            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                            localStorageService.set('setDataToken', data.data.accessToken);
                            loginService.validateSession();
                            localStorageService.clearAll();
                            $state.go('login');
                        });

                    }
                }).catch(function onError(data) {
                    $scope.progressBar = false;
                    //$state.go('home');
                    console.error("Error en acceso al servidor para obtener recurso" + data);
                    //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                    localStorageService.set('setDataToken', data.data.accessToken);
                    loginService.validateSession();
                    localStorageService.clearAll();
                    $state.go('login');
                })



            } else {
                $scope.exits = false;
                $scope.exitsPro = "";
            }


        }
        //poner en false bandera de perfil repetido para que no se muestre el mensaje mientras se esta escrbiendo 
        $scope.setExits = function () {
            $scope.exits = false;
        }



        $scope.menuProfile = function () {
            $mdSidenav('profile').open();
        }


        // función para inicializar el array de opciones de menu para consulta y modificación
        function cargarMenuC() {
            //console.log("llego a la funcion" + JSON.stringify($scope.opcmenuC.children))
            if ($scope.opcmenuC.children !== null && $scope.opcmenuC.children.length > 0) {
                //console.log("long_" + $scope.opcmenuC.children.length + JSON.stringify($scope.opcmenuC.children))
                for (i = 0; i < $scope.opcmenuC.children.length; i++) {
                    if ($scope.opcmenuC.children[i].isSelected) {
                        //      console.log("menus_"+ $scope.opcmenuC.children[i].label + "*" +$scope.opcmenuC.children[i].isSelected)
                        $scope.menus.push($scope.opcmenuC.children[i].id)
                        if ($scope.opcmenuC.children[i].children !== null && $scope.opcmenuC.children[i].children.length > 0) {
                            //      console.log("long_X" + JSON.stringify($scope.menus))
                            for (j = 0; j < $scope.opcmenuC.children[i].children.length; j++) {
                                if ($scope.opcmenuC.children[i].children[j].isSelected) {
                                    $scope.menus.push($scope.opcmenuC.children[i].children[j].id)
                                    //           console.log("sUBmenus_"+ JSON.stringify($scope.menus))
                                    if ($scope.opcmenuC.children[i].children[j].children !== null && $scope.opcmenuC.children[i].children[j].children.length > 0) {
                                        for (z = 0; z < $scope.opcmenuC.children[i].children[j].children.length; z++) {
                                            //            console.log("long_yz" + $scope.opcmenuC.children[i].children[j].children.length)
                                            if ($scope.opcmenuC.children[i].children[j].children[z].isSelected) {
                                                $scope.menus.push($scope.opcmenuC.children[i].children[j].children[z].id)
                                                //            console.log("array" + JSON.stringify($scope.menus))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            console.info("MENU_FINAL_" +
                JSON.stringify($scope.menus))
        }

        /****************************************
         *Consultar la información del perfil 
         *****************************************/
        $scope.toggle = function (item, list) {

            $scope.item = item.id;
            userService.setIdPerfil($scope.item)

            var idx = list.indexOf(item);
            if (idx > -1) {
                list.splice(idx, 1);
                if (list.length == 0) {
                    //habilitar tab crear cuando no hay un perfil seleccionado

                    $scope.opcHabNew = false;
                    $scope.opcHabCon = true;
                    $scope.selectAll = false;
                    $scope.hideFields = true;
                    resourceAccessService.setSpecificOpc(undefined)
                    $state.go('menu.perfiles.detail', {}, {
                        reload: 'menu.perfiles.detail'
                    });
                    userService.setSelected('');
                }
            } else {
                list.push(item);
                console.log("lista_ " + list.length)
                if (list.length > 1) {
                    //habilitar solo boton de reportes si hay mas de una opción seleccionada
                    $scope.opcHabNew = true;
                    $scope.opcHabMod = true;
                    $scope.opcHabDel = true;
                    $scope.opcHabRep = false;
                    $scope.opcHabCon = false;
                    $scope.selectAll = true;
                    resourceAccessService.setSpecificOpc(undefined)
                    userService.setSelected($scope.selected);
                    console.log("hay mas de dos opc seleccionadas");
                    $scope.selNueva = true;
                    $state.go('menu.perfiles.detail', {}, {
                        reload: 'menu.perfiles.detail'
                    });


                } else {
                    //$scope.selNueva = false; 
                    resourceAccessService.submitSearch($scope.userScope, $scope.userToken, $scope.resource, $scope.permisosurl[0], $scope.item).then(function onSuccess(data) {
                        if (data.status == 200) {
                            //actualizar token
                            responseOk(data);
                            // ir al tab de consulta y mostrar información de ese perfil
                            //informacion Consulta 

                            resourceAccessService.setSpecificOpc(data.data.otrasOpciones);
                            console.log("OtrasOpciones_" + JSON.stringify(resourceAccessService.getSpecificOpc()))
                            //Deshabilitar tab crear cuando solo hay un perfil seleccionado
                            $scope.opcHabNew = true;
                            $scope.selectAll = true;
                            $scope.opcHabCon = false;

                            console.log("$scope.selected" + JSON.stringify($scope.selected))
                            console.log("Seleccionado" + item.id);
                            console.log("Seleccionado_tab " + $scope.selectedTab);
                            $scope.itemSel = item.id;
                            console.log("item selec" +
                                $scope.itemSel)
                            userService.setSelected($scope.selected);
                            $state.go('menu.perfiles.detail', {}, {
                                reload: 'menu.perfiles.detail'
                            });

                        } else {
                            $mdToast.show({
                                hideDelay: 300000,
                                position: 'top right',
                                controller: ToastController,
                                template: '<md-toast><span class="md-toast-text" flex>No existe información del perfil<span><md-button class="md-highlight" ng-click="closeToast()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></md-toast>'
                            });
                            $scope.exits = "No existe información del perfil";
                        }

                    }).catch(function onError(data) {
                        //$state.go('home');
                        console.error("Error en acceso al servidor para obtener recurso");
                        //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                        localStorageService.set('setDataToken', data.data.accessToken);
                        loginService.validateSession();
                        localStorageService.clearAll();
                        $state.go('login');
                    });


                }
            }
        };

        // limpiar formularios en creacion
        $scope.menuClean = function () {
            //console.log("entro a menuClean" + JSON.stringify($scope.opcmenu ) + $scope.opcmenu.children.length)     
            for (i = 0; i < $scope.opcmenu.children.length; i++) {
                $scope.opcmenu.children[i].isSelected = false;
                console.log("$scope.opcmenu_" + JSON.stringify($scope.opcmenu ) + $scope.opcmenu.children[i].children.length);
                if ($scope.opcmenu.children[i].children.length > 0 && $scope.opcmenu.children[i].children != null) {
                    for (j = 0; j < $scope.opcmenu.children[i].children.length; j++) {
                        $scope.opcmenu.children[i].children[j].isSelected = false;
                        //  console.log("$scope.opcmenu[i].children[j]"+ JSON.stringify($scope.opcmenu)+ $scope.opcmenu.children[i].children[j].children.length)
                        if ($scope.opcmenu.children[i].children[j].children.length > 0 && $scope.opcmenu.children[i].children[j].children != null) {
                            for (k = 0; k < $scope.opcmenu.children[i].children[j].children.length; k++) {
                                $scope.opcmenu.children[i].children[j].children[k].isSelected = false;
                                //  console.log("$scope.opcmenu[i].children[j].children[k]" + JSON.stringify($scope.opcmenu ))
                            }
                        }
                    }
                }

            }

            //console.log("despues de clean_" + JSON.stringify($scope.opcmenu))
        }

        $scope.cargaDatos = function () {
            cargarDatos();
        }

        // cargar datos iniciales para formulario de modificación
        $scope.select = {};

        function cargarDatos() {

            //console.log("$scope.selected2" + $scope.selected.length)    
            $scope.specificOpcC = resourceAccessService.getSpecificOpc();
            $scope.select = userService.getSelected();
            //console.log("especifico" + JSON.stringify($scope.specificOpcC) )
            if ($scope.specificOpcC != undefined) {
                //nombre perfil
                $scope.formProfileC.nombreC = $scope.specificOpcC.perfil.nombre;
                //opciones de menu del perfil
                $scope.opcmenuC = $scope.specificOpcC.perfil.opcionesMenu;
                //console.log("menu___" + JSON.stringify($scope.opcmenuC))
                cargarMenuC();
                //perfil superior 
                $scope.perfilSuperiorCId = $scope.specificOpcC.perfil.perfilSuperior.id;
                //Buscar nombre de perfil superior para seleccionar  en la lista desplegable                              
                for (i = 0; i < $scope.items.length; i++) {
                    if ($scope.perfilSuperiorCId == $scope.items[i].id) {
                        //            console.log("id perfil superior_" + $scope.items[i].id)
                        $scope.formProfileC.perfilSuperiorC = $scope.items[i]
                    }
                }
                // Ir a tab de consulta y deshabilitar campos
                $scope.fieldsDisabled = true;
                $scope.hideFields = false;
                //$scope.selectedTab = 1;
            } else {
                $scope.hideFields = true;
                $scope.selNueva = true;
            }

        }

        /********************************************
         * Funciones para validación de lista de 
         * perfiles, si son seleccionados todos
         *********************************************/
        $scope.exists = function (item, list) {

            // console.log("exists" + JSON.stringify(item))
            // console.log("lista" + list)
            // console.log("resultado" + list.indexOf(item) > -1)
            return list.indexOf(item) > -1;
        };

        $scope.isIndeterminate = function () {
            return ($scope.selected.length !== 0 &&
                $scope.selected.length !== $scope.items.length);
        };

        $scope.isChecked = function () {
            //habilitar pantalla de consulta si selecciona todas las opciones
            if ($scope.items.length > 0) {
                if ($scope.selected.length === $scope.items.length) {
                    return $scope.selectAll = true;
                } else {
                    return $scope.selectAll = false;
                }
            }

            //return $scope.selectAll = $scope.selected.length === $scope.items.length;
            return $scope.selectAll = false;
        };

        $scope.toggleAll = function () {
            //console.log("tamaño lista" + $scope.items.length);
            if ($scope.selected.length === $scope.items.length) {

                $scope.selected = [];


            } else if ($scope.selected.length === 0 || $scope.selected.length > 0) {
                $scope.selected = $scope.items.slice(0);
                userService.setSelected($scope.selected);
                //console.log("toggleAll" + JSON.stringify(userService.getSelected() ));

            }
        };

        /*********************************
         *MODIFICAR FORMULARIO PERFIL
         *********************************/

        var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;
        $scope.modificar = function () {
            $scope.selected = userService.getSelected();
            //console.log("$scope.selected.length" + JSON.stringify($scope.selected))
            if ($scope.selected !== undefined) {
                if ($scope.selected.length > 1) {
                    $scope.selNueva = true;
                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Solo puede modificar un perfil</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: true,
                        fullscreen: useFullScreen
                    });

                } else if ($scope.selected.length == 0) {
                    $scope.selNueva = false;
                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Debe seleccionar un perfil para modificar</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: true,
                        fullscreen: useFullScreen

                    });
                } else {
                    console.log("$scope.selNueva" + $scope.selNueva)
                    if ($scope.selNueva) {
                        selectModificarMsg();
                    } else {
                        $scope.selNueva = false;
                        $scope.hideButton = true;
                        $scope.fieldsDisabled = false;
                        //habilitar o deshabilitar botones
                        $scope.tabMod = function () {
                            return false;
                        }
                        $scope.tabDel = function () {
                            return false;
                        }
                        $scope.tabRep = function () {
                            return false;
                        }
                        $scope.tabSav = function () {
                            return true;
                        }


                    }
                }
            } else {
                selectModificarMsg();
            }
        }

        function selectModificarMsg() {
            $mdDialog.show({
                controller: userService.DialogController,
                template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Debe seleccionar un perfil para modificar</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                fullscreen: useFullScreen

            });
            $state.go('menu.perfiles', {}, {
                reload: 'menu.perfiles'
            });

        }


        /*******************************************
         *ELIMINAR FORMULARIO PERFIL 
         ******************************************/
        $scope.eliminar = function (ev) {
            $scope.selected = userService.getSelected();
            if ($scope.selected !== undefined) {
                if ($scope.selected.length > 1) {
                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Solo puede eliminar un perfil</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: true,
                        fullscreen: useFullScreen

                    });

                } else if ($scope.selected.length == 0) {

                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Debe seleccionar un perfil para Eliminar</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: true,
                        fullscreen: useFullScreen

                    });
                } else {
                    if (!$scope.selNueva) {
                        var confirm = $mdDialog.confirm()
                            .title('Seguro desea eliminar el perfil?')
                            .textContent('')
                            .ariaLabel('Lucky day')
                            .targetEvent(ev)
                            .ok('Eliminar')
                            .cancel('Cancelar');

                        $mdDialog.show(confirm).then(function () {
                            //si confirma eliminación de perfil 
                            $scope.progressBar = true;
                            $scope.status =
                                /*********************************************************************************************
                                 *Validar si el perfil tiene usuarios en estado activo, si es asi no puede eliminar el perfil*/
                                resourceAccessService.dataSearch1($scope.userScope, $scope.userToken, $scope.resource, $scope.perfilEliminar, "", userService.getIdPerfil()).then(function onSuccess(data) {
                                    if (data.status == 200) {
                                        console.log("perfil tiene usuarios activos");
                                        responseOk(data);
                                        $scope.progressBar = false;
                                        $mdDialog.show({
                                            controller: userService.DialogController,
                                            template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>El perfil tiene usuarios en estado Activo y no puede ser Eliminado</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                                            parent: angular.element(document.body),
                                            clickOutsideToClose: true,
                                            fullscreen: useFullScreen,
                                            multiple: true

                                        });

                                    } else {

                                        console.log("perfil puede ser eliminado");
                                        resourceAccessService.deleteForm($scope.userScope, $scope.userToken, $scope.resource, $scope.permisosurl[3], userService.getIdPerfil()).then(function onSuccess(data) {
                                            if (data.status == 200) {
                                                console.info("eliminado");
                                                responseOk(data);
                                                $scope.progressBar = false;
                                                $mdDialog.show(
                                                    $mdDialog.alert()
                                                    .parent(angular.element(document.querySelector('#popupContainer')))
                                                    .clickOutsideToClose(true)
                                                    .title('El perfil se ha eliminado con éxito')
                                                    .ariaLabel('Alert Dialog Demo')
                                                    .ok('Aceptar!')
                                                )
                                                var datos = localStorageService.get('setGeneralOpc');
                                                //console.log("data***" + JSON.stringify(datos));
                                                //Actualizar listaPerfil
                                                for (i = 0; i < datos.listaPerfil.length; i++) {
                                                    if (datos.listaPerfil[i].id == userService.getIdPerfil()) {
                                                        datos.listaPerfil.splice(i, 1);
                                                        break;
                                                    }
                                                }
                                                //console.log(" data.listaPerfil" +  JSON.stringify(datos.listaPerfil))
                                                localStorageService.set('setGeneralOpc', datos);
                                                //console.log("localStorageService.get('setGeneralOpc'" + localStorageService.get('setGeneralOpc'));
                                                userService.setIdPerfil("");
                                                resourceAccessService.setSpecificOpc(undefined)
                                                $scope.hideFields = true;
                                                $state.go('menu.perfiles', {}, {
                                                    reload: 'menu.perfiles'
                                                });
                                            } else {
                                                $state.go('menu.perfiles');

                                            }

                                        }).catch(function onError(data) {
                                            if (data.status == 412) {
                                                $scope.progressBar = false;
                                                $mdDialog.show({
                                                    controller: userService.DialogController,
                                                    template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>' + data.data.message + '</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                                                    parent: angular.element(document.body),
                                                    clickOutsideToClose: false,
                                                    fullscreen: useFullScreen,
                                                    multiple: true
                                                }) //fin mdDialog


                                            } else {
                                                $scope.progressBar = false;
                                                // $state.go('home');
                                                $mdDialog.hide();
                                                console.error("Error en acceso al servidor para obtener recurso" + JSON.stringify(data));
                                                //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                                                localStorageService.set('setDataToken', data.data.accessToken);
                                                loginService.validateSession();
                                                localStorageService.clearAll();
                                                $state.go('login');
                                            }

                                        });
                                    }
                                }).catch(function onError(data) {
                                    $scope.progressBar = false;
                                    //$state.go('home');
                                    console.error("Error en acceso al servidor para obtener recurso");
                                    //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                                    localStorageService.set('setDataToken', data.data.accessToken);
                                    loginService.validateSession();
                                    localStorageService.clearAll();
                                    $state.go('login');

                                });

                        }, function () {
                            $scope.status = '';
                        });
                    } else {
                        selectEliminarMsg()
                    }
                } // fin else
            } else {
                selectEliminarMsg();
            }
        }

        function selectEliminarMsg() {

            $mdDialog.show({
                controller: userService.DialogController,
                template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Debe seleccionar un perfil para Eliminar</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                fullscreen: useFullScreen

            });
            $state.go('menu.perfiles', {}, {
                reload: 'menu.perfiles'
            });

        }

        /**********************************
         * GENERAR REPORTE DE PERFILES
         ***********************************/

        function profileArray(profiles) {
            $scope.itemsToAddP = [];
            if (profiles.length > 0) {
                for (i = 0; i < profiles.length; i++) {
                    $scope.itemsToAddP.push(profiles[i].id);
                    //console.log("id menu: " + profiles[i].id + "itemsToAdd" + JSON.stringify($scope.itemsToAddP));
                }
            }
        }


        $scope.report = function (ev) {
            $scope.selected = userService.getSelected();
            var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;
            if ($scope.selected !== undefined) {
                if ($scope.selected.length == 0) {
                    selectPerfilMsg();
                } else {
                    $mdDialog.show({
                            controller: userService.DialogController,
                            templateUrl: 'templates/pages/userProfilePages/generateReport.tmpl.html',
                            parent: angular.element(document.body),
                            targetEvent: ev,
                            clickOutsideToClose: true,
                            fullscreen: useFullScreen
                        })
                        .then(function (answer) {
                            console.log("seleccionados__" + answer + JSON.stringify($scope.selected))
                            profileArray($scope.selected);

                            //actualizar token antes de hacer solicitud  de reporte
                            resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, $scope.validaToken).then(function onSuccess(data) {
                                if (data.status == 200) {
                                    //actualizar token 
                                    responseOk(data);
                                    //llamar funcion que genera el reporte dependiendo de answer
                                    if (answer == "PDF") {
                                        answer = answer + '-ZIP';
                                    }
                                    $scope.progressBar = true;
                                    resourceAccessService.generateReport($scope.userScope, $scope.userToken, $scope.permisosurl[0], $scope.itemsToAddP, answer).then(function onSuccess(data) {
                                        if (data.status == 200) {
                                            console.log("generado");

                                            //PDF
                                            if (answer == 'PDF-ZIP') {
                                                var file = new Blob([data.data], {
                                                    type: 'application/zip'
                                                });
                                                var fileURL = URL.createObjectURL(file);
                                                var a = document.createElement('a');
                                                a.href = fileURL;
                                                a.target = '_blank';
                                                a.download = 'reporte_perfiles.zip';
                                                document.body.appendChild(a);
                                                a.click();
                                            } else {
                                                //XLS
                                                var file = new Blob([data.data], {
                                                    type: 'application/xlsx'
                                                });
                                                var fileURL = URL.createObjectURL(file);
                                                var a = document.createElement('a');
                                                a.href = fileURL;
                                                a.target = '_blank';
                                                a.download = 'reporte_perfiles.xlsx';
                                                document.body.appendChild(a);
                                                a.click();

                                            }

                                            $scope.progressBar = false;
                                            $mdDialog.show(
                                                $mdDialog.alert()
                                                .parent(angular.element(document.querySelector('#popupContainer')))
                                                .clickOutsideToClose(true)
                                                .title('Se ha generado el reporte con éxito')
                                                .ariaLabel('Alert Dialog Demo')
                                                .ok('Aceptar!')
                                            )
                                            //limpiar lista perfiles cuando se ha generado reporte
                                            $scope.itemsToAdd = [];
                                            $scope.selected = $scope.items.slice(0);
                                            $scope.formProfileC.nombreC = "";
                                            $scope.formProfileC = {};
                                            $scope.menus = [];
                                            $state.go('menu.perfiles', {}, {
                                                reload: 'menu.perfiles'
                                            });

                                        }
                                    }).catch(function onError(data) {
                                        $scope.progressBar = false;
                                        //$state.go('home');
                                        console.error("Error en acceso al servidor para obtener recurso");
                                        //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                                        localStorageService.set('setDataToken', data.data.accessToken);
                                        loginService.validateSession();
                                        localStorageService.clearAll();
                                        $state.go('login');

                                    });



                                }
                            }).catch(function onError(data) {
                                $scope.progressBar = false;
                                //$state.go('home');
                                console.error("Error en acceso al servidor para obtener recurso" + data);
                                //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                                localStorageService.set('setDataToken', data.data.accessToken);
                                loginService.validateSession();
                                localStorageService.clearAll();
                                $state.go('login');
                            })





                        }, function () {

                            //$scope.status = 'You cancelled the dialog.';
                        });

                    $scope.$watch(function () {
                        return $mdMedia('xs') || $mdMedia('sm');
                    }, function (wantsFullScreen) {
                        $scope.customFullscreen = (wantsFullScreen === true);
                    });
                }
            } else {
                selectPerfilMsg();

            }


        }

        function selectPerfilMsg() {
            $scope.selNueva = false;
            $mdDialog.show({
                controller: userService.DialogController,
                template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Debe seleccionar un perfil </h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                fullscreen: useFullScreen

            });
        }

        /********************************
         * FUNCIONES PARA TOAST
         *********************************/
        var isDlgOpen;
        var last = {
            bottom: false,
            top: true,
            left: false,
            right: true
        };

        $scope.toastPosition = angular.extend({}, last);

        $scope.getToastPosition = function () {
            sanitizePosition();

            return Object.keys($scope.toastPosition)
                .filter(function (pos) {
                    return $scope.toastPosition[pos];
                })
                .join(' ');
        };

        function sanitizePosition() {
            var current = $scope.toastPosition;

            if (current.bottom && last.top) current.top = false;
            if (current.top && last.bottom) current.bottom = false;
            if (current.right && last.left) current.left = false;
            if (current.left && last.right) current.right = false;

            last = angular.extend({}, current);
        }

        function ToastController($scope, $mdDialog, $mdToast) {

            $scope.closeToast = function () {
                if (isDlgOpen) return;

                $mdToast
                    .hide()
                    .then(function () {
                        isDlgOpen = false;
                    });
            };
        }
    }) //fin controlLer
