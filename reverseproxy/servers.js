var proxy = require('redbird')({
  port: 25565, // http port is needed for LetsEncrypt challenge during request / renewal. Also enables automatic http->https redirection for registered https routes.
});

// let config = {
//   ssl: {
//     letsencrypt: {
//       email: 'anbcodes@protonmail.com', // Domain owner/admin email
//       production: true, // WARNING: Only use this flag when the proxy is verified to work correctly to avoid being banned!
//     }
//   }
// }

// Route to any global ip

// Route to any local ip, for example from docker containers.
proxy.register("mc.anb.codes", "http://127.0.0.1:25566");

proxy.register("create.mc.anb.codes", "http://127.0.0.1:25567");
