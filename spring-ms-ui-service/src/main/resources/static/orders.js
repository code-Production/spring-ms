angular.module('app').controller('orderController', function ($scope, $http) {

    let gatewayAddress = null;

    $scope.init = function () {
        $http.get('/gateway-address')
            .then(function(response){
                // console.log(response.data.address);
                if (response.data.address !== '') {
                    gatewayAddress = response.data.address;
                    $scope.loadOrders();
                }
            });
    };

    $scope.init();


    $scope.loadOrders = function() {
        $http({
            url: gatewayAddress + '/order/all',
            method: 'GET'
        }).then(function(response) {
            $scope.orders = response.data;
            for (let i = 0; i <= $scope.orders.length; i++) {
                $http.get(gatewayAddress + '/address/' + $scope.orders[i].addressId)
                    .then(function(response) {
                        $scope.orders[i].addressTitle = response.data.country + ', ' +
                            response.data.region + ', ' +
                            response.data.city + ', ' +
                            response.data.street + ', ' +
                            response.data.houseNumber + ', ' +
                            response.data.apartmentNumber;
                    });

                $http({
                    url: gatewayAddress + '/user/profile',
                    method: 'GET',
                    // params: {
                    //     username: 'log'
                    // }
                }).then(function(response) {
                    // console.log(response.data);
                    for (let j = 0; j <= response.data.userBillings.length; j++) {
                        // console.log(response.data.userBillings[i].id);
                        // console.log($scope.orders[i].billingId);
                        if (response.data.userBillings[j].id == $scope.orders[i].billingId) {
                            $scope.orders[i].billingTitle = response.data.userBillings[j].cardNumber;
                            // console.log($scope.orders[i].billingTitle);
                        }
                    }
                });
            }
            // $scope.loadProduct($scope.orders);
        })
    }

    // $scope.loadProduct = function(orders) {
    //     console.log(orders.length);
    //     for (let i = 0; i <= orders.length; i++) {
    //         for (let j = 0; j <= orders[i].orderItems.length; j++) {
    //             $http({
    //                 url: gatewayAddress + '/products/' + orders[i].orderItems[j].productId,
    //                 method: 'GET'
    //             }).then(function(response) {
    //                 orders[i].orderItems[j].productId = response.data.title;
    //             });
    //         }
    //     }
    // }

});