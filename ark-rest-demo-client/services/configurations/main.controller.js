(function () {
    'use strict';

    angular
        .module('app')
        .controller('Configurations.MainController', Controller);

    function Controller($scope, ConfigurationService) {
        var vm = this;

        vm.configuration = [];
        
        
        
    }

})();