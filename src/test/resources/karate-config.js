function fn() {
  var env = karate.env;
  karate.log('karate.env system property was:', env);
  
  if (!env) {
    env = 'dev';
  }
  
  var config = {
    baseUrl: 'http://localhost:8080',
  };
  
  if (env == 'prod') {
    config.baseUrl = 'https://api.cotsys.prod';
  }
  
  return config;
}