<script lang="typescript">
	import { onDestroy, onMount, tick } from 'svelte';
	import Server from '../util/Server';

	let connected = false;
	let hostInput = '';
	let loggedIn = false;
	let loginInput = '';
	let messageInput = '';
	let history: string[][] = [];
	let historyItems: HTMLDivElement[] = [];
	let server: Server | null;
	let historyContainer: HTMLDivElement;
	let requestedHistory = false;
	let reachedEndOfHistory = false;

	onMount(() => {
		let host = localStorage.getItem('host');
		if (host !== null) {
			connect(host);
		}
		let authIDStorage = localStorage.getItem('authID');
		if (authIDStorage !== null) {
			loggedIn = true;
		}
	});

	onDestroy(() => {
		if (server) {
			server.ws.close();
		}
	});

	$: {
		if (loggedIn && connected) {
			tick().then(() => {
				historyContainer.addEventListener('scroll', () => {
					if (historyContainer.scrollTop < 200 && !requestedHistory && !reachedEndOfHistory) {
						requestedHistory = true;
						server.requestHistory(history.length + 1);
					}
				});
			});
		}
	}

	const connect = (host: string) => {
		localStorage.setItem('host', host);
		server = new Server(host);
		server.on('LOGIN_SUCCESS', () => {
			loggedIn = true;
			server.requestHistory();
			requestedHistory = true;
			console.log('Login Success');
		});
		server.on('HISTORY_DATA', async (data: string[]) => {
			let page = data.slice(-1)[0];
			let lines = data.slice(0, -1).reverse();

			if (lines.length === 0) {
				reachedEndOfHistory = true;
			}

			history[+page - 1] = lines;
			if (+page === 1) {
				await tick();
				if (historyItems.slice(-1)[0]) {
					historyItems.slice(-1)[0].scrollIntoView();
				}
			} else {
				if (historyItems.slice(100)[0]) {
					historyItems.slice(100)[0].scrollIntoView();
				}
			}
			requestedHistory = false;
			console.log('Received History', page, history[+page - 1].length);
		});
		server.on('OPEN', () => {
			connected = true;
			if (loggedIn) {
				server.requestHistory(1);
				requestedHistory = true;
			}
			console.log('Connection Opened');
		});
		server.on('CLOSE', () => {
			connected = false;
			server.ws.close();
			console.log('Connection Closed');
		});
		server.on('WS_MESSAGE', (data) => {
			// console.log('RAW DATA ', data);
		});
		server.on('NEW_MESSAGE', async (data) => {
			history[0].push(data[0]);
			history = history;
			await tick();
			if (historyItems.slice(-1)[0]) {
				historyItems.slice(-1)[0].scrollIntoView();
			}
			console.log('Received Message: ', data[0]);
		});
	};

	const login = (code: string) => {
		if (server) {
			server.login(code);
		}
	};

	const send = (message: string) => {
		if (server) {
			server.sendMessage(message);
		}
	};
</script>

{#if !connected}
	<div class="flex pt-5">
		<div class="pr-5">Enter server name</div>
		<input
			class="bg-gray-700 rounded p-1"
			bind:value={hostInput}
			on:keydown={(e) => {
				if (e.key === 'Enter') {
					connect(hostInput);
					hostInput = '';
				}
			}}
		/>
		<button
			class="mx-4 p-1 px-2 rounded bg-gray-700 hover:bg-gray-800"
			on:click={() => {
				connect(hostInput);
				hostInput = '';
			}}>Connect</button
		>
	</div>
{:else if !loggedIn}
	<div>
		<div>
			You are not logged in. To login type <code>/login</code> on the server and enter your code here
		</div>
		<input
			class="bg-gray-700 rounded p-1"
			bind:value={loginInput}
			on:keydown={(e) => {
				if (e.key === 'Enter') {
					login(loginInput);
					loginInput = '';
				}
			}}
		/>
		<button
			class="mx-4 p-1 px-2 rounded bg-gray-700 hover:bg-gray-800"
			on:click={() => {
				login(loginInput);
				loginInput = '';
			}}>Login</button
		>
	</div>
{:else}
	<div class="w-full flex justify-center">
		<div class="w-2/3 bg-gray-800 rounded m-5 p-5 h-[50rem] flex-col flex">
			<div bind:this={historyContainer} class="flex-grow overflow-scroll p-1">
				{#each [...history].reverse() as page, i}
					{#each page as line, i2}
						<div bind:this={historyItems[i * 100 + i2]}>
							{line.replace(/^Â/g, '').replace(/§./g, '')}
						</div>
					{/each}
				{/each}
			</div>
			<div class="flex mt-2">
				<input
					class="bg-gray-700 rounded p-1 flex-grow"
					bind:value={messageInput}
					on:keydown={(e) => {
						if (e.key === 'Enter') {
							send(messageInput);
							messageInput = '';
						}
					}}
				/>
				<button
					class="mx-4 p-1 px-2 rounded bg-gray-700 hover:bg-gray-800"
					on:click={() => {
						send(messageInput);
						messageInput = '';
					}}>Send</button
				>
			</div>
		</div>
	</div>
{/if}
