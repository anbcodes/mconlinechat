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
  type: number,
  dimension: number,
}

export interface WorldType {
	id: number,
	name: string,
	image: string,
}

function createStore<T>(value: T) {
	const {subscribe, set, update} = writable(value);
	let curValue: T;
	subscribe((v) => curValue = v);

	return {
		subscribe,
		set,
		update,
		get: () => curValue,
	};
}

export const connected = createStore(false);
export const authenticated = createStore(false);
export const requestedHistory = createStore(false);
export const reachedEndOfHistory = createStore(false);
export const historyOffset = createStore(0);
export const server = createStore(new Server());
export const addedListeners = createStore(false);
export const authID = createStore(null as string);
export const mapDimension = createStore(0);
export const mapPoints = createStore([[], [], []] as WorldPoint[][]);
export const mapTypes = createStore([] as WorldType[]);
export const players = createStore({} as {[username: string]: {x: number, y: number, z: number, dimension: number}});
