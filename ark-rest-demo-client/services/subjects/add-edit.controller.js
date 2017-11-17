(function () {
    'use strict';

    angular
        .module('app').controller('Subjects.AddEditController', Controller);

    function Controller($scope, $state, $stateParams, SubjectService,PedigreeService) {
        var vm = this;

		$scope.vitalstatuses = ["Alive","Deceased","Unknown"];
		$scope.gendertypes = ["Male","Female","Unknown"];
		$scope.status = ["Subject","Prospect","Withdrawn Subject","Archive","Inactive"];
        $scope.subject_error_show=false;
		$scope.subject_error="";
        $scope.isDisabled = false;
		$scope.consentStatuses = ["Consented","Not Consented","Ineligible Refused","Withdrawn","Pending"];


        vm.title = 'Add Subject';
        vm.subject = {};
        vm.saveSubject = saveSubject;

        initController();

        function initController() {
		
		if ($stateParams.id) {
		$scope.isDisabled = true;
                vm.title = 'Edit Subject';
               	SubjectService.fetchSubjectByID($stateParams.id).then(
            function(d) {
				var dt = new Date(d.dateOfBirth);
                vm.subject = d;
				vm.subject.dateOfBirth=dt;
				},
            function(errResponse){
                console.error('Error while fetching Users');
				$scope.subject_error=errResponse.data;
				$scope.subject_error_show=true;
            }
        );
		
		 }
		 else{
		 $scope.isDisabled = false;
		
            }
            //vm.subjects = SubjectService.fetchAllSubjects();
        }
            
        

        function saveSubject() {
            // save subject
            SubjectService.createSubject(vm.subject).then(
            function(d) {
                vm.subjects = d;
				$scope.subject_error_show=false;
				$state.go('subjects');
				 // redirect to subjects view
				 $scope.$emit('subjects-updated');
				 // emit event so list controller can refresh
            },
            function(errResponse){
                console.error('Error while fetching Users');
				$scope.subject_error=errResponse.data;
				$scope.subject_error_show=true;
            }
        );

           
            

          
           
        }
    }

})();