<script>
	import '../app.postcss';
</script>

<div class="text-white p-5">
	<div class="text-2xl text-center">Minecraft Server Control Panel</div>
	<div class="w-full flex justify-center">
		<div class="text-lg text-gray-400 w-2/3 flex justify-around">
			<a href="/">Chat</a>
			<a href="/map">Map</a>
		</div>
	</div>
	<slot />
</div>
