<script lang="ts">
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
			server.sendChatMessage('Hey!!!!');
		});
	});
</script>
