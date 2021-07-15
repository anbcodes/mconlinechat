import { chatHistory, context } from "./stores";

export default function globalSetup(): void {
  console.log("Setting up");

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
    });

    context.server.on('loginSuccess', (data) => {
      context.authenticated = true;
      context.authID = data.authID;
    });

    context.server.on('message', (data) => console.log(data));
		context.server.on('open', () => {
      context.connected = true;
			context.server.authenticate('faketoken');
		});

    context.server.on('close', () => {
      context.connected = false;
    })

		context.server.on('authSuccess', () => {
			context.server.requestHistory(0, 0);
		});

    context.addedListeners = true;
  }

  if (!context.connected) {
    console.log("Connecting");
    context.server.connect('localhost', 41663, false);
  }
}
