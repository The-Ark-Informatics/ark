(function () {
    'use strict';

    angular
        .module('app')
        .factory('ConfigurationService', Service);
    //var REST_SERVICE_URI = 'http://localhost:8080/Spring4MVCAngularJSExample/configuration/
	var REST_SERVICE_URI = 'http://demo.sphinx.org.au/ark/rest/study/';
	//var REST_SERVICE_URI = 'http://localhost:8080/Spring4MVCAngularJSExample/study/';
	 var REST_SERVICE_URI_RESOURCE = '/configuration/';
	 var REST_SERVICE_URI_RESOURCE_CUSTOM_FIELDS='/binarycustomfields/'
    function Service($filter,$http,$q) {

        var service = {};

        var studyId=130;
        service.fetchConfigurationByStudy = fetchConfigurationByStudy;
		service.createConfiguration = createConfiguration;
		service.updateConfiguration = updateConfiguration;
		service.fetchAllEncodedCustomFields = fetchAllEncodedCustomFields;
        

        return service;

       
   function fetchConfigurationByStudy() {
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
     function fetchAllEncodedCustomFields() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI+studyId+REST_SERVICE_URI_RESOURCE_CUSTOM_FIELDS,
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
    function createConfiguration(configuration) {
        var deferred = $q.defer();
		configuration.studyId=130;
		if (configuration.id) {
        $http.put(REST_SERVICE_URI+studyId+REST_SERVICE_URI_RESOURCE, configuration,
			 {
        withCredentials: true,
        headers:{ 'Authorization':  'Basic ' + btoa('demo-studyadmin@ark.org.au' + ":" + 'StudyAdmin@1')}}
		)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while creating User');
                deferred.reject(errResponse);
            }
        );
		}
		else{
        $http.post(REST_SERVICE_URI+studyId+REST_SERVICE_URI_RESOURCE, configuration
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
                console.error('Error while creating User');
                deferred.reject(errResponse);
            }
        );
		
		}
        return deferred.promise;
    }



    function updateConfiguration(configuration, id) {
        var deferred = $q.defer();
        $http.put(REST_SERVICE_URI+id, configuration)
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

   

      
    }
})();