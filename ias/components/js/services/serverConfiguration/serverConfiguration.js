/* Configuracion del acceso al servidor*/
angular.module('ias.serverConfiguration', [])
    .factory('configurationServer', function () {

        var angularAPI = {};

        /* ruta servidor backend */
        angularAPI.obtenerRutaConfig = function () {
            //casa
            return 'http://192.168.0.7:10001/IAS/usuarios/v1';

        }


       
        angularAPI.obtenerAmbiente = function () {
            //ruta servidor local
            return 'dev';

        }

        /*Configurar llave de acceso para google Recaptha se deb tener una llave por cada dominio */
        angularAPI.obtenerKeyCaptcha = function () {
            //Desarrollo
            return '6LcicR4UAAAAAN-EF7LLCLhXt9ZHS0Ue6XVF4NXW';
            
        }
        return angularAPI;
    });
    