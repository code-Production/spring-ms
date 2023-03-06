angular.module('app').controller('cartController', function ($scope, $http, $localStorage) {

    let gatewayAddress = null;

    let empty = true;

    $scope.init = function () {
        $http.get('/gateway-address')
            .then(function(response){
                if (response.data.address !== '') {
                    gatewayAddress = response.data.address;
                    console.log(gatewayAddress);
                    $scope.loadCart();
                    if (!$localStorage.marchMarketGuestCartId) {
                        // $scope.getGuestCartId();
                    }
                }

            });

    };

    // $scope.getGuestCartId = function() {
    //     $http.get(gatewayAddress + '/cart/generate_uid')
    //         .then(function (response) {
    //             $localStorage.marchMarketGuestCartId = response.data.value;
    //             // console.log(response.data.value);
    //             console.log("AGAIN:" + $localStorage.marchMarketGuestCartId)
    //         });
    // }

    $scope.init();

    $scope.loadCart = function() {
        $http({
            url: gatewayAddress + '/cart/' + $localStorage.marchMarketGuestCartId,
            method: 'GET'
        }).then(function(response) {
            $scope.CartList = response.data;
        });
    };

    $scope.addToCart = function(productId, num) {
        $http({
            url: gatewayAddress + '/cart/' + $localStorage.marchMarketGuestCartId,
            method: 'POST',
            params: {
                product_id: productId,
                amount: num
            }
        }).then(function(response) {
            $scope.CartList = response.data;
        });
    };

    $scope.deleteFromCart = function(productId, num) {
        $http({
            url: gatewayAddress + '/cart/' + $localStorage.marchMarketGuestCartId,
            method: 'DELETE',
            params: {
                product_id: productId,
                amount: num
            }
        }).then(function(response) {
            $scope.CartList = response.data;
        });
    };

    $scope.checkSizeCartPage = function () {
        console.log("CART:" + $scope.CartList.items.length);
        return $scope.CartList.items.length > 0;
        // return $scope.CartList.length > 0;
    }



    // $scope.clearCart = function() {
    //     $http({
    //         url: gatewayAddress + '/cart/clear',
    //         method: 'DELETE'
    //     }).then(function(response) {
    //         $scope.CartList = response.data;
    //     });
    // };

    // $scope.saveOrder = function() {
    //     $http.get(gatewayAddress + '/cart/checkout')
    //         .then(function(response) {
    //             $scope.loadCart();
    //             alert('Order #' + response.data + ' was created!');
    //         });
    // };

});