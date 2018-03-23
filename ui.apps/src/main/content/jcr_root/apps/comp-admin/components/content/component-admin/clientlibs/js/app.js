/*
 * #%L
 * ACS AEM Commons Package
 * %%
 * Copyright (C) 2014 Adobe
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L% 
 */

/*global angular: false, ace: false qrCode: false*/

angular.module('acs-commons-component-admin-app', ['acsCoral', 'ACS.Commons.notifications']).controller('MainCtrl', ['$scope', '$http', '$timeout', 'NotificationsService', function ($scope, $http, $timeout, NotificationsService) {

    $scope.app = {
        uri: ''
    };
    
    var xtypes = ["textfield","pathfield","image"];

    $scope.form = {
        enabled: false
    };
    $scope.init = function(appUri) {};
    $scope.saveConfig = function () {
$('#step0').hide();
$('#step1').show();
$("#step0").css("display", "none");
$("#step1").css("display", "block");
    };
    
    $scope.saveConfig1 = function () {
$('#step0').hide();
$('#step1').hide();
$('#step2').show();
$("#step0").css("display", "none");
$("#step1").css("display", "none");
$("#step2").css("display", "block");
};
$scope.saveConfig2 = function () {
	$('#step0').hide();
	$('#step1').hide();
	$("#step0").css("display", "none");
	$("#step1").css("display", "none");
	
	console.log('/bin/validateHTMLStrcture.html');
	$http({
        method: 'POST',
        url: '/bin/validateHTMLStrcture.html'
    }).success(function (data,status) {
        console.log(data);
        $("#step2").css("display", "none");
        $("#step3").css("display", "block");
        $('#step3').show();
       // var jsonResponse = JSON.parse(data);
        

        for(var element in data) {
	        	if (data.hasOwnProperty(element)) {
	        		
	                var select = "<coral-select name='Select'>";
	                for(var element1 in xtypes){
		                	console.log("element :"+element1);
		                	if(xtypes[element1] == data[element]){
		                		select = select + "<coral-select-item  value='"+xtypes[element1]+"' selected>"+xtypes[element1]+"</coral-select-item>";
		                	} else {
		                		select = select + "<coral-select-item  value='"+xtypes[element1]+"'>"+xtypes[element1]+"</coral-select-item>";
		                	}
	                }
	                select = select + "</coral-select>";
	                
	        		$('table').append("<tr class='coral-Table-row'><td class='coral-Table-cell'>"+element+"</td><td class='coral-Table-cell'>"+data[element]+"</td><td class='coral-Table-cell'>" +select +
	        				"</td>" +
	        				"<td class='coral-Table-cell'><div class='coral-RadioGroup coral-RadioGroup--labelsBelow'><label1 class='coral-Radio' style='float:none;'><coral-radio name='r1'>Yes</coral-radio></label1><label1 class='coral-Radio' style='float:none;'><coral-radio name='r1'>No</coral-radio></label1></div></td>" +
	        				"</td><td class='coral-Table-cell'><input is='coral-textfield' placeholder='Enter Field Label' name='fieldLabel' value=''></td></tr>");
	        	}
        }
        
    }).error(function (data, status) {
        // Response code 404 will be when configs are not available
    	$("#step2").css("display", "block");
        if (status !== 404) {
            NotificationsService.add('error', "Error", "Something went wrong while validating the HTML pattern");
        }
    });
	
	};
	
	$scope.saveConfig3 = function () {
		
		$http({
	        method: 'POST',
	        url: '/bin/createComponent.html'
	    }).success(function (data,status) {
	       console.log('successful');
	       // var jsonResponse = JSON.parse(data);
	    }).error(function (data, status) {
	        // Response code 404 will be when configs are not available
	        if (status !== 404) {
	            NotificationsService.add('error', "Error", "Something went wrong while validating the HTML pattern");
	        }
	    });
		
		};
	
	
	}]);
