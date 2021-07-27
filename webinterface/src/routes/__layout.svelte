<script>
	import '../app.postcss';
</script>

<div class="text-white flex flex-col md:p-5 h-screen">
	<div class="text-2xl text-center pt-5 md:pt-0">Minecraft Server Control Panel</div>
	<div class="w-full flex justify-center">
		<div class="text-lg text-gray-400 w-2/3 flex justify-around">
			<a href="/">Chat</a>
			<a href="/map">Map</a>
		</div>
	</div>
	<slot />
</div>
