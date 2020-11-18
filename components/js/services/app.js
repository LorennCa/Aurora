/*variables utilizadas para capturar los datos ingresados en la pantalla de parametrización para el tiempo de inactivadad del usuario
la información es enviada desde el fronted al Backend y este escribe el archivo app.js
Esto se hace porque el tiempo de inactividad debe ser cargado en el inicio de la aplicación, donde los parametros que solicitan al backend aón no 
han sido enviados por este
*/
//valores en segundos idle: tiempo de espera; 
var idle = 300;
var timeout = 60;
var interval = 60;

'use strict';

// Declare app level module which depends on views, and components
var aurora = angular.module('aurora', [
  'ngMaterial',
  'ngAnimate',
  'ngSanitize',
  'ngFileUpload',
  'fxpicklist',
  'ngMessages',
  'ngResource',
  'ngRoute',
  'ngIdle',
  'ui.router',
  'vcRecaptcha',
  'ui.tree',
  'md.data.table',
  'LocalStorageModule',
  'aurora.controller',
  'aurora.userProfileController',
  'aurora.usersController',
  'aurora.services',
  'aurora.serverConfiguration',
  'aurora.userProfileService',
  'aurora.inventarioController',
  'aurora.ecommerceController',
  'aurora.proveedoresController',
  'aurora.comprasController',
  'aurora.detallesController'
]);

aurora.run(function () {

    console.log('Aplicación iniciada');



});

aurora.run(function ($rootScope, localStorageService, $state) {
    $rootScope.$on('IdleTimeout', function () {
        // end their session and redirect to login
        $rootScope.closeSession();
    })
    // direccionar a login
    $rootScope.$on('$stateChangeStart', function (e, toState, toParams, fromState, fromParams) {
        var isLogin = toState.name === "principal";
        if (isLogin) {
            return; // no need to redirect 
        }

        var token = localStorageService.get("setDataToken");
        //console.log("token: " + token);

        if (token == null) {
            e.preventDefault(); // stop current execution
            $state.go('principal'); // go to login
        }
    });
});



aurora.config(['KeepaliveProvider', 'IdleProvider', 'localStorageServiceProvider', function (KeepaliveProvider, IdleProvider) {

    IdleProvider.idle(idle);
    IdleProvider.timeout(timeout);
    KeepaliveProvider.interval(interval);
}]);

/*Formatear fechas*/
aurora.config(['$mdDateLocaleProvider', function ($mdDateLocaleProvider) {
    $mdDateLocaleProvider.formatDate = function (date) {
        return date ? moment(date).format('DD/MM/YYYY') : '';
    };
    $mdDateLocaleProvider.parseDate = function (dateString) {
        var m = moment(dateString, 'DD/MM/YYYY', true);
        return m.isValid() ? m.toDate() : new Date(NaN);
    };
}]);

/*****************************
 *Definicion de constantes
 ******************************/
aurora.constant('globalConstant', {
    // Variables para los Mensajes de los formularios    
    'requiredMessage': 'Campo requerido',
    'chInvalidMessage': 'Carácteres Inválidos',
    'InvalidStructurMsg': 'La Ip no es Válida',
    //Variables para los mensajes del formulario de contraseñas
    'invalidLenght': 'la contraseña debe contener mínimo 8 caracteres o máximo 15',
    'passEqConsMsg': 'La contraseña no debe contener más de 3 caracteres idénticos consecutivos',
    'passConseMsg': 'La contraseña no debe contener caracteres consecutivos',
    'passMayusMsg': 'La contraseña debe tener al menos una mayúscula',
    'passCharaMsg': 'La contraseña debe tener un al menos carácter especial',
    'passNumbMsg': 'La contraseña debe tener al menos un número',
    'confirmPass': 'Las contraseñas ingresadas no coinciden',
    'LastPassMsg': 'La contraseña coincide con las últimas contraseñas anteriores',
    'invWordMsg': 'La contraseña contiene una palabra no permitida',
    'invPassMsg': 'La contraseña es incorrecta',
    'invDvMsg': 'Dígito de verificación no corresponde al NIT',
    'invEmail': 'La estructura del correo electrónico es inválida',
    'invSpaceField': 'Campo no debe contener espacios en blanco',
    'invFechaFin': 'Fecha fin menor que fecha inicio',
    'invHourFin': 'Hora fin menor que hora inicio',
    'invImgFormat': 'Por favor seleccione un archivo de tipo JPG, JPEG o PNG',
    'UploadLogOk': 'El archivo ha sido cargado satisfactoriamente',
    'invTasaMsg': 'La estructura del campo es inválida, máx. 3 enteros, máx. 3 decimales',
    'invPartMsg': 'Porcentaje total de participación debe ser 100%',
    'invComisionMsg': 'El valor supera el porcentaje máximo',
    'invFormatHour': 'La estructura del campo es inválida, hh:mm am/pm',
    'invFormatDate': 'La estructura del campo es inválida, dd/mm/aaaa',
    'invPrice': 'La estructura del campo es inválida, debe ser hasta 11 enteros y 2 decimales',
    'msjMax': 'Solo son permitidos 3 mensajes por repote',
    'invDateRange': 'Fechas fuera del rango de la operación',
    'invCtaInver': 'Cuenta Inversionista Inválida',
    'invDepositante': 'Depositante Directo Inválido',
    'invCtaSebra': 'Cuenta Sebra Inválida',
    'invFechaCumpl': 'Fecha de cumplimiento menor a la fecha fin de la operación',

    //patterns
    'patternAlfanumerico': /^[a-zA-Z0-9]*$/,
    'patternAlfanumericoEsp': /^[a-zA-ZñÑáéíóúÁÉÍÓÚ0-9\s]*$/,
    'patternNumerico': /^[0-9]*$/,
    'patternEmail': /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,3}$/,
    'patternIp': /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/
});



