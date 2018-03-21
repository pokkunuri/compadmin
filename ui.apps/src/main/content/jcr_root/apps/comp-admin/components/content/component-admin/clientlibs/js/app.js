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
}]);