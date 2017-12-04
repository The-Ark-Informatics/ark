(function () {
    'use strict';

    angular
        .module('app')
        .factory('RelationshipService', Service);
	  var REST_SERVICE_URI = 'http://demo.sphinx.org.au/ark/rest/study/';
    //var REST_SERVICE_URI = 'http://localhost:8080/Spring4MVCAngularJSExample/study/';
	 var REST_SERVICE_URI_RESOURCE = '/relationship/';
    function Service($filter,$http,$q) {

        var service = {};
        var studyId=130;
      
        service.fetchAllRelationships = fetchAllRelationships;
		service.createRelationship = createRelationship;
		service.updateRelationship = updateRelationship;
        service.deleteRelationship = deleteRelationship;
		 service.fetchRelationshipByID = fetchRelationshipByID;

        return service;

       

	function fetchRelationshipByID(id) {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI+id,
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

   function fetchAllRelationships() {
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
                console.error('Error while fetching Users');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }



function createRelationship(relationship) {
        var deferred = $q.defer();
		relationship.studyId=130;
		if (relationship.id) {
        $http.put(REST_SERVICE_URI+relationship.id, relationship
			,
			 {
        withCredentials: true,
        headers:{ 'Authorization':  'Basic ' + btoa('demo-studyadmin@ark.org.au' + ":" + 'StudyAdmin@1')}}
		)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while creating relationship');
                deferred.reject(errResponse);
            }
        );
		}
		else{
	    $http.post(REST_SERVICE_URI+studyId+REST_SERVICE_URI_RESOURCE, relationship
			,
			 {
        withCredentials: true,
        headers:{ 'Authorization':  'Basic ' + btoa('demo-studyadmin@ark.org.au' + ":" + 'StudyAdmin@1')}}
		)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while creating relationship');
                deferred.reject(errResponse);
            }
        );
		
		}
        return deferred.promise;
    }




    function updateRelationship(relationship, id) {
        var deferred = $q.defer();
        $http.put(REST_SERVICE_URI+id, relationship,
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
   function deleteRelationship(id) {
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

        function getRelationships() {
            if (!localStorage.relationships) {
                localStorage.relationships = JSON.stringify([]);
            }

            return JSON.parse(localStorage.relationships);
        }

        function setRelationships(relationships) {
            localStorage.relationships = JSON.stringify(relationships);
        }
    }
})();