(function () {
    'use strict';

    angular
        .module('app')
        .controller('Memberships.MainController', Controller);

    function Controller($scope,$sce, MembershipService,SubjectService) {
        var vm = this;
        vm.title = 'Search Memership';
        vm.subject = {};
        vm.membership = {};
		vm.subjectUID= {};
        vm.findMemershipBySubject = findMemershipBySubject;
		vm.clear=clear;
         vm.viewby = "10";
	    vm.totalItems = vm.membership.length;
	    vm.currentPage = 4;
	    vm.itemsPerPage = 10;
	    vm.maxSize = 5; //Number of pager buttons to show
	    vm.setPage = setPage;
		vm.pageChanged = pageChanged;
		vm.setItemsPerPage = setItemsPerPage;
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

				 function findMemershipBySubject() {
					  vm.membership = {};

		             MembershipService.fetchMembershipBySubjectID(vm.subjectUID)
			            .then(
			            function(d) {
			                vm.membership = d;
						    vm.totalItems = vm.membership.length;


			            },
			            function(errResponse){
			                console.error('Error while fetching Users');
			            }
        );


                 }
				 function setPage (pageNo) {
		vm.currentPage = pageNo;
	   };

       function pageChanged() {
			console.log('Page changed to: ' + vm.currentPage);
	   };

	   
	  function  setItemsPerPage(num) {
		  vm.itemsPerPage = num;
		  vm.currentPage = 1; //reset to first page
	  };

        function clear() {
        vm.membership = {};
		vm.subjectUID= {};
        }
    }

})();