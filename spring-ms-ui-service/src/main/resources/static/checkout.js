angular.module('app').controller('checkoutController', function ($scope, $http, $localStorage) {

    let gatewayAddress = null;
    $scope.totalAmount = 0;

    $scope.index1 = 1;
    $scope.index2 = 1;
    let selectedAddress = null;
    // $scope.addressPanelVisible = true;
    // $scope.billingPanelVisible = true;
    let country = '';
    let region = '';
    let city = '';
    let street = '';


    $scope.init = function () {
        $http.get('/gateway-address')
            .then(function(response){
                console.log(response.data.address);
                if (response.data.address !== '') {
                    gatewayAddress = response.data.address;
                    $scope.loadCart();
                    $scope.getUserAddresses();
                    $scope.getUserBillings();
                    $scope.loadCountries();
                }
            });
    };

    $scope.init();

    $scope.checkSizeCheckoutPage = function() {
        console.log("CHECKOUT:" + $scope.CartList.items.length);
        return $scope.CartList.items.length > 0;
    }


    $scope.loadCart = function() {
        $http({
            url: gatewayAddress + '/cart/' + $localStorage.marchMarketGuestCartId,
            method: 'GET',
        }).then(function(response) {
            $scope.CartList = response.data;
            for (let i = 0; i < $scope.CartList.items.length; i++) {
                $scope.totalAmount += $scope.CartList.items[i].amount;
                // console.log($scope.totalAmount);
            }
        });
    };

    $scope.createOrder = function() {
        let selectedAddress = document.getElementById("addressSelection").value;
        let selectedPayment = document.getElementById("paymentSelection").value;
        let selectedAddressId = $scope.addresses[selectedAddress - 1].id;
        let selectedPaymentId = $scope.billings[selectedPayment - 1].id;
        console.log(selectedAddressId);
        console.log(selectedPaymentId);

        $http({
            url: gatewayAddress + '/cart/checkout',
            method: 'GET',
            params: {
                billing_id: selectedPaymentId,
                address_id: selectedAddressId
            }
        }).then( function(response) {
            $scope.loadCart();
            alert(response.status);
            window.location.href = "#!/orders";
        })
    };

    $scope.getUserAddresses = function() {
      $http({
          url: gatewayAddress + '/address/all',
          method: 'GET'
      }).then(function(response) {
          $scope.addresses = response.data;
          console.log($scope.addresses.length);
          if ($scope.addresses.length === 0) {
              $scope.addressPanelVisible = true;
              // $scope.$apply();
          }
      });
    };

    $scope.getUserBillings = function() {
        $http({
            url: gatewayAddress + '/user/profile',
            method: 'GET'
        }).then(function(response) {
            $scope.billings = response.data.userBillings;
            if ($scope.billings.length === 0) {
                $scope.billingPanelVisible = true;
                $scope.$apply();
            }
        })
    }

    document.getElementById("paymentSelection").onchange = function() {
        let selected = document.getElementById("paymentSelection");
        if (selected.value == 10000) {
            $scope.billingPanelVisible = true;
        } else {
            $scope.billingPanelVisible = false;
        }
        $scope.$apply();
        console.log($scope.billingPanelVisible);
    }

    document.getElementById("addressSelection").onchange = function() {
        let selected = document.getElementById("addressSelection");
        if (selected.value == 10000) {
            $scope.addressPanelVisible = true;
        } else {
            $scope.addressPanelVisible = false;
        }
        $scope.$apply();
        // console.log($scope.addressPanelVisible);
        // console.log(selected.value);
        // console.log(selected.value == 10000);
    };

    document.getElementById("countrySelection").onchange = function() {
        let selected = document.getElementById("countrySelection");
        country = $scope.Countries[selected.value - 1];
        $scope.loadRegions(country);
        console.log(country);
    }

    document.getElementById("regionSelection").onchange = function() {
        let selected = document.getElementById("regionSelection");
        region = $scope.Regions[selected.value - 1];
        $scope.loadCities(country, region);
        console.log(region);
    }

    document.getElementById("citySelection").onchange = function() {
        let selected = document.getElementById("citySelection");
        city = $scope.Cities[selected.value - 1];
        $scope.loadStreets(country, region, city);
        console.log(city);
    }


    $scope.loadCountries = function() {
        $http({
            url: gatewayAddress + '/address/countries',
            method: 'GET'
        }).then(function(response){
            $scope.Countries = response.data;
        });
    };

    $scope.loadRegions = function(selectedCountry) {
        $http({
            url: gatewayAddress + '/address/regions',
            method: 'GET',
            params: {
                country: selectedCountry
            }
        }).then(function(response) {
            $scope.Regions = response.data;
        });
    };

    $scope.loadCities = function(selectedCountry, selectedRegion) {
        $http({
            url: gatewayAddress + '/address/cities',
            method: 'GET',
            params: {
                region: selectedRegion,
                country: selectedCountry
            }
        }).then(function(response) {
            $scope.Cities = response.data;
        });
    };

    $scope.loadStreets = function(selectedCountry, selectedRegion, selectedCity) {
        $http({
            url: gatewayAddress + '/address/streets',
            method: 'GET',
            params: {
                country: selectedCountry,
                region: selectedRegion,
                city: selectedCity
            }
        }).then(function(response) {
            $scope.Streets = response.data;
        })
    }

});