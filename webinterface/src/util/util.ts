import { goto } from '$app/navigation';
import { context, chatHistory, server } from './stores';

export function globalSetup(): void {
  const host = localStorage.getItem('host');
  const authID = localStorage.getItem('authID');
	server.get().authID = authID;
  if (host && !server.get()?.ws?.OPEN) {
		server.get().connect(host);
    server.get().on('OPEN', () => context.connected = true);

		server.get().on("OPEN", (ev) => {
			if (authID) {
				server.get().ws.send(JSON.stringify({
					type: 'AUTH',
					authID: authID,
				}))
			} else {
				goto('/login');
			}
    })
    server.get().on('CLOSE', () => {context.connected = false; goto('/connect')});

		server.get().on('AUTH_FAILED', () => {
			goto('/login');
		});

		server.get().on("AUTH_SUCCESS", () => {
			context.authenticated = true;
		})

    server.get().on('LOGIN_SUCCESS', (data) => {
			context.authenticated = true;
			server.get().requestHistory();
			context.requestedHistory = true;
			console.log('Login Success');
		});

		server.get().on('HISTORY_DATA', async (data: any) => {
			const page = data.page;
			const lines = data.items.reverse() as string[];

			if (lines.length === 0) {
				context.reachedEndOfHistory = true;
			}

			chatHistory.setPage(page - 1, lines.map(v => ({content: v})));
			
			context.requestedHistory = false;
			console.log('Received History', page, lines);
		});
		server.get().on('OPEN', () => {
			context.connected = true;
			if (context.authenticated) {
				server.get().requestHistory(1);
				context.requestedHistory = true;
			}
			console.log('Connection Opened');
		});
		server.get().on('CLOSE', () => {
			context.connected = false;
			server.get().ws.close();
			console.log('Connection Closed');
		});
		server.get().on('WS_MESSAGE', (data) => {
			console.log('RAW DATA ', data);
		});
		server.get().on('NEW_MESSAGE', async (data) => {
			chatHistory.addMessage({content: data.message});
			
			console.log('Received Message: ', data.message);
		});	
	}
    
}
