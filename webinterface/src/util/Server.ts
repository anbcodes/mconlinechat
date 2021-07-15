type MessageType = "chatMessage" | "authSuccess" | "authFailed" | "open" | "close" | "error" | "message";

export default class Server {
  private ws: WebSocket | null;
  private listeners: {[type: string]: ((data: any) => void)[]} = {};
  private onceListeners: {[type: string]: ((data: any) => void)[]} = {};
  private _authenticated = false;
  private authID: string;

  public get authenticated(): boolean {
    return this._authenticated;
  }

  public get connected(): boolean {
    return this.ws.readyState === this.ws.OPEN;
  }

  constructor() {
    this.on("authSuccess", () => this._authenticated = true);
    this.on("authFailed", () => this.authID = null);
    this.on('message', (data) => {
      console.log(data.data);
      const msg = JSON.parse(data.data);
      this.callListeners(msg.type, msg);
    })
  }

  public connect(host: string, port = 31661, secure = true): void {
    if (secure) {
      this.ws = new WebSocket(`wss://${host}:${port}`);
    } else {
      this.ws = new WebSocket(`ws://${host}:${port}`);
    }
    this.ws.onmessage = (event) => this.callListeners('message', event);
    this.ws.onopen = (event) => this.callListeners('open', event);
    this.ws.onclose = (event) => this.callListeners('close', event);
  }

  public authenticate(authID: string): void {
    this.send({
      type: 'auth',
      authID,
    });
    this.authID = authID;
  }

  public sendChatMessage(message: string): void {
    this.send({
      type: 'sendMessage',
      message,
      authID: this.authID,
    })
  }

  public requestHistory(page: number): void {
    this.send({
      type: 'requestHistory',
      page,
      authID: this.authID,
    })
  }

  public requestPoints(dim: number): void {
    this.send({
      type: 'requestPoints',
      dim,
      authID: this.authID,
    })
  }

  public on(messageType: MessageType, func: (data: any) => void): void {
    if (!this.listeners[messageType]) {
      this.listeners[messageType] = [];
    }

    this.listeners[messageType].push(func);
  }

  public once(messageType: MessageType, func: (data: any) => void): void {
    if (!this.onceListeners[messageType]) {
      this.onceListeners[messageType] = [];
    }

    this.onceListeners[messageType].push(func);
  }

  private send(data: any) {
    this.ws.send(JSON.stringify(data));
  }

  private callListeners(type, data: any) {
    if (this.listeners[type]) {
      this.listeners[type].forEach((f) => f(data));
    }
    if (this.onceListeners[type]) {
      this.onceListeners[type].forEach((f) => f(data));
      this.onceListeners[type] = [];
    }
  }

}
