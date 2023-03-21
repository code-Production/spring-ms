angular.module('app2', ['ngCookies'])
    .controller('signinController', function ($scope, $http, $cookies) {

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

        $scope.auth = function () {
            $http({
                url: gatewayAddress + '/auth/auth',
                method: 'POST',
                data: $scope.user
            }).then(function(response) {
                console.log(response.status);
                $cookies.put("token", response.data.token);
            });
        };

    });



