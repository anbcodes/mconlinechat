<!-- <script lang="ts">
	import { onMount } from 'svelte';

	import Server from '../util/Server';

	let server: Server;

	onMount(() => {
		server = new Server();
		server.connect('localhost', 41663, false);
		server.on('message', (data) => console.log(data));
		server.once('open', () => {
			server.authenticate('faketoken');
		});

		server.once('authSuccess', () => {
			server.requestHistory(0, 0);
		});

		server.on('historyData', (data) => {
			console.log('GOT HISTORY', data.page, data.offset, data.items);
		});
	});
</script> -->
<script lang="ts">
	import globalSetup from '../util/globalSetup';

	import { onDestroy, onMount, tick } from 'svelte';
	import { chatHistory, context } from '../util/stores';
	import type { ChatHistoryItem } from '../util/stores';
	import Connect from '../components/Connect.svelte';
	import Login from '../components/Login.svelte';

	let historyContainer: HTMLDivElement;
	let historyItems: HTMLDivElement[] = [];

	let messageInput = '';

	let listeners: symbol[] = [];
	let onWeb = false;

	const onScroll = () => {
		if (
			historyContainer.scrollTop < 200 &&
			!$context.requestedHistory &&
			!$context.reachedEndOfHistory
		) {
			$context.requestedHistory = true;
			$context.server.requestHistory($chatHistory.length, $context.historyOffset);
		}
	};

	onMount(() => {
		globalSetup();
		onWeb = true;
	});

	$: {
		if (onWeb && $context.authenticated && $context.connected) {
			tick().then(() => {
				console.log($context.authenticated);
				listeners.push(
					$context.server.on('chatMessage', async () => {
						await tick();
						historyItems.slice(-1)[0].scrollIntoView();
					})
				);

				listeners.push(
					$context.server.on('historyData', async ({ page, offset }) => {
						await tick();
						if (page === 0) {
							historyItems.slice(-1)[0].scrollIntoView();
						} else if (historyItems.slice(100)[0]) {
							historyItems.slice(100)[0].scrollIntoView();
						}
					})
				);

				historyContainer.addEventListener('scroll', onScroll);
			});
		}
	}

	onDestroy(() => {
		listeners.forEach((l) => {
			$context.server.remove(l);
		});

		if (historyContainer) {
			historyContainer.removeEventListener('scroll', onScroll);
		}
	});

	const send = (message: string) => {
		if ($context.authenticated) {
			$context.server.sendChatMessage(message);
		}
	};

	onDestroy(() => {});

	const timeFromNow = (at: number) => {
		let now = new Date();
		let duration = +now - at;
		let seconds = Math.floor(duration / 1000);
		let minutes = Math.floor(duration / 1000 / 60);
		let hours = Math.floor(duration / 1000 / 60 / 60);
		let days = Math.floor(duration / 1000 / 60 / 60 / 24);
		let weeks = Math.floor(duration / 1000 / 60 / 60 / 24 / 7);
		let months = new Date(duration).getMonth() + (new Date(duration).getFullYear() - 1970) * 12;
		let years = new Date(duration).getFullYear() - 1970 * 12;

		if (years > 0) return new Intl.RelativeTimeFormat().format(-years, 'year');
		if (months > 0) return new Intl.RelativeTimeFormat().format(-months, 'month');
		if (weeks > 0) return new Intl.RelativeTimeFormat().format(-weeks, 'week');
		if (days > 0) return new Intl.RelativeTimeFormat().format(-days, 'days');
		if (hours > 0) return new Intl.RelativeTimeFormat().format(-hours, 'hours');
		if (minutes > 0) return new Intl.RelativeTimeFormat().format(-minutes, 'minutes');
		if (seconds > 0) return new Intl.RelativeTimeFormat().format(-seconds, 'seconds');

		return new Intl.RelativeTimeFormat().format(-0, 'seconds');
	};

	const formatMessage = (message: ChatHistoryItem) => {
		let msg = message.message.replace(/^Â/g, '').replace(/§./g, '');
		if (message.sender !== '') {
			if (message.fromWebsite) {
				return `[${message.sender}] ${msg}`;
			} else {
				return `<${message.sender}> ${msg}`;
			}
		} else {
			return `${msg}`;
		}
	};
</script>

{#if !$context.connected}
	<Connect />
{:else if !$context.authenticated}
	<Login />
{:else}
	<div class="w-full flex justify-center">
		<div class="w-2/3 bg-gray-800 rounded m-5 p-5 h-[50rem] flex-col flex">
			<div bind:this={historyContainer} class="flex-grow overflow-scroll p-1">
				{#each [...$chatHistory].reverse() as page, i}
					{#each [...page].reverse() as line, i2}
						<div class="p-1">
							<div bind:this={historyItems[i * 100 + i2]}>
								{formatMessage(line)}
							</div>
							<div class="text-gray-400 ml-5">
								- {timeFromNow(line.sent)}
							</div>
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
