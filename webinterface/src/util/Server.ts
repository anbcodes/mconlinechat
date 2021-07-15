export type MessageType =
  "AUTH_FAILED" | "AUTH_SUCCESS" |"LOGIN" | "LOGIN_SUCCESS" | "LOGIN_FAILED" | "GET_HISTORY" | "HISTORY_DATA" | "SEND" | "NEW_MESSAGE" | "POINTS" | "GET" | "OPEN" | "CLOSE" | "ERROR" | "WS_MESSAGE"

export interface MapPoint {
  x: number;
  z: number;
  type: string;
  name: string;
  dim: string;
}

interface Message {
  type: MessageType,
  authID: string | null,
  data: string[],
}

type HandlerFunc = (data: any) => void;

export default class Server {
  private listeners: Record<MessageType, HandlerFunc[]> = {} as Record<MessageType, HandlerFunc[]>;
  public ws: WebSocket;
  public authID: string;

  constructor() {
    this.on("WS_MESSAGE", this.handleMessage.bind(this));
    this.on("LOGIN_SUCCESS", this.handleLogin.bind(this));
  }

  public connect(host: string, path = '/', port = 41663): void {
    this.ws = new WebSocket(`ws://${host}:${port}${path}`);
    this.ws.onopen = (ev) => this.callListeners("OPEN", ev);
    this.ws.onclose = (ev) => this.callListeners("CLOSE", ev);
    this.ws.onmessage = (ev) => this.callListeners("WS_MESSAGE", ev);
    this.ws.onerror = (ev) => this.callListeners("ERROR", ev);
  }

  public on(type: MessageType, func: HandlerFunc): void {
    if (this.listeners[type] == undefined) {
      this.listeners[type] = [func];
    } else {
      this.listeners[type].push(func);
    }
  }

  public login(code: string): void {
    console.log('Logging in with code', code);
    console.log(this.listeners);
    this.ws.send(JSON.stringify({
      type: "LOGIN",
      code,
    }));
  }

  public requestHistory(page = 1): void {
    console.log('Requesting History', page);
    this.ws.send(JSON.stringify({
      type: "GET_HISTORY",
      page,
      authID: this.authID,
    }));
  }

  public getPoints(dim: number): void {
    this.ws.send(JSON.stringify({
      type: "GET_POINTS",
      dim,
      authID: this.authID,
    }));
  }

  public sendMessage(msg: string): void {
    console.log('Sending message', msg);
    this.ws.send(JSON.stringify({
      type: "SEND",
      message: msg,
      authID: this.authID,
    }))
  }

  private callListeners(type: MessageType, data: any) {
    if (this.listeners[type]) {
      this.listeners[type].forEach(l => l(data));
    }
  }

  private handleMessage(ev: MessageEvent<any>) {
    const message = JSON.parse(ev.data);
    this.callListeners(message.type, message);
  }

  private handleLogin(data: any) {
    this.authID = data.authID;
		localStorage.setItem('authID', this.authID);
  }

}
