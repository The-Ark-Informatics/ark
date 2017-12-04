(function () {
    'use strict';

    angular
        .module('app')
        .factory('SubjectService', Service);
    var REST_SERVICE_URI = 'http://demo.sphinx.org.au/ark/rest/study/';
	 //var REST_SERVICE_URI = 'http://localhost:8080/Spring4MVCAngularJSExample/study/';
	 var REST_SERVICE_URI_RESOURCE = '/subject/';
	  var REST_SERVICE_URI_RESOURCE_SIBLINGS = '/siblings/';
	
    function Service($filter,$http,$q) {

        var service = {};
        var studyId=130;
        service.fetchAllSubjects = fetchAllSubjects;
		service.fetchSubjectByID=fetchSubjectByID;
		service.createSubject = createSubject;
		service.updateSubject = updateSubject;
		service.fetchAllSiblingsBySubject=fetchAllSiblingsBySubject;
		service.fetchAllSubjectsWithSiblings=fetchAllSubjectsWithSiblings;
      

        return service;

        
		
	function fetchSubjectByID(id) {
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

    function fetchAllSubjectsWithSiblings() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI+studyId+"/commonparentsubjects",
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


	  
	  function fetchAllSubjects() {
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

	function fetchAllSiblingsBySubject(id) {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI+studyId+REST_SERVICE_URI_RESOURCE_SIBLINGS+id,
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

    function createSubject(subject) {
        var deferred = $q.defer();
		subject.studyId=130;
		if (subject.id) {
        $http.put(REST_SERVICE_URI+studyId+REST_SERVICE_URI_RESOURCE+subject.id, subject,
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
        $http.post(REST_SERVICE_URI+studyId+REST_SERVICE_URI_RESOURCE, subject
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

     
    function updateSubject(subject, id) {
        var deferred = $q.defer();
        $http.put(REST_SERVICE_URI+id, subject)
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


        // private functions

        function getSubjects() {
            if (!localStorage.subjects) {
                localStorage.subjects = JSON.stringify([]);
            }

            return JSON.parse(localStorage.subjects);
        }

        function setSubjects(subjects) {
            localStorage.subjects = JSON.stringify(subjects);
        }
    }
})();