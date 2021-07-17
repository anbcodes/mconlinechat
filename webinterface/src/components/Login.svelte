<script lang="ts">
	import globalSetup from '../util/globalSetup';

	import { onMount } from 'svelte';
	import { connected, server } from '../util/stores';

	let loginInput = '';

	onMount(() => {
		globalSetup();
	});

	const login = (code: string) => {
		if ($connected) {
			$server.login(code);
		}
	};
</script>

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
