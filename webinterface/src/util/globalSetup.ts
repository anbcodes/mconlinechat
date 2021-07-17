import { addedListeners, authenticated, chatHistory, historyOffset, mapPoints, reachedEndOfHistory, requestedHistory, server, authID as contextAuthID, connected, mapTypes, players } from "./stores";

export default function globalSetup(): void {
  console.log("Setting up");

  const host = localStorage.getItem('host');
  const authID = localStorage.getItem('authID');

  const searchParams = new URLSearchParams(location.search);
  const loginCode = searchParams.get('code');

  if (!addedListeners.get()) {
    console.log("Adding listeners");
    server.get().on("chatMessage", (data) => {
      chatHistory.addMessage(data.message);
      historyOffset.update(n => n + 1);
    });

    server.get().on('historyData', (data) => {
      requestedHistory.set(false);
      if (data.items.length == 0) {
        reachedEndOfHistory.set(true);
      }
      chatHistory.setPage(data.page, data.items);
    });

    server.get().on('pointsData', (data) => {
      mapPoints.update((v) => {
        v[data.dimension] = data.points;
        return v;
      });
    });

    server.get().on('typesData', (data) => {
      mapTypes.set(data.types);
    });

    server.get().on('playerMove', (data) => {
      players.update(p => {
        p[data.username] = {x: data.x, y: data.y, z: data.z, dimension: data.dimension};
        return p;
      });
    });

    server.get().on('authSuccess', (data) => {
      authenticated.set(true);
      contextAuthID.set(data.authID);
      if (loginCode) {
        history.replaceState(null, null, '/');
      }
      localStorage.setItem('authID', data.authID);
      console.log("set authid", data.authID, authenticated.get());
    });

    server.get().on('message', (data) => console.log(data));
		server.get().on('open', () => {
      connected.set(true);
      if (loginCode) {
        server.get().login(loginCode);
      } else if (authID) {
			  server.get().authenticate(authID);
      }
		});

    server.get().on('close', () => {
      connected.set(false);
    })

		server.get().on('authSuccess', () => {
      setTimeout(() => {
			  server.get().requestHistory(0, 0);
      }, 10);
		});

    addedListeners.set(true);
  }

  if (!connected.get() && host) {
    console.log("Connecting");
    server.get().connect(host);
  }
}
