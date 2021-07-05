var proxy = require('redbird')({
  port: 80, // http port is needed for LetsEncrypt challenge during request / renewal. Also enables automatic http->https redirection for registered https routes.
  letsencrypt: {
    path: __dirname + '/certs',
    port: 81 // LetsEncrypt minimal web server port for handling challenges. Routed 80->9999, no need to open 9999 in firewall. Default 3000 if not defined.
  },
  ssl: {
    http2: true,
    port: 31661, // SSL port used to serve registered https routes with LetsEncrypt certificate.
  }
});

// Route to any global ip

// Route to any local ip, for example from docker containers.
proxy.register("mc.anb.codes", "http://127.0.0.1:31662", {
  ssl: {
    letsencrypt: {
      email: 'anbcodes@protonmail.com', // Domain owner/admin email
      production: true, // WARNING: Only use this flag when the proxy is verified to work correctly to avoid being banned!
    }
  }
});

//
// LetsEncrypt requires a minimal web server for handling the challenges, this is by default on port 3000
// it can be configured when initiating the proxy. This web server is only used by Redbird internally so most of the time
// you  do not need to do anything special other than avoid having other web services in the same host running
// on the same port.

//
// HTTP2 Support using LetsEncrypt for the certificates
//
