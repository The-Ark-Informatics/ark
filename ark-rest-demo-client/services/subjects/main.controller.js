(function () {
    'use strict';

    angular
        .module('app')
        .controller('Subjects.MainController', Controller);

    function Controller($scope, SubjectService) {
        var vm = this;

        vm.subjects = [];
        vm.deleteSubject = deleteSubject;
	    vm.viewby = "10";
	    vm.totalItems = vm.subjects.length;
	    vm.currentPage = 4;
	    vm.itemsPerPage = 10;
	    vm.maxSize = 5; //Number of pager buttons to show
	    vm.setPage = setPage;
		vm.pageChanged = pageChanged;
		vm.setItemsPerPage = setItemsPerPage;

	  


        initController();

        function initController() {
            loadSubjects();

            // reload subjects when updated
            $scope.$on('subjects-updated', loadSubjects);
        }

        function loadSubjects() {
           SubjectService.fetchAllSubjects()
            .then(
            function(d) {
                vm.subjects = d;
				vm.totalItems = vm.subjects.length;
            },
            function(errResponse){
                console.error('Error while fetching Users');
            }
        );

            //vm.subjects = SubjectService.fetchAllSubjects();
        }

        function deleteSubject(id) {
            SubjectService.Delete(id);
            loadSubjects();
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
    }

})();