aurora.config(function ($routeProvider, $locationProvider, $stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/login')

    $stateProvider
        // Inicio

        .state('/', {
            url: '/',
            controller: 'mainController'

        })

        // //route fot the home page
        .state('home', {
            url: '/home',
            views: {
                'modulosLogin': {
                    templateUrl: 'templates/pages/home.html',
                    controller: 'loginController'
                }
            }
        })

        // route for the login page
        .state('login', {
            url: '/login',
            views: {
                'modulosLogin': {
                    templateUrl: 'templates/pages/login.html',
                    controller: 'loginController'
                }
            }
        })
    // route for the login page
        .state('principal', {
            url: '/principal',
            views: {
                'modulosInicio': {
                    templateUrl: 'templates/pages/procesosClientes/modulosInicio.html',
                    controller: 'ecommerceController'
                }
            }
        })
        // route for the menu page
        .state('menu', {
            url: '/menu',
            views: {
                'modulosLogin': {
                    templateUrl: 'menu.html',
                    controller: 'menuController'
                }
            }

        })
        // route para gestion de perfiles page
        .state('menu.perfiles', {
            url: '/perfiles',
            views: {
                'modulosMenu': {
                    templateUrl: 'templates/pages/userProfilePages/profiles.html',
                    controller: 'userProfileController'
                }
            }

        })
        //route para gestion de perfiles nuevo 
        .state('menu.perfiles.new', {
            url: '/new',
            views: {
                'modulosPerfil': {
                    templateUrl: 'templates/pages/userProfilePages/profilesNewForm.html',
                    controller: 'userProfileController'
                }
            }

        })
        //route para gestion de perfiles consulta 
        .state('menu.perfiles.detail', {
            url: '/detail',
            views: {
                'modulosPerfil': {
                    templateUrl: 'templates/pages/userProfilePages/profilesModForm.html',
                    controller: 'userProfileController'
                }
            }

        })

        // route para gestion de Usuarios page
        .state('menu.usuarios', {
            url: '/usuarios',
            views: {
                'modulosMenu': {
                    templateUrl: 'templates/pages/userProfilePages/users.html',
                    controller: 'usersController'
                }
            }

        })
    .state('menu.productos', {
            url: '/productos',
            views: {
                'modulosMenu': {
                    templateUrl: 'templates/pages/procesosAurora/inventario.html',
                    controller: 'inventarioController'
                }
            }

        })
    .state('menu.accesorios', {
            url: '/accesorios',
            views: {
                'modulosMenu': {
                    templateUrl: 'templates/pages/procesosClientes/buscarProducto.html',
                    controller: 'ecommerceController'
                }
            }

        })
        .state('menu.detalles', {
            url: '/detalles',
            views: {
                'modulosMenu': {
                    templateUrl: 'templates/pages/procesosClientes/detallesProducto.html',
                    controller: 'detallesController'
                }
            }

        })
    .state('menu.carrito', {
            url: '/carrito',
            views: {
                'modulosMenu': {
                    templateUrl: 'templates/pages/procesosClientes/consultarCarrito.html',
                    controller: 'ecommerceController'
                }
            }

        })
        .state('menu.proveedores', {
            url: '/proveedores',
            views: {
                'modulosMenu': {
                    templateUrl: 'templates/pages/procesosAurora/proveedores.html',
                    controller: 'proveedoresController'
                }
            }

        })
        .state('menu.compras', {
            url: '/confirmar-compra',
            views: {
                'modulosMenu': {
                    templateUrl: 'templates/pages/procesosClientes/pagarProducto.html',
                    controller: 'ecommerceController'
                }
            }

        })
        .state('menu.pedidos', {
            url: '/pedidos',
            views: {
                'modulosMenu': {
                    templateUrl: 'templates/pages/procesosClientes/compraConfirmado.html',
                    controller: 'ecommerceController'
                }
            }

        })



    $urlRouterProvider.otherwise("/principal");


});
