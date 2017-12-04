(function () {
    'use strict';

    angular
        .module('app', ['ui.router', 'ngAnimate','ngMaterial','ngMessages','ui.bootstrap'])
        .config(config)
        .run(run);

    function config($stateProvider, $urlRouterProvider) {
        // default route
        $urlRouterProvider.otherwise("/");

        $stateProvider
            .state('overview', {
                url: '/',
                templateUrl: 'overview/main.html',
                controller: 'Overview.MainController',
                controllerAs: 'vm'
            })
            .state('subjects', {
                url: '/subjects',
                templateUrl: 'subjects/main.html',
                controller: 'Subjects.MainController',
                controllerAs: 'vm'
            })
          .state('subjects.add', {
                    url: '/add',
                    templateUrl: 'subjects/add-edit.html',
                    controller: 'Subjects.AddEditController',
                    controllerAs: 'vm'
                })
          .state('subjects.edit', {
                    url: '/edit/:id',
                    templateUrl: 'subjects/add-edit.html',
                    controller: 'Subjects.AddEditController',
                    controllerAs: 'vm'
                })
	.state('relationships', {
                url: '/relationships',
                templateUrl: 'relationships/main.html',
                controller: 'Relationships.MainController',
                controllerAs: 'vm'
            })
                .state('relationships.add', {
                    url: '/add',
                    templateUrl: 'relationships/add-edit.html',
                    controller: 'Relationships.AddEditController',
                    controllerAs: 'vm'
                })

    .state('twintypes', {
				                url: '/twintypes',
				                templateUrl: 'twintypes/main.html',
				                controller: 'Twintypes.MainController',
				                controllerAs: 'vm'
				            })
				     .state('twintypes.add', {
				                    url: '/add',
				                    templateUrl: 'twintypes/add-edit.html',
				                    controller: 'Twintypes.AddEditController',
				                    controllerAs: 'vm'
				                })

           .state('pedigrees', {
				                url: '/pedigree',
				                templateUrl: 'pedigrees/main.html',
				                controller: 'Pedigrees.MainController',
				                controllerAs: 'vm'
				            })
	      .state('configurations', {
				                url: '/configurations',
				                templateUrl: 'configurations/main.html',
				                controller: 'Configurations.MainController',
				                controllerAs: 'vm'
				            })
		                      .state('configurations.add', {
				                    url: '/add',
				                    templateUrl: 'configurations/add-edit.html',
				                    controller: 'Configurations.AddEditController',
				                    controllerAs: 'vm'
				                })
		  .state('memberships', {
				                url: '/memberships',
				                templateUrl: 'memberships/main.html',
				                controller: 'Memberships.MainController',
				                controllerAs: 'vm'
				            })

		  .state('visualisations', {
				                url: '/visualisations',
				                templateUrl: 'visualisations/main.html',
				                controller: 'Visualisations.MainController',
				                controllerAs: 'vm'
				            });
    }

    function run($rootScope, SubjectService) {
        // add some initial subjects
       // if (SubjectService.GetAll().length === 0) {
         //   SubjectService.Save({ firstname: 'Boardies', lastname: '25.00' });
           // SubjectService.Save({ firstname: 'Singlet', lastname: '9.50' });
            //SubjectService.Save({ firstname: 'Thongs (Flip Flops)', lastname: '12.95' });
        //}

        // track current state for active tab
        $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            $rootScope.currentState = toState.name;
        });
    }

})();