(function () {
    'use strict';

    angular
        .module('app').controller('Configurations.AddEditController', Controller);

    function Controller($scope, $state, $stateParams, ConfigurationService) {
        var vm = this;

		$scope.vitalstatuses = ["Alive","Deceased","Unknown"];
		$scope.gendertypes = ["Male","Female","Unknown"];
		vm.customFields=[];
        $scope.configuration_error_show=false;
		$scope.configuration_error="";
        $scope.isDisabledInbreed = false;
		vm.isDisabledCustomFields=false;
		vm.isMandotaryCustomFields=false;
        vm.required="";

        vm.title = 'Set Configuration';
        vm.configuration = {};
        vm.saveConfiguration = saveConfiguration;
        vm.updateSelection = updateSelection;
		vm.customeFiledSelection=[];
        initController();
 function initController() {

ConfigurationService.fetchConfigurationByStudy()
						            .then(
						            function(d) {
						                vm.configuration = d;
										if (vm.configuration.inbreedAllowed)
										$scope.isDisabledInbreed=true;
										else
										$scope.isDisabledInbreed=false;	
										
									if (vm.configuration.statusAllowed)
										{
										vm.isDisabledCustomFields=false;
										vm.isMandotaryCustomFields=true;
										vm.required="required";
										}
										
										else{
										vm.isDisabledCustomFields=true;	
										vm.isMandotaryCustomFields=false;
										vm.required="";
										}
										
						            },
						            function(errResponse){
						                console.error('Error while fetching Configurations');
						            }
        );

		ConfigurationService.fetchAllEncodedCustomFields()
						            .then(
						            function(d) {
			                           vm.customeFiledSelection=d.customFieldNames;
			                           if(vm.isDisabledCustomFields)
										vm.customFields=[];
									   else
						               vm.customFields=vm.customeFiledSelection;								
						            },
						            function(errResponse){
						                console.error('Error while fetching Custom fields');
						            }
        );
        }

 function updateSelection ($event) {
  var checkbox = $event.target;
  if (checkbox.checked)
  {
	  vm.isDisabledCustomFields=false;
	  vm.isMandotaryCustomFields=true;
	  vm.customFields=vm.customeFiledSelection;	
	  vm.required="required";
  }
  else{
  vm.isDisabledCustomFields=true;
  vm.isMandotaryCustomFields=false;
  vm.customFields=[];
  vm.required="";
  }

};

function saveConfiguration() {
            // save subject
            ConfigurationService.createConfiguration(vm.configuration).then(
            function(d) {
                vm.configuration = d;
				$scope.configuration_error_show=false;
				$state.go('configurations');
				 // redirect to subjects view
				 $scope.$emit('configuration-updated');
				 // emit event so list controller can refresh
            },
            function(errResponse){
                console.error('Error while Configurations');
				$scope.configuration_error=errResponse.data;
				$scope.configuration_error_show=true;
            }
        );






}        }


})();