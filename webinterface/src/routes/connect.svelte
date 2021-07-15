<script lang="typescript">
	import { context, server } from '../util/stores';
	import { globalSetup } from '../util/util';
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';

	let hostInput = '';
	let onWeb = false;

	onMount(() => {
		globalSetup();
		onWeb = true;
	});

	const connect = (host: string) => {
		localStorage.setItem('host', host);
		$server.connect(host);
		let authID = localStorage.getItem('authID');
		if (authID) {
		}
	};

	$: {
		if ($context.authenticated && $context.connected && onWeb) {
			goto('/');
		} else if ($context.connected && onWeb) goto('/login');
	}
</script>

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
