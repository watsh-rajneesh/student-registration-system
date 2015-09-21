
(function() {
  var app = angular.module('studentModule', []);

  app.controller('StudentController', function($scope, $http){
    $http.get("/api/students").success(function(data) {
        var jsonString = angular.toJson(data);
        console.log(jsonString)
        $scope.students = angular.fromJson(jsonString)
        console.log(students)
    });
  });
})();