(function () {
    'use strict';

    angular
        .module('app')
        .factory('TwintypeService', Service);

   	// var REST_SERVICE_URI = 'http://localhost:8080/Spring4MVCAngularJSExample/study/';
	   var REST_SERVICE_URI = 'http://demo.sphinx.org.au/ark/rest/study/';
	 var REST_SERVICE_URI_RESOURCE = '/twintype/';
    function Service($filter,$http,$q) {

        var service = {};
        var studyId=130;
       
        service.fetchAllTwintypes = fetchAllTwintypes;
		service.createTwintype = createTwintype;
		service.updateTwintype = updateTwintype;
        service.deleteTwintype = deleteTwintype;

        return service;

       
   function fetchAllTwintypes() {
        var deferred = $q.defer();
         $http.get(REST_SERVICE_URI+studyId+REST_SERVICE_URI_RESOURCE,
			 {
        withCredentials: true,
        headers:{ 'Authorization':  'Basic ' + btoa('demo-studyadmin@ark.org.au' + ":" + 'StudyAdmin@1')}})
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while fetching twins');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }

    function createTwintype(twintype) {
        var deferred = $q.defer();
       	$http.post(REST_SERVICE_URI+studyId+REST_SERVICE_URI_RESOURCE, twintype,
			 {
        withCredentials: true,
        headers:{ 'Authorization':  'Basic ' + btoa('demo-studyadmin@ark.org.au' + ":" + 'StudyAdmin@1')}})
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while creating twins');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }


    function updateTwintype(twintype, id) {
        var deferred = $q.defer();
        $http.put(REST_SERVICE_URI+id, twintype,
			 {
        withCredentials: true,
        headers:{ 'Authorization':  'Basic ' + btoa('demo-studyadmin@ark.org.au' + ":" + 'StudyAdmin@1')}})
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while updating User');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }


	function deleteTwintype(id) {
        var deferred = $q.defer();
        $http.delete(REST_SERVICE_URI+studyId+REST_SERVICE_URI_RESOURCE+id,
			{
        withCredentials: true,
		responseType: 'text',
		transformResponse: function (data, headers) {
                    //MESS WITH THE DATA
                    data = {};
                    data.del = 'initialize';
                    return data;
                },
        headers:{ 'Authorization':  'Basic ' + btoa('demo-studyadmin@ark.org.au' + ":" + 'StudyAdmin@1')}})
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

        function getTwintypes() {
            if (!localStorage.twintypes) {
                localStorage.twintypes = JSON.stringify([]);
            }

            return JSON.parse(localStorage.twintypes);
        }

        function setTwintypes(twintypes) {
            localStorage.twintypes = JSON.stringify(twintypes);
        }
    }
})();