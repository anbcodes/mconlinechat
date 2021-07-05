export type MessageType =
  "LOGIN" | "LOGIN_SUCCESS" | "LOGIN_FAILED" | "GET_HISTORY" | "HISTORY_DATA" | "SEND" | "NEW_MESSAGE" | "OPEN" | "CLOSE" | "ERROR" | "WS_MESSAGE"



interface Message {
  type: MessageType,
  authID: string | null,
  data: string[],
}

type HandlerFunc = (data: string[] | MessageEvent<any> | CloseEvent | Event | ErrorEvent) => void;

export default class Server {
  private listeners: Record<MessageType, HandlerFunc[]> = {} as Record<MessageType, HandlerFunc[]>;
  public ws: WebSocket;
  public authID: string = localStorage.getItem('authID');

  constructor(host: string, port = 31661) {
    this.ws = new WebSocket(`wss://${host}:${port}`);
    this.ws.onopen = (ev) => this.callListeners("OPEN", ev);
    this.ws.onclose = (ev) => this.callListeners("CLOSE", ev);
    this.ws.onmessage = (ev) => this.callListeners("WS_MESSAGE", ev);
    this.ws.onerror = (ev) => this.callListeners("ERROR", ev);

    this.on("WS_MESSAGE", this.handleMessage.bind(this));
    this.on("LOGIN_SUCCESS", this.handleLogin.bind(this));
    this.on("OPEN", (ev) => {
      this.ws.send(JSON.stringify({
        type: 'AUTH',
        authID: this.authID,
      }))
    })
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
      data: [code],
    }));
  }

  public requestHistory(page = 1): void {
    console.log('Requesting History', page);
    this.ws.send(JSON.stringify({
      type: "GET_HISTORY",
      data: [page],
      authID: this.authID,
    }));
  }

  public sendMessage(msg: string): void {
    console.log('Sending message', msg);
    this.ws.send(JSON.stringify({
      type: "SEND",
      data: [msg],
      authID: this.authID,
    }))
  }

  private callListeners(type: MessageType, data: string[] | MessageEvent<any> | CloseEvent | Event | ErrorEvent) {
    if (this.listeners[type]) {
      this.listeners[type].forEach(l => l(data));
    }
  }

  private handleMessage(ev: MessageEvent<any>) {
    const message = JSON.parse(ev.data) as Message;
    this.callListeners(message.type, message.data);
  }

  private handleLogin(data: string[]) {
    this.authID = data[0];
		localStorage.setItem('authID', this.authID);
  }

}
