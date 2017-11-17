(function () {
    'use strict';

    angular
        .module('app')
        .controller('Pedigrees.MainController', Controller);

    function Controller($scope,$sce, PedigreeService,SubjectService) {
        var vm = this;
        vm.title = 'Search Pedigree';
        vm.subject = {};
        vm.pedigree = {};
        vm.findByPedigree = findByPedigree;
        vm.clear=clear;
		vm.subjectUID= {};
        initController();

        function initController() {
            loadSubjects();

            // reload products when updated
            $scope.$on('relationships-updated', loadSubjects );
        }

        function loadSubjects() {

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
				 
				 function findByPedigree() {
                 vm.pedigree = {};
			PedigreeService.fetchPedigreeByID(vm.subjectUID)
			            .then(
			            function(d) {
			                vm.pedigree = d;
							vm.pedigree.svg = $sce.trustAsHtml(vm.pedigree.svg);

			            },
			            function(errResponse){
			                console.error('Error while fetching Users');
			            }
        );
		
		
                 }

        function clear() {
             vm.pedigree = {};
		    vm.subjectUID= {};
        }
    }

})();