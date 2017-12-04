(function () {
    'use strict';

    angular
        .module('app').controller('Twintypes.AddEditController', Controller);

    function Controller($scope, $state, $stateParams, TwintypeService,SubjectService) {
        var vm = this;

		 $scope.twinTypes = ["MZ","DZ"];



        vm.title = 'Set Twin Relationship';
        vm.twintype = {}
        vm.subject = {};
        vm.saveTwintype = saveTwintype;
		$scope.twintype_error_show=false;
		$scope.twintype_error="";
		vm.changeRelativeList=changeRelativeList;
		vm.siblings=[];

        initController();

        function initController() {
            if ($stateParams.id) {
                vm.title = 'Edit Twintype';
                vm.twintype = TwintypeService.GetById($stateParams.id);
            }

            SubjectService.fetchAllSubjectsWithSiblings()
						            .then(
						            function(d) {
						                vm.subjects = d.subjectUids;
						            },
						            function(errResponse){
						                console.error('Error while fetching subjects');
						            }
        );
        }

		function changeRelativeList(subjectUID){
           vm.siblings=[];
		  SubjectService.fetchAllSiblingsBySubject(subjectUID)
						            .then(
						            function(d) {
						              
										vm.siblings=d.subjectUids;
										
						            },
						            function(errResponse){
						                console.error('Error while fetching siblings');
						            }
        );

		};

        function saveTwintype() {
            // save twintype
            TwintypeService.createTwintype(vm.twintype).then(
            function(d) {
                vm.twintype = d;
				$scope.twintype_error_show=false;
				$state.go('twintypes');
				 // redirect to subjects view
				 $scope.$emit('twintypes-updated');
				 // emit event so list controller can refresh
            },
            function(errResponse){
                console.error('Error while fetching twintypes');
				$scope.twintype_error=errResponse.data;
				$scope.twintype_error_show=true;
            }
        );

           
        }
    }

})();