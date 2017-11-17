(function () {
    'use strict';

    angular
        .module('app')
        .controller('Visualisations.MainController', Controller);

    function Controller($scope,$filter ,$sce, VisualisationService,SubjectService) {
        var vm = this;
        vm.title = 'Search Visualisations';
        vm.visual = {};
		vm.svg='';
		vm.pedRow={};
		vm.pedRow.familyId='FAM001';
		vm.peds=[];
		vm.checked=false;
		$scope.twinTypes = ["MZ Twin","DZ Twin","Unknown"];
		$scope.genderTypes = ["Male","Female","Unknown"];
		$scope.YesNoUnknown = ["Yes","No","Unknown"];
		$scope.YesNo = ["Yes","No"];
		$scope.AffectedTypes = ["Affected","Unaffected","Missing"];
		vm.addRow=addRow;
		vm.removeRow=removeRow;
		vm.removeRowID=removeRowID;
		vm.showRemove=false;
		vm.loadFile1=loadFile1;
		vm.loadFile2=loadFile2;
		vm.loadFile3=loadFile3;
		vm.clear=clear;
		vm.showError=false;
		vm.isDisabledProband=false;
		$scope.visualisation_error_show=false;
		$scope.visualisation_error="";
        vm.findByFile = findByFile;
		
		 $scope.pedTemplate1 =
        [
            { 'familyId':'FAM001','individualId': 'A0101', 'father': 'A0102','mother': 'A0103','gender': 'Male',  'zygosity': 'DZ Twin' ,'proband': 'Yes','deceased': '', 'affected': ''        ,'dOB': '1983-07-01' },
            { 'familyId':'FAM001','individualId': 'A0102', 'father': ''		,'mother': ''	  ,'gender': 'Male',  'zygosity': ''	   ,'proband': ''   ,'deceased': '', 'affected': 'Affected','dOB': '1960-10-01'},
            { 'familyId':'FAM001','individualId': 'A0103', 'father': ''		,'mother': ''	  ,'gender': 'Female','zygosity': ''	   ,'proband': ''   ,'deceased': '', 'affected': ''        ,'dOB': '1961-06-15' },
		    { 'familyId':'FAM001','individualId': 'A0104', 'father': 'A0102','mother': 'A0103','gender': 'Male',  'zygosity': 'DZ Twin' ,'proband': ''   ,'deceased': '', 'affected': ''        ,'dOB': '1983-07-01' },
			{ 'familyId':'FAM001', 'individualId': 'A0105', 'father': 'A0102','mother': 'A0103','gender': 'Female','zygosity': ''	   ,'proband': ''   ,'deceased': '', 'affected': ''        ,'dOB': '1985-08-09' }
        ];
		
		$scope.pedTemplate2=
        [
            { 'familyId':'FAM001','individualId': 'A0001', 'father': 'A0002','mother': 'A0003','gender': 'Male',  'zygosity': 'DZ Twin','proband': '',   'deceased': 'Yes','affected': '',        'dOB': '1983-07-01' },
            { 'familyId':'FAM001','individualId': 'A0002', 'father': 'A0007','mother': ''     ,'gender': 'Male',  'zygosity': ''      ,'proband': '',   'deceased': '',	  'affected': 'Affected','dOB': '1960-10-01' },
            { 'familyId':'FAM001', 'individualId': 'A0003', 'father': 'A0010','mother': ''     ,'gender': 'Female','zygosity': ''      ,'proband': '',   'deceased': '',   'affected': '',        'dOB': '1961-06-15' },
			{ 'familyId':'FAM001',  'individualId': 'A0007', 'father': ''     ,'mother': ''     ,'gender': 'Male',  'zygosity': ''      ,'proband': '',   'deceased': '',   'affected': '',        'dOB': '1930-07-01' },
            { 'familyId':'FAM001','individualId': 'A0010', 'father': ''     ,'mother': ''     ,'gender': 'Male',  'zygosity': ''      ,'proband': 'Yes','deceased': '',   'affected': '',        'dOB': '1935-11-22' },
            { 'familyId':'FAM001','individualId': 'A0004', 'father': 'A0002','mother': 'A0003','gender': 'Male',  'zygosity': 'DZ Twin','proband': '',   'deceased': '',   'affected': '',        'dOB': '1983-07-01' },
			{ 'familyId':'FAM001','individualId': 'A0005', 'father': 'A0002','mother': 'A0003','gender': 'Female','zygosity': ''      ,'proband': '',   'deceased': '',   'affected': '',        'dOB': '1985-08-09' },
            { 'familyId':'FAM001','individualId': 'A0006', 'father': 'A0002','mother': 'A0003','gender': 'Male',  'zygosity': 'DZ Twin'      ,'proband': '',   'deceased': '',   'affected': '',        'dOB': '1983-07-01' },
            { 'familyId':'FAM001','individualId': 'A0008', 'father': ''     ,'mother': 'A0005','gender': 'Female','zygosity': ''      ,'proband': '',   'deceased': '',   'affected': '',        'dOB': '2011-10-15' },
			{ 'familyId':'FAM001','individualId': 'A0011', 'father': ''     ,'mother': 'A0005','gender': 'Female','zygosity': '','proband': '',   'deceased': '',   'affected': 'Affected','dOB': '2013-11-14' }
           


        ];
		
		$scope.pedTemplate3=
        [
            { 'familyId':'FAM001','individualId': 'A0020', 'father': 'A0018','mother': 'A0019' ,'gender': 'Male',  'zygosity': ''	   ,'proband': 'Yes','deceased': '',   'affected': '',        'dOB': '1983-07-01' },
            { 'familyId':'FAM001','individualId': 'A0018', 'father': ''     ,'mother': 'A0017' ,'gender': 'Male',  'zygosity': ''      ,'proband': '',   'deceased': '',   'affected': '',        'dOB': '1960-10-01' },
            { 'familyId':'FAM001','individualId': 'A0019', 'father': 'A0014','mother': ''      ,'gender': 'Female','zygosity': ''      ,'proband': '',   'deceased': '',   'affected': '',        'dOB': '1961-06-15' },
			{ 'familyId':'FAM001','individualId': 'A0017', 'father': 'A0016','mother': 'A0015' ,'gender': 'Female','zygosity': ''      ,'proband': '',   'deceased': '',   'affected': '',        'dOB': '1930-07-01' },
            { 'familyId':'FAM001','individualId': 'A0014', 'father': 'A0016','mother': 'A0015' ,'gender': 'Male',  'zygosity': ''      ,'proband': '',   'deceased': '',   'affected': '',        'dOB': '1935-11-22' },
            { 'familyId':'FAM001','individualId': 'A0016', 'father': '',     'mother': ''      ,'gender': 'Male',  'zygosity': ''	   ,'proband': '',   'deceased': '',   'affected': '',        'dOB': '1983-07-01' },
			{ 'familyId':'FAM001','individualId': 'A0015', 'father': '',     'mother': ''      ,'gender': 'Female','zygosity': ''      ,'proband': '',   'deceased': '',   'affected': '',        'dOB': '1985-08-09' }
           


        ];
		
		
		

        

        initController();

        function initController() {
            // reload products when updated
           // $scope.$on('relationships-updated', loadSubjects );
        }
		
		function loadFile1() {
			
		   vm.peds.length=0;
           vm.peds=[];
		   vm.peds=angular.copy($scope.pedTemplate1);
		   vm.showRemove=true;
           vm.visual.svg='';
		  vm.isDisabledProband=true;
        }
		function loadFile2() {
		   vm.peds.length=0;
           vm.peds=[];
		   vm.peds=angular.copy($scope.pedTemplate2);
		   vm.visual.svg='';
		   vm.showRemove=true;
		    vm.isDisabledProband=true;
       //$scope.$digest();

        }
		function loadFile3() {
		   vm.peds.length=0;
           vm.peds=[];
		   vm.peds=angular.copy($scope.pedTemplate3);
		   vm.visual.svg='';
		   vm.showRemove=true;
		   vm.isDisabledProband=true;
        }

// GET VALUES FROM INPUT BOXES AND ADD A NEW ROW TO THE TABLE.
        function addRow () {
          //  if (vm.pedRow.indivUID != undefined && vm.pedRow.fathUID != undefined) {
		    if (vm.pedRow.individualId != null) {
				vm.showError=false;
                var pedRowa = {};
				 pedRowa.familyId = vm.pedRow.familyId;
                pedRowa.individualId = vm.pedRow.individualId;
                pedRowa.father = vm.pedRow.father;
				pedRowa.mother = vm.pedRow.mother;
				pedRowa.gender = vm.pedRow.gender;
				pedRowa.zygosity = vm.pedRow.zygosity;
				pedRowa.proband = vm.pedRow.proband;
				pedRowa.deceased = vm.pedRow.deceased;

				pedRowa.affected = vm.pedRow.affected;
				pedRowa.dOB = vm.pedRow.dOB;


                vm.peds.push(pedRowa);
				vm.showRemove=true;
                setProbrandFlag(); 
                // CLEAR TEXTBOX.
                vm.pedRow.individualId = null;
                vm.pedRow.father = null;
				vm.pedRow.mother = null;
				vm.pedRow.gender = null;
				vm.pedRow.zygosity = null;
				vm.pedRow.proband = null;
				vm.pedRow.deceased = null;
				vm.pedRow.affected = null;
				vm.pedRow.dOB = null;
           }
		   else{
			   vm.showError=true;
		   }
        };

		function removeRowID (id) {
            var unit = $filter('filter')(vm.peds, {indivUID: id})[0];
            unit.isRemoved = true;
			
			
        };
        // REMOVE SELECTED ROW(s) FROM TABLE.
        function removeRow () {
            var arrMovie = [];
            angular.forEach(vm.peds, function (value) {
                if (!value.Remove) {
                    arrMovie.push(value);
                }
                 else {
					value.isRemoved = true;
									 }
            });
			if (arrMovie.length==0){
				vm.showRemove=false;
			}
            vm.peds = arrMovie;
			setProbrandFlag();
        };
		
		function clear () {
           vm.peds.length=0;
           vm.peds=[];
		   vm.visual.svg='';
		   $scope.visualisation_error_show=false;
        };

      function setProbrandFlag() {
	    var loop=false;
	   angular.forEach(vm.peds, function (value) {
		 
                if (value.proband==="Yes") {
                    vm.isDisabledProband=true;
					loop=true;
		       }
                        
            });

			 if (!loop)
           {
         vm.isDisabledProband=false;
           }
	  
	  }

		function findByFile() {

			VisualisationService.fetchPedigreeByID(vm.peds)
			            .then(
			            function(d) {
				            $scope.visualisation_error_show=false;
			                vm.visual= d;
							vm.visual.svg = $sce.trustAsHtml(vm.visual.svg);

			            },
			            function(errResponse){
			    vm.visual.svg='';          
				console.error('Error while Visualisation');
				$scope.visualisation_error=errResponse.data.message;
				$scope.visualisation_error_show=true;
			            }
        );


                 }
       
    }

})();