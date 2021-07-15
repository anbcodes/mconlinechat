<script>
	import globalSetup from '../util/globalSetup';

	import { onDestroy, onMount } from 'svelte';

	import { context } from '../util/stores';
	import Connect from '../components/Connect.svelte';
	import Login from '../components/Login.svelte';

	onMount(() => {
		globalSetup();
	});
</script>

{#if !$context.connected}
	<Connect />
{:else if !$context.authenticated}
	<Login />
{:else}
	Hey!
{/if}
