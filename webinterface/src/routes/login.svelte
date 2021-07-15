<script lang="ts">
	import { globalSetup } from '../util/util';

	import { onMount } from 'svelte';

	import { context, server } from '../util/stores';
	import { goto } from '$app/navigation';

	let loginInput = '';
	let onWeb = false;

	onMount(() => {
		globalSetup();
		onWeb = true;
	});

	const login = (code: string) => {
		if ($server) {
			$server.login(code);
		}
	};

	$: if (!$context.connected && onWeb) {
		goto('/connect');
	}

	$: if ($context.authenticated && onWeb) {
		goto('/');
	}
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
