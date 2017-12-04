(function () {
    'use strict';

    angular
        .module('app')
        .controller('Relationships.MainController', Controller);

    function Controller($scope,$filter, RelationshipService) {
        var vm = this;

        vm.relationships = [];
        vm.deleteRelationships = deleteRelationships;
        vm.viewby = "10";
	    vm.totalItems = vm.relationships.length;
	    vm.currentPage = 4;
	    vm.itemsPerPage = 10;
	    vm.maxSize = 5; //Number of pager buttons to show
	    vm.setPage = setPage;
		vm.pageChanged = pageChanged;
		vm.setItemsPerPage = setItemsPerPage;
        initController();

        function initController() {
            loadRelationships();

            // reload products when updated
            $scope.$on('relationships-updated', loadRelationships );
        }

        function loadRelationships() {

			RelationshipService.fetchAllRelationships()
			            .then(
			            function(d) {
			                vm.relationships = d;
							vm.totalItems = vm.relationships.length;
			            },
			            function(errResponse){
			                console.error('Error while fetchAllRelationships');
			            }
        );
                 }

        function deleteRelationships(id) {

			 RelationshipService.deleteRelationship(id)
			            .then(
			            function(d) {
		    var unit = $filter('filter')(vm.relationships, {id: id})[0];
            unit.isRemoved = true;

			angular.forEach(vm.relationships, function(value, key) {
            if (value.id === id){
				relationship.isRemoved=true;
                vm.relationships.splice(key, 1);
            }
             });
		 //   var index = vm.relationships.indexOf(id);
          //  vm.relationships.splice(index, 1);
		  },
			            function(errResponse){
			                console.error('Error while fetching deleteRelationship');
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
    }

})();