angular.module('app', ['ngCookies'])
    .controller('indexController', function ($scope, $http, $cookies) {

    $scope.pageNumber = 0;
    let gatewayAddress = null;



    $scope.init = function () {
        $http.get('/gateway-address')
            .then(function(response){
                console.log(response.data.address);
                if (response.data.address !== '') {
                    gatewayAddress = response.data.address;
                    $scope.filteredProducts();
                }
            });
    };

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

    $scope.init();


});










   //
   //
   //  $scope.deleteProduct = function (productId) {
   //      $http({
   //          url: contextPath + '/products/' + productId,
   //          method: 'delete',
   //          headers: {
   //              Content: 'application/json',
   //              Authorization: 'Bearer ' + $scope.token
   //          }
   //      }). then(function (response) {
   //          $scope.filteredProducts();
   //      });
   //  };
   //
   //
   // $scope.addProduct = function() {
   //     $http({
   //         url: contextPath + '/products',
   //         method: 'post',
   //         data: $scope.newProduct,
   //         headers: {
   //             // Content: 'application/json',
   //             Authorization: 'Bearer ' + $scope.token
   //         }
   //     }).then(function(response) {
   //         $scope.filteredProducts();
   //     })
   // }
   //

   //
   // $scope.loadCart = function() {
   //     $http({
   //         url: contextPath + '/cart',
   //         method: 'get',
   //         headers: {
   //             // Content: 'application/json',
   //             Authorization: 'Bearer ' + $scope.token
   //         }
   //     }).then(function(response) {
   //             $scope.CartList = response.data;
   //     });
   // };
   //
   // $scope.addToCart = function(productId, num) {
   //     $http({
   //         url: contextPath + '/cart',
   //         method: 'post',
   //         params: {
   //             id: productId,
   //             amount: num
   //         },
   //         headers: {
   //             // Content: 'application/json',
   //             Authorization: 'Bearer ' + $scope.token
   //         }
   //     }).then(function(response) {
   //         $scope.CartList = response.data;
   //     });
   // };
   //
   //
   // $scope.deleteFromCart = function(productId, num) {
   //     $http({
   //         url: contextPath + '/cart',
   //         method: 'delete',
   //         params: {
   //             id: productId,
   //             amount: num
   //         },
   //         headers: {
   //             // Content: 'application/json',
   //             Authorization: 'Bearer ' + $scope.token
   //         }
   //     }).then(function(response) {
   //         $scope.CartList = response.data;
   //     });
   // };
   //
   // $scope.auth = function() {
   //     $http({
   //         url: 'http://localhost:8081/app/auth',
   //         method: 'POST',
   //         data: $scope.credentials
   //     }).then(function (response) {
   //         $scope.token = response.data.token;
   //         sessionStorage.setItem("token", response.data.token);
   //         $cookies.put("token", response.data.token);
   //         // console.log(response);
   //         // $scope.filteredProducts();
   //         // $scope.loadCart();
   //         window.location.href = '/app/';
   //     });
   // };
   //
   //
   // $scope.saveOrder = function() {
   //     $http.get(contextPath + '/cart/order')
   //         .then(function(response) {
   //             $scope.CartList = null;
   //         });
   // };
   //
   //  $scope.saveOrder2 = function() {
   //      console.log($cookies.get("token"));
   //  };
   //
   //
   // // $scope.auth();
   //
   //
   //
   //  if ($scope.token !== '') {
   //      $scope.filteredProducts();
   //      $scope.loadCart();
   //  }


