<script lang="typescript">
	import globalSetup from '../util/globalSetup';
	import { onMount } from 'svelte';

	let hostInput = '';

	onMount(() => {
		globalSetup();
	});

	const connect = (host: string) => {
		localStorage.setItem('host', host);
		globalSetup();
	};
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
