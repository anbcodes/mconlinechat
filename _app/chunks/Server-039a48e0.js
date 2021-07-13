class s{constructor(s,t="/",e=31661){this.listeners={},this.authID=localStorage.getItem("authID"),this.ws=new WebSocket(`wss://${s}:${e}${t}`),this.ws.onopen=s=>this.callListeners("OPEN",s),this.ws.onclose=s=>this.callListeners("CLOSE",s),this.ws.onmessage=s=>this.callListeners("WS_MESSAGE",s),this.ws.onerror=s=>this.callListeners("ERROR",s),this.on("WS_MESSAGE",this.handleMessage.bind(this)),this.on("LOGIN_SUCCESS",this.handleLogin.bind(this)),this.on("OPEN",(s=>{this.ws.send(JSON.stringify({type:"AUTH",authID:this.authID}))}))}on(s,t){null==this.listeners[s]?this.listeners[s]=[t]:this.listeners[s].push(t)}login(s){console.log("Logging in with code",s),console.log(this.listeners),this.ws.send(JSON.stringify({type:"LOGIN",data:[s]}))}requestHistory(s=1){console.log("Requesting History",s),this.ws.send(JSON.stringify({type:"GET_HISTORY",data:[s],authID:this.authID}))}getPoints(){this.ws.send(JSON.stringify({type:"GET",authID:this.authID}))}sendMessage(s){console.log("Sending message",s),this.ws.send(JSON.stringify({type:"SEND",data:[s],authID:this.authID}))}callListeners(s,t){this.listeners[s]&&this.listeners[s].forEach((s=>s(t)))}handleMessage(s){const t=JSON.parse(s.data);this.callListeners(t.type,t.data)}handleLogin(s){this.authID=s[0],localStorage.setItem("authID",this.authID)}}export{s as S};
