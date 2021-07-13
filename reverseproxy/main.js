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

let config = {
  ssl: {
    letsencrypt: {
      email: 'anbcodes@protonmail.com', // Domain owner/admin email
      production: true, // WARNING: Only use this flag when the proxy is verified to work correctly to avoid being banned!
    }
  }
}

// Route to any global ip

// Route to any local ip, for example from docker containers.
proxy.register("mc.anb.codes/", "http://127.0.0.1:31663", config);

proxy.register("mc.anb.codes/map", "http://127.0.0.1:31664", config);
