type MessageType = "chatMessage" | "loginSuccess" | "loginFailed" | "authSuccess" | "authFailed" | "historyData" | "open" | "close" | "error" | "message";

type Listeners = {[type: string]: [symbol, (data: any) => void][]}

export default class Server {
  private ws: WebSocket | null;
  private listeners: Listeners = {};
  private onceListeners: Listeners = {};
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

  public requestHistory(page: number, offset: number): void {
    this.send({
      type: 'requestHistory',
      page,
      offset,
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

  public on(messageType: MessageType, func: (data: any) => void): symbol {
    if (!this.listeners[messageType]) {
      this.listeners[messageType] = [];
    }
    const id = Symbol();
    this.listeners[messageType].push([id, func]);
    return id;
  }

  public remove(id: symbol): void {
    Object.keys(this.listeners).forEach((key) => {
      if (this.listeners[key].map(v => v[0]).includes(id)) {
        this.listeners[key] = this.listeners[key].filter(v => v[0] !== id);
      }
    });

    Object.keys(this.onceListeners).forEach((key) => {
      if (this.onceListeners[key].map(v => v[0]).includes(id)) {
        this.onceListeners[key] = this.onceListeners[key].filter(v => v[0] !== id);
      }
    })
  }

  public once(messageType: MessageType, func: (data: any) => void): symbol {
    if (!this.onceListeners[messageType]) {
      this.onceListeners[messageType] = [];
    }

    const id = Symbol();
    this.onceListeners[messageType].push([id, func]);
    return id;
  }

  private send(data: any) {
    this.ws.send(JSON.stringify(data));
  }

  private callListeners(type, data: any) {
    if (this.listeners[type]) {
      this.listeners[type].forEach((v) => v[1](data));
    }
    if (this.onceListeners[type]) {
      Object.values(this.onceListeners[type]).forEach((v) => v[1](data));
      this.onceListeners[type] = [];
    }
  }

}
