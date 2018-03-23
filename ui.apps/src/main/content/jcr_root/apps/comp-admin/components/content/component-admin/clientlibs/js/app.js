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
	var processedHtml;

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
	function createRow($table) {
		var $row = document.createElement("tr");
		$row.classList.add("coral-Table-row", "js-suggestion-row");
		return $table.appendChild($row);
	}

	function createCell($row, value, addClass, $childEl) {
		console.log("Creating cell "+addClass + " with value "+value);
		var $cell = document.createElement("td");
		$cell.classList.add("coral-Table-cell", addClass);
		$cell.innerHTML = value;
		if ($childEl) {
			$cell.appendChild($childEl);
		}
		return $row.appendChild($cell);
	}

	function createSelectionDropdown(addClass, optionValues, defaultValue) {
		var $select = document.createElement("coral-select");
		$select.classList.add(addClass)
		$select.setAttribute("name", "Select");

		optionValues.forEach(element => {
			console.log("Creating option "+ element);
			var $newOptionEl = createOption(element, element == defaultValue);
			$select.appendChild($newOptionEl);
		});


		return $select;
	}

	function createOption(value, isDefault) {
		var $option = document.createElement("coral-select-item");
		$option.value = value;
		$option.setAttribute("name", value);
		$option.innerHTML = value;
		$option.innerText = value;
		if (isDefault) {
			$option.setAttribute("selected", "selected");
		}
		return $option;
	}

	function getFormData() {
		var formData = {}, dialogFields = [];
		var rows = document.querySelectorAll(".js-suggestion-row");
		rows.forEach($row => {
			var rowData = {};
			var $selected = $row.querySelector("coral-select-item[selected]");
			rowData["resourceType"] = $selected.value;

			var $isMandatory = $row.querySelector("input[type='radio']");
			rowData["isMandatory"] = $isMandatory.checked;

			var $fieldLabel = $row.querySelector("[name='fieldLabel']");
			rowData["fieldLabel"] = $fieldLabel.value;
			dialogFields.push(rowData);
		});

		formData.dialog = dialogFields;

		formData.compTitle = $("input[name=componentName]").val();
		formData.compDesc =  $("input[name=componentDesc]").val();
		formData.compGroup = $("input[name=componentGrp]").val();
		console.log("HTML"+processedHtml);
		formData.compHtml = processedHtml;
		return formData;
	}

$scope.saveConfig2 = function () {
	$('#step0').hide();
	$('#step1').hide();
	$("#step0").css("display", "none");
	$("#step1").css("display", "none");

	var jsonObject ={};
	jsonObject['componentHtml'] = $("textarea[name=componentHtml]").val();

	
	$http({
        method: 'POST',
		url: '/bin/validateHTMLStrcture.html',
		data: jsonObject
    }).success(function (data,status) {
        console.log(data);
        $("#step2").css("display", "none");
        $("#step3").css("display", "block");
        $('#step3').show();
	   // var jsonResponse = JSON.parse(data);
	   

	   var $table = document.querySelector('.js-dialog-create-table');
	   
	   var counter = 0;
	   var dialogFields = data.dialog;
	   processedHtml = data.processedHtml;
	   for (var element in dialogFields) {
		   var $row = createRow($table);
		   var $counterCell = createCell($row, element, "js-counter-" + counter);
		   
		   var $fieldTypeCell = createCell($row, dialogFields[element], "js-field-type" + counter);
		   
		   var $selectionDropdown = createSelectionDropdown("js-selection-dropdown" + counter, xtypes, dialogFields[element]);
		   var $selectionCell = createCell($row, "", "counter-" + counter, $selectionDropdown);

		   var radioHTML = "<div class='coral-RadioGroup coral-RadioGroup--labelsBelow'><label1 class='coral-Radio' style='float:none;'><coral-radio value='true' name='is-mandatory-" + counter +"'>Yes</coral-radio></label1><label1 class='coral-Radio' style='float:none;'><coral-radio value='false' name='is-mandatory-" + counter +"'>No</coral-radio></label1></div>";
		   var $radioCell = createCell($row, "", "js-radio-mandatory"+ counter);
		   $radioCell.innerHTML = radioHTML;

		   var textfieldHtml = "<input is='coral-textfield' placeholder='Enter Field Label' name='fieldLabel' value=''>";
		   var $textField = createCell($row, "", "js-field-name");
		   $textField.innerHTML = textfieldHtml;


		   var select = "<coral-select name='Select'>";
		   counter++;
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
		
		var formData = getFormData();
		console.log(formData);
		
			$http({
			method: 'POST',
			data : formData,
	        url: '/bin/createComponentStructure.html'
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
