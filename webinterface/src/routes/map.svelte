<script lang="typescript">
	import { onDestroy, onMount, tick } from 'svelte';
	import Server from '../util/Server';
	import type { MapPoint } from '../util/Server';

	let connected = false;
	let loggedIn = false;
	let server: Server | null;
	let stopped = false;

	const typeToImageURL = {
		'explored endcity': '/icons/endcity-explored.png',
		'explored endcity and endship': '/icons/endship-endcity-explored.png',
		'endcity and explored endship': '/icons/endship-explored.png',
		'endcity and endship': '/icons/endship.png'
	};

	let typeToImage: { [type: string]: HTMLImageElement };

	onMount(() => {
		let host = localStorage.getItem('host');
		if (host !== null) {
			connect(host);
		}
		let authIDStorage = localStorage.getItem('authID');
		if (authIDStorage !== null) {
			loggedIn = true;
		}

		typeToImage = Object.fromEntries(
			Object.entries(typeToImageURL).map(([key, value]) => {
				let img = new Image();
				img.src = value;
				return [key, img];
			})
		);
	});

	onDestroy(() => {
		if (server) {
			server.ws.close();
		}
	});

	let points: MapPoint[] = [];
	let currentDim = 'O';

	const connect = (host: string) => {
		localStorage.setItem('host', host);
		server = new Server(host, '/map');
		server.on('POINTS', async (data: MapPoint[]) => {
			points = data;
			console.log('points', data);
		});
		server.on('OPEN', () => {
			connected = true;
			console.log('Connection Opened');
			setTimeout(() => {
				server.getPoints();
			}, 250);
		});
		server.on('CLOSE', () => {
			connected = false;
			server.ws.close();
			console.log('Connection Closed');
		});
	};

	// map
	let canvas: HTMLCanvasElement;
	let ctx: CanvasRenderingContext2D;
	const mouse = { x: 0, y: 0, button: false, wheel: 0, lastX: 0, lastY: 0, drag: false };
	const gridLimit = 64; // max grid lines for static grid
	const gridSize = 128; // grid size in screen pixels for adaptive and world pixels for static
	const scaleRate = 1.02; // Closer to 1 slower rate of change
	// Less than 1 inverts scaling change and same rule
	// Closer to 1 slower rate of change
	const topLeft: Point = { x: 0, y: 0 }; // holds top left of canvas in world coords.

	let canvasWidth = 0;
	let canvasHeight = 0;

	interface Point {
		x: number;
		y: number;
	}

	const panZoom = {
		x: 0,
		y: 0,
		scale: 1,
		apply() {
			ctx.setTransform(this.scale, 0, 0, this.scale, this.x, this.y);
		},
		scaleAt(x, y, sc) {
			// x & y are screen coords, not world
			this.scale *= sc;
			this.x = x - (x - this.x) * sc;
			this.y = y - (y - this.y) * sc;
		},
		toWorld(x, y, point: Point = { x: NaN, y: NaN }) {
			// converts from screen coords to world coords
			const inv = 1 / this.scale;
			point.x = (x - this.x) * inv;
			point.y = (y - this.y) * inv;
			return point;
		},
		toReal(x, y, point: Point = { x: NaN, y: NaN }) {
			// converts from screen coords to world coords
			const inv = 1 / this.scale;
			point.x = x / inv + this.x;
			point.y = y / inv + this.y;
			return point;
		}
	};

	$: {
		if (canvas && canvas !== null) {
			requestAnimationFrame(update);
			console.log(canvas);
			ctx = canvas.getContext('2d');

			['mousedown', 'mouseup', 'mousemove'].forEach((name) =>
				document.addEventListener(name, mouseEvents)
			);
			document.addEventListener('wheel', mouseEvents, { passive: false });
		}
	}

	function mouseEvents(e) {
		if (canvas) {
			const bounds = canvas.getBoundingClientRect();
			mouse.x = e.pageX - bounds.left - scrollX;
			mouse.y = e.pageY - bounds.top - scrollY;
			mouse.button = e.type === 'mousedown' ? true : e.type === 'mouseup' ? false : mouse.button;
			if (e.type === 'wheel') {
				mouse.wheel += -e.deltaY;
				e.preventDefault();
			}
		}
	}

	function drawGrid(gridScreenSize = 128) {
		var scale,
			gridScale,
			size,
			x,
			y,
			limitedGrid = false;
		scale = 1 / panZoom.scale;
		gridScale = 2 ** (Math.log2(gridScreenSize * scale) | 0);
		size = Math.max(canvasWidth, canvasHeight) * scale + gridScale * 2;
		x = (((-panZoom.x * scale - gridScale) / gridScale) | 0) * gridScale;
		y = (((-panZoom.y * scale - gridScale) / gridScale) | 0) * gridScale;

		panZoom.apply();
		ctx.lineWidth = 1;
		ctx.strokeStyle = '#aaa';
		ctx.beginPath();
		for (let i = 0; i < size; i += gridScale) {
			ctx.moveTo(x + i, y);
			ctx.lineTo(x + i, y + size);
			ctx.moveTo(x, y + i);
			ctx.lineTo(x + size, y + i);
		}
		ctx.setTransform(1, 0, 0, 1, 0, 0); // reset the transform so the lineWidth is 1
		ctx.stroke();
	}
	function drawCursor(x, y) {
		const worldCoord = panZoom.toWorld(x, y);
		ctx.lineWidth = 1;
		ctx.strokeStyle = 'red';
		ctx.beginPath();
		ctx.fillStyle = 'red';
		ctx.setTransform(1, 0, 0, 1, 0, 0); //reset the transform so the lineWidth is 1
		ctx.moveTo(x - 15, y);
		ctx.lineTo(x + 15, y);
		ctx.moveTo(x, y - 15);
		ctx.lineTo(x, y + 15);
		ctx.stroke();
		ctx.font = `14px Arial`;
		ctx.fillText(`${worldCoord.x.toFixed(0)} ${worldCoord.y.toFixed(0)}`, x + 5, y - 5);
	}

	function update() {
		if (canvas) {
			ctx.setTransform(1, 0, 0, 1, 0, 0); // reset transform
			ctx.globalAlpha = 1; // reset alpha
			if (canvas) {
				if (canvasWidth !== canvas.clientWidth || canvasHeight !== canvas.clientHeight) {
					canvasWidth = canvas.width = canvas.clientWidth;
					canvasHeight = canvas.height = canvas.clientHeight;
				}
			}

			ctx.clearRect(0, 0, canvasWidth, canvasHeight);
			if (mouse.wheel !== 0) {
				let scale = 1;
				scale = mouse.wheel < 0 ? 1 / scaleRate : scaleRate;
				mouse.wheel *= 0.8;
				if (Math.abs(mouse.wheel) < 1) {
					mouse.wheel = 0;
				}
				panZoom.scaleAt(mouse.x, mouse.y, scale); //scale is the change in scale
			}
			if (mouse.button) {
				if (!mouse.drag) {
					mouse.lastX = mouse.x;
					mouse.lastY = mouse.y;
					mouse.drag = true;
				} else {
					panZoom.x += mouse.x - mouse.lastX;
					panZoom.y += mouse.y - mouse.lastY;
					mouse.lastX = mouse.x;
					mouse.lastY = mouse.y;
				}
			} else if (mouse.drag) {
				mouse.drag = false;
			}
			drawGrid(gridSize);
			drawOrigin();
			drawCursor(mouse.x, mouse.y);
			drawLocations();
		}
		if (!stopped) {
			requestAnimationFrame(update);
		}
	}

	function drawLocations() {
		points
			.filter((v) => v.dim === currentDim)
			.forEach(({ x, z, type, name }) => {
				let onMap = panZoom.toReal(x, z);
				ctx.fillStyle = '#fff';
				ctx.font = `14px Arial`;
				let hasImage = false;
				if (typeToImage[type.toLowerCase()]) {
					try {
						ctx.drawImage(typeToImage[type.toLowerCase()], onMap.x - 25, onMap.y - 25, 50, 50);
						hasImage = true;
					} catch (e) {
						console.log('Error drawing image', e);
					}
				}
				if (panZoom.scale > 0.5 || !hasImage) {
					if (name) {
						ctx.fillText(`${name} (${type})`, onMap.x - 25, onMap.y + 35);
					} else {
						ctx.fillText(`${type}`, onMap.x - 25, onMap.y + 40);
					}
					ctx.font = `12px Arial`;
					ctx.fillText(`${x} ${z}`, onMap.x - 15, onMap.y + 52);
				}
			});
	}

	function drawOrigin() {
		ctx.strokeStyle = 'blue';
		let { x, y } = panZoom.toReal(0, 0);
		ctx.beginPath();
		ctx.moveTo(x - 10, y);
		ctx.lineTo(x + 10, y);
		ctx.moveTo(x, y - 10);
		ctx.lineTo(x, y + 10);
		ctx.stroke();
	}
</script>

<div class="text-white p-5">
	{#if !connected}
		<div class="flex pt-5">
			<div class="pr-5">Can't connect to server. Setup connection <a href="/">here</a></div>
		</div>
	{:else if !loggedIn}
		<div>
			You are not logged in. Click <a href="/">here</a> to login
		</div>
	{:else}
		<div class="w-full flex justify-center">
			<div class="w-2/3 flex-col flex">
				<div class="w-full flex justify-around">
					<div
						class:border-b={currentDim === 'O'}
						on:click={() => (currentDim = 'O')}
						class="text-gray-400 border-gray-400"
					>
						Overworld
					</div>
					<div
						class:border-b={currentDim === 'N'}
						on:click={() => (currentDim = 'N')}
						class="text-gray-400 border-gray-400"
					>
						Nether
					</div>
					<div
						class:border-b={currentDim === 'E'}
						on:click={() => (currentDim = 'E')}
						class="text-gray-400 border-gray-400"
					>
						End
					</div>
				</div>
				<canvas bind:this={canvas} class="w-full rounded m-5 p-5 h-[50rem] flex-col flex" />
			</div>
		</div>
	{/if}
</div>
