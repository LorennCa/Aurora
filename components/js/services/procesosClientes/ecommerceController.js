angular.module('aurora.ecommerceController', ['ngIdle', 'ngMaterial'])

    .controller('ecommerceController', function ($scope, $route, $state, $mdEditDialog, $mdMedia, $mdToast, $location, $mdSidenav, $mdDialog, $sce, mainService, configurationServer, globalConstant, localStorageService, resourceAccessService, loginService, userService) {
        

        //cargas permisos otras opciones
        $scope.generalOpc = localStorageService.get('setGeneralOpc');
        $scope.ambiente = configurationServer.obtenerAmbiente();
        
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
            limit: 40,
            page: 1
        };

        $scope.selected = [];
        $scope.limitOptions = [20,40,60];
        $scope.toggleLimitOptions = function () {
            $scope.limitOptions = $scope.limitOptions ? undefined : [20,40,60];
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
                    responseOk(data);
                    $scope.progressBar = false;
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
    
    
    
    
    $scope.detalles = function(producto){
        $scope.progressBar = true;
        $scope.productoSeleccionado = producto;
        console.log($scope.productoSeleccionado);
        console.log($scope.productoSeleccionado.clave.id_producto);
        resourceAccessService.nameValidate($scope.userScope,$scope.userToken, '/detalles/consultar',$scope.productoSeleccionado.clave.id_producto,null).then(function onSuccess(data) {
                if (data.status == 200) {
                    //actualizar token 
                    responseOk(data);
                    console.log("hola");
                    console.log(data.data.otrasOpciones.detalles);
                    //console.log('Lista: ' + JSON.stringify(data.data.otrasOpciones.listaInventario));
                    if (data.data.otrasOpciones.detalles != null) {
                        $scope.detalles = data.data.otrasOpciones.detalles;
                        localStorageService.set('detalleProducto',$scope.detalles);
                        console.log($scope.detalles);
                        $scope.progressBar = false;
                        $state.go('menu.detalles', {}, {
                        reload: 'menu.detalles'
                    });
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
    
    $scope.productosDisp = function(ev){
            $scope.progressBar = true;
        console.log("InventarioDispo");
            resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, '/accesorios/buscar').then(function onSuccess(data) {
                if (data.status == 200) {
                    //actualizar token 
                    responseOk(data);
                    //console.log('Lista: ' + JSON.stringify(data.data.otrasOpciones.listaInventario));
                    if (data.data.otrasOpciones.inventario.length > 0) {
                        $scope.listaInventario = data.data.otrasOpciones.inventario;
                        console.log($scope.listaInventario);
                        //console.log($scope.listaInventario[0].referencia)
                        $scope.listInventarioLen = $scope.listaInventario.length;
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
    
    $scope.productosDispPrincipal = function(ev){
            $scope.progressBar = true;
        console.log("InventarioDispo");
            resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, '/accesorios/buscar').then(function onSuccess(data) {
                if (data.status == 200) {
                    //actualizar token 
                    responseOk(data);
                    //console.log('Lista: ' + JSON.stringify(data.data.otrasOpciones.listaInventario));
                    if (data.data.otrasOpciones.inventario.length > 0) {
                        $scope.listaInventario = data.data.otrasOpciones.inventario;
                        console.log($scope.listaInventario);
                        //console.log($scope.listaInventario[0].referencia)
                        $scope.listInventarioLen = $scope.listaInventario.length;
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
    
    $scope.pagar = function(ev){
        $state.go('menu.compras');
    }
    
    $scope.compraConfirmada = function(ev){
        $state.go('menu.pedidos');
    }
    
    $scope.irLogin= function(ev){
        console.log('ir al logn')
        $state.go('login');
    }
    
    
    
    $scope.carrito = function(ev){
            $scope.progressBar = true;
        console.log("InventarioDispo");
            resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, '/carrito/consultar').then(function onSuccess(data) {
                if (data.status == 200) {
                    //actualizar token 
                    responseOk(data);
                    if (data.data.otrasOpciones.carrito.length > 0) {
                        $scope.listaInventario = data.data.otrasOpciones.carrito;
                        console.log($scope.listaInventario);
                        $scope.listInventarioLen = $scope.listaInventario.length;
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
    function armarJsonNuevo() {
        $scope.producto = localStorageService.get('producto');
        console.log($scope.producto);
            $scope.dataReferencia = {
                id:$scope.producto.clave.id_producto,
                usuario:localStorageService.get('setUserLogin'),
                referencia: $scope.producto.clave.referencia,
                color:$scope.producto.clave.color,
                recurso:$scope.producto.recurso,
                precio:$scope.producto.precio,
            }
        }
    
    
    
    $scope.seguirComprando = function($event){
        $state.go('menu.accesorios');
    }
    
     $scope.agregarCarrito = function (producto, ev) {
            localStorageService.set('producto', producto);
            $scope.login = localStorageService.get('setUserLogin');
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
                submitServer('/carrito/agregar');

            

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
        
    
    
    
            //Carga datos para modificar tip
        $scope.cargarData = function () {
            $scope.inventarioData = userService.getUserData();
            $scope.modRefInventario.referencia = $scope.inventarioData[0].referencia,
            $scope.modRefInventario.cantidad = $scope.inventarioData[0].cantidad,
            $scope.modRefInventario.proveedor = $scope.inventarioData[0].proveedor,
            $scope.modRefInventario.locacion = $scope.inventarioData[0].locacion,
            $scope.modRefInventario.marca = $scope.inventarioData[0].marca,
            $scope.modRefInventario.descripcion = $scope.inventarioData[0].descripcion,
            $scope.modRefInventario.tipo = $scope.inventarioData[0].tipo,
            $scope.modRefInventario.precio_final = $scope.inventarioData[0].precio_final,
            $scope.modRefInventario.peso = $scope.inventarioData[0].peso_final
        } //Fin function carga Data //Fin function carga Data
             
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
