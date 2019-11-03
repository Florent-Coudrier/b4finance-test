'use strict';


// Declare app level module which depends on filters, and services
angular.module('b4financeTestApp', [
    'ngRoute',
    'transactionControllers',
    'pascalprecht.translate',
    'base64'
]).service('sharedInfo', function () {
	     var infos = {

        password: ''

    };
    return infos;
}).config(['$translateProvider', function($translateProvider) {
        $translateProvider.useStaticFilesLoader({
            prefix: 'locales/',
            suffix: '.json'
        });
        //$translateProvider.determinePreferredLanguage();
        $translateProvider.use('fr');
    }]).factory('ConfService', function() {
    return {
        urlpost: '/b4finance-test/api/',
        urlget: '/b4finance-test/api/',
    };
}).config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
}]);
