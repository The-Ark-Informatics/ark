(function () {
    'use strict';

    angular
        .module('app')
        .controller('Twintypes.MainController', Controller);

    function Controller($scope,$filter, TwintypeService) {
        var vm = this;

        vm.twintypes = [];
        vm.deleteTwintypes = deleteTwintypes;
  vm.viewby = "10";
	    vm.totalItems = vm.twintypes.length;
	    vm.currentPage = 4;
	    vm.itemsPerPage = 10;
	    vm.maxSize = 5; //Number of pager buttons to show
	    vm.setPage = setPage;
		vm.pageChanged = pageChanged;
		vm.setItemsPerPage = setItemsPerPage;
        initController();

        function initController() {
            loadTwintypes();

            // reload products when updated
            $scope.$on('twintypes-updated', loadTwintypes );
        }

        function loadTwintypes() {

			TwintypeService.fetchAllTwintypes()
			            .then(
			            function(d) {
			                vm.twintypes = d;
							vm.totalItems = vm.twintypes.length;
			            },
			            function(errResponse){
			                console.error('Error while fetching twintypes');
			            }
        );
                 }

        function deleteTwintypes(id) {
          
			  TwintypeService.deleteTwintype(id)
			            .then(
			            function(d) {
			var unit = $filter('filter')(vm.twintypes, {id: id})[0];
            unit.isRemoved = true;
			            angular.forEach(vm.twintypes, function(value, key) {
            if (value.id === id){
                vm.twintypes.splice(key, 1);
            }
             });
			            },
			            function(errResponse){
			                console.error('Error while fetching twintypes');
			            }
        );
          //  TwintypeService.deleteTwintype(id);
           // loadTwintypes();
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