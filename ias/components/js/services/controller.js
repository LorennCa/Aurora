/* main controller*/

angular.module('ias.controller', ['ngIdle', 'ngMaterial'])

    //    .config(function ($mdThemingProvider) {
    //
    //        //CAMBIAR COLOR AL THEME
    //        var neonRedMap = $mdThemingProvider.extendPalette('light-blue', {
    //            '800': '#2C2D2F',
    //            'contrastDefaultColor': 'dark'
    //        });
    //
    //        // Register the new color palette map with the name <code>neonRed</code>
    //        $mdThemingProvider.definePalette('neonRed', neonRedMap);
    //
    //        // Use that theme for the primary intentions
    //        $mdThemingProvider.theme('default')
    //            .primaryPalette('neonRed');
    //
    //    })

    .config(function ($mdThemingProvider) {
        //CAMBIAR COLOR AL THEME    
        var neonRedMap = $mdThemingProvider.extendPalette('mcgpalette0', {
            '50': 'e6e6e6',
            '100': 'c0c0c1',
            '200': '979798',
            '300': '6d6d6f',
            '400': '4d4d50',
            '500': '2e2e31',
            '600': '29292c',
            '700': '232325',
            '800': '1d1d1f',
            '900': '121213',
            'A100': '6767f3',
            'A200': '3737f0',
            'A400': '0000f4',
            'A700': '0000da',
            'contrastDefaultColor': 'light',
            'contrastDarkColors': [
    50,
    100,
    200
  ],
            'contrastLightColors': [
    '300',
    '400',
    '500',
    '600',
    '700',
    '800',
    '900',
    'A100',
    'A200',
    'A400',
    'A700'
  ]
        });

        //Register the new color palette map with the name < code > neonRed < /code>
        $mdThemingProvider.definePalette('neonRed', neonRedMap);

        // Use that theme for the primary intentions
        $mdThemingProvider.theme('default')
            .primaryPalette('neonRed');
    })


    //MAIN CONTROLLER
    .controller('mainController', function ($scope, $rootScope, $location, $state, $window, mainService, loginService, localStorageService,$scope, $route, $mdEditDialog, $mdMedia, $mdToast, $mdSidenav, $mdDialog, $sce, configurationServer, globalConstant, localStorageService, resourceAccessService, loginService, userService) {
        //Cargar parametros Iniciales
        $scope.initialParams = function () {
            mainService.InitialParams().then(function onSuccess(data) {
                if (data.status == 200) {
                    console.info("parametros Inic_");
                    //Captcha
                    mainService.setParamCaptcha(data.data.captchaActivado);
                    $rootScope.paramCaptcha = mainService.getParamCaptcha();
                    //banner        
                    mainService.setParamBanner(data.data.logoPrincipal);
                    $rootScope.paramBanner = mainService.getParamBanner();
                    //timeout
                    mainService.setParamTimeOut(data.data.tiempoSessionActiva);
                    $rootScope.ParamTimeOut = mainService.getParamTimeOut();
                    // //intentos fallidos
                    mainService.setParamFailed(data.data.intentosPermitidosLogin);
                    $rootScope.ParamFailed = mainService.getParamFailed();
                } else {
                    $state.go('home');
                    console.log("Error al cargar parametros iniciales");

                }

            }).catch(function onError(data) {
                $state.go('home');
                console.log("parametros Error");

            });

            //cargar archivo de festivos
            mainService.loadFestivos().then(function onSuccess(data) {
                if (data.status == 200) {
                    localStorageService.set('setFestivos', data.data.festivos);
                    //console.log(JSON.stringify(localStorageService.get('setFestivos')));
                }

            }).catch(function onError(data) {
                $state.go('home');
                console.log("parametros Error festivos" + data);

            });




        }


    }) //Fin controlador main

    .controller('resetPasswordController', function ($scope, $location, $rootScope, $timeout, $state, $mdMedia, $mdDialog, resourceAccessService, userService) {
        $scope.lloginUsuario = userService.getSelected(); //SET SELECTED VACÍO AL FINAL DEL PROCESO
        //console.log("lloginUsuario: " + $scope.lloginUsuario);
        $scope.closeDialog = function (ev) {
            $mdDialog.hide();
        }

        $scope.submitResetPassword = function (ev) {

            resourceAccessService.resetPassword($scope.lloginUsuario)
                .then(function onSuccess(data) {
                    if (data.status == 200) {
                        var confirm = $mdDialog.confirm()
                            .title('La nueva contraseña ha sido enviada al correo registrado en el sistema.')
                            .ariaLabel('Lucky day')
                            .targetEvent(ev)
                            .ok('OK')
                            .multiple(true);
                    } else {
                        //mensaje 201, ya existe un restablecimiento de contraseña
                        var confirm = $mdDialog.confirm()
                            .title(data.data.message)
                            .ariaLabel('Lucky day')
                            .targetEvent(ev)
                            .ok('OK')
                            .multiple(true);
                    }

                    $mdDialog.show(confirm).then(function () {
                            controller: userService.DialogController;
                            multiple: true;
                            $mdDialog.hide();
                        },
                        function () {});
                })
                .catch(function onError(data) {
                    /*404 no se encontro usuario 
                     *bussinessErrorCode = 100, usuario no existe en el sistema
                     *bussinessErrorCode = 101, usuario se encuentra en un estado diferente de Activo   
                     */
                    if (data.status == 404) {
                        var confirm = $mdDialog.confirm()
                            .title(data.data.message)
                            .ariaLabel('Lucky day')
                            .targetEvent(ev)
                            .ok('OK')
                            .multiple(true);

                    } else {
                        var confirm = $mdDialog.confirm()
                            .title("Error en acceso al servidor, por favor intente nuevamente")
                            .ariaLabel('Lucky day')
                            .targetEvent(ev)
                            .ok('OK')
                            .multiple(true);
                    }



                    $mdDialog.show(confirm).then(function () {
                        controller: userService.DialogController;
                        multiple: true;
                        $mdDialog.hide();
                    }, function () {});
                });
        }
    })

    //LOGIN CONTROLLER
    .controller('loginController', function ($scope, $location, $rootScope, $timeout, $state, $mdMedia, $mdDialog, loginService, mainService, vcRecaptchaService, Idle, userService, localStorageService, configurationServer) {

        resetPasswordFunc = function (ev) {
            userService.setSelected(ev);

            $mdDialog.show({
                    controller: userService.DialogController,
                    templateUrl: 'templates/pages/security/resetPassword.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    fullscreen: true,
                    multiple: true
                })
                .then(function () {
                    //userService.setSelected("")
                });
        }

        // evita que vaya atras con el boton de atras
        history.pushState(null, null, location.href);
        window.onpopstate = function (event) {
            history.go(1);
        };

        loginService.validateSession();

        //cargar key para captcha de acuerdo al ambiente
        $scope.keyCapctcha = configurationServer.obtenerKeyCaptcha();

        //$scope.paramBanner = localStorageService.get('setParamBanner');
        // Verificar si el usuario presiono la tecla enter
        $scope.verificarEnter = function (e) {
            if (e.which == 13) {
                $scope.validarLogin();
            }
        };

        //Validar Login de Usuario

        $scope.entrarLogin = function (view) {
            console.info("to login page ");
            $location.path(view);

        }

        $scope.menu = function () {
            console.info("to menu page ");
            $state.go('menu')
            //  $location.path('/menu'); 

        }

        $scope.progressBar = false;
        $scope.validarLogin = function () {

            if ($scope.loginUsuario !== null && $scope.loginUsuario !== "" && typeof $scope.loginUsuario !== "undefined" &&
                $scope.loginClave !== null && $scope.loginClave !== "" && typeof $scope.loginClave !== "undefined") {
                if ($scope.paramCaptcha == true) {
                    console.log("vcRecaptchaService.getResponse()" + JSON.stringify(vcRecaptchaService.getResponse()))
                    if (vcRecaptchaService.getResponse() == "" || vcRecaptchaService.getResponse() == "undefined") {
                        $scope.mostrarMsjLogin = true;
                        $scope.mensajeLogin = "Todos los campos en pantalla son requeridos";
                    } else {
                        doLogin();
                    }
                } else {
                    doLogin();
                }

            } else {
                $scope.mostrarMsjLogin = true;
                $scope.mensajeLogin = "Todos los campos en pantalla son requeridos";

            }
        }


        //Proceso para hacer login en el sistema y cargar variables 
        function doLogin() {
            $scope.progressBar = true;
            //si hay dos pestañas abiertas tomar el login de la primera que se logeo
            var token = localStorageService.get("setDataToken");
            //console.log("loginController token: " + token);
            if (token != null) {
                $state.go('menu.carrito')
                Idle.watch(); //Mantener el timeout al cerrar ventanas o tabs, aunque vuelve a contar cuando se vuelve a la 
                return;
            }

            //console.log("usuario_"+ $scope.loginUsuario + "pass_" + $scope.loginClave);

            $scope.userPassword = $scope.loginClave;
            //VALIDAR USUARIO Y PASSWORD SON CORRECTOS Y VALIDA Y USUARIO ACTIVO
            //SE DEBEN CAMBIAR LAS RUTAS A LAS DEL SERVIDOR
            loginService.loginUser(angular.lowercase($scope.loginUsuario), $scope.userPassword).then(function onSuccess(data) {

                if (data.status == 200) {
                    $scope.progressBar = false;
                    //name 
                    //localStorageService.set('setUsuarios', $scope.generalOpc.listaUsuarios);
                    localStorageService.set('setDataName', data.data.userFullName);
                    //token
                    localStorageService.set('setDataToken', data.data.accessToken);
                    //Scope
                    localStorageService.set('setDataScope', data.data.scope);
                    // //IP
                    localStorageService.set('setDataIp', data.data.ip);
                    // //Last Login
                    localStorageService.set('setDataLastLog', data.data.lastLogin);
                    // //tipo usuario entidad
                    localStorageService.set('setUserEntity', data.data.usuarioRolEntidad);
                    // console.log("entit" + JSON.stringify(localStorageService.get('setUserEntity')))
                    // //menu
                    localStorageService.set('setDataMenu', data.data.menuTemplate);
                    // //ID
                    localStorageService.set('setUserId', data.data.id);
                    // //Login
                    localStorageService.set('setUserLogin', data.data.login);
                    // //Cambio de password
                    localStorageService.set('setPassChange', data.data.cambioClave);
                    // //uri de consulta clave historico
                    localStorageService.set('setUriConsultaHistorico', data.data.uriValidarClaveHistorico);
                    // //uri para actualizar clave
                    localStorageService.set('setUriGuardar', data.data.uriGuardarClave);
                    // //uri de consulta clave palabras restringidas
                    localStorageService.set('setUriConsultaRestringido', data.data.uriValidarClaveRestringido);
                    // //urio valida Usuario Documento Entidad
                    localStorageService.set('setUriValidaDocEntidad', data.data.uriValidarUsuarioDocumentoEntidad);
                    // //perfil Eliminar
                    localStorageService.set('setUriPerfilEliminar', data.data.uriValidarPerfilEliminar);
                    // console.log("id_" + loginService.getUserId() + "login" + loginService.getUserLogin() + "chPass" + loginService.getPassChange());                        
                    // //uri logout
                    localStorageService.set('setUriLogout', data.data.uriLogout);
                    //validar token
                    localStorageService.set('setUriToken', data.data.uriValidaToken);
                    /*console.log("localStorageService.set(setUriToken" + localStorageService.get('setUriToken'))*/
                    // IdPerfil
                    localStorageService.set('setIdPerfil', data.data.perfilId);
                    localStorageService.set('uriFechaServidor', data.data.uriValidarFecha);
                    validaChPassword();

                } else {
                    $scope.progressBar = false;
                    $scope.mostrarMsjLogin = true;
                    $scope.mensajeLogin = data.data.message;
                }
            }).catch(function onError(data) {
                $scope.progressBar = false;
                console.error("error en respuesta de servidor");
                $mdDialog.show(
                    $mdDialog.alert()
                    .parent(angular.element(document.querySelector('#popupContainer')))
                    .clickOutsideToClose(true)
                    .title('Error en acceso al servidor')
                    .textContent('Error en acceso al servidor, por favor intente nuevamente')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('ACEPTAR')

                ); // fin mdDialog Alert 

            })


        } //fin dologin


        // Para que vaya a menú si van a login y hay sesión

        var token = localStorageService.get("setDataToken");
        // console.log("loginController token: " + token);
        if ($location.$$path == '/login' && token != null)
            $state.go('menu')
        /*función para validar si se requiere cambio de contraseña por las siguientes razones
         * Primer login del ususario
         * Cambio de contraseña por expiración
         */
    
    
        function validaChPassword() {
            //$scope.PassChange = loginService.getPassChange(); 
            $scope.PassChange = localStorageService.get('setPassChange');
            var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;
            if ($scope.PassChange) {

                $mdDialog.show({
                    controller: userService.DialogController,
                    templateUrl: 'templates/pages/userProfilePages/chPasswordUserFormInit.html',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false,
                    fullscreen: useFullScreen
                }).then(function (answer) {



                }, function () {
                    /*** COMIENZO ARREGLO 1***/
                    //Rediccionar a login haciendo logout, no deja nada en localStorage          
                    if (localStorageService.get('setPassChange')) {
                        console.log("logout por no cambio de contraseña");
                        loginService.logOutUser(
                            localStorageService.get('setDataToken'),
                            localStorageService.get('setDataScope'),
                            localStorageService.get('setUriLogout')).then(
                            function onSuccess(data) {
                                if (data.status == 200) {
                                    console.log("hizo logout correctamente");

                                    localStorageService.clearAll();
                                    //$state.go('login');
                                    $mdDialog.close();
                                    $state.go('login', {}, {
                                        reload: 'login'
                                    });
                                } else {
                                    $state.go('login');
                                    console.error("Error interno al no cambiar contraseña");
                                }

                                console.log("sin cambio de contraseña");
                                //***FIN ARREGLO 1***//



                            }).catch(function onError(data) {
                            // $state.go('home'); 
                            console.error("error en respuesta de servidor")
                            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                            localStorageService.set('setDataToken', data.data.accessToken);
                            loginService.validateSession();
                            localStorageService.clearAll();
                            $state.go('login');

                        })

                    }
                }); //fin function
                $scope.$watch(function () {
                    return $mdMedia('xs') || $mdMedia('sm');
                }, function (wantsFullScreen) {
                    $scope.customFullscreen = (wantsFullScreen === true);
                });

            } else {
                Idle.watch();
                $state.go('menu.carrito');
            }
        }


    })

    // MENU CONTROLLER
    .controller('menuController', function ($scope, $rootScope, $location, $state, $window, mainService, loginService, localStorageService,$scope, $route, $mdEditDialog, $mdMedia, $mdToast, $mdSidenav, $mdDialog, $sce, configurationServer, globalConstant, localStorageService, resourceAccessService, loginService, userService) {

        // evita que vaya atras con el boton de atras
        history.pushState(null, null, location.href);
        window.onpopstate = function (event) {
            history.go(1);
            
        };
    loginService.validateSession();
    
    $scope.generalOpc = localStorageService.get('setGeneralOpc');
    
    $scope.iniClientes = function () {
            $scope.progressBar = true;
            $scope.userScope = localStorageService.get('setDataScope');
            $scope.userToken = localStorageService.get('setDataToken');
            $scope.login = localStorageService.get('setUserLogin');
            resourceAccessService.nameValidate($scope.userScope,$scope.userToken, '/seleccione/clientes',$scope.login,null).then(function onSuccess(data) {
                $scope.progressBar = false;
                if (data.status == 200) {
                    if(data.data.otrasOpciones.clientes.length>0){
                        $scope.listadoClientes = data.data.otrasOpciones.clientes;
                        $scope.tamanoListado = data.data.otrasOpciones.clientes.length;                  
                       }else{
                        $scope.exits = true;   
                        $scope.exitsReferencia = "No se encontraron registros";
                        $scope.listadoParaCarrito = "";
                       } 
                } else {
                    $scope.progressBar = false;
                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Se recibio status diferente a 200</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
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
        
        function getUrlPermiso(permiso) {
            for (var i = 0; i < $scope.generalOpc.permisos.length; i++) {
                if ($scope.generalOpc.permisos[i].isSelected && $scope.generalOpc.permisos[i].id == permiso) {
                    return $scope.generalOpc.permisos[i].resource;
                }
            }
            return 'NOT_FOUND';
        }
    
        if (localStorageService.get('setPassChange')) {
            localStorageService.set('setDataToken', null);
            $state.go('login');
            
        }
        $scope.userName = localStorageService.get('setDataName');

        $scope.userIp = localStorageService.get('setDataIp');

        $scope.userLastlog = localStorageService.get('setDataLastLog');

        $scope.userToken = localStorageService.get('setDataToken');

        $scope.userScope = localStorageService.get('setDataScope');

        $scope.userLogin = localStorageService.get('setUserLogin');
        $scope.validaToken = localStorageService.get('setUriToken');

        //$scope.paramBanner = localStorageService.get('setParamBanner');

        // obtener menu principal de la aplicación      
        $scope.menuTrue = false;
        $scope.userMenu = localStorageService.get('setDataMenu');
        //ocultar boton menu principal cuando no haya permisos a opciones transversales de ese menú
        if ($scope.userMenu.children.length == 0) {
            $scope.menuTrue = true;
        }
        JSON.stringify($scope.userMenu);
        $scope.toggle = {};
        $rootScope.prom = '';



        $scope.menuLeft = function () {
            $mdSidenav('left').open();
        }

        $scope.closeMenu = function () {
            $mdSidenav('left').close();
        };

        $scope.isOpenRight = function () {
            return $mdSidenav('left').isOpen();
        };


        $scope.go = function (path) {
            $location.path(path);
        };

   

        $rootScope.closeSession = function () {

            if ($scope.userToken != null && $scope.userToken != "" && $scope.userToken != "undefined") {


                loginService.logOutUser($scope.userToken, $scope.userScope, localStorageService.get('setUriLogout')).then(function onSuccess(data) {
                    if (data.status == 200) {
                        //console.log ($scope.userToken ,  $scope.userScope);
                        localStorageService.clearAll()
                        $state.go('login');
                    } else {
                        //$state.go('home'); 
                        console.error("error al cerrar sesión");
                        //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                        localStorageService.set('setDataToken', data.data.accessToken);
                        loginService.validateSession();
                        localStorageService.clearAll();
                        $state.go('login');

                    }

                }).catch(function onError(data) {
                    //$state.go('home');
                    console.error("Error en acceso al servidor para cerrar sesión");
                    //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                    localStorageService.set('setDataToken', data.data.accessToken);
                    loginService.validateSession();
                    localStorageService.clearAll();
                    $state.go('login');
                })

            }
        }


        var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;
        //VALIDAR ACCESO A RECURSO 
        $scope.checkChildren = function (resource, children) {
            //console.log("children" + children.length)
            if (children.length == 0 || children == undefined) {
                validarAcceso(resource);
                $scope.closeMenu();


            }
        }

        function validarAcceso(resource) {
            if (resource.includes('productos')||resource.includes('carrito')||resource.includes('precompra')){
                console.log('entra a la dunciona');
                console.log('menu.' + resource.substring(1, resource.length).replace('/', '.'));
                $state.go('menu.' + resource.substring(1, resource.length).replace('/', '.'), {}, {
                    reload: 'menu.' + resource.substring(1, resource.length).replace('/', '.')
                });

            } else {
                resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, resource).then(function onSuccess(data) {
                    if (data.status == 200) {
                        userService.setUserData(undefined);
                        userService.setIdPerfil("");
                        userService.setSelected("");
                        resourceAccessService.setSpecificOpc(undefined);
                        //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                        localStorageService.set('setDataToken', data.data.accessToken);

                        //Opciones Generales del módulo
                        localStorageService.set('setGeneralOpc', data.data.otrasOpciones)

                        if ((!resource.includes('inventario'))&&(!resource.includes('buscar'))) {
                            console.log("no es ias")
                            $scope.profiles = localStorageService.get('setGeneralOpc');
                            JSON.stringify($scope.profiles);  
                            localStorageService.set('setResource', data.data.resource);
                            //Validacion para limpiar localstorage 20180716
                            localStorageService.set('setOperaciones', '');
                            localStorageService.set('setOtrasOpciones', '');
                            localStorageService.set('setOpeData', '');
                            $state.go('menu.' + resource.substring(1, resource.length), {}, {
                                reload: 'menu.' + resource.substring(1, resource.length)
                            });
                            console.log('menu.' + resource);
                            console.log('menu.' + resource.substring(1, resource.length));
                        } else {
                            //console.log("es ias-->>" + resource + "-->>" + resource.substring(1, resource.length).replace(new //RegExp('-', 'g'), ''));
                            //resource del recurso         
                            localStorageService.set('setResource', data.data.resource);
                            $state.go('menu.' + resource.substring(1, resource.length).replace(new RegExp('-', 'g'), ''), {}, {
                                reload: 'menu.' + resource.substring(1, resource.length).replace(new RegExp('-', 'g'), '')
                            });
                            
                        }




                    }

                }).catch(function onError(data) {
                    if (data.status == 412) {
                        //validar errores de negocio
                        console.info("Error 412" + data.data.message)

                        $mdDialog.show({
                            controller: userService.DialogController,
                            template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>' + data.data.message + '</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                            parent: angular.element(document.body),
                            clickOutsideToClose: true,
                            fullscreen: useFullScreen,
                            multiple: true

                        });
                    } else {
                        //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                        localStorageService.set('setDataToken', data.data.accessToken);
                        loginService.validateSession();
                        localStorageService.clearAll();
                        $state.go('login');

                    }

                });
            }
        }

        $scope.validarAcceso = function (resource) {
            validarAcceso(resource);
        }

        //Cambio de contraseña usuario
        $scope.chUserPassword = function (ev) {
            var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;

            //ajuste para que se robe la sesion cuando inicia otro usuario en el mismo navegador      
            if (!loginService.validateSessionR()) {
                $state.go('login');
                return;
            }

            $mdDialog.show({
                controller: userService.DialogController,
                templateUrl: 'templates/pages/userProfilePages/chPasswordUserFormInit.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: useFullScreen
            }).then(function (answer) {
                //aca va algo

            }, function () {
                console.log("dialogo cancelado")

                //$scope.status = 'You cancelled the dialog.';
            });
            $scope.$watch(function () {
                return $mdMedia('xs') || $mdMedia('sm');
            }, function (wantsFullScreen) {
                $scope.customFullscreen = (wantsFullScreen === true);
            });

        }

        //home de la pagina
        $scope.goMain = function () {

            resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, $scope.validaToken).then(function onSuccess(data) {
                if (data.status == 200) {
                    $scope.showSearch = false;
                    $scope.modulosInicio = true;
                    localStorageService.set('setDataToken', data.data.accessToken);
                    $scope.userToken = localStorageService.get('setDataToken');
                    userService.setUserData(undefined);
                    userService.setIdPerfil("");
                    userService.setSelected("");
                    //Validacion para limpiar localstorage
                    localStorageService.set('setGeneralOpc', '');
                    //
                    resourceAccessService.setSpecificOpc(undefined);
                    //localStorageService.set('setOperaciones', data.data.otrasOpciones.listaOperaciones);

                    $state.go('menu.carrito');
                }

            }).catch(function onError(data) {
                //$state.go('home');
                console.error("Error en acceso al servidor para obtener recurso" + data);

                //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                localStorageService.set('setDataToken', data.data.accessToken);
                loginService.validateSession();
                localStorageService.clearAll();
                $state.go('login');
            });
        }
        
        //buscador
        $scope.goSearch = function(ev){
            $scope.id_cliente = localStorageService.get('setIdCliente');
            if(!($scope.id_cliente==null)){
            $state.go('menu.search');
            }else{
                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>No ha seleccionado un cliente</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        fullscreen: useFullScreen,
                        multiple: true

                    });
                }
        }
    
        //cart
        $scope.goCart = function(ev){
            $scope.id_cliente = localStorageService.get('setUserLogin');
            if(!($scope.id_cliente==null)){
            $state.go('menu.precompra');
            }else{
                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>No ha seleccionado un cliente</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        fullscreen: useFullScreen,
                        multiple: true

                    });
                }
        }
        
        $scope.goCompras = function(){
            $scope.id_cliente = localStorageService.get('setIdCliente');
            if(!($scope.id_cliente==null)){
            $state.go('menu.comprar');
            }else{
                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>No ha seleccionado un cliente</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        fullscreen: useFullScreen,
                        multiple: true

                    });
                }
        }
        
        $scope.goCotizaciones = function(ev){
            $scope.id_cliente = localStorageService.get('setIdCliente');
            if(!($scope.id_cliente==null)){
            $state.go('menu.todascotizaciones');
            }else{
                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>No ha seleccionado un cliente</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        fullscreen: useFullScreen,
                        multiple: true

                    });
                }
        }

        /******************************
         * Confirmar Cierre ventana
         *******************************/
        $scope.confirmClose = function (ev) {
            var confirm = $mdDialog.confirm()
                .title('Esta cerrando la ventana sin guardar, perdera todos los cambios!')
                .ariaLabel('Lucky day')
                .targetEvent(ev)
                .ok('OK')
                .cancel('CANCELAR')
                .multiple(true);

            $mdDialog.show(confirm).then(function () {
                controller: userService.DialogController;
                multiple: true;
                $scope.status =
                $mdDialog.cancel();

            }, function () {
                $scope.status = '';
            });
        }
        
        irMotorBusqueda = function(){
            $scope.modulosInicio = false;
            console.log('funcion irMotor')
            $scope.userScope = localStorageService.get('setDataScope');
            $scope.userToken = localStorageService.get('setDataToken');
            //alert('hola')
            resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, '/inventario/formagregar').then(function onSuccess(data) {
                console.log(data.status)
                if (data.status == 200) {
                    //actualizar token 
                    console.log('entra a a aa a')
                    $scope.showSearch = true;
                    $state.go('menu.search')
                    //responseOk(data);
                } else{
                    $scope.progressBar = false;
                    $mdDialog.show({
                        controller: userService.DialogController,
                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>El archivo cargado tiene errores o no cumple con la estructura esperada</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
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
            });
        }
    
    
    
        
    
    
       
    }) // fin Controller


    // Controlador para Cambio de contraseñas Iniciales
    .controller('passwordController', function ($state, $scope, $mdDialog, $window, $location, loginService, userService, resourceAccessService, globalConstant, localStorageService) {

        //Globales Variables
        $scope.userToken = localStorageService.get('setDataToken');
        $scope.userIp = localStorageService.get('setDataIp');
        $scope.userScope = localStorageService.get('setDataScope');
        $scope.validaToken = localStorageService.get('setUriToken');
        // resource
        $scope.resource = localStorageService.get('setResource');
        //Datos usuario que inicia sesión
        $scope.id = localStorageService.get('setUserId');
        $scope.uriConsulta = localStorageService.get('setUriConsultaHistorico');
        $scope.uriGuardar = localStorageService.get('setUriGuardar');
        $scope.uriRestringido = localStorageService.get('setUriConsultaRestringido');
        //constantes de Mensajes
        $scope.requerido = globalConstant.requiredMessage;
        $scope.invalidLenght = globalConstant.invalidLenght;
        $scope.passEqConseMsg = globalConstant.passEqConsMsg;
        $scope.passConseMsg = globalConstant.passConseMsg;
        $scope.passMayusMsg = globalConstant.passMayusMsg;
        $scope.passCharaMsg = globalConstant.passCharaMsg;
        $scope.passNumbMsg = globalConstant.passNumbMsg
        $scope.confirmPass = globalConstant.confirmPass;
        $scope.LastPassMsg = globalConstant.LastPassMsg;
        $scope.invWordMsg = globalConstant.invWordMsg;
        $scope.invPassMsg = globalConstant.invPassMsg;

        var patternMayus = /([A-ZÑ])/;
        var patternNum = /([0-9])/;
        $scope.fromD = {};
        $scope.chPassUser = {};

        /**********************************************
         * CARGAR NOMBRE DE USUARIO EN CAMBIO CONTRASEÑA
         **********************************************/
        $scope.cargarDataChPass = function () {
            //$scope.dataChPass = loginService.getUserLogin();
            $scope.dataChPass = localStorageService.get('setUserLogin');
            console.log("data ch password" + JSON.stringify($scope.dataChPass))
            $scope.chPassUser.loginD = $scope.dataChPass;
        }

        /*********************************
         * Validar que en el modulo de cambio 
         * de contraseña coincida con la anterior
         **********************************/
        $scope.validaAnteriores = function (password, tipo) {
            validaAnt(password, tipo);
        }
        //funcion para validar en el servidor contraseñas anteriores
        function validaAnt(password, tipo) {

            //asignar id de usuario para verificar contraseña

            if (tipo == 'N') {
                $scope.invalidLastPass = false;
                $scope.password = {
                    id: $scope.id,
                    newPass: password,
                    oldPass: ""
                }
            } else {
                $scope.invalidPass = false;
                $scope.password = {
                    id: $scope.id,
                    newPass: "",
                    oldPass: password
                }
            }

            console.log("datapass" +
                JSON.stringify($scope.password))

            if (password !== '' && password !== undefined) {

                //actualizar token para que en caso de respuesta 204, que no tiene response, el token ya este actualizado
                resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, $scope.validaToken).then(function onSuccess(data) {
                    if (data.status == 200) {
                        //actualizar token 
                        responseOk(data);
                        resourceAccessService.searchServer($scope.userScope, $scope.userToken, "", $scope.uriConsulta, $scope.password).then(function onSuccess(data) {
                            if (data.status == 200) {
                                // console.warn("password ya existe");
                                //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor
                                //responseOk(data);
                                if (tipo == 'N') {
                                    console.warn("password ya existe N");
                                    $scope.invalidLastPass = true;
                                    $scope.formD.chPassUser.newPass.$invalid = true;
                                    return true;
                                } else {
                                    console.warn("password ya existe");
                                    $scope.invalidPass = false;
                                    $scope.formD.chPassUser.oldPass.$invalid = false;
                                    return false;
                                }

                            } else {
                                console.info("Contraseña no repetida");

                                if (tipo == 'N') {
                                    $scope.invalidLastPass = false;
                                    //$scope.LastPassMsg = "";
                                    $scope.formD.chPassUser.newPass.$invalid = false;
                                    return false;
                                } else {
                                    $scope.invalidPass = true;
                                    $scope.formD.chPassUser.oldPass.$invalid = true;
                                    return true;
                                }


                            }

                        }).catch(function onError(data) {
                            //$state.go('home');
                            $mdDialog.hide();
                            console.error("Error en acceso al servidor para obtener recurso" + data);
                            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                            localStorageService.set('setDataToken', data.data.accessToken);
                            loginService.validateSession();
                            localStorageService.clearAll();
                            $state.go('login');
                        });

                    }
                }).catch(function onError(data) {
                    //$state.go('home');
                    $mdDialog.hide();
                    console.error("Error en acceso al servidor para obtener recurso" + data);
                    //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                    localStorageService.set('setDataToken', data.data.accessToken);
                    loginService.validateSession();
                    localStorageService.clearAll();
                    $state.go('login');
                })

            }
        }


        /************************************************
         * Valida Password Formulario Cambio Contraseña
         ************************************************/
        $scope.validaPassCh = function (password) {
            //se debe ir al backend para validar que la constraseña no coincida con las ultimas 3 y que no hay palabras restringidas del diccionario
            $scope.invalidMayuPass = false;
            $scope.invalEqConsPass = false;
            $scope.invalidCharPass = false;
            $scope.invalidNumbPass = false;
            $scope.invalidLastPass = false;
            $scope.restrictiveWord = false;
            $scope.invalidConsPass = false;
            $scope.invalidlengPass = false;
            if (password != null && password != '') {
                if (!validaAnt(password, 'N')) {
                    if (userService.validaLongitudContra(password)) {
                        if (userService.caracteresConsecutivos(password)) {
                            if (userService.validarSecuen(password)) {
                                if (password.match(patternMayus)) {
                                    if (userService.validaunCaracter(password)) {
                                        if (password.match(patternNum)) {
                                            if (!restringidas(password)) {
                                                $scope.formD.chPassUser.newPass.$invalid = false;
                                            } else {
                                                console.warn("password invalida contiene palabras restringidas")
                                                $scope.formD.chPassUser.newPass.$invalid = true;
                                                $scope.restrictiveWord = true;
                                            }
                                        } else {
                                            console.warn("password invalida debe tener numero")
                                            $scope.formD.chPassUser.newPass.$invalid = true;
                                            $scope.invalidNumbPass = true;
                                        }
                                    } else {
                                        console.warn("password invalida debe tener caracter")
                                        $scope.formD.chPassUser.newPass.$invalid = true;
                                        $scope.invalidCharPass = true;
                                    }
                                } else {
                                    console.warn("password invalida debe tener mayuscula")
                                    $scope.formD.chPassUser.newPass.$invalid = true;
                                    $scope.invalidMayuPass = true;
                                }
                            } else {
                                console.warn(" password invalida caracteres  consecutivos ");
                                $scope.formD.chPassUser.newPass.$invalid = true;
                                $scope.invalidConsPass = true;
                            }
                        } else {
                            console.warn("password invalida caracteres consecutivos identicos")
                            $scope.formD.chPassUser.newPass.$invalid = true;
                            $scope.invalEqConsPass = true;
                        }
                    } else {
                        console.warn("password invalida longitud incorrecta")
                        $scope.formD.chPassUser.newPass.$invalid = true;
                        $scope.invalidlengPass = true;
                    }

                } else {
                    //valida contraseñas anteriores
                    console.warn("password invalida coincide contraseña anterior")
                    $scope.formD.chPassUser.newPass.$invalid = true;
                    $scope.invalidLastPass = true;
                }
            }
        }

    
    

        /********************************
         * Confirmar password
         *********************************/
        $scope.passConfirm = function (password, password2) {
            $scope.invalidConfirm = false;
            if (password2 != undefined) {
                if (password !== password2) {
                    console.info("constraseñas Diferentes" + password + password2)
                    $scope.formD.chPassUser.confirmPass.$invalid = true;
                    return $scope.invalidConfirm = true;
                } else {
                    $scope.formD.chPassUser.confirmPass.$invalid = false;
                    return $scope.invalidConfirm = false;
                }
            }
            return $scope.invalidConfirm = false;
        }
        // validar palabras restringidas en la contraseña
        function restringidas(palabra) {
            $scope.restrictiveWord = false;
            $scope.clave = {
                clave: palabra
            }
            if (palabra !== '' && palabra !== undefined) {

                //actualizar token para que en caso de respuesta 204, que no tiene response, el token ya este actualizado
                resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, $scope.validaToken).then(function onSuccess(data) {
                    if (data.status == 200) {
                        //actualizar token 
                        responseOk(data);
                        resourceAccessService.searchServer($scope.userScope, $scope.userToken, "", $scope.uriRestringido, $scope.clave).then(function onSuccess(data) {
                            if (data.status == 200) {
                                console.warn("Contiene palabra invalida");
                                responseOk(data);
                                $scope.formD.chPassUser.newPass.$invalid = true;
                                $scope.restrictiveWord = true;
                                return true;

                            } else {
                                console.info("No Contiene palabra invalida");
                                $scope.formD.chPassUser.newPass.$invalid = false;
                                return false;
                            }
                        }).catch(function onError(data) {
                            //$state.go('home');
                            $mdDialog.hide();
                            console.error("Error en acceso al servidor para obtener recurso" + data);
                            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                            localStorageService.set('setDataToken', data.data.accessToken);
                            loginService.validateSession();
                            localStorageService.clearAll();
                            $state.go('login');
                        });

                    }
                }).catch(function onError(data) {
                    //$state.go('home');
                    $mdDialog.hide();
                    console.error("Error en acceso al servidor para obtener recurso" + data);
                    //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                    localStorageService.set('setDataToken', data.data.accessToken);
                    loginService.validateSession();
                    localStorageService.clearAll();
                    $state.go('login');
                })

            }

        }


    

        /*****************************
         *SUBMIT FORM CAMBIO CONTRASEÑA
         ******************************/
        $scope.chPassUserForm = function (ev) {
            $scope.dataUserPass = {
                id: $scope.id,
                estado: "",
                password: $scope.chPassUser.newPass
            }

            console.log("guardarcambioContraseña___" + JSON.stringify($scope.dataUserPass));
            var confirm = $mdDialog.confirm()
                .title('Desea guardar el cambio de contraseña del Usuario?')
                //.textContent('All of the banks have agreed to forgive you your debts.')
                .ariaLabel('Lucky day')
                .targetEvent(ev)
                .ok('OK')
                .cancel('CANCELAR')
                .multiple(true);

            $mdDialog.show(confirm).then(function () {
                $scope.status =
                    /*********peticion submit******
                     *******************************/
                    resourceAccessService.submitMod($scope.userScope, $scope.userToken, $scope.uriGuardar, $scope.uriGuardar, $scope.dataUserPass, $scope.id).then(function onSuccess(data) {
                        if (data.status == 200) {
                            $mdDialog.show(
                                $mdDialog.alert()
                                .parent(angular.element(document.querySelector('#popupContainer')))
                                .clickOutsideToClose(true)
                                .title('La contraseña se actualizo con éxito')
                                .ariaLabel('Alert Dialog Demo')
                                .ok('Aceptar!')
                            )
                            console.log("respondio ok")
                            //cambio de contraseña exitoso y no hacer logout
                            localStorageService.set('setPassChange', false);
                            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor
                            //loginService.setDataToken(data.data.accessToken); 
                            localStorageService.set('setDataToken', data.data.accessToken);
                            $state.go('menu');

                        } else {
                            //$state.go('home'); 
                            console.error("Error al guardar formulario");
                            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                            localStorageService.set('setDataToken', data.data.accessToken);
                            loginService.validateSession();
                            localStorageService.clearAll();
                            $state.go('login');

                        }

                    }).catch(function onError(data) {

                        //$state.go('home');
                        $mdDialog.hide();
                        console.error("Error en acceso al servidor para obtener recurso" + data);

                        //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                        localStorageService.set('setDataToken', data.data.accessToken);
                        loginService.validateSession();
                        localStorageService.clearAll();
                        $state.go('login');
                    });
            }, function () {
                $scope.status = '';
                console.log("Dialogo cambio contraseña inicio cancelado ")
                //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor
                localStorageService.set('setDataToken', null);
                loginService.validateSession();
                localStorageService.clearAll();
                $mdDialog.close();
                $state.go('login');
            });


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
    

        /******************************
         * Confirmar Cierre ventana
         *******************************/
        $scope.confirmClose = function (ev) {
            var confirm = $mdDialog.confirm()
                .title('Esta cerrando la ventana sin guardar, perdera todos los cambios!')
                .ariaLabel('Lucky day')
                .targetEvent(ev)
                .ok('OK')
                .cancel('CANCELAR')
                .multiple(true);

            $mdDialog.show(confirm).then(function () {
                controller: userService.DialogController;
                multiple: true;
                $scope.status =
                $mdDialog.cancel();

            }, function () {
                $scope.status = '';
            });
        }
    }) // fin controller
    .directive('ngChange', function() {
    return {
        scope: {
            onchange: '&ngChange'
        },
        link: function(scope, element, attrs) {
            element.on('input', function() {
                scope.onchange();
            });
        }
    };
});
