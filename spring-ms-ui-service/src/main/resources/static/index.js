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
        $http.post(gatewayAddress + '/user/auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    let payload = JSON.parse(atob(response.data.token.split('.')[1]));
                    // $scope.authorities = payload.authorities;
                    $localStorage.marchMarketUser = {
                        username: $scope.user.username,
                        token: response.data.token,
                        roles: payload.authorities
                    };

                    $rootScope.username = $localStorage.marchMarketUser.username;
                    for (let i = 0; i <= $localStorage.marchMarketUser.roles.length; i++) {
                        if ($localStorage.marchMarketUser.roles[i] === 'ROLE_ADMIN') {
                            $rootScope.specialAuthority = true;
                            break;
                        }
                    }

                    $scope.user.username = null;
                    $scope.user.password = null;

                    // $location.path('/');
                    $scope.showLoginPanel = false;

                    // $route.reload();
                    $window.location.reload();
                }
            }, function errorCallback(response) {
                alert('Bad credentials');
            });
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