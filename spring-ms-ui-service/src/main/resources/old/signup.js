angular.module('app3', ['ngCookies'])
    .controller('signupController', function ($scope, $http, $cookies) {

        let gatewayAddress = null;

        $scope.init = function () {
            $http.get('/gateway-address')
                .then(function(response){
                    console.log(response.data.address);
                    if (response.data.address !== '') {
                        gatewayAddress = response.data.address;
                    }
                });
        };

        $scope.init();

        $scope.register = function () {
            $http({
                url: gatewayAddress + '/auth/register',
                method: 'POST',
                data: $scope.user
            }).then(function(response) {
                console.log(response.status);
                // $cookies.put("token", response.data.token);
            });
        };

    });



