(function () {
    'use strict';

    angular
        .module('app')
        .factory('PedigreeService', Service);
	var REST_SERVICE_URI = 'http://demo.sphinx.org.au/ark/rest/study/';
  	//var REST_SERVICE_URI = 'http://localhost:8080/Spring4MVCAngularJSExample/study/';
	 var REST_SERVICE_URI_RESOURCE = '/pedigree/';
    function Service($filter,$http,$q) {

        var service = {};
        var studyId=130;
      
        service.fetchAllPedigrees = fetchAllPedigrees;
		service.fetchPedigreeByID = fetchPedigreeByID;
        service.deletePedigree = deletePedigree;

        return service;

       
   function fetchAllPedigrees() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI,
			 {
        withCredentials: true,
        headers:{ 'Authorization':  'Basic ' + btoa('demo-studyadmin@ark.org.au' + ":" + 'StudyAdmin@1')}})
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while fetching Users');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }
	
	
	
	function fetchPedigreeByID (id) {
        var deferred = $q.defer();
		 $http.get(REST_SERVICE_URI+studyId+REST_SERVICE_URI_RESOURCE+id,
			 {
        withCredentials: true,
        headers:{ 'Authorization':  'Basic ' + btoa('demo-studyadmin@ark.org.au' + ":" + 'StudyAdmin@1')}})
         .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while fetching Users');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }



    function deletePedigree(id) {
        var deferred = $q.defer();
        $http.delete(REST_SERVICE_URI+id)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while deleting User');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }
        // private functions

        function getPedigrees() {
            if (!localStorage.pedigrees) {
                localStorage.pedigrees = JSON.stringify([]);
            }

            return JSON.parse(localStorage.pedigrees);
        }

        function setPedigrees(pedigrees) {
            localStorage.pedigrees = JSON.stringify(pedigrees);
        }
    }
})();