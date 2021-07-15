<script lang="typescript">
	import { onDestroy, onMount, tick } from 'svelte';
	import { globalSetup } from '../util/util';
	import { server, context, chatHistory } from '../util/stores';
	import { goto } from '$app/navigation';

	let messageInput = '';
	let history: string[][] = [];
	let historyItems: HTMLDivElement[] = [];
	let historyContainer: HTMLDivElement;
	let requestedHistory = false;
	let reachedEndOfHistory = false;
	let onWeb = false;

	onMount(() => {
		globalSetup();
		onWeb = true;
	});

	let lastLoadedPage = 0;
	$: {
		if ($server) {
			$server.on('HISTORY_DATA', (data) => (lastLoadedPage = data.page));
		}
	}

	$: {
		if (lastLoadedPage === 1) {
			tick().then(() => {
				if (historyItems.slice(-1)[0]) {
					historyItems.slice(-1)[0].scrollIntoView();
				}
			});
		} else {
			if (historyItems.slice(100)[0]) {
				historyItems.slice(100)[0].scrollIntoView();
			}
		}
	}

	$: {
		if ($server && $context.authenticated && $context.connected && onWeb) {
			if (!$context.requestedHistory) {
				$server.requestHistory(1);
				$context.requestedHistory = true;
			}
			tick().then(() => {
				historyContainer.addEventListener('scroll', () => {
					if (
						historyContainer.scrollTop < 200 &&
						!$context.requestedHistory &&
						!$context.reachedEndOfHistory
					) {
						$context.requestedHistory = true;
						$server.requestHistory(history.length + 1);
					}
				});
			});
		}
	}

	const send = (message: string) => {
		if ($server) {
			$server.sendMessage(message);
		}
	};

	$: if (!$context.connected && onWeb) {
		goto('/connect');
	}

	$: if (!$context.authenticated && onWeb) {
		goto('/login');
	}
</script>

<div class="w-full flex justify-center">
	<div class="w-2/3 bg-gray-800 rounded m-5 p-5 h-[50rem] flex-col flex">
		<div bind:this={historyContainer} class="flex-grow overflow-scroll p-1">
			{#each [...$chatHistory].reverse() as page, i}
				{#each page as line, i2}
					<div bind:this={historyItems[i * 100 + i2]}>
						{line.content.replace(/^Â/g, '').replace(/§./g, '')}
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
