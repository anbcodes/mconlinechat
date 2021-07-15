<script>
	import globalSetup from '../util/globalSetup';

	import { onDestroy, onMount } from 'svelte';

	import { context } from '../util/stores';

	onMount(() => {
		globalSetup();
	});
</script>
