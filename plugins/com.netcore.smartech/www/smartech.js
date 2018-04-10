/*global cordova, module*/

module.exports = {
   track: function (data) {
      cordova.exec(function(m){}, function(m){}, "Smartech", "track", [data]);
   }
};
