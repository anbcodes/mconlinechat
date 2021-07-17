import { writable } from "svelte/store";
import Server from './Server';


export interface ChatHistoryItem {
	sender: string;
  sent: number;
  fromWebsite: boolean;
  message: string;
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
		addMessage: (message: ChatHistoryItem, page = 0) => update(n => {
      console.log("adding ", message);
			n[page].splice(0, 0, message);
			return n;
		}),
		clear: () => set([])
	};
}

export const chatHistory = createChatHistory();


export interface WorldPoint {
  x: number,
  z: number,
  creator: string,
  created: number,
  id: number,
  name: string,
  type: string,
  dimension: number,
}

function createContext() {
	const props = {
		connected: false,
		authenticated: false,
		requestedHistory: false,
		reachedEndOfHistory: false,
		historyOffset: 0,
    server: new Server(),
    addedListeners: false,
    authID: null,
    mapDimension: 0,
    mapPoints: [] as WorldPoint[],
	}

	const { subscribe, set, update } = writable(props);
	let currentObj = {};
	subscribe(v => {
		currentObj = v;
	})

	const obj = {
		subscribe,
    set,
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

	return obj as (typeof props) & (typeof obj);
}

export const context = createContext();
