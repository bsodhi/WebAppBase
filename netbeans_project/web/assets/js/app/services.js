//-----------------------------------------------------------------------------
// Contains the code for any common services that we will create. Mainly it
// will contain shared/reusable directives and functions etc.
//-----------------------------------------------------------------------------
'use strict';

/* Declare app level module and its dependencies on other services if any. */
var sharedMod = angular.module('myApp.SharedServices', []);
/* An HTTP interceptor is created as a service */
sharedMod.config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push(['$q', '$rootScope', function ($q, $rootScope) {
                return {
                    // optional method
                    'request': function (config) {
                        $('#ajax-waiting').addClass('animate-text');
                        $('#ajax-loader').show();
                        return config;
                    },
                    // optional method
                    'requestError': function (rejection) {
                        // do something on error
                        $('#ajax-waiting').removeClass('animate-text');
                        $('#ajax-loader').hide();
                        return $q.reject(rejection);
                    },
                    // optional method
                    'response': function (response) {
                        // do something on success
                        $('#ajax-waiting').removeClass('animate-text');
                        $('#ajax-loader').hide();
                        $rootScope.$broadcast("ClearMessages", []);
                        return response;
                    },
                    // optional method
                    'responseError': function (rejection) {
                        // do something on error
                        $('#ajax-waiting').removeClass('animate-text');
                        $('#ajax-loader').hide();
                        C.log("Error occurred: >>>>>> " + JSON.stringify(rejection));
                        // Tell the main controller about errors
                        // msg.push({ Message: "Error occurred. Status code " +
                        // response.status });
                        // $rootScope.$broadcast("Errors", rejection);
                        return $q.reject(rejection);
                    }
                };
            }]);
    }]);
/* This service allows two controllers to share data. */
sharedMod.factory('Shared', [
    '$http',
    '$location',
    '$route',
    '$filter',
    function ($http, $location, $route, $filter) {
        var Shared = {
            User: {
                loggedIn: false
            }
        };
        Shared.loginTooltip = function (msg) {
            return !Shared.User.loggedIn ? msg : undefined;
        };

        Shared.hasRole = function (role) {
            C.log("Checking role: "+role);
            if (Shared.User.roles !== undefined) {
                return Shared.User.roles.some(function (arrVal) {
                        return role === arrVal.role;
                    });
            } else {
                return false;
            }
        };

        Shared.requireLogin = function () {
            $http.get('./App/isLoggedIn.a').success(
                    function (data, status, headers, config) {
                        if (data.Payload === true) {
                            Shared.User.loggedIn = true;
                            C.log("User is logged in. ");
                        } else {
                            $location.path("/Non-existent");
                            C.log("User is not logged in. ");
                        }
                    }).error(
                    function (data, status, headers, config) {
                        var msg = "Shared.requireLogin(): Error occurred: "
                                + JSON.stringify(data);
                        C.log(msg);
                    });
        };

        Shared.init = function () {
            C.log("Shared.init().");
            $http.get('./App/getSessionDetails.a').success(
                    function (data, status, headers, config) {
                        if (data.Payload !== undefined) {
                            Shared.User = data.Payload;
                            Shared.User.loggedIn = true;
                        }
                        $route.reload();
                        C.log("Shared.init(): Loaded User: "
                                + JSON.stringify(data.Payload));
                    }).error(
                    function (data, status, headers, config) {
                        var msg = "Shared.init(): Error occurred: "
                                + JSON.stringify(data);
                        C.log(msg);
                    });
        };
        return Shared;
    }]);

/**
 * Directive to convert input field text to upper case.
 */
sharedMod.directive('uppercase', function () {
    return {
        require: 'ngModel',
        link: function (scope, el, attr, ngModel) {
            var capitalize = function (inputVal) {
                if (inputVal === undefined)
                    return;
                var cap = inputVal.toUpperCase();
                if (inputVal !== cap) {
                    ngModel.$setViewValue(cap);
                    ngModel.$render();
                }
                return cap;
            };
            ngModel.$parsers.push(capitalize);
            capitalize(scope[attr.ngModel]);
        }
    };
});

sharedMod.directive('dateinput', ['$filter', function ($filter) {
        /*
         * Directive for formatting the dates. Any textfield capturing a date should
         * have the 'dateinput' attribute specified for formatting the date as
         * dd/MM/yyyy.
         */
        return {
            require: '^ngModel',
            restrict: 'A',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$formatters.push(function (modelValue) {
                    // Model -> View
                    if (!modelValue)
                        return "";
                    var retVal = $filter('date')(modelValue, 'dd/MM/yyyy');
                    // C.log("Formatted date:"+retVal);
                    return retVal;
                });
                ctrl.$parsers.push(function (modelValue) {
                    // View -> Model
                    // return data
                    var d = $filter('date')(modelValue, "dd/MM/yyyy");
                    // C.log("Parsed date:"+d);
                    return d;
                });
            }
        };
    }]);
sharedMod.directive('currencyInput', ['$filter', function ($filter) {
        // Directive to format the currency in text box
        return {
            require: '^ngModel',
            restrict: 'A',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$formatters.unshift(function (modelValue) {
                    if (!modelValue)
                        return "";
                    var retVal = $filter('currency')(modelValue);
                    return retVal;
                });
            }
        };
    }]);
sharedMod.directive("compareTo", function () {
    /* Credit: http://goo.gl/4TCtwW (odetocode.com blogs) */
    return {
        require: "ngModel",
        scope: {
            otherModelValue: "=compareTo"
        },
        link: function (scope, element, attributes, ngModel) {

            ngModel.$validators.compareTo = function (modelValue) {
                return modelValue === scope.otherModelValue;
            };

            scope.$watch("otherModelValue", function () {
                ngModel.$validate();
            });
        }
    };
});
