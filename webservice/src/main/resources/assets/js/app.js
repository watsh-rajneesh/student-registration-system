
(function() {
  var app = angular.module('studentModule', []);

  app.controller('StudentController', ['$http', function($http){
    var store = this;
    store.students = [];

    $http.get("/api/students").success(function(data) {
        store.students = data;
    });
  }]);
})();