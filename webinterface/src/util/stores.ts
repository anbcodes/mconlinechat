import { writable } from 'svelte/store';
import Server from './Server';

export interface ChatHistoryItem {
	content: string;
}

function createChatHistory() {
	const { subscribe, set, update } = writable<ChatHistoryItem[][]>([]);

	return {
		subscribe,
		addPage: (items: ChatHistoryItem[]) => update(n => {
			n.push(items);
			return n;
		}),
		set,
		setPage: (page: number, items: ChatHistoryItem[]) => update(n => {
			n[page] = items;
			return n;
		}),
		addMessage: (message: ChatHistoryItem, page = -1) => update(n => {
			n.slice(page)[0].push(message);
			return n;
		}),
		clear: () => set([])
	};
}

export const chatHistory = createChatHistory();

function createServer() {
	const { subscribe } = writable<Server>(new Server());
	let v: Server;
	subscribe((value) => v = value);

	return {
		subscribe,
		get: () => v,
	}
}

export const server = createServer();

function createContext() {
	const props = {
		connected: false,
		authenticated: false,
		history: chatHistory,
		requestedHistory: false,
		reachedEndOfHistory: false,
	}

	const { subscribe, update } = writable(props);
	let currentObj = {};
	subscribe(v => {
		currentObj = v;
	})

	const obj = {
		subscribe,
	}

	Object.entries(props).forEach(([key]) => {
		Object.defineProperty(obj, key, {
			get() {
				return currentObj[key]
			},
			set(v) {
				update((n) => {
					n[key] = v;
					return n;
				})
			}
		});
	});

	return obj as (typeof props & {subscribe: typeof subscribe});
}

export const context = createContext();
