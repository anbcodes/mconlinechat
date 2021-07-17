<script lang="ts">
	import globalSetup from '../util/globalSetup';

	import { onDestroy, onMount } from 'svelte';

	import {
		authenticated,
		connected,
		mapDimension,
		mapPoints,
		mapTypes,
		players,
		server
	} from '../util/stores';

	import type { WorldType } from '../util/stores';

	import Connect from '../components/Connect.svelte';
	import Login from '../components/Login.svelte';

	let typeToImage: { [type: number]: HTMLImageElement } = {};

	onMount(() => {
		globalSetup();
	});

	$: {
		if ($authenticated) {
			$server.requestPoints($mapDimension);
			$server.requestTypes();
		}
	}

	let mapTypeIDToType: { [id: number]: WorldType } = {};

	$: {
		$mapTypes.forEach((type) => {
			mapTypeIDToType[type.id] = type;
			if (type.image) {
				typeToImage[type.id] = new Image();
				typeToImage[type.id].src = type.image;
			}
		});
	}

	// map
	let canvas: HTMLCanvasElement;
	let ctx: CanvasRenderingContext2D;
	const mouse = { x: 0, y: 0, button: false, wheel: 0, lastX: 0, lastY: 0, drag: false };
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
		let scale = 1 / panZoom.scale;
		let gridScale = 2 ** (Math.log2(gridScreenSize * scale) | 0);
		let size = Math.max(canvasWidth, canvasHeight) * scale + gridScale * 2;
		let x = (((-panZoom.x * scale - gridScale) / gridScale) | 0) * gridScale;
		let y = (((-panZoom.y * scale - gridScale) / gridScale) | 0) * gridScale;

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
		ctx.lineWidth = 1;
		ctx.strokeStyle = 'red';
		ctx.beginPath();
		ctx.fillStyle = 'red';
		ctx.setTransform(1, 0, 0, 1, 0, 0);
		ctx.moveTo(x - 15, y);
		ctx.lineTo(x + 15, y);
		ctx.moveTo(x, y - 15);
		ctx.lineTo(x, y + 15);
		ctx.stroke();
		ctx.font = `14px Arial`;
		const worldCoord = panZoom.toWorld(x, y);
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
			drawPlayers();
		}
		requestAnimationFrame(update);
	}

	function drawLocations() {
		$mapPoints[$mapDimension].forEach(({ x, z, type, name, id }) => {
			let onMap = panZoom.toReal(x, z);
			ctx.fillStyle = '#fff';
			ctx.font = `14px Arial`;
			let hasImage = false;
			if (typeToImage[type]) {
				try {
					ctx.drawImage(typeToImage[type], onMap.x - 25, onMap.y - 25, 50, 50);
					hasImage = true;
				} catch (e) {
					console.log('Error drawing image', e);
				}
			}
			if (panZoom.scale > 0.5 || !hasImage) {
				if (mapTypeIDToType[type]) {
					if (name) {
						ctx.fillText(
							`${name} - ${mapTypeIDToType[type].name} (${id})`,
							onMap.x - 25,
							onMap.y + 35
						);
					} else {
						ctx.fillText(`${mapTypeIDToType[type].name} (${id})`, onMap.x - 25, onMap.y + 40);
					}
				} else {
					if (name) {
						ctx.fillText(`${name} (${id})`, onMap.x - 25, onMap.y + 35);
					} else {
						ctx.fillText(`(${id})`, onMap.x - 25, onMap.y + 40);
					}
				}

				ctx.font = `12px Arial`;
				ctx.fillText(`${x} ${z}`, onMap.x - 15, onMap.y + 52);
			}
		});
	}

	function drawPlayers() {
		Object.entries($players).forEach(([username, { x, z, dimension }]) => {
			if (dimension !== $mapDimension) {
				return;
			}
			let onMap = panZoom.toReal(x, z);
			ctx.fillStyle = '#fff';
			ctx.font = `14px Arial`;
			ctx.fillText(`${username} (${x.toFixed(0)}, ${z.toFixed(0)})`, onMap.x + 10, onMap.y);
			ctx.fillStyle = 'yellow';
			ctx.beginPath();
			ctx.arc(onMap.x, onMap.y, 5, 0, 2 * Math.PI);
			ctx.fill();
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

{#if !$connected}
	<Connect />
{:else if !$authenticated}
	<Login />
{:else}
	<div class="text-white p-5">
		<div class="w-full flex justify-center">
			<div class="w-2/3 flex-col flex">
				<div class="w-full flex justify-around">
					<div
						class:border-b={$mapDimension === 0}
						on:click={() => ($mapDimension = 0)}
						class="text-gray-400 border-gray-400"
					>
						Overworld
					</div>
					<div
						class:border-b={$mapDimension === 1}
						on:click={() => ($mapDimension = 1)}
						class="text-gray-400 border-gray-400"
					>
						Nether
					</div>
					<div
						class:border-b={$mapDimension === 2}
						on:click={() => ($mapDimension = 2)}
						class="text-gray-400 border-gray-400"
					>
						End
					</div>
				</div>
				<canvas bind:this={canvas} class="w-full rounded m-10 h-[50rem] flex-col flex" />
			</div>
		</div>
	</div>
{/if}
