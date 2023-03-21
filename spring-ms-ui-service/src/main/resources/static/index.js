(function () {
    angular
        .module('app', ['ngRoute', 'ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'welcome.html',
                controller: 'welcomeController'
            })
            .when('/products', {
                templateUrl: 'products.html',
                controller: 'productController'
            })
            .when('/cart', {
                templateUrl: 'cart.html',
                controller: 'cartController'
            })
            .when('/checkout', {
                templateUrl: 'checkout.html',
                controller: 'checkoutController'
            })
            .when('/orders', {
                templateUrl: 'orders.html',
                controller: 'orderController'
            })
            .when('/signup', {
                templateUrl: 'signup.html',
                controller: 'signupController'
            })
            .otherwise({
                redirectTo: '/'
            });
    }

    function run($rootScope, $http, $localStorage) {
        if ($localStorage.marchMarketUser) {
            console.log('user data existed');
            try {
                let jwt = $localStorage.marchMarketUser.token;
                let payload = JSON.parse(atob(jwt.split('.')[1]));
                let currentTime = parseInt(new Date().getTime() / 1000);

                $rootScope.username = $localStorage.marchMarketUser.username;
                for (let i = 0; i <= $localStorage.marchMarketUser.roles.length; i++) {
                    if ($localStorage.marchMarketUser.roles[i] === 'ROLE_ADMIN') {
                        $rootScope.specialAuthority = true;
                        break;
                    }
                }
                if (currentTime > payload.exp) {
                    console.log("Token is expired!!!");
                    delete $localStorage.marchMarketUser;
                    $http.defaults.headers.common.Authorization = '';
                }
            } catch (e) {
            }

            if ($localStorage.marchMarketUser) {
                $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marchMarketUser.token;
            }

            // $localStorage.marchMarketGuestCartId = null;
        }
        if (!$localStorage.marchMarketGuestCartId) {

            $http.get('/gateway-address')
                .then(function(response){
                    // console.log(response.data.address);
                    $http.get(response.data.address + '/cart/generate_uid')
                        .then(function (response) {
                            $localStorage.marchMarketGuestCartId = response.data.value;
                            // console.log(response.data.value);
                            console.log("AGAIN_root:" + $localStorage.marchMarketGuestCartId)
                        });
                });


        } else {
            console.log("STORE:" + $localStorage.marchMarketGuestCartId)
        }

    }


})();

angular.module('app').controller('indexController', function ($rootScope, $scope, $http, $location, $localStorage, $window) {

    let gatewayAddress;

    $scope.init = function () {
        $http.get('/gateway-address')
            .then(function(response){
                // console.log(response.data.address);
                if (response.data.address !== '') {
                    gatewayAddress = response.data.address;
                }
            });
        $scope.showLoginPanel = false;

    };



    $scope.init();


    $scope.tryToAuth = function () {
        let jwt = 'eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJMX3Z2OFB0Tl95U29lcjdvbkFKLV8xdkt4U3lmSTRxVWZfTC1WR0xNdERRIn0.eyJleHAiOjE2NzkzNjk3NTQsImlhdCI6MTY3OTMzMzc1NCwianRpIjoiYjIxODAwYmQtNTNiMy00NTc1LTlhYTYtNGFiODk2YWU3ODlhIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDEyL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOlsiZ2F0ZXdheSIsImFjY291bnQiXSwic3ViIjoiN2M1NTVkMWUtYTEyZS00NjFiLTk2OGUtMGQzY2Y4ZGUxOGY2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicG9zdG1hbiIsInNlc3Npb25fc3RhdGUiOiIxMTI0MzMxNS1mYTgyLTQ2ZjMtYWQ0OS0yY2Y1NjFkMTRjNTciLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLW1hc3RlciIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJnYXRld2F5Ijp7InJvbGVzIjpbInJvbGVfdXNlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiMTEyNDMzMTUtZmE4Mi00NmYzLWFkNDktMmNmNTYxZDE0YzU3IiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIiLCJnaXZlbl9uYW1lIjoiIiwiZmFtaWx5X25hbWUiOiIiLCJlbWFpbCI6InRlc3RAbWFpbC5ydSJ9.FkBxXw69zsKXoayJVtkzy3FEC6z7OdgG6UUukW3aDRkEkVMIJiUT9Of_AmycSmzQUsLnuVvI1cbhuTPvTG9-RGJ8Sc9LpbfBQfMyFzKRNH79R-_Z97RFXtfwVqKauIN1HbVTv0Fvsa5ZbsugGotB0qPiyty4bn5XCdVNDhxh9GSIgwFiPLcvIBD6t1hdMgffaWKE26MmU0jYauEkLCwDa4msCEQ_M7i7dcobwYUuq3WVupMzsb9Is5in-LUiw-6oNugsZAtUp7JeIagMIteEPhPtUMGUD7HViJdHY55AkoPSG91ctcUDscBbqTaGWSMGQAe8FhUz-8dd3QMMNjJbsw';
                    $localStorage.marchMarketUser = {
                        username: 'user',
                        token: jwt,
                        roles: 'role_user'
                    };
        // $http.post(gatewayAddress + '/user/auth', $scope.user)
        //     $http({
        //         // url: gatewayAddress + '/auth/realms/master/protocol/openid-connect/token',
        //         url: 'http://localhost:9012/realms/master/protocol/openid-connect/token',
        //         method: 'POST',
        //         // headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'},
        //         params: {
        //             grant_type: 'password',
        //             client_id: 'gateway',
        //             client_secret: 'Ml9YusPYU8d4MdtOZY64JlgU1GdyJq0e',
        //             username: $scope.user.username,
        //             password: $scope.user.password
        //         },
        //         paramSerializer: '$httpParamSerializerJQLike'
        //     })
        //     .then(function successCallback(response) {
        //         if (response.data.access_token) {
        //             console.log(response.data.access_token);
        //             $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
        //             let payload = JSON.parse(atob(response.data.token.split('.')[1]));
        //             // $scope.authorities = payload.authorities;
        //             $localStorage.marchMarketUser = {
        //                 username: $scope.user.username,
        //                 token: response.data.token,
        //                 roles: payload.authorities
        //             };
        //
        //             $rootScope.username = $localStorage.marchMarketUser.username;
        //             for (let i = 0; i <= $localStorage.marchMarketUser.roles.length; i++) {
        //                 if ($localStorage.marchMarketUser.roles[i] === 'ROLE_ADMIN') {
        //                     $rootScope.specialAuthority = true;
        //                     break;
        //                 }
        //             }
        //
        //             $scope.user.username = null;
        //             $scope.user.password = null;
        //
        //             // $location.path('/');
        //             $scope.showLoginPanel = false;
        //
        //             // $route.reload();
        //             $window.location.reload();
        //         }
        //     }, function errorCallback(response) {
        //         alert('Bad credentials');
        //     });
    };

    $scope.tryToLogout = function () {
        $scope.clearUser();
        // $route.reload();
        $window.location.reload();
        // $location.path('/');
    };

    $scope.clearUser = function () {
        delete $localStorage.marchMarketUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $rootScope.isUserLoggedIn = function () {
        if ($localStorage.marchMarketUser) {
            return true;
        } else {
            return false;
        }
    };

    $scope.wantToLogIn = function() {
        $scope.showLoginPanel = true;
        // $scope.$apply();
    }

    $scope.hideLoginPanel = function() {
        $scope.showLoginPanel = false;
    }

    // $scope.wantToRegister = function() {
    //     $scope.
    // }

});