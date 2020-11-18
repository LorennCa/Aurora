angular.module('aurora.comprasController', ['ngIdle', 'ngMaterial'])

    .controller('comprasController', function ($scope, $route, $state, $mdEditDialog, $mdMedia, $mdToast, $location, $mdSidenav, $mdDialog, $sce, mainService, configurationServer, globalConstant, localStorageService, resourceAccessService, loginService, userService) {
        

        //cargas permisos otras opciones
        $scope.generalOpc = localStorageService.get('setGeneralOpc');
        $scope.ambiente = configurationServer.obtenerAmbiente();
    $scope.newReferencia = {};
    $scope.modProducto = {};
    $scope.referenciaBuscada = "";
        
        /********************************************
        mostrar u ocultar opciones botones / permisos 
        *********************************************/
        function getUrlPermiso(permiso) {
            for (var i = 0; i < $scope.generalOpc.permisos.length; i++) {
                if ($scope.generalOpc.permisos[i].isSelected && $scope.generalOpc.permisos[i].id == permiso) {
                    return $scope.generalOpc.permisos[i].resource;
                }
            }
            return 'NOT_FOUND';
        }
    
    
    var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;
    $scope.validaToken = localStorageService.get('setUriToken');

        $scope.options = {
            rowSelection: true,
            multiSelect: true,
            autoSelect: false,
            decapitate: false,
            largeEditDialog: false,
            boundaryLinks: false,
            limitSelect: true,
            pageSelect: true
        };
        
        
        
         

        $scope.query = {
            order: 'name',
            limit: 10,
            page: 1
        };

        $scope.selected = [];
        $scope.limitOptions = [5, 10, 15, 20,25,30];
        $scope.toggleLimitOptions = function () {
            $scope.limitOptions = $scope.limitOptions ? undefined : [5, 10, 15, 20,25,30];
        };

        $scope.loadStuff = function () {
            $scope.promise = $timeout(function () {
                // loading
            }, 2000);
        }

        $scope.logItem = function (item) {
            //console.log(item.referencia);
            //console.info("array_" + JSON.stringify($scope.selected))
        };

        $scope.logOrder = function (order) {
            console.log('order: ', order);
        };

        $scope.logPagination = function (page, limit) {
            console.log('page: ', page);
            console.log('limit: ', limit);
        }

        /************************************
         * Respuesta exitosa, actualizar campos
         *************************************/
        function responseOk(data) {
            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor           
            localStorageService.set('setDataToken', data.data.accessToken);
            $scope.userToken = localStorageService.get('setDataToken');


        }
    
    if ($scope.generalOpc != null) {
            $scope.mostrarBotonConPermiso = function (permiso) {
                for (var i = 0; i < $scope.generalOpc.permisos.length; i++) {
                    if ($scope.generalOpc.permisos[i].isSelected && $scope.generalOpc.permisos[i].id == permiso) {
                        return true;
                    }
                }
                return false;


            }
        }

            
        
           
           
           
        /************************************
        * Limpiar entrada del archivo
        *************************************/
           function limpiar(){
                var input = document.getElementById('file');
                input.value = '';
           }

  
        
        
        function selectItem() {
            $mdDialog.show({
                controller: userService.DialogController,
                template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Debe seleccionar una referencia de la lista</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: useFullScreen

            })
        }

        
            function submitServer(url) {
            $scope.resource = localStorageService.get('setResource');
            $scope.userScope = localStorageService.get('setDataScope');
            $scope.userToken = localStorageService.get('setDataToken');
            resourceAccessService.submitServer($scope.userScope, $scope.userToken, url, url, $scope.dataReferencia).then(function onSuccess(data) {
                if (data.status == 200) {
                    //if(data.data.otrasOpciones.businessErrorCode == 100){
                    responseOk(data);
                    $scope.progressBar = false;
                    $mdDialog.show(
                        $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#popupContainer')))
                        .clickOutsideToClose(true)
                        .title('La acción se ha ejecutado exitosamente!')
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar!')
                    )
                    $scope.selected = [];
                    $scope.inventarioDisp();
                    var datos = localStorageService.get('setGeneralOpc');
                } else {
                    $scope.progressBar = false;
                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Hubo un error al intentar guardar el registro</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        fullscreen: useFullScreen,
                        multiple: true

                    });
                }
                //}
            }).catch(function onError(data) {
                if (data.status == 412) {
                    responseOk(data);
                    $scope.progressBar = false;
                    //validar errores de negocio
                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>' + data.data.message + '</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        fullscreen: useFullScreen,
                        multiple: true

                    });
                } else {
                    //$state.go('home');
                    $mdDialog.hide();
                    console.error("Error en acceso al servidor para obtener recurso" + data);
                    //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                    localStorageService.set('setDataToken', data.data.accessToken);
                    loginService.validateSession();
                    localStorageService.clearAll();
                }
            });
        }
    
    $scope.verificarEnter = function (e) {
            if (e.which == 13) {
                $scope.compras(e);
            }
        };
    
    
    
    
    $scope.compras = function(ev){
        $scope.progressBar = true;
        console.log("Compras");
        $scope.referencia = $scope.referenciaBuscada;
        console.log($scope.referencia);
            resourceAccessService.nameValidate($scope.userScope,$scope.userToken, '/compras/consultar',$scope.referencia,null).then(function onSuccess(data) {
                if (data.status == 200) {
                    //actualizar token 
                    responseOk(data);
                    //console.log('Lista: ' + JSON.stringify(data.data.otrasOpciones.listaInventario));
                    if (data.data.otrasOpciones.compras.length > 0) {
                        $scope.compras = data.data.otrasOpciones.compras;
                        $scope.comprasLen = $scope.compras.length;
                        $scope.referenciaBuscada = "";
                        $scope.progressBar = false;
                    } else {
                        $scope.progressBar = false;
                        $mdDialog.show({
                            controller: userService.DialogController,
                            template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>No se encontraron registros en la base de datos</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                            parent: angular.element(document.body),
                            clickOutsideToClose: false,
                            fullscreen: useFullScreen,
                            multiple: true

                        });
                    }



                } else {
                    $scope.progressBar = false;
                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>El archivo no ha sido cargado o no está disponible</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        fullscreen: useFullScreen,
                        multiple: true

                    });
                }
            }).catch(function onError(data) {

                $mdDialog.hide();
                console.error("Error en acceso al servidor para obtener recurso" + data);
                //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                localStorageService.set('setDataToken', data.data.accessToken);
                loginService.validateSession();
                localStorageService.clearAll();
                $state.go('login');
            })
        }
    
    $scope.cargarData = function () {
            $scope.productoData = localStorageService.get('producto');
            $scope.modProducto.id_producto = $scope.productoData.clave.id_producto;
            $scope.modProducto.referencia = $scope.productoData.clave.referencia;
            $scope.modProducto.color = $scope.productoData.clave.color;
            $scope.modProducto.precio = $scope.productoData.precio;
            $scope.modProducto.cantidad = $scope.productoData.cantidad;
            $scope.modProducto.recurso = $scope.productoData.recurso;
            $scope.modProducto.palabra_clave = $scope.productoData.palabra_clave;
            
        } 
    
    $scope.actualizarProducto = function(producto, ev){
        localStorageService.set('producto',producto);
        $mdDialog.show({
                            controller: userService.DialogController,
                            templateUrl: 'templates/pages/procesosAurora/modificarProducto.html',
                            parent: angular.element(document.body),
                            targetEvent: ev,
                            clickOutsideToClose: false,
                            fullscreen: useFullScreen,
                            multiple: true
                        })
                        .then(function () {

                            //aca va algo

                        }, function () {

                            //$scope.status = 'You cancelled the dialog.';
                        });
                    $scope.$watch(function () {
                        return $mdMedia('xs') || $mdMedia('sm');
                    }, function (wantsFullScreen) {
                        $scope.customFullscreen = (wantsFullScreen === true);
                    });
        
    }
    
    
    $scope.creaReferencia = function(ev){
                    $mdDialog.show({
                            controller: userService.DialogController,
                            templateUrl: 'templates/pages/procesosAurora/crearProducto.html',
                            parent: angular.element(document.body),
                            targetEvent: ev,
                            clickOutsideToClose: false,
                            fullscreen: useFullScreen,
                            multiple: true
                        })
                        .then(function () {


                        }, function () {

                            //$scope.status = 'You cancelled the dialog.';
                        });
                    $scope.$watch(function () {
                        return $mdMedia('xs') || $mdMedia('sm');
                    }, function (wantsFullScreen) {
                        $scope.customFullscreen = (wantsFullScreen === true);
                    });

                }
             
    
    function armarJsonNuevo() {
            $scope.dataReferencia = {
                clave: {
                    id_producto:$scope.newReferencia.cod_producto,
                    referencia:$scope.newReferencia.referencia,
                    color:$scope.newReferencia.color,
                },
                precio:$scope.newReferencia.precio,
                cantidad: $scope.newReferencia.cantidad,
                recurso:$scope.newReferencia.recurso,                
                palabra_clave:$scope.newReferencia.palabra_clave,
            }
        }
    
    function armarJsonMod() {
            $scope.dataReferencia = {
                clave: {
                    id_producto:$scope.modProducto.id_producto,
                    referencia:$scope.modProducto.referencia,
                    color:$scope.modProducto.color,
                },
                precio:$scope.modProducto.precio,
                cantidad: $scope.modProducto.cantidad,
                recurso:$scope.modProducto.recurso,                
                palabra_clave:$scope.modProducto.palabra_clave,
            }
        }
    $scope.submitReferenciaForm = function (ev) {
            $scope.userScope = localStorageService.get('setDataScope');
            $scope.userToken = localStorageService.get('setDataToken');
            armarJsonNuevo();
            var confirm = $mdDialog.confirm()
                .title('¿Está seguro que desea incluir la nueva referencia?')
                .ariaLabel('Lucky day')
                .targetEvent(ev)
                .ok('OK')
                .cancel('CANCELAR')
                .multiple(true);
                submitServer('/productos/crear');

            

            }, function () {
                $scope.status = '';
                $mdDialog.hide();
            } 
    
    $scope.submitUpdReferenciaForm = function (ev) {
            $scope.userScope = localStorageService.get('setDataScope');
            $scope.userToken = localStorageService.get('setDataToken');
            armarJsonMod();
            var confirm = $mdDialog.confirm()
                .title('¿Está seguro que desea incluir la nueva referencia?')
                .ariaLabel('Lucky day')
                .targetEvent(ev)
                .ok('OK')
                .cancel('CANCELAR')
                .multiple(true);
                submitServer('/productos/actualizar');

            

            }, function () {
                $scope.status = '';
                $mdDialog.hide();
            } 
    
    
    
    function selectItem() {
            $mdDialog.show({
                controller: userService.DialogController,
                template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Debe seleccionar una referencia de la lista</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Debe seleccionar una referencia de la lista</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: useFullScreen

            })
        }
        
             
})        
      
.directive('date', function (dateFilter) {
    return {
        require:'ngModel',
        link:function (scope, elm, attrs, ctrl) {

            var dateFormat = attrs['date'] || 'yyyy-MM-dd';
           
            ctrl.$formatters.unshift(function (modelValue) {
                return dateFilter(modelValue, dateFormat);
            });
        }
    };
})//Fin controller
