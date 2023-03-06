angular.module('app').controller('productController', function ($scope, $http, $localStorage) {

        $scope.pageNumber = 0;
        let gatewayAddress = null;


        $scope.init = function () {
            $http.get('/gateway-address')
                .then(function(response){
                    // console.log(response.data.address);
                    if (response.data.address !== '') {
                        gatewayAddress = response.data.address;
                        $scope.filteredProducts();
                        if (!$localStorage.marchMarketGuestCartId) {
                            // $scope.getGuestCartId();
                        }
                    }
                });
        };

        $scope.init();

    // $scope.getGuestCartId = function() {
    //     $http.get(gatewayAddress + '/cart/generate_uid')
    //         .then(function (response) {
    //             $localStorage.marchMarketGuestCartId = response.data.value;
    //             console.log(response.data.value);
    //             console.log($localStorage.marchMarketGuestCartId)
    //         });
    // }

        $scope.filteredProducts = function () {
            $http({
                url: gatewayAddress + '/products',
                method: 'GET',
                params: {
                    min_price: $scope.filter ? $scope.filter.min_price : null,
                    max_price: $scope.filter ? $scope.filter.max_price : null,
                    page_num: $scope.pageNumber
                }
            }).then(function (response) {
                $scope.firstPage = response.data.first;
                $scope.lastPage = response.data.last;
                $scope.ProductList = response.data.content;
                if ($scope.ProductList.toString() === '') {
                    if ($scope.pageNumber > 0) {
                        $scope.prevPage();
                    }
                }
            });
        };

        $scope.clearFilter = function() {
            $scope.filter.min_price = null;
            $scope.filter.max_price = null;
            $scope.filteredProducts();
        }

        $scope.prevPage = function () {
            $scope.pageNumber--;
            $scope.filteredProducts();
        }

        $scope.nextPage = function () {
            $scope.pageNumber++;
            $scope.filteredProducts();
        }

        $scope.addProduct = function() {
            $http({
                url: gatewayAddress + '/products/',
                method: 'POST',
                data: $scope.newProduct
            }).then(function(response) {
                $scope.newProduct.title = null;
                $scope.newProduct.price = null;
                $scope.filteredProducts();
            })
        }

        $scope.deleteProduct = function (productId) {
            $http({
                url: gatewayAddress + '/products/' + productId,
                method: 'DELETE',
            }). then(function (response) {
                $scope.filteredProducts();
            });
        };

        // $scope.loadCart = function() {
        //     $http({
        //         url: gatewayAddress + '/cart',
        //         method: 'GET',
        //     }).then(function(response) {
        //         $scope.CartList = response.data;
        //     });
        // };
        //
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
        //
        // $scope.deleteFromCart = function(productId, num) {
        //     $http({
        //         url: gatewayAddress + '/cart',
        //         method: 'DELETE',
        //         params: {
        //             id: productId,
        //             amount: num
        //         }
        //     }).then(function(response) {
        //         $scope.CartList = response.data;
        //     });
        // };
        //
        // $scope.clearCart = function() {
        //     $http({
        //         url: gatewayAddress + '/cart/clear',
        //         method: 'DELETE'
        //     }).then(function(response) {
        //         $scope.CartList = response.data;
        //     });
        // };
        //
        // $scope.saveOrder = function() {
        //     $http.get(gatewayAddress + '/cart/checkout')
        //         .then(function(response) {
        //             $scope.loadCart();
        //             alert('Order #' + response.data + ' was created!');
        //         });
        // };


});


