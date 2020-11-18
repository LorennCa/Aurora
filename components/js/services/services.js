angular.module('aurora.services', [])
    //Service main


    //Service Login
    .factory('loginService', function ($http, $state, configurationServer, localStorageService) {
        var angularAPI = {};
        var rutaConfig = configurationServer.obtenerRutaConfig();
        var userData = {};
        var userName = {};
        var userToken = {};
        var userScope = {};
        var userIp = {};
        var userLastLog = {};
        var userMenu = {};

        userData.validateSession = function () {
            var token = localStorageService.get("setDataToken");
            if (token == null) {
                $state.go('login'); // go to login
            }
        }

        //ajuste para que se robe la sesion cuando inicia otro usuario en el mismo navegador
        userData.validateSessionR = function () {
            var token = localStorageService.get("setDataToken");
            if (token == null) {
                return false;
            } else
                return true;
        }

        userData.loginUser = function (usuario, clave) {
            //debugger;
            return $http({
                method: "POST",
                url: rutaConfig + '/authenticate/login',
                data: {
                    user: usuario,
                    password: clave
                },
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }


        userData.logOutUser = function (accessToken, scope, uri) {
            var parametros = {
                'scope': scope,
                'accesstoken': accessToken,
                'uri': uri
            };
            return $http({
                method: "DELETE",
                url: rutaConfig + '/authenticate/logout',
                data: {},
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }

            });
        }

        return userData;

    })

    // Parametros iniciales
    .factory('mainService', function ($http, configurationServer) {
        var initialParams = {};
        var paramCaptcha = {};
        var paramBanner = {};
        var paramTimeOut = {};
        var requiredMessage = {};
        var invalidChMessage = {};


        var paramFailed = {};
        var rutaConfig = configurationServer.obtenerRutaConfig();

        initialParams.InitialParams = function () {
            return $http({
                method: 'GET',
                //url    : rutaConfig + '/authenticate/appConf ', 
                url: rutaConfig + '/acceso-general/appConf ',
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }

        initialParams.loadFestivos = function () {
            return $http({
                method: 'GET',
                url: 'components/json/festivos.json',
                headers: {
                    'Content-Type': 'application/json'
                }

            });
        }

        // Obtener variable captcha
        initialParams.setParamCaptcha = function (data) {
            //debugger;
            initialParams.paramCaptcha = data;
        }
        initialParams.getParamCaptcha = function () {
            return initialParams.paramCaptcha;
        }
        //banner
        initialParams.setParamBanner = function (data) {
            //debugger;
            initialParams.paramBanner = data;
        }
        initialParams.getParamBanner = function () {
            return initialParams.paramBanner;
        }
        //timeOut
        initialParams.setParamTimeOut = function (data) {
            //debugger;
            initialParams.paramTimeOut = data;
        }
        initialParams.getParamTimeOut = function () {
            return initialParams.paramTimeOut;
        }
        //intentos fallidos
        initialParams.setParamFailed = function (data) {
            //debugger;
            initialParams.paramFailed = data;
        }
        initialParams.getParamFailed = function () {
            return initialParams.paramFailed;
        }
        return initialParams;


    })

    /*acceso a recursos, devuelve los siguientes datos:
     * confirmacion de acceso o negación de acceso
     * permisos del perfil 
     * data para acceso al recurso
     */
    .factory('resourceAccessService', function ($http, configurationServer) {

        var dataAccess = {};
        var GeneralOpc = {};
        var resource = {};
        var rutaConfig = configurationServer.obtenerRutaConfig();

        dataAccess.resetPassword = function (login) {
            //console.log("correoe_verif: " + correoe_verif);
            return $http({
                method: 'GET',
                url: rutaConfig + "/usuarios/resetPassword",
                params: {
                    'login': login
                },
                headers: {
                    'Content-Type': 'application/json'
                }
            });
        }

        dataAccess.validarAcceso = function (accesstoken, scope, uri) {
            return $http({
                method: 'GET',
                url: rutaConfig + uri,
                params: {
                    'accesstoken': accesstoken,
                    'scope': scope,
                    'uri': uri
                },
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }
        
        dataAccess.validarAccesoa = function (accesstoken, scope, uri) {
            return $http({
                method: 'GET',
                url: 'http://localhost:8083/aurora/v1/inventario/cargado',
                params: {
                    'accesstoken': accesstoken,
                    'scope': scope,
                    'uri': uri
                },
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }
        
         dataAccess.validarAccesoo = function (accesstoken, scope, uri) {
            console.log(rutaConfig);
             console.log('como alma que lleva el diablo')
            return $http({
                method: 'GET',
                url: 'http://localhost:8083/aurora/v1/referencias/consultar',
                params: {
                    'accesstoken': accesstoken,
                    'scope': scope,
                    'uri': uri
                },
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }

        /*
        Option para dejar acceder a lista de usuarios
        cuando no se tiene perfiles hijos
        */
        dataAccess.validaPerfilesHijos = function (scope, accesstoken, uri) {
            url = rutaConfig + uri

            return $http({
                method: 'POST',
                url: url,
                params: {
                    'accesstoken': accesstoken,
                    'scope': scope,
                    'uri': uri
                },
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }

        /* Submit de los formularios se envian los siguientes parametros:
         * token
         * ip
         * scope
         * resource
         * data form
         */
        dataAccess.submitServer = function (scope, accesstoken, resource, uri, data) {

            // ruta : /parameters
            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri
            };
            return $http({
                method: 'POST',
                url: rutaConfig + resource,
                data: data,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }
        
        dataAccess.submitServerCarrito = function (scope, accesstoken, login, uri, data) {
            console.log('funcion')
            // ruta : /parameters
            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri,
                'login':login
            };
            return $http({
                method: 'POST',
                url: rutaConfig + uri,
                data: data,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }
        
        dataAccess.submitServerCotizacion = function (scope, accesstoken, login, uri, data, id_cotizacion) {
            console.log('funcion')
            console.log(id_cotizacion)
            // ruta : /parameters
            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri,
                'login':login,
                'id_cotizacion':id_cotizacion,
            };
            return $http({
                method: 'POST',
                url: rutaConfig + uri,
                data: data,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }
        

        // GET peticion con parametro en url -id 
        dataAccess.submitSearch = function (scope, accesstoken, resource, uri, data, flag) {
            console.log("flag" + flag)
            if (flag != undefined) {
                var parametros = {
                    'scope': scope,
                    'accesstoken': accesstoken,
                    'uri': uri,
                    'filtroTipoOperacion': flag
                };
            } else {
                var parametros = {
                    'scope': scope,
                    'accesstoken': accesstoken,
                    'uri': uri
                };
            }

            // var parametros ={'scope': scope, 'accesstoken': accesstoken, 'uri':uri};
            return $http({
                method: 'GET',
                url: rutaConfig + resource + "/" + data,
                //url    : rutaConfig + resource,                    
                data: {},
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }

        // GET petición con parametros
        dataAccess.nameValidate = function (scope, accessToken, uri, data, id) {
            //console.log("uri" +  uri + "id"+ id + "accesstoken" + accessToken)
            if (id == null) {
                var parametros = {
                    scope: scope,
                    accesstoken: accessToken,
                    uri: uri,
                    nombre: data
                }

            } else {
                var parametros = {
                    scope: scope,
                    accesstoken: accessToken,
                    uri: uri,
                    nombre: data,
                    id: id
                }
            }
            return $http({
                method: 'GET',
                url: rutaConfig + uri,
                //url    : rutaConfig + '/login ', 
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }
        
        
        // GET petición con parametros
        dataAccess.consultarInventario = function (scope, accessToken, uri, referencia) {
            //console.log("uri" +  uri + "id"+ id + "accesstoken" + accessToken)
                var parametros = {
                    scope: scope,
                    accesstoken: accessToken,
                    uri: uri,
                    referencia: referencia
                }
            return $http({
                method: 'GET',
                url: uri,
                //url    : rutaConfig + '/login ', 
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }
        
        dataAccess.generaCotizacion = function (scope, accessToken, uri, data, id, nota) {
            //console.log("uri" +  uri + "id"+ id + "accesstoken" + accessToken)
            if (id == null) {
                var parametros = {
                    scope: scope,
                    accesstoken: accessToken,
                    uri: uri,
                    nombre: data,
                    nota:nota
                }

            } else {
                var parametros = {
                    scope: scope,
                    accesstoken: accessToken,
                    uri: uri,
                    nombre: data,
                    id: id,
                    nota:nota
                }
            }
            return $http({
                method: 'GET',
                url: rutaConfig + uri,
                //url    : rutaConfig + '/login ', 
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }
        // GET petición con parametros
        dataAccess.cotizacionCreada = function (scope, accessToken, uri, id_cotizacion, id_usuario, id_cliente) {
                var parametros = {
                    scope: scope,
                    accesstoken: accessToken,
                    uri: uri,
                    id_cotizacion: id_cotizacion,
                    id_usuario:id_usuario,
                    id_cliente:id_cliente
                }
            return $http({
                method: 'GET',
                url: rutaConfig + uri + "/" + id_cotizacion,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }
        // GET petición con parametros
        dataAccess.actualizarEstado = function (scope, accessToken, uri, id_cotizacion, id_usuario, id_cliente, id_compra, estado) {
                var parametros = {
                    scope: scope,
                    accesstoken: accessToken,
                    uri: uri,
                    id_cotizacion: id_cotizacion,
                    id_usuario:id_usuario,
                    id_cliente:id_cliente,
                    id_compra:id_compra,
                    estado:estado
                }
            return $http({
                method: 'PUT',
                url: rutaConfig + uri + "/" + id_cotizacion,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }
        
         // GET petición con parametros
        dataAccess.buscarReferenciaDesdeCotizacion = function (scope, accessToken, uri, referencia, id_cotizacion, id_usuario, id_cliente) {
                var parametros = {
                    scope: scope,
                    accesstoken: accessToken,
                    uri: uri,
                    id_cotizacion: id_cotizacion,
                    id_usuario:id_usuario,
                    id_cliente:id_cliente,
                    referencia:referencia
                }
            return $http({
                method: 'GET',
                url: rutaConfig + uri,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }
        
         // GET petición con parametros
        dataAccess.cambiarCantidadCotizacion = function (scope, accessToken, uri, id_cotizacion, id_usuario, id_cliente, id_referencia, referencia,locacion,proveedor, cantidadEscrita) {
            console.log("cantidad escrita")
            console.log(cantidadEscrita);
                var parametros = {
                    scope: scope,
                    accesstoken: accessToken,
                    uri: uri,
                    id_cotizacion: id_cotizacion,
                    id_usuario:id_usuario,
                    id_cliente:id_cliente,
                    id_referencia: id_referencia,
                    referencia: referencia,
                    locacion:locacion,
                    proveedor:proveedor,
                    cantidadEscrita:cantidadEscrita
                }
            return $http({
                method: 'GET',
                url: rutaConfig + uri + "/" + id_cotizacion,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }
        dataAccess.listaCotizaciones = function (scope, accessToken, uri, id_usuario, id_cliente) {
                var parametros = {
                    scope: scope,
                    accesstoken: accessToken,
                    uri: uri,
                    id_usuario:id_usuario,
                    id_cliente:id_cliente
                }
            return $http({
                method: 'GET',
                url: rutaConfig + uri,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }


        //DELETE con parametro id

        dataAccess.deleteForm = function (scope, accesstoken, resource, uri, data) {
            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri
            };
            return $http({
                method: 'DELETE',
                url: rutaConfig + resource + "/" + data,
                data: {},
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }
        //DELETE con parametro id e informacion

        dataAccess.deleteForm2 = function (scope, accesstoken, resource, uri, data, informacion) {
            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri
            };
            return $http({
                method: 'DELETE',
                url: rutaConfig + resource + "/" + data,
                data: informacion,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }

            });

        }


        //SUBMIT MODIFICAR PUT 

        dataAccess.submitMod = function (scope, accesstoken, resource, uri, data, id) {

            // ruta : /parameters
            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri
            };
            if (id == null) {
                url = rutaConfig + resource
            } else {
                url = rutaConfig + resource + "/" + id
            }
            return $http({
                method: 'PUT',
                url: url,
                data: data,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }

        //generar reportes
        dataAccess.generateReport = function (scope, accesstoken, uri, data, tipo) {
            // ruta : /parameters
            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri,
                'listaIn': data
            };

            return $http({
                method: 'GET',
                //data:  data,
                url: rutaConfig + uri + "/" + tipo,
                params: parametros,
                responseType: 'arraybuffer',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }
        
        //generar reportes
        dataAccess.generateReportPDF = function (scope, accesstoken, uri) {
            // ruta : /parameters
            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri,
            };

            return $http({
                method: 'GET',
                //data:  data,
                url: rutaConfig + uri,
                params: parametros,
                responseType: 'arraybuffer',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }
        
        //generar reportes
        dataAccess.generateReportReferencias = function (scope, accesstoken, uri, data, tipo) {
            // ruta : /parameters
            console.log(data);
            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri,
                'listaIn': data
            };

            return $http({
                method: 'GET',
                //data:  data,
                url: rutaConfig + uri,
                params: parametros,
                responseType: 'arraybuffer',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }
        

        //Metodo Options para consultas varias

        dataAccess.dataSearch = function (scope, accesstoken, resource, uri, data, id) {

            // ruta : /parameters
            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri
            };
            //console.log("data_" + JSON.stringify(data));
            if (id == null) {
                url = rutaConfig + uri
            } else {
                url = rutaConfig + uri + "/" + id
            }
            return $http({
                method: 'OPTIONS',
                url: url,
                data: data,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }

        dataAccess.dataSearchPost = function (scope, accesstoken, resource, uri, data, id) {

            // ruta : /parameters
            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri
            };
            //console.log("data_" + JSON.stringify(data));
            if (id == null) {
                url = rutaConfig + uri
            } else {
                url = rutaConfig + uri + "/" + id
            }
            return $http({
                method: 'POST',
                url: url,
                data: data,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }
        //Servicio para cargar archivos
        dataAccess.fileUploadService = function (scope, accessToken, uri, file, url) {
            console.log('fileUpload')
            console.log(file.name);
            var data = new FormData();
            data.append('file', file);
            console.log(data.get('file'));
            parametros = {
                'scope': scope,
                'accesstoken': accessToken,
                'uri': uri,
            }
            console.log(parametros);
            return $http({
                method: 'POST',
                url : rutaConfig + url,
                params: parametros,
                headers: {
                    'Content-Type': undefined
                },
                data: data
                
            });
        }
        
        dataAccess.fileUploadServicea = function (scope, accessToken, uri, file, url) {
            console.log('fileUpload')
            console.log(file.name);
            var data = new FormData();
            data.append('file', file);
            console.log(data.get('file'));
            parametros = {
                'scope': scope,
                'accesstoken': accessToken,
                'uri': uri,
            }
            console.log(parametros);
            return $http({
                method: 'POST',
                url : 'http://engine.aurora.com:8083/aurora/v1/inventario/cargar',
                params: parametros,
                headers: {
                    'Content-Type': undefined
                },
                data: data
                
            });
        }
        
        dataAccess.fileUploadServiceReferencias = function (scope, accessToken, uri, file, url) {
            var fd = new FormData();
            fd.append('file', file);
            console.log(fd)
            parametros = {
                'scope': scope,
                'accesstoken': accessToken,
                'uri': uri,
            }
            return $http({
                method: 'POST',
                url : rutaConfig + url,
                params: parametros,
                headers: {
                    'Content-Type': undefined
                },
                data: fd
                
            });
        }



        // GET peticion con parametro inlcuido en data
        dataAccess.findData = function (scope, accesstoken, resource, uri, data) {

            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri,
                'accountNumber': data
            };

            return $http({
                method: 'GET',
                url: rutaConfig + resource,
                data: {},
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }

        //SUBMIT ADJUDICACION POST 

        dataAccess.submitAdj = function (scope, accesstoken, resource, uri, data, id) {

            // ruta : /parameters
            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri,
                'operacionAdjudicada': data
            };

            if (id == null) {
                url = rutaConfig + resource
            } else {
                url = rutaConfig + resource + "/" + id
            }
            return $http({
                method: 'POST',
                url: url,
                data: data,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }

        dataAccess.searchServer = function (scope, accesstoken, resource, uri, data) {

            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri
            };
            return $http({
                method: 'POST',
                url: rutaConfig + uri,
                data: data,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }


        dataAccess.dataSearch1 = function (scope, accesstoken, resource, uri, data, id) {


            var parametros = {
                'scope': scope,
                'accesstoken': accesstoken,
                'uri': uri
            };

            if (id == null) {
                url = rutaConfig + uri
            } else {
                url = rutaConfig + uri + "/" + id
            }
            return $http({
                method: 'POST',
                url: url,
                data: data,
                params: parametros,
                headers: {
                    'Content-Type': 'application/json'
                }
            });

        }
        //opciones y permisos de acuerdo al modulo seleccionado
        dataAccess.setGeneralOpc = function (data) {

            dataAccess.GeneralOpc = data;
        }
        dataAccess.getGeneralOpc = function () {
            return dataAccess.GeneralOpc;
        }

        //Resource del recurso que accede
        dataAccess.setResource = function (data) {

            dataAccess.resource = data;
        }
        dataAccess.getResource = function () {
            return dataAccess.resource;
        }
        //opciones de acuerdo al perfil seleccionado
        dataAccess.setSpecificOpc = function (data) {

            dataAccess.SpecificOpc = data;
        }
        dataAccess.getSpecificOpc = function () {
            return dataAccess.SpecificOpc;
        }



        return dataAccess;



    })
