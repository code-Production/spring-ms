angular.module('app').controller('signupController', function ($scope, $http, $window) {

    let gatewayAddress;

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

    $scope.registerUser = function() {
        $http.post(gatewayAddress + '/user/register', $scope.newUser)
            .then(function(response) {
                if (response.status == 200) {
                    alert('Success!');
                    $window.location.href = "/";
                    $scope.newUser.email = null;
                    $scope.newUser.username = null;
                    $scope.newUser.password = null;
                }
            })
    }

});