/* Configuracion del acceso al servidor*/
angular.module('aurora.serverConfiguration', [])
    .factory('configurationServer', function () {

        var angularAPI = {};

        /* ruta servidor backend */
        angularAPI.obtenerRutaConfig = function () {
            //casa
            return 'http://181.51.214.75:10001/Aurora/odin/v1';

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
    