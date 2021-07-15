import { chatHistory, context } from "./stores";

export default function globalSetup(): void {
  console.log("Setting up");

  const host = localStorage.getItem('host');
  const authID = localStorage.getItem('authID');

  const searchParams = new URLSearchParams(location.search);
  const loginCode = searchParams.get('code');

  if (!context.addedListeners) {
    console.log("Adding listeners");
    context.server.on("chatMessage", (data) => {
      chatHistory.addMessage(data.message);
      context.historyOffset += 1;
    });

    context.server.on('historyData', (data) => {
      context.requestedHistory = false;
      if (data.items.length == 0) {
        context.reachedEndOfHistory = true;
      }
      chatHistory.setPage(data.page, data.items);
    });

    context.server.on('authSuccess', (data) => {
      context.authenticated = true;
      context.authID = data.authID;
      if (loginCode) {
        history.replaceState(null, null, '/');
      }
      localStorage.setItem('authID', data.authID);
      console.log("set authid", data.authID, context.authenticated);
    });

    context.server.on('message', (data) => console.log(data));
		context.server.on('open', () => {
      context.connected = true;
      if (loginCode) {
        context.server.login(loginCode);
      } else if (authID) {
			  context.server.authenticate(authID);
      }
		});

    context.server.on('close', () => {
      context.connected = false;
    })

		context.server.on('authSuccess', () => {
      setTimeout(() => {
			  context.server.requestHistory(0, 0);
      }, 10);
		});

    context.addedListeners = true;
  }

  if (!context.connected && host) {
    console.log("Connecting");
    context.server.connect(host, 41663, false);
  }
}
