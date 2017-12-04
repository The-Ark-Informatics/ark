(function () {
    'use strict';

    angular
        .module('app')
        .controller('Relationships.AddEditController', Controller);

    function Controller($scope, $state, $stateParams,RelationshipService,SubjectService) {
        var vm = this;


        $scope.relationshipTypes = ["Mother","Father"];
        vm.title = 'Set Relationship';
        vm.relationship = {};
        vm.subject = {};
        vm.saveRelationship = saveRelationship;
        $scope.relationship_error_show=false;
		$scope.relationship_error="";
        $scope.isRelDisabled = false;
        initController();

        function initController() {
           		if ($stateParams.id) {
		$scope.isDisabled = true;
                vm.title = 'Edit Relationships';
               	RelationshipService.fetchRelationshipByID($stateParams.id).then(
            function(d) {
                vm.relationship = d;
				},
            function(errResponse){
                console.error('Error while fetching Relationship');
				$scope.relationship_error=errResponse.data;
				$scope.relationship_error_show=true;
            }
        );
		
		 }
		 else{
		 $scope.isDisabled = false;
		
            }

			SubjectService.fetchAllSubjects()
			            .then(
			            function(d) {
			                vm.subjects = d;
			            },
			            function(errResponse){
			                console.error('Error while fetching Users');
			            }
        );
        }

        function saveRelationship() {
            // save product
       RelationshipService.createRelationship(vm.relationship).then(
            function(d) {
                vm.subjects = d;
				$scope.subject_error_show=false;
				$state.go('relationships');
				 // redirect to subjects view
				 $scope.$emit('relationships-updated');
				 // emit event so list controller can refresh
            },
            function(errResponse){
                console.error('Error while fetching relationships');
				$scope.relationship_error=errResponse.data;
				$scope.relationship_error_show=true;
            }
        );


   }
    }

})();