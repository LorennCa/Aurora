angular.module('ias.usersController', ['ngIdle', 'ngMaterial'])

    .controller('usersController', function ($scope, $route, $state, $mdEditDialog, $mdMedia, $mdToast, $location, $mdSidenav, $mdDialog, mainService, loginService, resourceAccessService, userService, globalConstant, localStorageService) {

        console.log(" localStorage.length" + localStorage.length);
        // evita que vaya atras con el boton de atras
        history.pushState(null, null, location.href);
        window.onpopstate = function (event) {
            history.go(1);
        };

        loginService.validateSession();

        $scope.progressBar = false;
        //patterns
        $scope.validaNumerico = globalConstant.patternNumerico;
        $scope.validaAlfaNumerico = globalConstant.patternAlfanumerico;
        $scope.validaAlfaNumericoEsp = globalConstant.patternAlfanumericoEsp;
        $scope.validaEmail = globalConstant.patternEmail;
        $scope.validIp = globalConstant.patternIp;

        //Messages
        $scope.requerido = globalConstant.requiredMessage;
        $scope.invalidCh = globalConstant.chInvalidMessage;
        $scope.InvalidStructur = globalConstant.InvalidStructurMsg;
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
        $scope.invDvMsg = globalConstant.invDvMsg;
        $scope.invEmail = globalConstant.invEmail;
        $scope.invSpaceField = globalConstant.invSpaceField
        $scope.repPtoyComaMsg = 'No pueden existir dos punto y coma (;) seguidos';
        $scope.finPtoyComaMsg = 'La estructura del campo no debe tener espacios y debe terminar con (;)';
        $scope.repPalabraMsg = 'El campo contiene palabras repetidas';


        //Globales Variables
        $scope.userToken = localStorageService.get('setDataToken');
        $scope.userIp = localStorageService.get('setDataIp');
        $scope.userScope = localStorageService.get('setDataScope');
        // resource
        $scope.resource = localStorageService.get('setResource');
        //url para validar que no se crean dos usuarios con el mismo documento en la misma entidad    
        $scope.uriValidaDocEntidad = localStorageService.get('setUriValidaDocEntidad');
        //user entity
        $scope.userEntity = localStorageService.get('setUserEntity');
        $scope.validaToken = localStorageService.get('setUriToken');
        //user id que inicia sesion
        $scope.userId = localStorageService.get('setUserId');
        //perfil que inicia sesión
        $scope.IdProfile = localStorageService.get('setIdPerfil');
        //uir valida palabra restringida diccionario
        $scope.uriRestringidas = localStorageService.get('setUriConsultaRestringido');


        //States
        $scope.estadosCrear = ["ACTIVO", "SUSPENDIDO", "BLOQUEADO"];
        $scope.estadosModif = ["ACTIVO", "SUSPENDIDO", "BLOQUEADO", "INACTIVO"];
        $scope.estadosBloq = ["ACTIVO", "SUSPENDIDO"];
        //obtener todas las opciones para el módulo de usuarios
        $scope.generalOpc = localStorageService.get('setGeneralOpc');

        //user List
        $scope.userList = $scope.generalOpc.listaUsuarios;
        //console.log("lista_usuarios_" +  JSON.stringify($scope.userList))
        if ($scope.userList != null) {
            $scope.userListLen = $scope.userList.length;
        }


        //Uri Acceso General
        $scope.accespGeneral = "/acceso-general";
        $scope.validaPerfilesHijos = "/validar-perfiles-hijos";

        //document type list
        $scope.tipoDocumentos = $scope.generalOpc.tipoDocumento;

        //profile list 
        $scope.perfiles = [];
        if ($scope.generalOpc.listaPerfil != null || $scope.generalOpc.listaPerfil != undefined) {
            for (i = 0; i < $scope.generalOpc.listaPerfil.length; i++) {
                if (($scope.generalOpc.listaPerfil[i].id == $scope.IdProfile && $scope.userEntity.tipoRol !== 'SUPER_ADMIN') || ($scope.generalOpc.listaPerfil[i].id == 1 && $scope.userEntity.tipoRol == 'SUPER_ADMIN')) {

                } else {
                    $scope.perfiles.push($scope.generalOpc.listaPerfil[i])
                }
            }

            console.log("perfiles_" + JSON.stringify($scope.perfiles))
        } else {
            $scope.perfiles = $scope.generalOpc.listaPerfil;
        }


        //lista de entidades: scb, afiliados Mec, Emisor y SuperAdmin
        //scb
        if ($scope.generalOpc.entidades != null) {
            $scope.scbs = $scope.generalOpc.entidades.scb;
            //console.log("scb_" + JSON.stringify($scope.scbs )) 
            //Emisor
            $scope.emisores = $scope.generalOpc.entidades.emisores;
            //console.log("emisores" + JSON.stringify($scope.emisores ))
            //Super Admin
            $scope.superAdmins = $scope.generalOpc.entidades.superAdministrador;
            //console.log("superAdmin" + JSON.stringify($scope.superAdmins ))
            // afiliados Mec
            $scope.mecs = $scope.generalOpc.entidades.afiliadosMEC;
            //console.log("mec" + JSON.stringify($scope.mecs ))    
        }


        //console.log("$scope.generalOpc.permisos__"  + JSON.stringify($scope.generalOpc.permisos))
        //Diccionario de Contraseñas
        $scope.palabras = $scope.generalOpc.diccionario;
        //console.log("Diccionario_" + $scope.palabras)
        //specific Resources
        $scope.permisosurl = [];
        for (i = 0; i < $scope.generalOpc.permisos.length; i++) {
            $scope.permisosurl.push($scope.generalOpc.permisos[i].resource)
            //console.log("arrayPermisos_" + $scope.permisosurl )
        }

        /******************
         * VARIABLES
         *******************/
        $scope.form = {};
        $scope.newUser = {};
        $scope.selected = [];
        $scope.limitOptions = [5, 10, 15, 20];
        $scope.idSelectedEntity = [];
        $scope.entity = {};
        $scope.tipoRol = "";
        $scope.userSelected = [];

        $scope.idTipoDoc = [];
        $scope.idPerfil = [];
        $scope.userData = [];
        $scope.modUser = {};
        $scope.formC = {};
        $scope.bloqUser = {};
        $scope.formD = {};
        $scope.chPassUser = {};
        $scope.dictionary = {};

        $scope.options = {
            rowSelection: true,
            multiSelect: true,
            autoSelect: true,
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

        $scope.habSCB = true;
        $scope.habEmisor = true;
        $scope.habMec = true;
        $scope.habSAdmin = true;

        $scope.requiredscb = true;
        $scope.requiredemisor = true;
        $scope.requiredmec = true;
        $scope.requiredSAdmin = true;
        $scope.newUser.scb = {};
        $scope.newUser.emisor = {};
        $scope.newUser.mec = {};
        $scope.newUser.superAdmin = {};
        $scope.habSubmit = false;


        var patternMayus = /([A-ZÑ])/;
        var patternNum = /([0-9])/;


        /********************************************
        mostrar u ocultar opciones botones / permisos 
        *********************************************/
        //Consultar
        $scope.buttonCon = function () {
            for (var i = 0; i < 1; i++) {
                return $scope.generalOpc.permisos[i].isSelected;
            }
        };

        //Reportes
        $scope.buttonRep = function () {
            for (var i = 0; i < 1; i++) {
                return $scope.generalOpc.permisos[i].isSelected;
            }
        };

        //crear
        $scope.buttonNew = function () {
            for (var i = 1; i < 2; i++) {
                // console.log("entro1" +  JSON.stringify($scope.generalOpc.permisosPerfil[i].estado))
                return $scope.generalOpc.permisos[i].isSelected;
            }
        };
        //modificar
        $scope.buttonMod = function () {
            for (var i = 2; i < 3; i++) {

                return $scope.generalOpc.permisos[i].isSelected;
            }
        };

        //Bloquear
        $scope.buttonBloq = function () {
            for (var i = 3; i < 4; i++) {

                return $scope.generalOpc.permisos[i].isSelected;
            }
        };
        //Cambio Contraseña
        $scope.buttonChPass = function () {
            for (var i = 4; i < 5; i++) {

                return $scope.generalOpc.permisos[i].isSelected;
            }
        };
        //Diccionario de contraseñas
        $scope.buttonDicc = function () {
            for (var i = 5; i < 6; i++) {

                return $scope.generalOpc.permisos[i].isSelected;
            }
        };

        //validar si deshabilitar o habilitar tabla

        $scope.validarTabla = function () {

            if (($scope.buttonDicc() === false && $scope.buttonMod() === false && $scope.buttonChPass() === false && $scope.buttonBloq() === false && $scope.buttonRep() === false) && $scope.buttonNew() === true) {
                return true
            } else if (($scope.buttonDicc() === false && $scope.buttonMod() === false && $scope.buttonChPass() === false && $scope.buttonBloq() === false && $scope.buttonRep() === false) && $scope.buttonNew() === false) {
                return true
            } else {
                return false
            }
        }

        //validar que cuando solo este activo el permiso de diccionario la tabla de usuarios se oculte
        $scope.validaShow = function () {
            if (($scope.buttonNew() === false && $scope.buttonMod() === false && $scope.buttonChPass() === false && $scope.buttonBloq() === false && $scope.buttonRep() === false) && $scope.buttonDicc() === true) {
                return false
            } else if (($scope.buttonNew() === false && $scope.buttonMod() === false && $scope.buttonChPass() === false && $scope.buttonBloq() === false && $scope.buttonRep() === false) && $scope.buttonDicc() === false) {
                return false
            } else {
                return true
            }
        }

        /**************************************
         * Dialog Seleccione un item de la lista
         ***************************************/
        var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;

        function selectItem() {
            $mdDialog.show({
                controller: userService.DialogController,
                template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Debe seleccionar un usuario de la lista</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: useFullScreen

            })
        }
        /***********************************
         * Valida Tipo Documento
         ************************************/
        // $scope.validatipoDoc = function(tipo){    
        //   if(tipo == 1 || tipo == 3 || tipo == 6){     
        //     return $scope.validaNumerico;
        //   }else if(tipo == 2 || tipo == 4 || tipo == 5){
        //     return $scope.validaAlfaNumerico;
        //   }

        // }

        /******************************************************
         * VALIDA SI TIPO DE DOCUMENTO NIT Y ACTIVA EL CAMPO DV
         ******************************************************/
        $scope.validaNit = function (tipoDoc, tipo) {
            $scope.HabDv = false;
            $scope.requiredDv = false;
            if (tipoDoc == 3) {
                $scope.HabDv = true;
                $scope.requiredDv = true;
            } else {
                $scope.requiredDv = false;
                if (tipo == 'C') {
                    $scope.newUser.dv = "";
                    $scope.form.newUser.dv.$invalid = false;
                } else {
                    $scope.modUser.dv = "";
                    $scope.formC.modUser.dv.$invalid = false;
                }

            }
        }

    $scope.clientes = localStorageService.get('setClientes');
    $scope.datosClientes = [];
    $scope.agregarCliente = function(){
        $scope.cliente = $scope.newUser.cliente.nombre;
        console.log($scope.cliente)
        var textoBusqueda = $scope.cliente;
            if (textoBusqueda != "") {
            $("#DESCRIPCION_BUSQUEDA").append(" <div id='DESCRIPCION_BUSQUEDA'>"+ textoBusqueda + "</div>");
            $scope.datosClientes.push({id_cliente:$scope.newUser.cliente.id})
           } else { 
              $("#DESCRIPCION_BUSQUEDA").append('Ingrese un cliente válido');
              };   
    }
    
    
    
    
        $(document).ready(function() {
            var i = 1;
            
            
            $(document).on("click","#bn_buscar",function() {
 
            var textoBusqueda = $("#NUMERO_ITEM1").val();
            console.log()
                
            if (textoBusqueda != "") {
            $.ajax({
                url: "https://jsonplaceholder.typicode.com/posts/" + i,
                method: "GET",
                data: { textoBusqueda: textoBusqueda }
              }).done(function(data) {
                  $("#DESCRIPCION_BUSQUEDA").append(" <div id='DESCRIPCION_BUSQUEDA_" + i +"'>"+ textoBusqueda + ": " +data.title + "</div>");
                  i++;
                  $("#NUMERO_ITEM1").val("");
              });

           } else { 
              $("#DESCRIPCION_BUSQUEDA").append('Ingrese numero de item');
              };

          });
        })

        /*********************************
         * Valida tipo de Entidad
         * compara string
         * SUPER_ADMIN,
         * EMISOR,
         * AFILIADO_MEC,
         * SCB
         **********************************/


        $scope.validaUserEntity = function () {

            if ($scope.userEntity.tipoRol == 'SUPER_ADMIN') {
                $scope.habSCB = false;
                $scope.habEmisor = false;
                $scope.habMec = false;
                $scope.habSAdmin = false;

            } else if ($scope.userEntity.tipoRol == 'EMISOR') {
                //deshabilitar listas seleccionables de entidad
                $scope.habSCB = true;
                $scope.habEmisor = true;
                $scope.habMec = true;
                $scope.habSAdmin = true;
                // Emisor por defecto
                //console.log("entidad_" + JSON.stringify($scope.userEntity.entidad.id) )
                if ($scope.emisores.length != 0) {
                    for (i = 0; i < $scope.emisores.length; i++) {
                        $scope.idSelectedEntity.push($scope.emisores[i].id)
                        //console.log("ids" + $scope.idSelectedEntity)
                    }
                    var idx = $scope.idSelectedEntity.indexOf($scope.userEntity.entidad.id);
                    if (idx > -1) {

                        $scope.emisorSelected = $scope.emisores[idx].id
                    }
                }

            } else if ($scope.userEntity.tipoRol == 'AFILIADO_MEC') {
                $scope.habSCB = true;
                $scope.habEmisor = true;
                $scope.habMec = true;
                $scope.habSAdmin = true;
                // Afiliado Mec por defecto
                //console.log("entidad_" + JSON.stringify($scope.userEntity.entidad.id) )
                if ($scope.mecs.length != 0) {
                    for (i = 0; i < $scope.mecs.length; i++) {
                        $scope.idSelectedEntity.push($scope.mecs[i].id)
                        //console.log("ids" + $scope.idSelectedEntity)
                    }
                    var idx = $scope.idSelectedEntity.indexOf($scope.userEntity.entidad.id);
                    if (idx > -1) {
                        $scope.mecSelected = $scope.mecs[idx].id
                    }
                }

            } else {
                //SCB
                $scope.habSCB = true;
                $scope.habEmisor = true;
                $scope.habMec = true;
                $scope.habSAdmin = true;
                // Scb por defecto
                //console.log("entidad_" + JSON.stringify($scope.userEntity.entidad.id) )
                if ($scope.scbs.length != 0) {
                    for (i = 0; i < $scope.scbs.length; i++) {
                        $scope.idSelectedEntity.push($scope.scbs[i].id)
                        //console.log("ids" + $scope.idSelectedEntity)
                    }
                    var idx = $scope.idSelectedEntity.indexOf($scope.userEntity.entidad.id);
                    if (idx > -1) {
                        $scope.scbSelected = $scope.scbs[idx].id
                    }
                }

            }


        }

        /*
         *validar que campos se habilitan dependiendo del tipo de entidad
         *SCB : Deshabilita Mec, SuperAdmin,Emisor
         *MEC : Deshabilita Scb, SuperAdmin, Emisor
         *SuperAdmin : Deshabilita Mec,Scb,Emisor
         *Emisor : Deshabilita Mec, Scb, SuperAdmin
         */

        $scope.habOptions = function (select) {

            switch (select) {
                case 1:
                    if ($scope.newUser.scb == "undefined") {
                        $scope.newUser.scb = {};
                        optionsEntity();
                    } else {
                        if ($scope.userEntity.tipoRol == 'SCB') {
                            $scope.habSCB = true;
                        } else {
                            $scope.habSCB = false;
                        }
                        $scope.habEmisor = true;
                        $scope.habMec = true;
                        $scope.habSAdmin = true;
                        $scope.newUser.emisor = {};
                        $scope.newUser.mec = {};
                        $scope.newUser.superAdmin = {};
                        $scope.requiredscb = true;
                        $scope.requiredemisor = false;
                        $scope.requiredmec = false;
                        $scope.requiredSAdmin = false;
                    }
                    break;
                case 2:
                    if ($scope.newUser.emisor == "undefined") {
                        optionsEntity();
                        $scope.newUser.emisor = {};
                    } else {
                        if ($scope.userEntity.tipoRol == 'EMISOR') {
                            $scope.habEmisor = true;
                        } else {
                            $scope.habEmisor = false;
                        }
                        $scope.habSCB = true;
                        $scope.habMec = true;
                        $scope.habSAdmin = true;
                        $scope.newUser.mec = {};
                        $scope.newUser.scb = {};
                        $scope.newUser.superAdmin = {};
                        $scope.requiredscb = false;
                        $scope.requiredemisor = true;
                        $scope.requiredmec = false
                        $scope.requiredSAdmin = false;
                    }
                    break;
                case 3:
                    if ($scope.newUser.mec == "undefined") {
                        optionsEntity();
                        $scope.newUser.mec = {};
                    } else {
                        if ($scope.userEntity.tipoRol == 'AFILIADO_MEC') {
                            $scope.habMec = true;
                        } else {
                            $scope.habMec = false;
                        }
                        $scope.habSCB = true;
                        $scope.habEmisor = true;
                        $scope.habSAdmin = true;
                        $scope.newUser.emisor = {};
                        $scope.newUser.scb = {};
                        $scope.newUser.superAdmin = {};
                        $scope.requiredscb = false;
                        $scope.requiredemisor = false;
                        $scope.requiredmec = true
                        $scope.requiredSAdmin = false;

                    }
                    break;
                case 4:
                    if ($scope.newUser.superAdmin == "undefined") {
                        optionsEntity();
                        $scope.newUser.superAdmin = {};

                    } else {
                        //if ($scope.userEntity.tipoRol == 'SUPER_ADMIN'){
                        // $scope.habSAdmin = true;
                        //}else{
                        $scope.habSAdmin = false;
                        //}            
                        $scope.habSCB = true;
                        $scope.habEmisor = true;
                        $scope.habMec = true;
                        $scope.newUser.emisor = {};
                        $scope.newUser.scb = {};
                        $scope.newUser.mec = {};
                        $scope.requiredscb = false;
                        $scope.requiredemisor = false;
                        $scope.requiredmec = false
                        $scope.requiredSAdmin = true;
                    }
                    break;
                default:
            }
        }

        function optionsEntity() {
            //console.log("entro a optionsEntity")
            $scope.habSCB = false;
            $scope.habEmisor = false;
            $scope.habMec = false;
            $scope.habSAdmin = false;

        }

        /**************************************************************************************
         * VALIDACIONES A CONTRASEÑAS 
         ***************************************************************************************/
        /*********************************
         * Valida Password Formulario Nuevo
         **********************************/
        $scope.validaPassNew = function (password) {
            //se debe ir al backend para validar que la constraseña no coincida con las ultimas 3 y que no hay palabras restringidas del diccionario
            $scope.invalidMayuPass = false;
            $scope.invalEqConsPass = false;
            $scope.invalidCharPass = false;
            $scope.invalidNumbPass = false;
            $scope.restrictiveWord = false;
            $scope.invalidConsPass = false;
            $scope.invalidlengPass = false;
            if (password != null && password != '') {
                if (userService.validaLongitudContra(password)) {
                    if (userService.caracteresConsecutivos(password)) {
                        if (userService.validarSecuen(password)) {
                            if (password.match(patternMayus)) {
                                if (userService.validaunCaracter(password)) {
                                    if (password.match(patternNum)) {
                                        if (!restringidas(password, 'N')) {
                                            $scope.form.newUser.password.$invalid = false;
                                        } else {
                                            console.warn("passsword invalida palabra restringida")
                                            $scope.form.newUser.password.$invalid = true;
                                            $scope.restrictiveWord = true;
                                        }
                                    } else {
                                        console.warn("password invalida debe tener numero")
                                        $scope.form.newUser.password.$invalid = true;
                                        $scope.invalidNumbPass = true;
                                    }
                                } else {
                                    console.warn("password invalida debe tenere caracter")
                                    $scope.form.newUser.password.$invalid = true;
                                    $scope.invalidCharPass = true;
                                }
                            } else {
                                console.warn("password invalida debe tener mayuscula")
                                $scope.form.newUser.password.$invalid = true;
                                $scope.invalidMayuPass = true;
                            }
                        } else {
                            console.warn("password invalida caracteres  consecutivos");
                            $scope.form.newUser.password.$invalid = true;
                            $scope.invalidConsPass = true;
                        }
                    } else {
                        console.warn("password invalida consecutivos identicos")
                        $scope.form.newUser.password.$invalid = true;
                        $scope.invalEqConsPass = true;
                    }
                } else {
                    console.warn("password invalida logintud incorrecta")
                    $scope.form.newUser.password.$invalid = true;
                    $scope.invalidlengPass = true;
                }
            }


        }


        /********************************
         * Confirmar password
         *********************************/
        $scope.passConfirm = function (password, password2, tipo) {
            $scope.invalidConfirm = false;
            if (password2 != undefined) {
                if (password !== password2) {
                    console.warn("constraseñas Diferentes" + password + password2)
                    //console.log("tipo" + tipo) 
                    if (tipo == 'N') {
                        $scope.form.newUser.ConfirmPass.$invalid = true;
                    } else {
                        $scope.formD.chPassUser.confirmPass.$invalid = true;
                    }

                    return $scope.invalidConfirm = true;
                } else {
                    if (tipo == 'N') {
                        $scope.form.newUser.ConfirmPass.$invalid = false;
                    } else {
                        $scope.formD.chPassUser.confirmPass.$invalid = false;
                    }
                    return $scope.invalidConfirm = false;
                }
            }
            return $scope.invalidConfirm = false;


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
                                            if (restringidas(password, 'C')) {
                                                $scope.formD.chPassUser.newPass.$invalid = true;
                                                $scope.restrictiveWord = true;
                                            } else {
                                                $scope.formD.chPassUser.newPass.$invalid = false;
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
                                console.warn("password invalida caracteres consecutivos")
                                $scope.formD.chPassUser.newPass.$invalid = true;
                                $scope.invalidConsPass = true;
                            }
                        } else {
                            console.warn("password invalida caracteres consecutivos identicos");
                            $scope.formD.chPassUser.newPass.$invalid = true;
                            $scope.invalEqConsPass = true;
                        }

                    } else {
                        console.warn("password invalida longitud incorrecta");
                        $scope.formD.chPassUser.newPass.$invalid = true;
                        $scope.invalidlengPass = true;
                    }

                } else {
                    //valida contraseñas anteriores
                    console.warn("entro aca false contraseña last")
                    $scope.formD.chPassUser.newPass.$invalid = true;
                    $scope.invalidLastPass = true;
                }
            }
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
                    id: $scope.dataChPass[0].id,
                    newPass: password,
                    oldPass: ""
                }
            } else {
                $scope.invalidPass = false;
                $scope.password = {
                    id: $scope.dataChPass[0].id,
                    newPass: "",
                    oldPass: password
                }
            }

            console.log("datapass" +
                $scope.password)

            if (password !== '' && password !== undefined) {

                //actualizar token para que en caso de respuesta 204, que no tiene response, el token ya este actualizado
                resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, $scope.validaToken).then(function onSuccess(data) {
                    if (data.status == 200) {
                        //actualizar token 
                        responseOk(data);

                        resourceAccessService.searchServer($scope.userScope, $scope.userToken, $scope.resource, $scope.permisosurl[4], $scope.password, "").then(function onSuccess(data) {
                            if (data.status == 200) {
                                console.warn("password ya existe");
                                //responseOk(data);
                                if (tipo == 'N') {
                                    $scope.invalidLastPass = true;
                                    $scope.formD.chPassUser.newPass.$invalid = true;
                                } else {
                                    $scope.invalidPass = false;
                                    $scope.formD.chPassUser.oldPass.$invalid = false;
                                }

                            } else {
                                console.info("Contraseña no repetida");

                                if (tipo == 'N') {
                                    $scope.invalidLastPass = false;

                                    $scope.formD.chPassUser.newPass.$invalid = false;
                                } else {
                                    $scope.invalidPass = true;
                                    $scope.formD.chPassUser.oldPass.$invalid = true;
                                }

                                //return false;               
                            }

                        }).catch(function onError(data) {
                            //$state.go('home');
                            $mdDialog.hide();
                            console.error("Error en acceso al servidor para obtener recurso");
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

        //fin valida password 

        /*Validaciones de la tabla*/
        $scope.toggleLimitOptions = function () {
            $scope.limitOptions = $scope.limitOptions ? undefined : [5, 10, 15];
        };

        //Esto es para sacar una lista desplegable en la tabla
        // $scope.getTypes = function () {
        //   return ['Candy', 'Ice cream', 'Other', 'Pastry'];
        // };

        $scope.loadStuff = function () {
            $scope.promise = $timeout(function () {
                // loading
            }, 2000);
        }

        $scope.logItem = function (item) {
            console.info(item.id, 'was selected');
            console.info("array_" + JSON.stringify($scope.selected))


        };

        $scope.logOrder = function (order) {
            console.log('order: ', order);
        };

        $scope.logPagination = function (page, limit) {
            console.log('page: ', page);
            console.log('limit: ', limit);
        }

        /**************************
         * Validar estado Inactivo
         ***************************/
        $scope.validaEstado = function (estado) {
            if (estado == 'INACTIVO') {

                $mdDialog.show({
                    controller: userService.DialogController,
                    template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Ha seleccionado el estado Inactivo, esta opción inhabilita al usuario del sistema y no permite que sea activado nuevamente</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false,
                    fullscreen: useFullScreen,
                    multiple: true

                });

            }
        }

        /************************************
         * Valida que login no exista
         *************************************/
        $scope.validarLogin = function (login) {
            $scope.spaceField = false;

            if (login !== '' && login !== undefined) {
                if (login.split(" ").length > 1) {
                    //console.log("palabra mayor a 1")
                    //validar que login no contenga espacios
                    $scope.spaceField = true;
                    $scope.form.newUser.login.$invalid = true;
                } else {
                    //actualizar token para que en caso de respuesta 204, que no tiene response, el token ya este actualizado
                    resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, $scope.validaToken).then(function onSuccess(data) {
                        if (data.status == 200) {
                            //actualizar token 
                            responseOk(data);
                            resourceAccessService.nameValidate($scope.userScope, $scope.userToken, $scope.permisosurl[0], login.trim(), "").then(function onSuccess(data) {
                                if (data.status == 200) {
                                    console.warn("Login ya existe en el Sistema");
                                    responseOk(data);
                                    $scope.exits = true;
                                    $scope.form.newUser.login.$invalid = true;
                                    $scope.exitsPro = data.data.message;

                                } else {
                                    console.info("Login no existe en el Sistema");
                                    $scope.exits = false;
                                    $scope.form.newUser.login.$valid;
                                    $scope.exitsPro = "";

                                }

                            }).catch(function onError(data) {
                                //$state.go('home');
                                $mdDialog.hide();
                                console.error("Error en acceso al servidor para obtener recurso");
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


        }

        //poner en false bandera de login repetido para que no se muestre el mensaje mientras se esta escrbiendo 
        $scope.setExits = function () {
            //console.log("setExits")
            $scope.exits = false;
        }

        /*************************************
         * validar regex de email
         **************************************/
        // $scope.validaEmail = function(email, tipo){
        //    $scope.invalidEmail = false;
        //   if (email != "" && email != undefined ){
        //     if(!email.match(globalConstant.patternEmail)){
        //       console.warn("regex no cumple")
        //       $scope.invalidEmail = true;
        //       if(tipo == 'N'){
        //         $scope.form.newUser.email.$invalid= true;
        //       }else{

        //       }
        //     }

        //   }

        // }
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


        /*********************************************************************************
         * USUARIO NUEVO
         **********************************************************************************/
        $scope.nuevoUsuario = function (ev) {

            //dataAccess.validaPerfilesHijos
            resourceAccessService.validaPerfilesHijos($scope.userScope, $scope.userToken, $scope.accespGeneral.concat($scope.validaPerfilesHijos)).then(function onSuccess(data) {
                if (data.status == 200) {

                    var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;
                    $scope.selected = [];
                    $mdDialog.show({
                            controller: userService.DialogController,
                            templateUrl: 'templates/pages/userProfilePages/newUserForm.html',
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
                    $state.go('login');
                }
            });

        }

        /*************************************
         * SUBMIT FORM NUEVO
         **************************************/
        $scope.progressBar = false;

        //validar que tipo de entidad esta asociada al usuario
        function validaEntity() {
            if ($scope.userEntity.tipoRol == 'EMISOR') {
                //console.log("emisor_" + $scope.newUser.emisor)
                $scope.entity = $scope.newUser.emisor.id;
                $scope.tipoRol = 'EMISOR';
            } else if ($scope.userEntity.tipoRol == 'AFILIADO_MEC') {
                //console.log("mec" + $scope.newUser.mec)
                $scope.entity = $scope.newUser.mec.id;
                $scope.tipoRol = 'AFILIADO_MEC';
            } else if ($scope.userEntity.tipoRol == 'SCB') {
                //console.log("scb" + $scope.newUser.scb)
                $scope.entity = $scope.newUser.scb.id;
                $scope.tipoRol = 'SCB';
            } else {
                //console.log("$scope.newUser.emisor" + JSON.stringify($scope.newUser.emisor))
                //VALIDAR CAMPOS SI ES SUPER ADMIN
                if ($scope.habSAdmin == false && $scope.habEmisor == true &&
                    $scope.habMec == true &&
                    $scope.habSCB == true) {
                    //console.log("SA1" + $scope.newUser.superAdmin)
                    $scope.entity = $scope.newUser.superAdmin.id;
                    $scope.tipoRol = 'SUPER_ADMIN';
                } else if ($scope.habSCB == false && $scope.habSAdmin == true && $scope.habEmisor == true &&
                    $scope.habMec == true) {
                    //console.log("SCB1" + $scope.newUser.scb)
                    $scope.entity = $scope.newUser.scb.id;
                    $scope.tipoRol = 'SCB';
                } else if ($scope.habMec == false && $scope.habSCB == true && $scope.habSAdmin == true && $scope.habEmisor == true) {
                    //console.log("mec1" + $scope.newUser.mec)
                    $scope.entity = $scope.newUser.mec.id;
                    $scope.tipoRol = 'AFILIADO_MEC';
                } else {
                    //console.log("emi1" + JSON.stringify($scope.newUser.emisor))
                    $scope.entity = $scope.newUser.emisor.id;
                    $scope.tipoRol = 'EMISOR';
                }

            }

        }

        // armar json para submit
        function armarJsonNuevo() {
            $scope.dataUser = {
                tipoDocumento: {
                    id: $scope.newUser.tipo
                },
                numeroIdentificacion: $scope.newUser.numeroDoc,
                dv: $scope.newUser.dv,
                nombres: $scope.newUser.nombre,
                apellidos: $scope.newUser.apellidos,
                telefono: $scope.newUser.telefono,
                direccion: $scope.newUser.direccion,
                email: $scope.newUser.email,
                login: $scope.newUser.login,
                password: $scope.newUser.password,
                perfil: {
                    id: $scope.newUser.perfil.id
                },
                estado: $scope.newUser.estado,
                listIpPermitidas: [
                    {
                        ip: $scope.newUser.ip1
          },
                    {
                        ip: $scope.newUser.ip2
          },
                    {
                        ip: $scope.newUser.ip3
          }

        ],
                usuarioRolEntidad: {
                    entidad: {
                        id: $scope.entity
                    },
                    tipoRol: $scope.tipoRol
                },
                clientes:$scope.datosClientes
                
            }
        }

        $scope.submitUserForm = function (ev) {

            validaEntity();
            armarJsonNuevo();
            console.log("data_Json_" + JSON.stringify($scope.dataUser))
            var confirm = $mdDialog.confirm()
                .title('Está seguro que desea incluir el nuevo usuario?')
                //.textContent('All of the banks have agreed to forgive you your debts.')
                .ariaLabel('Lucky day')
                .targetEvent(ev)
                .ok('OK')
                .cancel('CANCELAR')
                .multiple(true);

            $mdDialog.show(confirm).then(function () {
                multiple: true;
                $scope.habSubmit = true;
                $scope.status =

                /***********************************************************************************
                 *Validar si hay un usuario con número y tipo de documento igual en la misma entidad*/
                //json para consulta
                $scope.dataDoc = {
                    numeroDocumento: $scope.newUser.numeroDoc,
                    tipoDocumento: {
                        id: $scope.newUser.tipo
                    },
                    usuarioRolEntidad: {
                        entidad: {
                            id: $scope.entity
                        },
                        tipoRol: $scope.tipoRol
                    }

                }
                //actualizar token para que en caso de respuesta 204, que no tiene response, el token ya este actualizado
                resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, $scope.validaToken).then(function onSuccess(data) {
                    if (data.status == 200) {
                        //actualizar token 
                        responseOk(data);

                        console.log("dataDoc" + JSON.stringify($scope.dataDoc))
                        $scope.progressBar = true;
                        resourceAccessService.searchServer($scope.userScope, $scope.userToken, $scope.resource, $scope.uriValidaDocEntidad, $scope.dataDoc, null).then(function onSuccess(data) {
                            if (data.status == 200) {

                                console.warn("numero de documento ya existe");
                                console.log("data" + data.data.otrasOpciones);
                                //responseOk(data);
                                $scope.progressBar = false;
                                /*Se realiza ajuste para que solo pregunte si esta seguro de incluir el usuario cuando 
                                 * es uno de estos casos:
                                 * scb o afiliado mec +  emisor
                                 * De lo contrario debe solo scar el mensaje de alerta 
                                 */
                                console.log("tipoRol----" + $scope.tipoRol);
                                if (data.data.otrasOpciones.businessErrorCode == 103) {
                                    var confirm = $mdDialog.confirm()
                                        .title('Alerta')
                                        .textContent('El documento ya existe en la entidad, como Afliado Al MEC/SCB/Emisor/SuperAdmin, ¿desea continuar?')
                                        .ariaLabel('Lucky day')
                                        .targetEvent(ev)
                                        .ok('OK')
                                        .cancel('CANCELAR')
                                        .multiple(true);
                                    $mdDialog.show(confirm).then(function () {
                                        $scope.progressBar = true;
                                        submitServer();
                                        //});
                                    }, function () {
                                        console.log("Se ha cancelado el popup");
                                        $scope.progressBar = false;
                                        //$scope.status = 'You cancelled the dialog.';
                                    });
                                    //ELSE OTRO TIPOROL
                                } else {
                                    //                $scope.progressBar = true;
                                    //                submitServer();

                                    $mdDialog.show({
                                        controller: userService.DialogController,
                                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>El documento ya existe en la entidad, como Afliado Al MEC/SCB/Emisor/SuperAdmin</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                                        parent: angular.element(document.body),
                                        clickOutsideToClose: false,
                                        fullscreen: useFullScreen,
                                        multiple: true

                                    });
                                }




                            } else {
                                console.info("numero de documento no existe");

                                /*********peticion submit******
                                 *******************************/
                                $scope.progressBar = true;
                                submitServer();
                            }
                        }).catch(function onError(data) {
                            //$state.go('home');
                            $mdDialog.hide();
                            console.error("Error en acceso al servidor para obtener recurso");
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

            }, function () {
                $scope.status = '';
                //$mdDialog.hide();
            });


        }

        console.log("resource")
    console.log($scope.resource)
    console.log($scope.permisosurl[1])
        /*SUBMIT USUARIO*/
        function submitServer() {
            resourceAccessService.submitServer($scope.userScope, $scope.userToken, $scope.resource, $scope.permisosurl[1], $scope.dataUser).then(function onSuccess(data) {
                if (data.status == 200) {
                    //if(data.data.otrasOpciones.businessErrorCode == 100){
                    responseOk(data);
                    $scope.progressBar = false;
                    $mdDialog.show(
                        $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#popupContainer')))
                        .clickOutsideToClose(true)
                        .title('El Usuario se ha creado con éxito')
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar!')
                    )
                    console.log("respondio ok");
                    /*				  var datos = localStorageService.get('setGeneralOpc');				                   
                    				   //console.log("data***" + JSON.stringify(datos));
                                                datos.listaUsuarios = [];
                                                datos.listaUsuarios = data.data.otrasOpciones.listaUsuarios;  
                    				  // console.log("Usuarios***" + JSON.stringify(datos.listaUsuarios));
                    			    	localStorageService.set('setGeneralOpc', datos);*/


                    var datos = localStorageService.get('setGeneralOpc');
                    // otrasOpciones.listaDemanda = [];
                    datos.listaUsuarios.push(data.data.otrasOpciones.listaUsuarios[0]);
                    localStorageService.set('setGeneralOpc', datos);

                    $state.go('menu.usuarios', {}, {
                        reload: 'menu.usuarios'
                    });
                } else {
                    //$state.go('home'); 
                    $mdDialog.hide();
                    console.error("Error en acceso al servidor para obtener recurso" + data);
                    //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                    localStorageService.set('setDataToken', data.data.accessToken);
                    loginService.validateSession();
                    localStorageService.clearAll();
                    $state.go('login');
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



        //funcion usada en la creacion de usuarios
        function validarUsuario() {

            $scope.existeUsuario = false;
            console.log("lista_usuarios_" + JSON.stringify(datos.listaUsuarios))
            if (datos.listaUsuarios != null) {
                for (i = 0; i < datos.listaUsuarios.length; i++) {
                    if (datos.listaUsuarios[i].tipoDocumento === $scope.newUser.tipo && datos.listaUsuarios[i].numeroIdentificacion === $scope.newUser.numeroDoc && datos.listaUsuarios[i].usuarioRolEntidad.entidad.id === $scope.entity) {
                        $scope.existeUsuario = true;
                    }
                }
            }
            return $scope.existeUsuario;
        }

        /**************************************************************************************
         * MODIFICAR USUARIO
         **************************************************************************************/
        $scope.modificaUsuario = function (ev) {

            if ($scope.selected.length == 0) {
                selectItem();
            } else if ($scope.selected.length > 1) {
                $mdDialog.show({
                    controller: userService.DialogController,
                    template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Solo puede modificar un usuario</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false,
                    fullscreen: useFullScreen

                });
                $scope.selected = [];
            } else {
                userService.setUserData($scope.selected);
                $mdDialog.show({
                        controller: userService.DialogController,
                        templateUrl: 'templates/pages/userProfilePages/modifiedUserForm.html',
                        parent: angular.element(document.body),
                        targetEvent: ev,
                        clickOutsideToClose: false,
                        fullscreen: useFullScreen
                    })
                    .then(function (answer) {

                    }, function () {
                        $scope.selected = [];
                        $scope.userList = $scope.generalOpc.listaUsuarios;
                        //$scope.status = 'You cancelled the dialog.';
                    });
                $scope.$watch(function () {
                    return $mdMedia('xs') || $mdMedia('sm');
                }, function (wantsFullScreen) {
                    $scope.customFullscreen = (wantsFullScreen === true);
                });
            }

        }


        /*******************************
         * Cargar datos para modificar
         ********************************/

        $scope.habInactivo = false;
        $scope.idPerfiles = [];
        $scope.cargarData = function () {

            //console.log("cargar_" + JSON.stringify(userService.getUserData()))
            $scope.userData = userService.getUserData();
            console.log($scope.userData);
            //lore
            //validar si perfil de usuario esta dentro de la lista  de perfiles hijos, de lo contrario bloquear campos

            for (i in $scope.perfiles) {

                $scope.idPerfiles.push($scope.perfiles[i].id)
            }


            $scope.modUser.tipoC = $scope.userData[0].tipoDocumento.id;
            //número de Documento   
            $scope.modUser.numeroDocC = $scope.userData[0].numeroIdentificacion;
            //Digito de verficacion si viene Nit
            if ($scope.userData[0].dv !== "" && $scope.userData[0].dv !== null) {
                $scope.HabDv = true;
                $scope.requiredDv = true;
                $scope.modUser.dv = $scope.userData[0].dv;
                ///console.log("dv" + $scope.modUser.dv )
            }
            //nombres
            $scope.modUser.nombre = $scope.userData[0].nombres;
            //apellidos
            $scope.modUser.apellidos = $scope.userData[0].apellidos;
            $scope.modUser.telefono = $scope.userData[0].telefono;
            $scope.modUser.direccion = $scope.userData[0].direccion;
            //email
            $scope.modUser.email = $scope.userData[0].email;
            //login
            $scope.modUser.login = $scope.userData[0].login;
            //contraseña
            $scope.modUser.passwordC = $scope.userData[0].password;
            //confirm Contraseña
            $scope.modUser.ConfirmPassC = $scope.userData[0].password;
            //estado
            $scope.modUser.estadoC = $scope.userData[0].estado;
            $scope.modUser.perfilC = $scope.userData[0].perfil.id;
            $scope.modUser.perfilPadre = $scope.userData[0].perfil.perfilSuperior.id;



            //deshabilitar campos en los siguientes casos:
            //*usuario inactivo
            /*  if ($scope.modUser.estadoC == 'INACTIVO'){
                  console.log("INACTIVO")
               $scope.habInactivo = true;
               $scope.habSubmit = true;
               //*el usuario a modificar pertenece a mi mismo perfil y no sea entidad super administradora   
             }else if($scope.IdProfile == $scope.modUser.perfilC && $scope.userEntity.tipoRol != 'SUPER_ADMIN'){
                 console.log("MISMO PERFIL")
               $scope.habInactivo = true;
               $scope.habSubmit = true;
                  //*el usuario a modificar no es un perfil hijo y no sea entidad super administradora   
             }else if($scope.IdProfile != $scope.modUser.perfilPadre && $scope.userEntity.tipoRol != 'SUPER_ADMIN'){
                 console.log("PERFIL PADRE")
               $scope.habInactivo = true;
               $scope.habSubmit = true;

             }*/
            //tipo de entidad a la que pertenece el usuario
            if ($scope.userData[0].usuarioRolEntidad.tipoRol == 'SUPER_ADMIN') {
                $scope.modUser.superAdmin = $scope.userData[0].usuarioRolEntidad.entidad.id;
            } else if ($scope.userData[0].usuarioRolEntidad.tipoRol == 'AFILIADO_MEC') {
                $scope.modUser.mec = $scope.userData[0].usuarioRolEntidad.entidad.id;
            } else if ($scope.userData[0].usuarioRolEntidad.tipoRol == 'EMISOR') {
                $scope.modUser.emisor = $scope.userData[0].usuarioRolEntidad.entidad.id;
            } else {
                $scope.modUser.scb = $scope.userData[0].usuarioRolEntidad.entidad.id;
            }

            //ip1
            $scope.modUser.ip1 = $scope.userData[0].listIpPermitidas[0].ip;
            //ip2
            if ($scope.userData[0].listIpPermitidas[1] != null && $scope.userData[0].listIpPermitidas[1].ip != null) {
                $scope.modUser.ip2 = $scope.userData[0].listIpPermitidas[1].ip;
            }

            //ip3
            if ($scope.userData[0].listIpPermitidas[2] != null && $scope.userData[0].listIpPermitidas[2].ip != null) {
                $scope.modUser.ip3 = $scope.userData[0].listIpPermitidas[2].ip;
            }

            var idx = $scope.idPerfiles.indexOf($scope.userData[0].perfil.id);
            if (idx > -1) {
                validaHabilitaCampos();
            } else {
                $scope.habInactivo = true;
                $scope.habSubmit = true;
            }

        } //Fin function carga Data


        function validaHabilitaCampos() {


            //deshabilitar campos en los siguientes casos:
            //*usuario inactivo
            if ($scope.modUser.estadoC == 'INACTIVO') {
                console.log("INACTIVO")
                $scope.habInactivo = true;
                $scope.habSubmit = true;
                //*el usuario a modificar pertenece a mi mismo perfil y no sea entidad super administradora   
            } else if ($scope.IdProfile == $scope.modUser.perfilC && $scope.userEntity.tipoRol != 'SUPER_ADMIN') {
                console.log("MISMO PERFIL")
                $scope.habInactivo = true;
                $scope.habSubmit = true;
                //*el usuario a modificar no es un perfil hijo y no sea entidad super administradora   
            } else if ($scope.IdProfile != $scope.modUser.perfilPadre && $scope.userEntity.tipoRol != 'SUPER_ADMIN') {
                console.log("PERFIL PADRE")
                $scope.habInactivo = true;
                $scope.habSubmit = true;

            }
        }

        /*****************************
         *SAVE MODIFICAR
         *******************************/

        // armar json para submit
        function armarJsonMod() {

            if ($scope.modUser.ip2 == false) {
                $scope.modUser.ip2 = ""
            }
            if ($scope.modUser.ip3 == false) {
                $scope.modUser.ip3 = ""
            }

            $scope.dataUser = {
                tipoDocumento: {
                    id: $scope.modUser.tipoC
                },
                numeroIdentificacion: $scope.modUser.numeroDocC,
                dv: $scope.modUser.dv,
                nombres: $scope.modUser.nombre,
                apellidos: $scope.modUser.apellidos,
                telefono: $scope.modUser.telefono,
                direccion: $scope.modUser.direccion,
                email: $scope.modUser.email,
                login: $scope.modUser.login,
                //password : $scope.modUser.passwordC,
                perfil: {
                    id: $scope.modUser.perfilC
                },
                estado: $scope.modUser.estadoC,
                listIpPermitidas: [
                    {
                        ip: $scope.modUser.ip1
          },
                    {
                        ip: $scope.modUser.ip2
          },
                    {
                        ip: $scope.modUser.ip3
          }

        ],
                usuarioRolEntidad: {
                    entidad: {
                        id: $scope.userData[0].usuarioRolEntidad.entidad.id
                    },
                    tipoRol: $scope.userData[0].usuarioRolEntidad.tipoRol
                }

            }
        }
        /*SUBMIT FORM MODIFICAR*/
        $scope.modifiedUserForm = function (ev) {
            validaEntity();
            armarJsonMod();
            console.log("guardar___" + JSON.stringify($scope.dataUser));
            var confirm = $mdDialog.confirm()
                .title('Desea guardar los cambios del Usuario?')
                //.textContent('All of the banks have agreed to forgive you your debts.')
                .ariaLabel('Lucky day')
                .targetEvent(ev)
                .ok('OK')
                .cancel('CANCELAR')
                .multiple(true);

            $mdDialog.show(confirm).then(function () {
                $scope.habSubmit = true;
                $scope.status =
                    /***********************************************************************************
                     *Validar si hay un usuario con número y tipo de documento igual en la misma entidad*/
                    //json para consulta
                    $scope.dataDoc = {
                        id: $scope.userData[0].id,
                        numeroDocumento: $scope.modUser.numeroDocC,
                        tipoDocumento: {
                            id: $scope.modUser.tipoC
                        },
                        usuarioRolEntidad: {
                            entidad: {
                                id: $scope.userData[0].usuarioRolEntidad.entidad.id
                            },
                            tipoRol: $scope.tipoRol
                        }
                    }

                //datos iniciales:
                $scope.tipodocIni = $scope.userData[0].tipoDocumento.id;
                $scope.modUser.numdocIni = $scope.userData[0].numeroIdentificacion;

                //actualizar token para que en caso de respuesta 204, que no tiene response, el token ya este actualizado
                resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, $scope.validaToken).then(function onSuccess(data) {
                    if (data.status == 200) {
                        //actualizar token 
                        responseOk(data);
                        $scope.progressBar = true;

                        if ($scope.tipodocIni != $scope.modUser.tipoC || $scope.modUser.numdocIni != $scope.modUser.numeroDocC) {

                            resourceAccessService.searchServer($scope.userScope, $scope.userToken, $scope.resource, $scope.uriValidaDocEntidad, $scope.dataDoc, null).then(function onSuccess(data) {
                                if (data.status == 200) {
                                    console.warn("numero de documento ya existe");
                                    //responseOk(data);
                                    $scope.progressBar = false;
                                    $mdDialog.show({
                                        controller: userService.DialogController,
                                        template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>El documento ya existe en la entidad, como Afliado Al MEC/SCB/Emisor/SuperAdmin</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                                        parent: angular.element(document.body),
                                        clickOutsideToClose: false,
                                        fullscreen: useFullScreen,
                                        multiple: true

                                    });
                                    $scope.habSubmit = false;

                                } else {
                                    console.info("numero de documento no existe");
                                    /*********peticion submit******
                                     *******************************/
                                    $scope.progressBar = true;
                                    resourceAccessService.submitMod($scope.userScope, $scope.userToken, $scope.resource, $scope.permisosurl[2], $scope.dataUser, $scope.userData[0].id).then(function onSuccess(data) {
                                        if (data.status == 200) {
                                            responseOk(data);
                                            //console.log("token actualizado" + localStorageService.get('setDataToken'))
                                            $scope.progressBar = false;


                                            var datos = localStorageService.get('setGeneralOpc');
                                            console.log("Usuarios***" + JSON.stringify(datos.listaUsuarios));
                                            //Actualizar listaUsuarios
                                            for (i = 0; i < datos.listaUsuarios.length; i++) {
                                                if (datos.listaUsuarios[i].id == data.data.otrasOpciones.listaUsuarios[0].id) {
                                                    datos.listaUsuarios.splice(i, 1);
                                                    datos.listaUsuarios.push(data.data.otrasOpciones.listaUsuarios[0]);
                                                    break;
                                                }
                                            }
                                            localStorageService.set('setGeneralOpc', datos);
                                            $state.go('menu.usuarios', {}, {
                                                reload: 'menu.usuarios'
                                            });




                                            $mdDialog.show(
                                                $mdDialog.alert()
                                                .parent(angular.element(document.querySelector('#popupContainer')))
                                                .clickOutsideToClose(true)
                                                .title('El Usuario se ha modificado con éxito')
                                                .ariaLabel('Alert Dialog Demo')
                                                .ok('Aceptar!')
                                            )
                                            console.log("respondio ok")
                                            /*var datos = localStorageService.get('setGeneralOpc');
				   //console.log("data***" + JSON.stringify(datos));
                            datos.listaUsuarios = [];
                            datos.listaUsuarios = data.data.otrasOpciones.listaUsuarios;  
				  // console.log("Usuarios***" + JSON.stringify(datos.listaUsuarios));
			    	localStorageService.set('setGeneralOpc', datos);*/

                                        } else {
                                            $scope.progressBar = false;
                                            console.error("Error al guardar formulario");
                                            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                                            localStorageService.set('setDataToken', data.data.accessToken);
                                            loginService.validateSession();
                                            localStorageService.clearAll();
                                            $state.go('login');


                                        }
                                        // catch modificar
                                    }).catch(function onError(data) {
                                        $scope.progressBar = false;
                                        // $state.go('home');
                                        $mdDialog.hide();
                                        console.error("Error en acceso al servidor para obtener recurso" + data);
                                        //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                                        localStorageService.set('setDataToken', data.data.accessToken);
                                        loginService.validateSession();
                                        localStorageService.clearAll();
                                        $state.go('login');
                                    });

                                } //fin else
                            }).catch(function onError(data) {
                                $scope.progressBar = false;
                                $mdDialog.hide();
                                console.error("Error en acceso al servidor para obtener recurso");
                                //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                                localStorageService.set('setDataToken', data.data.accessToken);
                                loginService.validateSession();
                                localStorageService.clearAll();
                                $state.go('login');

                            });
                        } //fin if
                        else {
                            $scope.progressBar = true;
                            resourceAccessService.submitMod($scope.userScope, $scope.userToken, $scope.resource, $scope.permisosurl[2], $scope.dataUser, $scope.userData[0].id).then(function onSuccess(data) {
                                if (data.status == 200) {
                                    responseOk(data);
                                    //console.log("token actualizado" + localStorageService.get('setDataToken'))
                                    $scope.progressBar = false;
                                    $mdDialog.show(
                                        $mdDialog.alert()
                                        .parent(angular.element(document.querySelector('#popupContainer')))
                                        .clickOutsideToClose(true)
                                        .title('El Usuario se ha modificado con éxito')
                                        .ariaLabel('Alert Dialog Demo')
                                        .ok('Aceptar!')
                                    )
                                    console.log("respondio ok")
                                    /*var datos = localStorageService.get('setGeneralOpc');
				   //console.log("data***" + JSON.stringify(datos));
                            datos.listaUsuarios = [];
                            datos.listaUsuarios = data.data.otrasOpciones.listaUsuarios;  
				  // console.log("Usuarios***" + JSON.stringify(datos.listaUsuarios));
			    	localStorageService.set('setGeneralOpc', datos);
				   
                   $state.go('menu.usuarios', {}, { reload: 'menu.usuarios' });*/


                                    var datos = localStorageService.get('setGeneralOpc');
                                    console.log("Usuarios***" + JSON.stringify(datos.listaUsuarios));
                                    //Actualizar listaUsuarios
                                    for (i = 0; i < datos.listaUsuarios.length; i++) {
                                        if (datos.listaUsuarios[i].id == data.data.otrasOpciones.listaUsuarios[0].id) {
                                            datos.listaUsuarios.splice(i, 1);
                                            datos.listaUsuarios.push(data.data.otrasOpciones.listaUsuarios[0]);
                                            break;
                                        }
                                    }
                                    localStorageService.set('setGeneralOpc', datos);
                                    $state.go('menu.usuarios', {}, {
                                        reload: 'menu.usuarios'
                                    });

                                } else {
                                    $scope.progressBar = false;
                                    //$state.go('home'); 
                                    console.error("Error al guardar formulario");
                                    //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                                    localStorageService.set('setDataToken', data.data.accessToken);
                                    loginService.validateSession();
                                    localStorageService.clearAll();
                                    $state.go('login');


                                }
                                // catch modificar
                            }).catch(function onError(data) {
                                $scope.progressBar = false;
                                // $state.go('home');
                                $mdDialog.hide();
                                console.error("Error en acceso al servidor para obtener recurso" + data);
                                //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                                localStorageService.set('setDataToken', data.data.accessToken);
                                loginService.validateSession();
                                localStorageService.clearAll();
                                $state.go('login');
                            });
                        }

                    }

                }).catch(function onError(data) {
                    $scope.progressBar = false;
                    // $state.go('home');
                    $mdDialog.hide();
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


        }



        /**************************************************************************************
         * BLOQUEAR USUARIO
         ***************************************************************************************/

        $scope.bloqUsuario = function (ev) {
            //dataAccess.validaPerfilesHijos
            resourceAccessService.validaPerfilesHijos($scope.userScope, $scope.userToken, $scope.accespGeneral.concat($scope.validaPerfilesHijos)).then(function onSuccess(data) {
                if (data.status == 200) {



                    if ($scope.selected.length == 0) {
                        selectItem();
                    } else if ($scope.selected.length > 1) {
                        $mdDialog.show({
                            controller: userService.DialogController,
                            template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Solo puede Desbloquear un usuario</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                            parent: angular.element(document.body),
                            clickOutsideToClose: false,
                            fullscreen: useFullScreen

                        });
                        $scope.selected = [];
                    } else if ($scope.selected[0].estado !== 'BLOQUEADO') {
                        $mdDialog.show({
                            controller: userService.DialogController,
                            template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>El Usuario no se encuentra Bloqueado</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                            parent: angular.element(document.body),
                            clickOutsideToClose: false,
                            fullscreen: useFullScreen

                        })
                        $scope.selected = [];
                    } else if ($scope.IdProfile == $scope.selected[0].perfil.id && $scope.userEntity.tipoRol != 'SUPER_ADMIN') {
                        console.log("MISMO PERFIL")
                        $mdDialog.show({
                            controller: userService.DialogController,
                            template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>No tiene permisos para gestionar este usuario</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                            parent: angular.element(document.body),
                            clickOutsideToClose: false,
                            fullscreen: useFullScreen

                        });
                        $scope.selected = [];
                        //*el usuario a modificar no es un perfil hijo y no sea entidad super administradora   
                    } else if ($scope.IdProfile != $scope.selected[0].perfil.perfilSuperior.id && $scope.userEntity.tipoRol != 'SUPER_ADMIN') {
                        console.log("PERFIL PADRE")
                        $mdDialog.show({
                            controller: userService.DialogController,
                            template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>No tiene permisos para gestionar este usuario</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                            parent: angular.element(document.body),
                            clickOutsideToClose: false,
                            fullscreen: useFullScreen

                        });
                        $scope.selected = [];

                    } else {
                        userService.setUserData($scope.selected);
                        $mdDialog.show({
                                controller: userService.DialogController,
                                templateUrl: 'templates/pages/userProfilePages/blockUser.html',
                                parent: angular.element(document.body),
                                targetEvent: ev,
                                clickOutsideToClose: false,
                                fullscreen: useFullScreen
                            })
                            .then(function (answer) {


                            }, function () {
                                $scope.selected = [];
                            });
                        $scope.$watch(function () {
                            return $mdMedia('xs') || $mdMedia('sm');
                        }, function (wantsFullScreen) {
                            $scope.customFullscreen = (wantsFullScreen === true);
                        });
                    }


                }
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
                    $state.go('login');
                }
            });



        }

        /********************************
         * CARGAR NOMBRE DE USUARIO A BLOQUEAR
         *********************************/
        $scope.cargarDataBloq = function () {
            $scope.dataBloq = userService.getUserData();
            //console.log("data Bloq" + JSON.stringify($scope.dataBloq[0].login))
            $scope.bloqUser.loginB = $scope.dataBloq[0].login;

        }

        /********************************
         * SUBMIT FROM BLOQUEADO
         *********************************/
        $scope.bloqUserForm = function (ev) {
            $scope.dataUserBloq = {
                id: $scope.dataBloq[0].id,
                estado: $scope.bloqUser.estado,
                password: ""
            }

            console.info("guardarBloq___" + JSON.stringify($scope.dataUserBloq));
            var confirm = $mdDialog.confirm()
                .title('Desea guardar el cambio de estado del Usuario?')
                //.textContent('All of the banks have agreed to forgive you your debts.')
                .ariaLabel('Lucky day')
                .targetEvent(ev)
                .ok('OK')
                .cancel('CANCELAR')
                .multiple(true);

            $mdDialog.show(confirm).then(function () {
                $scope.habSubmit = true;
                $scope.status =
                    /*********peticion submit******
                     *******************************/
                    $scope.progressBar = true;
                resourceAccessService.submitMod($scope.userScope, $scope.userToken, $scope.resource, $scope.permisosurl[3], $scope.dataUserBloq, $scope.dataBloq[0].id).then(function onSuccess(data) {
                    if (data.status == 200) {
                        responseOk(data);
                        $scope.progressBar = false;
                        $mdDialog.show(
                            $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#popupContainer')))
                            .clickOutsideToClose(true)
                            .title('El usuario se actualizó con éxito')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar!')
                        )
                        console.log("respondio ok");

                        //            var datos = localStorageService.get('setGeneralOpc');
                        //              //console.log("data***" + JSON.stringify(datos));			  
                        //                datos.listaUsuarios = [];
                        //                datos.listaUsuarios = data.data.otrasOpciones.listaUsuarios;  
                        //			  // console.log("Usuarios***" + JSON.stringify(datos.listaUsuarios));
                        //			  localStorageService.set('setGeneralOpc', datos);

                        var datos = localStorageService.get('setGeneralOpc');

                        //Actualizar listaUsuarios
                        for (i = 0; i < datos.listaUsuarios.length; i++) {
                            if (datos.listaUsuarios[i].id == data.data.otrasOpciones.listaUsuarios[0].id) {
                                datos.listaUsuarios.splice(i, 1);
                                datos.listaUsuarios.push(data.data.otrasOpciones.listaUsuarios[0]);
                                break;
                            }
                        }

                        localStorageService.set('setGeneralOpc', datos);
                        $state.go('menu.usuarios', {}, {
                            reload: 'menu.usuarios'
                        });
                    }

                }).catch(function onError(data) {
                    $scope.progressBar = false;
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
            });

        }


        /**************************************************************************************
         * RESTABLECER DE CONTRASEÑA
         **************************************************************************************/
        $scope.chPassUser = function (ev) {
            //dataAccess.validaPerfilesHijos
            resourceAccessService.validaPerfilesHijos($scope.userScope, $scope.userToken, $scope.accespGeneral.concat($scope.validaPerfilesHijos)).then(function onSuccess(data) {
                if (data.status == 200) {

                    if ($scope.selected.length == 0) {
                        selectItem();
                    } else if ($scope.selected.length > 1) {
                        $mdDialog.show({
                            controller: userService.DialogController,
                            template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Solo puede cambiar la contraseña a un usuario</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                            parent: angular.element(document.body),
                            clickOutsideToClose: false,
                            fullscreen: useFullScreen

                        });
                        $scope.selected = [];
                    } else {
                        userService.setUserData($scope.selected);
                        if ($scope.selected[0].estado == 'INACTIVO') {
                            $mdDialog.show({
                                controller: userService.DialogController,
                                template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>No puede realizar modificaciones sobre un usuario en estado Inactivo</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                                parent: angular.element(document.body),
                                clickOutsideToClose: false,
                                fullscreen: useFullScreen

                            });
                            $scope.selected = [];
                        } else if ($scope.IdProfile == $scope.selected[0].perfil.id && $scope.userEntity.tipoRol != 'SUPER_ADMIN') {
                            console.log("MISMO PERFIL")
                            $mdDialog.show({
                                controller: userService.DialogController,
                                template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>No tiene permisos para gestionar este usuario</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                                parent: angular.element(document.body),
                                clickOutsideToClose: false,
                                fullscreen: useFullScreen

                            });
                            $scope.selected = [];
                            //*el usuario a modificar no es un perfil hijo y no sea entidad super administradora   
                        } else if ($scope.IdProfile != $scope.selected[0].perfil.perfilSuperior.id && $scope.userEntity.tipoRol != 'SUPER_ADMIN') {
                            console.log("PERFIL PADRE")
                            $mdDialog.show({
                                controller: userService.DialogController,
                                template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>No tiene permisos para gestionar este usuario</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                                parent: angular.element(document.body),
                                clickOutsideToClose: false,
                                fullscreen: useFullScreen

                            });
                            $scope.selected = [];

                        } else {
                            $mdDialog.show({
                                    controller: userService.DialogController,
                                    templateUrl: 'templates/pages/userProfilePages/chPasswordUserForm.html',
                                    parent: angular.element(document.body),
                                    targetEvent: ev,
                                    clickOutsideToClose: false,
                                    fullscreen: useFullScreen
                                })
                                .then(function (answer) {

                                }, function () {
                                    $scope.selected = [];

                                });
                            $scope.$watch(function () {
                                return $mdMedia('xs') || $mdMedia('sm');
                            }, function (wantsFullScreen) {
                                $scope.customFullscreen = (wantsFullScreen === true);
                            });
                        }
                    }

                }
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
                    $state.go('login');
                }
            });




        }


        /**********************************************
         * CARGAR NOMBRE DE USUARIO EN CAMBIO CONTRASEÑA
         **********************************************/
        $scope.cargarDataChPass = function () {
            $scope.dataChPass = userService.getUserData();
            //console.log("data ch password" + JSON.stringify($scope.dataChPass[0].login))
            $scope.chPassUser.loginD = $scope.dataChPass[0].login;

        }

        /*****************************
         *SUBMIT FORM CAMBIO CONTRASEÑA
         ******************************/
        $scope.chPassUserForm = function (ev) {
            $scope.dataUserPass = {
                id: $scope.dataChPass[0].id,
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
                $scope.habSubmit = true;
                $scope.status =
                    /*********peticion submit******
                     *******************************/
                    $scope.progressBar = true;
                resourceAccessService.submitMod($scope.userScope, $scope.userToken, $scope.resource, $scope.permisosurl[2], $scope.dataUserPass, $scope.dataChPass[0].id).then(function onSuccess(data) {
                    if (data.status == 200) {
                        responseOk(data);
                        $scope.progressBar = false;
                        //$scope.userList.push((data.data.otrasOpciones.listaUsuarios[0]));
                        $mdDialog.show(
                            $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#popupContainer')))
                            .clickOutsideToClose(true)
                            .title('La contraseña se modifico correctamente.')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar!')
                        )
                        console.log("respondio ok");
                        /*var datos = localStorageService.get('setGeneralOpc');
              //console.log("data***" + JSON.stringify(datos));			  
                datos.listaUsuarios = [];
                datos.listaUsuarios = data.data.otrasOpciones.listaUsuarios;  
			  // console.log("Usuarios***" + JSON.stringify(datos.listaUsuarios));
			  localStorageService.set('setGeneralOpc', datos);
				   */

                        var datos = localStorageService.get('setGeneralOpc');

                        //Actualizar listaUsuarios
                        for (i = 0; i < datos.listaUsuarios.length; i++) {
                            if (datos.listaUsuarios[i].id == data.data.otrasOpciones.listaUsuarios[0].id) {
                                datos.listaUsuarios.splice(i, 1);
                                datos.listaUsuarios.push(data.data.otrasOpciones.listaUsuarios[0]);
                                break;
                            }
                        }

                        localStorageService.set('setGeneralOpc', datos);

                        $state.go('menu.usuarios', {}, {
                            reload: 'menu.usuarios'
                        });

                    }

                }).catch(function onError(data) {
                    $scope.progressBar = false;
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
            });


        }

        /***************************************************************************************
         * DICCIONARIO DE CONTRASEÑAS
         ****************************************************************************************/

        $scope.diccionario = function (ev) {

            $scope.selected = [];
            $mdDialog.show({
                    controller: userService.DialogController,
                    templateUrl: 'templates/pages/userProfilePages/passwordDictionary.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    fullscreen: useFullScreen
                })
                .then(function (answer) {

                    //aca va algo

                }, function () {
                    $scope.selected = [];
                    //$scope.status = 'You cancelled the dialog.';
                });
            $scope.$watch(function () {
                return $mdMedia('xs') || $mdMedia('sm');
            }, function (wantsFullScreen) {
                $scope.customFullscreen = (wantsFullScreen === true);
            });

        }

        /**********************************************
         * CARGAR DICCIONARIO DE CONTRASEÑAS
         **********************************************/
        $scope.cargarPalabras = function () {

            $scope.dictionary.palabras = $scope.palabras;
        }

        $scope.validaPalabras = function (palabras) {
            $scope.repPuntoyComa = false;
            $scope.finPuntoyComa = false;
            $scope.invLenght = false;
            $scope.repPalabra = false;

            if (palabras != undefined && palabras != "") {
                if (palabras.indexOf(";;") > -1) {
                    console.log("; seguidos")
                    $scope.repPuntoyComa = true;
                    $scope.form.dictionary.palabra.$invalid = true;
                } else {
                    //$scope.form.dictionary.palabras.$invalid = false;
                    var arrayPalDiv = palabras.split(";");
                    console.log("Array" + arrayPalDiv)
                    if (puntoycomafinal(palabras)) {
                        //console.log("tiene punto y como final")
                        $scope.form.dictionary.palabra.$invalid = false;
                        if (palabraRepetida(arrayPalDiv)) {
                            //console.log("palabra repetida");
                            $scope.form.dictionary.palabra.$invalid = true;
                            $scope.repPalabra = true;
                        } else {
                            for (i = 0; i < arrayPalDiv.length; i++) {
                                if (arrayPalDiv[i].length > 15) {
                                    $scope.form.dictionary.palabra.$invalid = true;
                                    $scope.invLenght = true;
                                    $scope.invLenghtMsg = arrayPalDiv[i] + ' tiene una longitud mayor a la permitida';
                                    console.log("tiene una longitud mayor a la permitida" + arrayPalDiv[i]);
                                } else {
                                    console.info("Longitud correcta");
                                    $scope.form.dictionary.palabra.$invalid = false;
                                }
                            }
                        }
                    } else {
                        $scope.finPuntoyComa = true;
                        $scope.form.dictionary.palabra.$invalid = true;
                        //console.log("No tiene punto y como final")
                    }

                }
            }
        }


        function puntoycomafinal(cadena) {
            var regexpuntoycomafinal = "[A-Za-z0-9áéíóúñÑÁÉÍÓÚ_+-.,!@#$%^&*()\\/|<>\"']\\S+;$";
            var separapal1 = cadena.split(";");
            if (cadena.length > 1) {
                if (separapal1.length == 1 && cadena.match(regexpuntoycomafinal)) {
                    return true;
                } else {
                    if (cadena.match(regexpuntoycomafinal)) {
                        return true;
                    }
                }
            }
            return false;
        }


        function palabraRepetida(array) {
            //console.log("long" + array +array.length)
            for (i = 0; i < array.length; i++) {
                for (j = i + 1; j < array.length; j++) {
                    if (array[i] === array[j]) {
                        console.info("repetidas" + array[i] + array[j])
                        return true;
                    }
                }
            }
            //console.info("NO repetidas" + array[i] + array[j])
            return false;
        }

        /***************************************
         * SUBMIT FORM DICCIONARIO DE CONTRASEÑAS
         ****************************************/

        $scope.dictionaryFormSubmit = function (ev) {
            console.info($scope.dictionary.palabras);
            $scope.dictionaryWords = {
                clave: $scope.dictionary.palabras
            }

            var confirm = $mdDialog.confirm()
                .title('Desea guardar los cambios en el diccionario?')
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
                    $scope.progressBar = true;
                resourceAccessService.submitMod($scope.userScope, $scope.userToken, $scope.permisosurl[5], $scope.permisosurl[5], $scope.dictionaryWords, "").then(function onSuccess(data) {
                    if (data.status == 200) {

                        //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor
                        responseOk(data);
                        $scope.generalOpc.diccionario = (data.data.otrasOpciones.diccionario);
                        var datos = localStorageService.get('setGeneralOpc');
                        datos.diccionario = data.data.otrasOpciones.diccionario;
                        localStorageService.set('setGeneralOpc', datos);

                        /* datos.listaUsuarios = [];
            datos.listaUsuarios = data.data.otrasOpciones.listaUsuarios;  			
			localStorageService.set('setGeneralOpc', datos); */

                        // otrasOpciones.listaDemanda = [];
                        datos.listaUsuarios.push(data.data.otrasOpciones.listaUsuarios[0]);
                        localStorageService.set('setGeneralOpc', datos);

                        $state.go('menu.usuarios', {}, {
                            reload: 'menu.usuarios'
                        });

                        $scope.progressBar = false;
                        $mdDialog.show(
                            $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#popupContainer')))
                            .clickOutsideToClose(true)
                            .title('El Diccionario de contraseñas se actualizó con éxito')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar!')

                        )


                    }

                }).catch(function onError(data) {
                    $scope.progressBar = false;
                    //$state.go('home');
                    $mdDialog.hide();
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
        }

        function restringidas(palabra, tipo) {
            $scope.clave = {
                clave: palabra
            }
            if (palabra !== '' && palabra !== undefined) {
                //actualizar token para que en caso de respuesta 204, que no tiene response, el token ya este actualizado
                resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, $scope.validaToken).then(function onSuccess(data) {
                    if (data.status == 200) {
                        //actualizar token 
                        responseOk(data);
                        resourceAccessService.searchServer($scope.userScope, $scope.userToken, $scope.resource,
                            $scope.uriRestringidas, $scope.clave).then(function onSuccess(data) {
                            //$scope.permisosurl[5] , $scope.clave).then(function onSuccess(data){
                            if (data.status == 200) {
                                console.warn("Contiene palabra invalida");
                                //responseOk(data);
                                if (tipo == 'N') {
                                    $scope.form.newUser.password.$invalid = true;
                                } else {
                                    $scope.formD.chPassUser.newPass.$invalid = true;
                                }

                                $scope.restrictiveWord = true;
                                return true;
                            } else {
                                console.info("No Contiene palabra invalida");
                                if (tipo == 'N') {
                                    $scope.form.newUser.password.$invalid = false;
                                } else {
                                    $scope.formD.chPassUser.newPass.$invalid = false;
                                }

                                return false;
                            }
                        }).catch(function onError(data) {
                            //$state.go('home');
                            $mdDialog.hide();
                            console.error("Error en acceso al servidor para obtener recurso");
                            //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor

                            localStorageService.set('setDataToken', data.data.accessToken);
                            loginService.validateSession();
                            localStorageService.clearAll();
                            $state.go('login');
                        });

                    }
                }).catch(function onError(data) {
                    // $state.go('home');
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
        // fin diccionario


        /**********************************
         * GENERAR REPORTE DE USUARIOS
         ***********************************/
        function userArray(users) {
            $scope.itemsToAdd = [];
            if (users.length > 0) {
                for (i = 0; i < users.length; i++) {
                    $scope.itemsToAdd.push(users[i].id);
                    //console.log("id menu: " + users[i].id + "itemsToAdd" + JSON.stringify($scope.itemsToAdd));
                }
            }
        }

        /*funcion para enviar al servidor la informacion de los usuarios para reporte*/
        $scope.reportes = function (ev) {
            console.info("$scope.selected" + JSON.stringify($scope.selected));

            var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;
            if ($scope.selected != undefined && $scope.selected.length > 0) {
                $mdDialog.show({
                        controller: userService.DialogController,
                        templateUrl: 'templates/pages/userProfilePages/generateReport.tmpl.html',
                        parent: angular.element(document.body),
                        targetEvent: ev,
                        clickOutsideToClose: false,
                        fullscreen: useFullScreen
                    })
                    .then(function (answer) {
                        console.info("seleccionados__" + answer + JSON.stringify($scope.selected))
                        userArray($scope.selected);
                        //actualizar token antes de hacer solicitud  de reporte
                        resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, $scope.validaToken).then(function onSuccess(data) {
                            if (data.status == 200) {
                                //actualizar token 
                                responseOk(data);
                                //llamar funcion que genera el reporte dependiendo de answer pdf o xls
                                $scope.progressBar = true;
                                resourceAccessService.generateReport($scope.userScope, $scope.userToken, $scope.permisosurl[0], $scope.itemsToAdd, answer).then(function onSuccess(data) {
                                    if (data.status == 200) {
                                        console.log("generado");
                                        //PDF
                                        if (answer == 'PDF') {
                                            var file = new Blob([data.data], {
                                                type: 'application/pdf'
                                            });
                                            var fileURL = URL.createObjectURL(file);
                                            var a = document.createElement('a');
                                            a.href = fileURL;
                                            a.target = '_blank';
                                            a.download = 'reporte_usuarios.pdf';
                                            document.body.appendChild(a);
                                            a.click();
                                        } else {
                                            //XLSX
                                            var file = new Blob([data.data], {
                                                type: 'application/xlsx'
                                            });
                                            var fileURL = URL.createObjectURL(file);
                                            var a = document.createElement('a');
                                            a.href = fileURL;
                                            a.target = '_blank';
                                            a.download = 'reporte_usuarios.xlsx';
                                            document.body.appendChild(a);
                                            a.click();

                                        }
                                        $scope.progressBar = false;
                                        $scope.itemsToAdd = [];
                                        $mdDialog.show(
                                            $mdDialog.alert()
                                            .parent(angular.element(document.querySelector('#popupContainer')))
                                            .clickOutsideToClose(true)
                                            .title('Se ha generado el reporte con éxito')
                                            .ariaLabel('Alert Dialog Demo')
                                            .ok('Aceptar!')
                                        )
                                        //limpiar lista perfiles cuando se ha generado reporte
                                        $scope.selected = []
                                        $state.go('menu.usuarios', {}, {
                                            reload: 'menu.usuarios'
                                        });
                                    }
                                }).catch(function onError(data) {
                                    // $state.go('home');
                                    console.error("Error en acceso al servidor para obtener recurso");
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

                    }, function () {

                        $scope.selected = [];
                    });
                $scope.$watch(function () {
                    return $mdMedia('xs') || $mdMedia('sm');
                }, function (wantsFullScreen) {
                    $scope.customFullscreen = (wantsFullScreen === true);
                });

            } else {
                $mdDialog.show({
                    controller: userService.DialogController,
                    template: '<md-dialog aria-label="Mensaje Alerta" ng-cloak><form><md-toolbar><div class="md-toolbar-tools"><md-icon md-svg-src="components/assets/svg/warning_white.svg" ></md-icon><h5>Mensaje Alerta</h5><span flex></span><md-button class="md-icon-button" ng-click="cancel()"><md-icon md-svg-src="components/assets/svg/close.svg" aria-label="Close dialog"></md-icon></md-button></div></md-toolbar><md-dialog-content><div class="md-dialog-content"><h4>Seleccione los usuarios para generar el reporte</h4></div></md-dialog-content><md-dialog-actions layout="row"><md-button type="submit" ng-click="cancel()" layout="row" layout-align="center center">ACEPTAR</md-button></md-dialog-actions></form></md-dialog>',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false,
                    fullscreen: useFullScreen

                });
            }


        };
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

        /**********************************************************************
         * Método para actualizar la pantalla con  la lista de usuarios reciente
         ************************************************************************/

        $scope.refresh = function (ev) {
            resourceAccessService.validarAcceso($scope.userToken, $scope.userScope, '/usuarios').then(function onSuccess(data) {
                if (data.status == 200) {
                    localStorageService.set('setDataToken', data.data.accessToken);
                    $scope.userToken = localStorageService.get('setDataToken');
                    userService.setUserData(undefined);
                    userService.setSelected("");
                    resourceAccessService.setSpecificOpc(undefined);
                    console.log("Imprime usuarios" + JSON.stringify(data.data.otrasOpciones.listaUsuarios));
                    var datos = localStorageService.get('setGeneralOpc');
                    //console.log("data***" + JSON.stringify(datos));
                    datos.listaUsuarios = [];
                    datos.listaUsuarios = data.data.otrasOpciones.listaUsuarios;
                    // console.log("Usuarios***" + JSON.stringify(datos.listaUsuarios));
                    localStorageService.set('setGeneralOpc', datos);
                    $scope.listaUsuarios = datos.listaUsuarios;
                    if ($scope.listaUsuarios != null) {
                        $scope.listaUsuariosLen = $scope.listaUsuarios.length;
                    }

                    $state.go('menu.usuarios', {}, {
                        reload: 'menu.usuarios'
                    });

                }

            }).catch(function onError(data) {
                console.error("Error en acceso al servidor para obtener recurso" + data);
                //actualizar token si ya ha expirado y se requirio refresh token en el lado del servidor                    
                localStorageService.set('setDataToken', data.data.accessToken);
                loginService.validateSession();
                localStorageService.clearAll();
                $state.go('login');
            });
        }


    }); //fin controller
